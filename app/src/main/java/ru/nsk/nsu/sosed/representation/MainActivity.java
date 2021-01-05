package ru.nsk.nsu.sosed.representation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import ru.nsk.nsu.sosed.representation.Ad.StartAdFragment;
import ru.nsk.nsu.sosed.representation.HCS.HCSFragment;
import ru.nsk.nsu.sosed.representation.Message.MessageFragment;

import com.example.sosed.R;

import ru.nsk.nsu.sosed.representation.auth.AuthFBActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //drawerLayout = findViewById(R.id.drawer_layout);


        checkCurrentUser();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HCSFragment()).commit();
        }
    }

    public void checkCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
        } else {
            redirectActivity(this, AuthFBActivity.class);
        }

        // [END check_current_user]
    }


    public void ClickMenu(){
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(){
        closeDrawer(drawerLayout);
    }

    private static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        recreate();
    }

    public void ClickProfile(View view){
        redirectActivity(this, ProfileActivity.class);
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
                            selectedFragment = new HCSFragment();
                            break;
                        case R.id.nav_ads:
                            selectedFragment = new StartAdFragment();
                            break;
                        case R.id.nav_message:
                            selectedFragment = new MessageFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
}