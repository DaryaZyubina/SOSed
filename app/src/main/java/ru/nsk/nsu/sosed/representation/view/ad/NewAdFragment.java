package ru.nsk.nsu.sosed.representation.view.ad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sosed.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.UUID;

import ru.nsk.nsu.sosed.model.Ad;
import ru.nsk.nsu.sosed.representation.view.ad.AdFragment;
import ru.nsk.nsu.sosed.representation.viewmodel.AdViewModel;

import static android.app.Activity.RESULT_OK;

public class NewAdFragment extends Fragment {
    Ad newAd;

    public static final int IMAGE_PICK_CODE = 1000;
    public static final int IMAGE_PERMISSION_CODE = 1001;
    private static final int GALLERY_REQUEST = 1;

    private int category;
    private Uri imageUri;

    EditText title;
    EditText text;
    Button sendAdBtn;
    ImageButton addImageBtn;

    StorageReference storageReference;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_new_ad, container, false);
        storageReference = FirebaseStorage.getInstance().getReference();

        Bundle bundle = this.getArguments();
        assert bundle != null;
        category = bundle.getInt("ad");

        //setContentView(R.layout.activity_new_ad);
        title = v.findViewById(R.id.title_edit_text);
        text = v.findViewById(R.id.main_edit_text);
        sendAdBtn = v.findViewById(R.id.send_button);
        addImageBtn = v.findViewById(R.id.add_image_button);

        AdViewModel viewModel = new ViewModelProvider(this).get(AdViewModel.class);

        newAd = new Ad();
        newAd.setAuthorName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        newAd.setAuthorId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        newAd.setCreatedDate(new Date(System.currentTimeMillis()));
        newAd.setTopic(category+1);

        addImageListener(addImageBtn);
        sendAdBtn.setOnClickListener(v1 -> {
            Log.i("title", title.getText().toString());
            Log.i("text", text.getText().toString());
            newAd.setTitle(title.getText().toString());
            newAd.setText(text.getText().toString());
            viewModel.saveAd(newAd);
            goToPrevFragment();
        });
        return v;
    }

    private void goToPrevFragment() {
        getActivity().getFragmentManager().popBackStack();
        Bundle bundle = new Bundle();
        bundle.putInt("ad", category);
        Fragment fragment = new AdFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == IMAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void addImageListener (final ImageButton addImageButton) {
        addImageButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_REQUEST);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            uploadPicture();
        }
    }

    private void uploadPicture(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Загрузка изображения...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference profileImagesRef = storageReference.child("images/ads/" + randomKey);

        newAd.setImageUrl(randomKey);

        profileImagesRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    pd.dismiss();
                    Snackbar.make(getView().findViewById(R.id.add_image_button), "Изображение загружено", Snackbar.LENGTH_LONG).show();
                })
                .addOnFailureListener(exception -> {
                    pd.dismiss();
                    Toast.makeText(getContext(), "Ошибка загрузки", Toast.LENGTH_LONG).show();
                })
                .addOnProgressListener(snapshot -> {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Прогресс: " + (int) progressPercent + "%");
                });

    }

}
