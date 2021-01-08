package ru.nsk.nsu.sosed.representation.view.message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sosed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.nsk.nsu.sosed.data.profile.ProfileEntity;
import ru.nsk.nsu.sosed.model.Chat;
import ru.nsk.nsu.sosed.representation.adapter.MessageAdapter;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    ImageButton btn_send;
    EditText text_send;

    FirebaseUser fUser;
    DatabaseReference reference;
    StorageReference image_Reference;

    Intent intent;
    String userUid;
    ProfileEntity user;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;

    ValueEventListener seenListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(view -> finish());

        recyclerView = findViewById(R.id.recycler_view_message);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.button_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        username.setText(intent.getStringExtra("username"));
        userUid = intent.getStringExtra("useruid"); //кому пишем

        fUser = FirebaseAuth.getInstance().getCurrentUser(); //ты
        reference = FirebaseDatabase.getInstance().getReference("users").child(userUid);

        btn_send.setOnClickListener(view -> {
            String msg = text_send.getText().toString();
            if (msg.equals("")){
                Toast.makeText(MessageActivity.this, "Вы не можете отправить пустое сообщение", Toast.LENGTH_SHORT).show();
            }else{
                sendMessage(fUser.getUid(), userUid, msg);
            }
            text_send.setText("");
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(ProfileEntity.class);

                if (!user.getMessagingEnabled()){
                    Toast.makeText(MessageActivity.this, "Автор этого объявления не разрешил обмен сообщениями в приложении", Toast.LENGTH_SHORT).show();
                    finish();
                }
                if (user.getImageUrl() == null) {
                    profile_image.setImageResource(R.drawable.ic_baseline_account_box_24);

                }else{
                    download_image();
                }
                readMessages(fUser.getUid(), userUid, user.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        seenMessage(userUid);
    }

    private void download_image(){
        image_Reference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = image_Reference.child("images/profiles/"+ user.getImageUrl());
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(getApplicationContext()).load(uri).into(profile_image));
    }

    private void seenMessage(String userid){
        reference = FirebaseDatabase.getInstance().getReference("chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);
                    if (chat.getReciever().equals(fUser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot1.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String sender, String reciever, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("reciever", reciever);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        reference.child("chats").push().setValue(hashMap);

        //add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chatlist")
                .child(fUser.getUid())
                .child(userUid);

        final DatabaseReference chatRef_reciever = FirebaseDatabase.getInstance().getReference("chatlist")
                .child(userUid)
                .child(fUser.getUid());

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    chatRef.child("id").setValue(userUid);
                    chatRef_reciever.child("id").setValue(fUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readMessages(final String myid, String userid, String imageurl){
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);
                    if (chat.getReciever().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReciever().equals(userid) && chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
    }
}
