package com.example.walking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Spash_screen extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

         intent= new Intent(Spash_screen.this , MainActivity.class);

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                startActivity(intent);

            }
        };handler.postDelayed(runnable , 2500);

    }
}