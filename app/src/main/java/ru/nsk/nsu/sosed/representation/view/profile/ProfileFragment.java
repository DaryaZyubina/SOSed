package ru.nsk.nsu.sosed.representation.view.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosed.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

import ru.nsk.nsu.sosed.data.profile.ProfileEntity;
import ru.nsk.nsu.sosed.representation.MainActivity;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    public ProfileFragment(){ }
    public static final int IMAGE_PICK_CODE = 1000;
    public static final int IMAGE_PERMISSION_CODE = 1001;
    private static final int GALLERY_REQUEST = 1;
    Bitmap bitmap;
    ImageView image_user;
    CheckBox checkBox;
    TextView name_view, email_view;
    Button logout_button;

    DatabaseReference databaseReference;
    FirebaseUser user;

    FirebaseStorage storage;
    StorageReference storageReference;

    String uid;
    Uri imageUri;


    ProfileEntity userProfile;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle(getString(R.string.title_profile));

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        uid = user.getUid();

       // userProfile = new ProfileEntity();

        name_view = v.findViewById(R.id.userView);
        email_view = v.findViewById(R.id.email_userView);
        image_user = v.findViewById(R.id.avatarView);
        checkBox = v.findViewById(R.id.checkBox);
        logout_button = v.findViewById(R.id.logout_button);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        logout_button.setOnClickListener(v1 -> logout());

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(ProfileEntity.class);
                if (userProfile != null){
                    String name = userProfile.getName();
                    String email = userProfile.getEmail();

                    //добавить номер дома, квартиру

                    name_view.setText(name);
                    email_view.setText(email);

                    addCheckBoxListener(checkBox, userProfile);
                    addImageViewListener(image_user);
                    download_image();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Что-то пошло не так :(", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    private void logout() {
        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(task -> {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                });
    }

    private void addCheckBoxListener(CheckBox checkBox, ProfileEntity userProfile){
        Boolean messagingEnabled = userProfile.getMessagingEnabled();
        checkBox.setChecked(messagingEnabled);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfile.setMessagingEnabled(checkBox.isChecked());
                databaseReference.child(uid).setValue(userProfile);
            }
        });
    }

    private void download_image(){
        StorageReference profileRef  = storageReference.child("images/profiles/" + userProfile.getImageUrl());
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getContext()).load(uri).into(image_user);
            }
        });
    }

    @Override public void onSaveInstanceState(Bundle toSave) {
        super.onSaveInstanceState(toSave);
        toSave.putParcelable("bitmap", bitmap);
    }


    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case IMAGE_PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }else{
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private void addImageViewListener (final ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            image_user.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Загрузка изображения...");
        pd.show();


        final String randomKey = UUID.randomUUID().toString();
        StorageReference profileImagesRef = storageReference.child("images/profiles/" + randomKey);

        userProfile.setImageUrl(randomKey);
        databaseReference.child(uid).setValue(userProfile);

        profileImagesRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(getView().findViewById(R.id.avatarView), "Изображение загружено", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Ошибка загрузки", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Прогресс: " + (int) progressPercent + "%");
                    }
                });

    }

}
