package com.example.walking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    NavHostFragment navHostFragment;
  //  LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // locationManager = (LocationManager) getS

        bottomNavigationView = findViewById(R.id.bottom_navigationview);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navhost_frgment);
        NavigationUI.setupWithNavController(bottomNavigationView , navHostFragment.getNavController());
    }
}