package ru.nsk.nsu.sosed.representation.Message;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sosed.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import ru.nsk.nsu.sosed.data.profile.ProfileEntity;
import ru.nsk.nsu.sosed.model.Chat;
import ru.nsk.nsu.sosed.representation.view.ad.AdDetailsActivity;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<ProfileEntity> mUsers;
    StorageReference image_Reference;

    //String lastMessage;

    public UserAdapter(Context mContext, List<ProfileEntity> mUsers){
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfileEntity user = mUsers.get(position);
        holder.username.setText(user.getName());
        image_Reference = FirebaseStorage.getInstance().getReference();

        if (user.getImageUrl() == null) {       //ну или не дефолт -- null покатит
            holder.profile_image.setImageResource(R.drawable.ic_baseline_account_box_24);
        }else{
            StorageReference profileRef  = image_Reference.child("images/profiles/" + user.getImageUrl());
            profileRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(mContext).load(uri).into(holder.profile_image));
        }

       /* if (ischat){
            lastMessage(user.getUid(), holder.last_msg);
        }else{
            holder.last_msg.setVisibility(View.GONE);
        }*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("useruid", user.getUid());
                intent.putExtra("username", user.getName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
       // private TextView last_msg;

        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            //last_msg = itemView.findViewById(R.id.last_msg);

        }
    }

   /* private void lastMessage(String userid, TextView last_msg){
        lastMessage = "def";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);
                    if (chat.getReciever().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getReciever().equals(userid) && chat.getSender().equals(firebaseUser.getUid())){
                        lastMessage = chat.getMessage();
                    }
                }

                switch (lastMessage){
                    case "def":
                        last_msg.setText("Нет сообщений");
                        break;

                    default:
                        last_msg.setText(lastMessage);
                        break;
                }

                lastMessage = "def";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

}
