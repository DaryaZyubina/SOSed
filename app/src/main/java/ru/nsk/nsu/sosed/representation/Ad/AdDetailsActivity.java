package ru.nsk.nsu.sosed.representation.Ad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sosed.R;

import java.text.SimpleDateFormat;

import ru.nsk.nsu.sosed.domain.Ad;

public class AdDetailsActivity extends AppCompatActivity {

    //TextView title;
    TextView text;
    ImageView photoAttached;
    TextView createdDate;
    TextView author;

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

        if (getIntent().hasExtra(AD)) {
            final Ad ad = getIntent().getParcelableExtra(AD);
            if (null != ad) {
                setTitle(ad.getTitle());
                text.setText(ad.getText());
                createdDate.setText(SimpleDateFormat.getInstance().format(ad.getCreatedDate()));
                author.setText(ad.getAuthor());
            }
        }
    }

}
