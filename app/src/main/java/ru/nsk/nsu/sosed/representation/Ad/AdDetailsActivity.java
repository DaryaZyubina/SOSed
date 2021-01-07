package ru.nsk.nsu.sosed.representation.Ad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sosed.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import ru.nsk.nsu.sosed.model.Ad;

public class AdDetailsActivity extends AppCompatActivity {

    //TextView title;
    TextView text;
    ImageView photoAttached;
    TextView createdDate;
    TextView author;

    StorageReference storageReference;

    private static final String AD = "ad";

    public static Intent getIntent(final Context context, final Ad ad) {
        return new Intent(context, AdDetailsActivity.class).putExtra(AD, ad);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);
        text = findViewById(R.id.detail_text_text_view);
        photoAttached = findViewById(R.id.photo_image_view);
        createdDate = findViewById(R.id.detail_created_date_text_view);
        author = findViewById(R.id.detail_author_text_view);

        storageReference = FirebaseStorage.getInstance().getReference();

        if (getIntent().hasExtra(AD)) {
            final Ad ad = getIntent().getParcelableExtra(AD);
            if (null != ad) {
                setTitle(ad.getTitle());
                text.setText(ad.getText());
                createdDate.setText(SimpleDateFormat.getInstance().format(ad.getCreatedDate()));
                author.setText(ad.getAuthor());
                download_image(ad.getImageUrl());
            }
        }
    }

    private void download_image(String url){
        StorageReference profileRef  = storageReference.child("images/ads/" + url);
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.with(AdDetailsActivity.this).load(uri).into(photoAttached));
    }

}