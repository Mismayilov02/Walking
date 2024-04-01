package com.imehemmed.walking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SplashScreen extends AppCompatActivity {


    TextView main_text;
    Date date;
    int real_time;
    Calendar calendar ;
    SimpleDateFormat simpleDateFormat;
    Animation animation;
    SharedPreferenceManager sharedPreferenceManager;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);

        main_text = findViewById(R.id.main_text);
         intent = new Intent(this , MainActivity.class);
        animation = AnimationUtils.loadAnimation(this,R.anim.image_animation);
        sharedPreferenceManager = new SharedPreferenceManager(SplashScreen.this);


        // Log.e("OPENCV" , String.valueOf(OpenCVLoader.initDebug()));
        main_text.startAnimation(animation);

//        try {
//            date =new Date();
//            calendar = Calendar.getInstance();
//            simpleDateFormat = new SimpleDateFormat("HH");
//            real_time  =  Integer.parseInt(simpleDateFormat.format(date.getTime()));
//
//
//            if(real_time <=11 && real_time>=0){
//                main_text.setText("Good Morning");
//            }else  if(real_time >= 12 && real_time <=18){
//                main_text.setText("Good Afternoun");
//            }else{
//                main_text.setText("Good Night");
//            }
//        }catch (Exception e){
//            System.out.println(e);
//        }


        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(sharedPreferenceManager.getBoolean("permition" , true)){
                    image_dialog();
                }
               else {
                    startActivity(intent);
                }
            }
        };handler.postDelayed(runnable , 2500);




    }

     public  void image_dialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.konum_open_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button imageButton= dialog.findViewById(R.id.open_gps_btn);
        ImageView imageView  = dialog.findViewById(R.id.konum_btn_exit);


        imageButton.setOnClickListener(view -> {
            sharedPreferenceManager.setValue("permition" , false);
            startActivity(intent);
        });

        imageView.setOnClickListener(view -> {
           this.finishAffinity();
        });
    }
}