package ru.nsk.nsu.sosed.representation.view.ad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sosed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import ru.nsk.nsu.sosed.model.Ad;
import ru.nsk.nsu.sosed.representation.view.message.MessageActivity;

public class AdDetailsActivity extends AppCompatActivity {

    TextView text;
    ImageView photoAttached;
    TextView createdDate;
    TextView author;
    Button messageButton;

    StorageReference storageReference;

    private static final String AD = "ad";
    private Ad ad;

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
        messageButton = findViewById(R.id.message_button);

        storageReference = FirebaseStorage.getInstance().getReference();

        if (getIntent().hasExtra(AD)) {
            ad = getIntent().getParcelableExtra(AD);
            if (null != ad) {
                setTitle(ad.getTitle());
                text.setText(ad.getText());
                createdDate.setText(SimpleDateFormat.getInstance().format(ad.getCreatedDate()));
                author.setText(ad.getAuthorName());
                String imageUrl = ad.getImageUrl();
                Log.d("ad details on create", "author uid & name" + ad.getAuthorId() + ad.getAuthorName());
                if (imageUrl!=null) download_image(imageUrl);
            }
        }

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(ad.getAuthorId()) || ad.getTopic()==0){
            messageButton.setVisibility(View.GONE);
        }

        messageButton.setOnClickListener(view -> {
            Intent intent = new Intent(AdDetailsActivity.this, MessageActivity.class);
            Log.d("ad details on click", "author uid & name" + ad.getAuthorId() + ad.getAuthorName());
            intent.putExtra("useruid", ad.getAuthorId());
            intent.putExtra("username", ad.getAuthorName());
            startActivity(intent);
        });
    }

    private void download_image(String url){
        StorageReference profileRef  = storageReference.child("images/ads/" + url);
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.with(AdDetailsActivity.this).load(uri).into(photoAttached));
    }

}
