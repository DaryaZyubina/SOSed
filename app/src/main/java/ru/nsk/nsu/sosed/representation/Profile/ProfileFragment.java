package ru.nsk.nsu.sosed.representation.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosed.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.nsk.nsu.sosed.data.profile.ProfileEntity;

public class ProfileFragment extends Fragment {
    private RecyclerView recyclerView;

    public ProfileFragment(){ }

    DatabaseReference databaseReference;
    FirebaseUser user;
    String uid;

    TextView name_view, email_view;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        uid = user.getUid();

        name_view = v.findViewById(R.id.userView);
        email_view = v.findViewById(R.id.email_userView);

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileEntity userProfile = snapshot.getValue(ProfileEntity.class);
                if (userProfile != null){
                    String name = userProfile.getName();
                    String email = userProfile.getEmail();
                    //добавить номер дома, квартиру

                    name_view.setText(name);
                    email_view.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Что-то пошло не так:(", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
}
