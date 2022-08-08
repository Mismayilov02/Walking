package com.i_mehemmed.walking;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Dialog;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class home_fragment extends Fragment  implements OnMapReadyCallback {

    CardView home_start_cardview , home_location_carview , home_history_carcview , home_camera_cardview;
    SharedPreferences sharedPreferencesleng , sharedPreferences_permition;
    SharedPreferences.Editor sharedPreferences_permition_editor;
    TextView home_totalhistory_text;
    int izin_kontol = 0;
    LocationRequest locationRequest;
    Task<Location> locationTask;
    SharedPreferences go_map;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 0;
    SharedPreferences.Editor go_maps_editor;
    FusedLocationProviderClient fusedLocationProviderClient;
    View view;
    private GoogleMap mMap;
    Dialog dialog;
    boolean konum_boolean = false;
    int history_leng = 0;
    boolean Gpson = false ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_fragment, container, false);
        view = v;
        sharedPreferencesleng = this.getActivity().getSharedPreferences("shard_history_leng"  , MODE_PRIVATE);
        sharedPreferences_permition = this.getActivity().getSharedPreferences("permition" , MODE_PRIVATE);
        sharedPreferences_permition_editor = sharedPreferences_permition.edit();
//        permition_boolean =  sharedPreferences_permition.getBoolean("true"  , false);
        fusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(getActivity());


        home_location_carview = v.findViewById(R.id.home_cardview_location);
        home_totalhistory_text = v.findViewById(R.id.home_totalhistory_text);
        home_start_cardview = v.findViewById(R.id.home_cardview_start);
        home_history_carcview = v.findViewById(R.id.home_cardview_history);
        home_camera_cardview = v.findViewById(R.id.home_cardview_camera);

        history_leng = sharedPreferencesleng.getInt("history_leng" , 0);
        home_totalhistory_text.setText(String.valueOf(history_leng));
        //btn_clik_false(false);
        checkAndRequestPermissions();
        openGPS();


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




    public void  btn_clik_false(int  acttivity , View view){
        openGPS();

            if(Gpson) {
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


                }
            }else {
//                konumbilgisi();
            }

    }


//



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





    private boolean checkAndRequestPermissions() {

        int locationpermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {

            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {

                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "location granted");

                        }



                }

            }


        }
    }

    public void openGPS(){
        locationRequest  = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        Task<LocationSettingsResponse> locationSettingsResponseTask = LocationServices.getSettingsClient(getActivity())
                .checkLocationSettings(builder.build());

        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
//                    Toast.makeText(getActivity() , "GPS is already enable" , Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getActivity(), Control_Panel.class));
                    Gpson = true;
                    System.out.println(1);

                }catch (ApiException e){
                    if(e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        System.out.println(2);
                        try {
                            resolvableApiException.startResolutionForResult(getActivity() , 101);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            System.out.println(3);
                            sendIntentException.printStackTrace();
                        }
                    }

                    if(e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE){
                        Toast.makeText(getActivity() , "settings no avaible" , Toast.LENGTH_SHORT).show();
                        System.out.println(4);
                    }
                }
            }
        });
    }

}