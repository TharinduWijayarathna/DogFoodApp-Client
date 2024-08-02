package com.tharindux.dogfoodapp.client.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.tharindux.dogfoodapp.client.ui.fragment.HomeFragment;
import com.tharindux.dogfoodapp.client.ui.fragment.InfoFragment;
import com.tharindux.dogfoodapp.client.ui.fragment.OrderHistoryFragment;
import com.tharindux.dogfoodapp.client.ui.fragment.ProfileFragment;
import com.tharindux.dogfoodapp.client.R;
import com.tharindux.dogfoodapp.client.ui.fragment.CartFragment;

public class MainLayout extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {


    TextView textView24;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            textView24.setText(user.getEmail().toString());

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlayout);

        textView24 = findViewById(R.id.textView24);
//        textView24.setText(getIntent().getStringExtra("email"));
//            textView24.setText("ruvindudilsara555@gmail.com");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        displayFragment(new HomeFragment());

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        if (R.id.home == item.getItemId()) {
            fragment = new HomeFragment();
        } else if (R.id.cart == item.getItemId()) {
            fragment = new CartFragment();
        } else if (R.id.profile == item.getItemId()) {
            fragment = new ProfileFragment();
        } else if (R.id.PaymentHistory == item.getItemId()) {
            fragment = new OrderHistoryFragment();
        } else if (R.id.info == item.getItemId()) {
            fragment = new InfoFragment();
        } else {
            fragment = new HomeFragment();
        }
        displayFragment(fragment);
        return true;


    }

    private void displayFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();

    }
}