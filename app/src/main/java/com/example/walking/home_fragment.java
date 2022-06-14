package com.example.walking;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.Task;


public class home_fragment extends Fragment  implements OnMapReadyCallback {

    CardView home_start_cardview , home_location_carview , home_history_carcview , home_camera_cardview;
    SharedPreferences sharedPreferencesleng , sharedPreferences_permition;
    SharedPreferences.Editor sharedPreferences_permition_editor;
    TextView home_totalhistory_text;
    int izin_kontol = 0;
    Task<Location> locationTask;
    SharedPreferences go_map;
    SharedPreferences.Editor go_maps_editor;
    FusedLocationProviderClient fusedLocationProviderClient;
    View view;
    private GoogleMap mMap;
    Dialog dialog;
    boolean konum_boolean = false;
    int history_leng = 0;
    boolean permition_boolean = false  , camera_boolean = false , write_boolean = false , read_boolean = false , manage_storage_boolean = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_fragment, container, false);
        view = v;
        sharedPreferencesleng = this.getActivity().getSharedPreferences("shard_history_leng"  , MODE_PRIVATE);
        sharedPreferences_permition = this.getActivity().getSharedPreferences("permition" , MODE_PRIVATE);
        sharedPreferences_permition_editor = sharedPreferences_permition.edit();
        permition_boolean =  sharedPreferences_permition.getBoolean("true"  , false);
        fusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(getActivity());


        home_location_carview = v.findViewById(R.id.home_cardview_location);
        home_totalhistory_text = v.findViewById(R.id.home_totalhistory_text);
        home_start_cardview = v.findViewById(R.id.home_cardview_start);
        home_history_carcview = v.findViewById(R.id.home_cardview_history);
        home_camera_cardview = v.findViewById(R.id.home_cardview_camera);

        history_leng = sharedPreferencesleng.getInt("history_leng" , 0);
        home_totalhistory_text.setText(String.valueOf(history_leng));
        //btn_clik_false(false);
        permition_location();


        home_location_carview.setOnClickListener(view -> {
            go_map = this.getActivity().getSharedPreferences("go_konum" , MODE_PRIVATE);
            go_maps_editor = go_map.edit();
            go_maps_editor.putBoolean("true"  , false);
            go_maps_editor.commit();
            btn_clik_false(3 , view);
            this.view = view;
        });

        home_start_cardview.setOnClickListener(view -> {
            btn_clik_false(1 , view);
            this.view = view;
        });

        home_history_carcview.setOnClickListener(view -> {
            btn_clik_false(2 , view);
            this.view = view;
        });

        home_camera_cardview.setOnClickListener(view -> {
            btn_clik_false(4 , view);
            this.view = view;
        });


        return v;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        permition_boolean = true;


                    }
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();

                    permition_boolean = true;

                }
                return;
            }
        }
    }


    public void  btn_clik_false(int  acttivity , View view){


        if(permition_boolean) {

            if(true) {
                switch (acttivity) {
                    case 1:
                        Navigation.findNavController(view ).navigate(R.id.home_walking_gecis);
                        break;

                    case 2:
                        if (history_leng != 0) {
                            Navigation.findNavController(view ).navigate(R.id.home_history_gecis);
                        }
                        break;

                    case 3:
                        Navigation.findNavController(view ).navigate(R.id.home_maps_gecis);
                        break;

                    case 4:
                        if (camera_boolean && write_boolean  && read_boolean ) {
                            Navigation.findNavController(view ).navigate(R.id.home_camera_gecis);
                        }
                        else if(camera_boolean == false)
                        {
                            permition_camera();

                        }
                        else if(write_boolean == false){

                            wreat_storage();
                        }
                        else if(read_boolean == false){
                            read_storage();
                        }
                        break;

                }
            }else {
                konumbilgisi();
            }
        }else{
            permition_location();
        }

    }


    public void permition_location(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                // permition_boolean = true;
            }else{
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            // permition_boolean = true;
            permition_boolean = false;
        }
        else {
            permition_boolean = true;
        }

    }

    void  permition_camera(){

        if(ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.CAMERA} , 100);
            camera_boolean = false;

        }else{
            camera_boolean = true;
            btn_clik_false(4 ,view);
        }
    }
    void  wreat_storage(){

        if(ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} , 100);
            write_boolean = false;
            System.out.println("wreat false");
        }else{
            write_boolean = true;
            btn_clik_false(4 , view);
            System.out.println("wreat true");
        }
    }
    void  read_storage(){

        if(ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , 100);
            read_boolean = false;
            System.out.println("read false");
        }else{
            read_boolean = true;
            System.out.println("read true");
            btn_clik_false(4 , view);
        }
    }
    void  manage_storage(){

        if(ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity() , new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE} , 100);
            manage_storage_boolean = false;
            System.out.println("manage false");
        }else{
            manage_storage_boolean = true;
            System.out.println("manage true");
            btn_clik_false(4 , view);
        }
    }

//    @SuppressLint("MissingSuperCall")
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(requestCode ==100){
//            Bitmap cature = (Bitmap)  data.getExtras().get("data");
//
//        }
//    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            izin_kontol = ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.ACCESS_FINE_LOCATION);
            locationTask = fusedLocationProviderClient.getLastLocation();
            mMap = googleMap;
        }catch (Exception e){
            konum_boolean = false;
        }

    }


    boolean  konumbilgisi() {
        try {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
            izin_kontol = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            locationTask = fusedLocationProviderClient.getLastLocation();

            locationTask.addOnCompleteListener(task -> {
                if (task != null) {

                    konum_boolean = true;
                } else {
                    System.out.println("false");
                    //   Toast.makeText(this, "eror", Toast.LENGTH_LONG).show();
                    konum_boolean = false;
                    get_gps_dialog();
                }
            });
        }catch (Exception e){
            konum_boolean = false;
        }

        return konum_boolean;
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
                Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
                intent.putExtra("enabled", true);
                getActivity().sendBroadcast(intent);

                dialog.dismiss();
            }
        });
    }


}