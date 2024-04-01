package com.imehemmed.walking;

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

import com.imehemmed.walking.Room.MyroomDatabase;
import com.imehemmed.walking.Room.RunHistory;

import java.util.Date;

public class RunFragment extends Fragment {

    View v;
    Handler handler = new Handler();
    Runnable runnable;
    int minute = 0, second = 0, horse = 0;
    SharedPreferenceManager sharedPreferenceManager;
    LocationService mLocationService;
    Intent intent;
    MyroomDatabase myroomDatabase;
    TextView runTimerText, speedText, kcalText, runkmText;
    ImageView lockscreenBtn;
    Button runingExitBtn, runPlayBtn;
    boolean lockscreenBtnBoolean = false, runPlayBtnBollean = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_run_, container, false);


        runTimerText = v.findViewById(R.id.run_timer_text);
        runingExitBtn = v.findViewById(R.id.runing_exit_btn);
        runkmText = v.findViewById(R.id.run_km_text);
        lockscreenBtn = v.findViewById(R.id.run_lockscreen_btn);
        runPlayBtn = v.findViewById(R.id.run_play_btn);
        speedText = v.findViewById(R.id.run_speed_text);
        kcalText = v.findViewById(R.id.run_kcal_text);
        sharedPreferenceManager = new SharedPreferenceManager(getActivity());
        myroomDatabase = MyroomDatabase.getINSTANCE(getActivity());

        runingExitBtn.setOnClickListener(view -> {
            if (!LocationService.service_run) {
                Navigation.findNavController(view).navigate(R.id.walking_home_gecis);
            } else {
                stop_dialog(getActivity(), view);
            }

        });

        lockscreenBtn.setOnClickListener(view -> {
            if (!lockscreenBtnBoolean) {
                lockscreenBtnBoolean = true;
                lockscreenBtn.setImageResource(R.drawable.unlock);
                Toast.makeText(getActivity(), "always-on display: on", Toast.LENGTH_LONG).show();
            } else {
                lockscreenBtnBoolean = false;
                lockscreenBtn.setImageResource(R.drawable.lock);
                Toast.makeText(getActivity(), "always-on display: default", Toast.LENGTH_LONG).show();
            }

        });


        runPlayBtn.setOnClickListener(view -> {
            if (!LocationService.service_run) {
                starServiceFunc();
                LocationService.service_run = true;
                timehandlar();
                speedText.setText("0 km/h");
                kcalText.setText("0 kcal");
                runkmText.setText("0.0");

            } else {
                stopServiceFunc();
                LocationService.service_run = false;
                handler.removeCallbacks(runnable);
            }
        });

        return v;
    }


    private void starServiceFunc() {
        mLocationService = new LocationService();
        intent = new Intent(getActivity(), mLocationService.getClass());
        if (!Constant.isMyServiceRunning(mLocationService.getClass(), getActivity())) {
            runPlayBtn.setText("Stop");
            Date date = new Date();
            myroomDatabase.historyDao().insertHistory(new RunHistory(date.getTime(),0l));
            Constant.startDate = Constant.simpleDateFormat.format(date.getTime());
            getActivity().startService(intent);
        } else {
        }
    }

    private void stopServiceFunc() {
        mLocationService = new LocationService();
        myroomDatabase = MyroomDatabase.getINSTANCE(getActivity());
        intent = new Intent(getActivity(), mLocationService.getClass());
        if (Constant.isMyServiceRunning(mLocationService.getClass(), getActivity())) {
            second = 0;
            minute = 0;
            horse = 0;
            getActivity().stopService(intent);
            myroomDatabase.historyDao().updateHistory(new Date().getTime());
            runPlayBtn.setText("Play");
        }
    }

    public void timehandlar() {
        runnable = new Runnable() {
            @Override
            public void run() {
                time();
                String horseStr = horse<10 ? "0"+horse : String.valueOf(horse);
                String minuteStr = minute<10 ? "0"+minute : String.valueOf(minute);
                String secondStr = second<10 ? "0"+second : String.valueOf(second);
                runTimerText.setText(horseStr + ":" + minuteStr + ":" + secondStr);
                handler.postDelayed(runnable, 1000);

            }
        };
        handler.post(runnable);
    }

    public void time() {
        second++;
        if (second == 60) {
            second = 0;
            minute++;

        }
        if (minute == 60) {
            minute = 0;
            horse++;
        }

    }

    public void stop_dialog(Context context, View v) {
        Dialog dialogwifi = new Dialog(context);
        dialogwifi.setContentView(R.layout.stop_dialog);
        dialogwifi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogwifi.show();

        Button wifiAgain = dialogwifi.findViewById(R.id.stop_dialog_btn);
        wifiAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogwifi.dismiss();
                stopServiceFunc();
                LocationService.service_run = false;
                handler.removeCallbacks(runnable);
                Navigation.findNavController(v).navigate(R.id.walking_home_gecis);
            }
        });
    }

}