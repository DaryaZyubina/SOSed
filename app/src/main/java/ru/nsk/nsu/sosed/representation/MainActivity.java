package ru.nsk.nsu.sosed.representation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import ru.nsk.nsu.sosed.representation.Ad.AdFragment;
import ru.nsk.nsu.sosed.representation.Ad.StartAdFragment;
import ru.nsk.nsu.sosed.representation.Auth.CompleteAuthFragment;
import ru.nsk.nsu.sosed.representation.Message.MessageFragment;

import com.example.sosed.R;

import ru.nsk.nsu.sosed.representation.Profile.ProfileFragment;
import ru.nsk.nsu.sosed.representation.Auth.AuthFBActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkCurrentUser();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new StartAdFragment()).commit();
        }
    }

    public void checkCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            FirebaseUserMetadata metadata = user.getMetadata();
            if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                CompleteAuthFragment fragment = new CompleteAuthFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                Log.i("MainActivity", "new user's name: "+user.getDisplayName());
            }

        } else {
            redirectActivity(this, AuthFBActivity.class);
        }

        // [END check_current_user]
    }

    public void ClickHome(View view){
        recreate();
    }

    private static void redirectActivity(Activity activity, Class cClass){
        Intent intent = new Intent(activity, cClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            Bundle bundle = new Bundle();
                            bundle.putInt("ad", -1);
                            selectedFragment = new AdFragment();
                            selectedFragment.setArguments(bundle);
                            break;
                        case R.id.nav_ads:
                            selectedFragment = new StartAdFragment();
                            break;
                        case R.id.nav_message:
                            selectedFragment = new MessageFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
}