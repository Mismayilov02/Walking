package com.example.walking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Spash_screen extends AppCompatActivity {


    TextView main_text;
    Date date;
    int real_time;
    Calendar calendar ;
    SimpleDateFormat simpleDateFormat;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        main_text = findViewById(R.id.main_text);
         intent = new Intent(this , MainActivity.class);


        // Log.e("OPENCV" , String.valueOf(OpenCVLoader.initDebug()));

        try {
            date =new Date();
            calendar = Calendar.getInstance();
            simpleDateFormat = new SimpleDateFormat("HH");
            real_time  =  Integer.parseInt(simpleDateFormat.format(date.getTime()));


            if(real_time <=11 && real_time>=0){
                main_text.setText("Good Morning");
            }else  if(real_time >= 12 && real_time <=18){
                main_text.setText("Good Afternoun");
            }else{
                main_text.setText("Good Night");
            }
        }catch (Exception e){
            System.out.println(e);
        }


        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        };handler.postDelayed(runnable , 2700);


    }
}