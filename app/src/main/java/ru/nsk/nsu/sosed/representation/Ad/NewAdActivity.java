package ru.nsk.nsu.sosed.representation.Ad;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.sosed.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import ru.nsk.nsu.sosed.domain.Ad;

public class NewAdActivity extends AppCompatActivity {

    EditText title;
    EditText text;

    Button sendAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);
        title = findViewById(R.id.title_edit_text);
        text = findViewById(R.id.main_edit_text);
        sendAd = findViewById(R.id.send_button);

        AdViewModel viewModel = new ViewModelProvider(this).get(AdViewModel.class);

        sendAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("title", title.getText().toString());
                Log.i("text", text.getText().toString());
                viewModel.saveAd(new Ad(title.getText().toString(),
                        text.getText().toString(),
                        null,
                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        3, //todo: взять тему где-то !!
                        new Date(System.currentTimeMillis())));
                //AdFragment fragment = new AdFragment();
                //Bundle b = new Bundle();
                //b.putString("ad", getResources().getStringArray(R.array.ad_categories)[2]);
                //fragment.setArguments(b);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }

}
