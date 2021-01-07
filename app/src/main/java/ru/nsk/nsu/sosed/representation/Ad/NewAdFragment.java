package ru.nsk.nsu.sosed.representation.Ad;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sosed.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

import ru.nsk.nsu.sosed.domain.Ad;

public class NewAdFragment extends Fragment {

    private int category;

    EditText title;
    EditText text;
    Button sendAd;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_new_ad, container, false);

        Bundle bundle = this.getArguments();
        assert bundle != null;
        category = bundle.getInt("ad");

        //setContentView(R.layout.activity_new_ad);
        title = v.findViewById(R.id.title_edit_text);
        text = v.findViewById(R.id.main_edit_text);
        sendAd = v.findViewById(R.id.send_button);

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
                        category+1,
                        new Date(System.currentTimeMillis())));
                goToPrevFragment();
                //AdFragment fragment = new AdFragment();
                //Bundle b = new Bundle();
                //b.putString("ad", getResources().getStringArray(R.array.ad_categories)[2]);
                //fragment.setArguments(b);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
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

}
