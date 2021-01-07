package ru.nsk.nsu.sosed.representation.Auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sosed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import ru.nsk.nsu.sosed.data.profile.ProfileEntity;
import ru.nsk.nsu.sosed.model.Ad;
import ru.nsk.nsu.sosed.representation.Ad.AdAdapter;
import ru.nsk.nsu.sosed.representation.Ad.AdViewModel;
import ru.nsk.nsu.sosed.representation.Ad.NewAdFragment;

public class CompleteAuthFragment extends Fragment {
    AuthViewModel viewModel;

    EditText addressEditText;
    EditText apartmentEditText;
    Button saveButton;
    TextView skipTextView;

    public CompleteAuthFragment() {
    }
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_complete_auth, container, false);

        addressEditText = v.findViewById(R.id.address_editText);
        apartmentEditText = v.findViewById(R.id.apartmentNum_editText);
        saveButton= v.findViewById(R.id.auth_button_continue);
        skipTextView = v.findViewById(R.id.skip_textView);
        skipTextView.setOnClickListener(v1 -> {
            saveProfile(null, null);
        });
        saveButton.setOnClickListener(v1 -> {
            saveProfile(addressEditText.getText().toString(), apartmentEditText.getText().toString());
        });
        return v;
    }

    private void saveProfile(String address, String apartmentNum) {
        ProfileEntity profile = new ProfileEntity();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        profile.setUid(user.getUid());
        profile.setName(user.getDisplayName());
        profile.setEmail(user.getEmail());
        profile.setApartmentNum(apartmentNum);
        profile.setHouse(address);
        profile.setMessagingEnabled(apartmentNum != null);
        viewModel.saveProfile(profile);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }
}
