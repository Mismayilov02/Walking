package com.imehemmed.walking;

import android.Manifest;
import android.content.IntentSender;
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
import com.imehemmed.walking.Room.MyroomDatabase;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements OnMapReadyCallback {

    CardView homeStartCardview, homeLocationCarview, homeHistoryCarcview;
    SharedPreferenceManager sharedPreferenceManager;
    TextView homeTotalhistoryText;
    int permitionController = 0, backround;
    LocationRequest locationRequest;
    Task<Location> locationTask;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 0;
    FusedLocationProviderClient fusedLocationProviderClient;
    View view;
    private GoogleMap mMap;
    MyroomDatabase myroomDatabase;
    boolean konumBoolean = false;
    int historyLeng = 0;
    boolean gpsOn = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_fragment, container, false);
        myroomDatabase = MyroomDatabase.getINSTANCE(getActivity());
        sharedPreferenceManager = new SharedPreferenceManager(getActivity());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        homeLocationCarview = view.findViewById(R.id.home_cardview_location);
        homeTotalhistoryText = view.findViewById(R.id.home_totalhistory_text);
        homeStartCardview = view.findViewById(R.id.home_cardview_start);
        homeHistoryCarcview = view.findViewById(R.id.home_cardview_history);

        historyLeng = myroomDatabase.historyDao().readHistoryCount();
        homeTotalhistoryText.setText(String.valueOf(historyLeng));
        checkAndRequestPermissions();
        openGPS();


        homeLocationCarview.setOnClickListener(view -> {
            btnClik(3);
            this.view = view;
        });

        homeStartCardview.setOnClickListener(view -> {
            btnClik(1);
            this.view = view;
        });

        homeHistoryCarcview.setOnClickListener(view -> {
            btnClik(2);
            this.view = view;
        });

        return view;

    }


    public void btnClik(int tag) {
        openGPS();
        if (gpsOn) {
            switch (tag) {
                case 1:
                    Navigation.findNavController(view).navigate(R.id.home_walking_gecis);
                    break;
                case 2:
                    if (historyLeng != 0) {
                        Navigation.findNavController(view).navigate(R.id.home_history_gecis);
                    } else
                        Toast.makeText(getActivity(), "There is no history", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Navigation.findNavController(view).navigate(R.id.home_maps_gecis);
                    break;

            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            permitionController = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            backround = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            locationTask = fusedLocationProviderClient.getLastLocation();
            mMap = googleMap;
        } catch (Exception e) {
            konumBoolean = false;
        }

    }


    private boolean checkAndRequestPermissions() {

        int locationpermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (locationpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
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

                    if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "location granted");

                        }
                    } else if (permissions[i].equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "location granted");

                        }
                    }


                }

            }


        }
    }

    public void openGPS() {
        locationRequest = LocationRequest.create();
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
                    gpsOn = true;

                } catch (ApiException e) {
                    if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult(getActivity(), 101);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            sendIntentException.printStackTrace();
                        }
                    }

                    if (e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                    }
                }
            }
        });
    }

}