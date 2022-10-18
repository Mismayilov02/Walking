package com.mehemmed_i.walking;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mehemmed_i.walking.Room.MyroomDatabase;

import java.util.Date;

public class Run_Fragment extends Fragment {

    View v;
    Handler handler= new Handler();
    Runnable runnable;
    int minute = 0 , second = 0 , horse = 0;
    SharedPreferenceManager sharedPreferenceManager;
    LocationService mLocationService;
    Intent intent;
    MyroomDatabase myroomDatabase;
      TextView run_timer_text  ,speed_text , kcal_text , runkm_text;

    //private ActivityMapsBinding binding;
    ImageView lockscreen_btn;
    Button runing_exit_btn, run_play_btn;
    boolean lockscreen_btn_boolean = false, run_play_btn_bollean = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_run_, container, false);


        run_timer_text = v.findViewById( R.id.run_timer_text);
        runing_exit_btn = v.findViewById(R.id.runing_exit_btn);
        runkm_text = v.findViewById(R.id.run_km_text);
        lockscreen_btn = v.findViewById(R.id.run_lockscreen_btn);
        run_play_btn = v.findViewById(R.id.run_play_btn);
        speed_text = v.findViewById(R.id.run_speed_text);
        kcal_text  = v.findViewById(R.id.run_kcal_text);
        sharedPreferenceManager = new SharedPreferenceManager(getActivity());

        runing_exit_btn.setOnClickListener(view -> {
            if(!LocationService.service_run){
                Navigation.findNavController(view ).navigate(R.id.walking_home_gecis);
            }else {

                stop_dialog(getActivity() , view);

            }

        });

        lockscreen_btn.setOnClickListener(view -> {
            if (!lockscreen_btn_boolean) {
                // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                // lockscreen_btn.setDr;
                lockscreen_btn_boolean = true;
                lockscreen_btn.setImageResource(R.drawable.unlock);
//                Toast.makeText(getActivity(), "always-on display: on", Toast.LENGTH_LONG).show();
            } else {
                lockscreen_btn_boolean = false;
                lockscreen_btn.setImageResource(R.drawable.lock);
//                Toast.makeText(getActivity(), "always-on display: default", Toast.LENGTH_LONG).show();
            }

        });


        run_play_btn.setOnClickListener(view -> {
            if(!LocationService.service_run){
                starServiceFunc();
                LocationService.service_run = true;
                timehandlar();
                speed_text.setText("0 km/h");
                kcal_text.setText("0 kcal");
                runkm_text.setText("0.0");

            }else {
                stopServiceFunc();
                LocationService.service_run = false;
                handler.removeCallbacks(runnable);
            }
        });

        return  v;
    }


    private void starServiceFunc(){
        mLocationService = new LocationService();
        intent  = new Intent(getActivity(), mLocationService.getClass());
        if (!Static.isMyServiceRunning(mLocationService.getClass(), getActivity())) {
            run_play_btn.setText("Pause");
            Date date = new Date();
            Static.id = (sharedPreferenceManager.getInt("id" , 0));
            Static.tableName = String.valueOf("table"+Static.id);
            sharedPreferenceManager.setValue("date"+Static.id , Static.simpleDateFormat.format(date.getTime()));
            sharedPreferenceManager.setValue("id" ,Static.id+1);

            Static.startDate = Static.simpleDateFormat.format(date.getTime());
            getActivity().startService(intent);
//            Toast.makeText(getActivity(), "succes", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
        }
    }

  private void stopServiceFunc(){
        mLocationService = new LocationService();
        myroomDatabase = MyroomDatabase.getINSTANCE(getActivity());
        intent = new Intent(getActivity(), mLocationService.getClass());
        if (Static.isMyServiceRunning(mLocationService.getClass(), getActivity())) {
            getActivity().stopService(intent);
            System.out.println(myroomDatabase.history_dao().read_history_all());
            run_play_btn.setText("Play");
//            Toast.makeText(getActivity(), "Service stopped!!", Toast.LENGTH_SHORT).show();
            //saveLocation(); // explore it by your self
        } else {
//            Toast.makeText(getActivity(), "Service is already stopped!!", Toast.LENGTH_SHORT).show();
        }
    }

    public  void timehandlar(){

         runnable = new Runnable() {
            @Override
            public void run() {

                time();
                run_timer_text.setText(String.valueOf(horse+":"+minute+":"+second));
                handler.postDelayed(runnable , 1000);

            }
        };handler.post(runnable);
    }

    public void time(){
        second ++;
        if(second == 60){
            second = 0;
            minute ++;

        } if (minute == 60){
            minute =0;
            horse ++;
        }

    }

     public  void stop_dialog(Context context , View v){
        Dialog dialogwifi = new Dialog(context);
        dialogwifi.setContentView(R.layout.stop_dialog);
        dialogwifi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogwifi.show();

        Button wifiAgain  = dialogwifi.findViewById(R.id.stop_dialog_btn);
        wifiAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogwifi.dismiss();
                stopServiceFunc();
                LocationService.service_run = false;
                handler.removeCallbacks(runnable);
                Navigation.findNavController(v ).navigate(R.id.walking_home_gecis);
            }
        });
    }

}