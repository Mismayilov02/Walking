package com.example.walking;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class walking_fragment extends Fragment  {

  //  private FragmentWalkingFragmentBinding binding;

    Handler handler ;
    Runnable runnable;
    Location location1;
    Dialog dialog;
    Date date ;
    Calendar calendar;
    String date_time;
    SimpleDateFormat simpleDateFormat;

    TextView run_timer_text;
    SharedPreferences sharedPreferenceskonum , sharedPreferencesleng;
    SharedPreferences.Editor editorkonum , editorleng;
    int shared_leng = 0;
    // ArrayList< LatLng> latLngs  = new ArrayList<>();

    //  Polyline polyline;
    //PolylineOptions polylineOptions;
    FusedLocationProviderClient fusedLocationProviderClient;
    // List<LatLng> latLng;
    Task<Location> locationTask;
    boolean konum = false , handlar_boolean = false;
    int izin_kontol = 0;
    int time = 0;
    private GoogleMap mMap;
    //private ActivityMapsBinding binding;

    Button runing_exit_btn, lockscreen_btn, run_play_btn;
    boolean lockscreen_btn_boolean = false, run_play_btn_bollean = false;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            izin_kontol = ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.ACCESS_FINE_LOCATION);
            locationTask = fusedLocationProviderClient.getLastLocation();
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
            konumbilgisi();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.fragment_walking_fragment, container, false);

        fusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(getActivity());

        run_timer_text = v.findViewById( R.id.run_timer_text);
        runing_exit_btn = v.findViewById(R.id.runing_exit_btn);
        lockscreen_btn = v.findViewById(R.id.run_lockscreen_btn);
        run_play_btn = v.findViewById(R.id.run_play_btn);

        runing_exit_btn.setOnClickListener(view -> {
           Navigation.findNavController(view ).navigate(R.id.walking_home_gecis);
        });

        lockscreen_btn.setOnClickListener(view -> {
            if (!lockscreen_btn_boolean) {
                // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                // lockscreen_btn.setDr;
                lockscreen_btn_boolean = true;
                lockscreen_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unlock, 0, 0, 0);
                Toast.makeText(getActivity(), "always-on display: on", Toast.LENGTH_LONG).show();
            } else {
                lockscreen_btn_boolean = false;
                lockscreen_btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, 0, 0);
                Toast.makeText(getActivity(), "always-on display: default", Toast.LENGTH_LONG).show();
            }

        });


        run_play_btn.setOnClickListener(view -> {

            get_gps_dialog();
//            if (!run_play_btn_bollean) {
//                sharedPreferencesleng = this.getActivity().getSharedPreferences("shard_history_leng"  , MODE_PRIVATE);
//                editorleng = sharedPreferencesleng.edit();
//                shared_leng = sharedPreferencesleng.getInt("history_leng" , 0);
//                Toast.makeText(getActivity() , String.valueOf(shared_leng) , Toast.LENGTH_SHORT).show();
//                sharedPreferenceskonum = this.getActivity().getSharedPreferences(String.valueOf("shared_history_konum"+shared_leng) ,MODE_PRIVATE);
//                editorkonum = sharedPreferenceskonum.edit();
//
//
//
//                izin_kontol = ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.ACCESS_FINE_LOCATION);
//
//
//                if(izin_kontol != PackageManager.PERMISSION_GRANTED){
//                    ActivityCompat.requestPermissions(getActivity()  ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , 100);
//
//                    //getcurrent_location();
//                }else {
//
//                    locationTask = fusedLocationProviderClient.getLastLocation();
//                    konumbilgisi();
//                    run_play_btn_bollean= true;
//                    run_play_btn.setText("Pause");
//                    if(konum){
//                        play_runn();
//                    }
//                }
//
//            } else {
//                run_play_btn_bollean = false;
//
//                run_play_btn.setText("Play");
//
//                if(handlar_boolean){
//                    date =new Date();
//                    calendar = Calendar.getInstance();
//                    simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//                    date_time = simpleDateFormat.format(date.getTime());
//                    handler.removeCallbacks(runnable);
//                    editorleng.putInt("history_leng"  ,++shared_leng);
//                    editorkonum.putString("history_date"  , date_time);
//                    editorkonum.putInt("konum_leng" ,time);
//                    Toast.makeText(getActivity() , String.valueOf(time) , Toast.LENGTH_SHORT).show();
//
//                    editorleng.commit();
//                    editorkonum.commit();
//                    time =0;
//                    run_timer_text.setText("0");
//
//                    handlar_boolean=false;
//                    // for()
//                }
//
//            }
        });
                return v;
            }



//            @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.fragment_walking_fragment, container, false);
//
//        // binding = ActivityMapsBinding.inflate(getLayoutInflater());
//        // setContentView(binding.getRoot());
//
//        //  mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) this.getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.mappa);
//        mapFragment.getMapAsync(this);
//
//
//
//
//        return  v;
//    }


    void konumbilgisi() {
        try {
            fusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(getActivity());
            izin_kontol = ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.ACCESS_FINE_LOCATION);
            locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    location1 = task.getResult();
                    konum = true;
                } else {
                    System.out.println("false");
                    Toast.makeText(getActivity(), "Konumunuz bulunamdi!", Toast.LENGTH_LONG).show();
                }

            });
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if(requestCode ==100){
            try {
                izin_kontol = ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.ACCESS_FINE_LOCATION);

                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    System.out.println("izin succes");
                    locationTask = fusedLocationProviderClient.getLastLocation();
                    konumbilgisi();
                }
            }catch (Exception e){
                System.out.println(e);
            }

        }else {
            System.out.println("izin eror ");
        }
    }

    void  play_runn(){
        handlar_boolean = true;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                izin_kontol = ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.ACCESS_FINE_LOCATION);
                locationTask = fusedLocationProviderClient.getLastLocation();
                konumbilgisi();

                if (location1 !=null) {
                    //  sydney = new LatLng;
                    // editorkonum = sharedPreferenceskonum.edit();
                    editorkonum.putFloat(String.valueOf("konumlatitute"+time) , (float) location1.getLatitude());
                    editorkonum.putFloat(String.valueOf("konumlongitute"+time) , (float) location1.getLongitude());
                    time+=1;
//                             editorkonum.commit();

                    // Toast.makeText(getApplicationContext() , String.valueOf(location1.getLatitude() +" "+ location1.getLongitude()) , Toast.LENGTH_SHORT).show();
                    // System.out.println(sydney);
                } else {
                    Toast.makeText(getActivity() , "konum bulunamadi" , Toast.LENGTH_LONG).show();
                }


                run_timer_text.setText(String.valueOf(time));


                handler.postDelayed(this , 2000);
            }
        };
        handler.post(runnable);



    }

    void get_gps_dialog(){
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.konum_open_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        Button wifiAgain  = dialog.findViewById(R.id.open_gps_btn);
        wifiAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enable GPS
//                Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
//                intent.putExtra("enabled", true);
//                sendBroadcast(intent);

                dialog.dismiss();
            }
        });
    }


}