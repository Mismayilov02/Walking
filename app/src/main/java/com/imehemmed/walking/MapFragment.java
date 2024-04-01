package com.imehemmed.walking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.imehemmed.walking.Room.MyroomDatabase;
import com.imehemmed.walking.Room.RunHistoryDetail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {

    private GoogleMap mMap;
    private MapFragmentArgs args;
    SearchView searchView;
    ImageView layerImage, exitImage;
    Polyline polyline;

    SharedPreferenceManager sharedPreferenceManager;
    FusedLocationProviderClient fusedLocationProviderClient;

    Task<Location> locationTask;
    MyroomDatabase myroomDatabase;


    int permission = 0;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            locationTask = fusedLocationProviderClient.getLastLocation();
            myroomDatabase = MyroomDatabase.getINSTANCE(getActivity());
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            locationInfo();
            args = MapFragmentArgs.fromBundle(getArguments());

            if (args.getRunOrMap()) {
                int parentID = args.getParentId();
                RunHistoryDetail[] history = myroomDatabase.historyDetailDao().readHistorybyParentId(parentID);
                LatLng latLngg = null;
                LatLng start = null, stop = null;
                List<LatLng> latLngList = new ArrayList<>();


                for (int i = 0; i < history.length; i++) {
                    try {

                        if (i == 0) {
                            start = new LatLng(history[i].getLaitute(), history[i].getLongtitute());
                        } else if (i == history.length - 2) {
                            stop = new LatLng(history[i].getLaitute(), history[i].getLongtitute());
                        }

                        latLngg = new LatLng(history[i].getLaitute(), history[i].getLongtitute());

                        latLngList.add(latLngg);
                    } catch (Exception e) {
                        Log.e("MapFragment", "onMapReady: ", e);
                    }

                }

                if (!latLngList.isEmpty()) {

                    PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList);

                    polyline = mMap.addPolyline(polylineOptions);
                    mMap.addMarker(new MarkerOptions().position(start).title("Start"));
                    if (stop!= null) {
                        mMap.addMarker(new MarkerOptions().position(stop).title("Stop"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stop, 20f));
                    }else {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngg, 20f));
                    }
                }
            } else {
                currentLocation();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        sharedPreferenceManager = new SharedPreferenceManager(getActivity());
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


        layerImage = v.findViewById(R.id.layerImage);
        exitImage = v.findViewById(R.id.map_exit_image);
        searchView = v.findViewById(R.id.searchView);

        exitImage.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navhost_map_backHome);
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                try {
                    if (searchView.getQuery() == null || searchView.getQuery().toString() == "") {

                    } else {
                        String searchText = searchView.getQuery().toString();
                        List<Address> addressList = null;
                        Geocoder geocoder = new Geocoder(getActivity());
                        try {
                            addressList = geocoder.getFromLocationName(searchText, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(latLng).title("position"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                    }
                } catch (Exception e) {

                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        layerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    layerImage.setColorFilter(Color.WHITE);
                    exitImage.setColorFilter(Color.WHITE);

                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    layerImage.setColorFilter(Color.BLACK);
                    exitImage.setColorFilter(Color.BLACK);

                }
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == 100) {
            permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                System.out.println("izin succes");
                locationTask = fusedLocationProviderClient.getLastLocation();
                locationInfo();
            }
        } else {
//            System.out.println("izin eror ");
        }
    }

    void locationInfo() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location1 = task.getResult();

            } else {
                System.out.println("false");
//                Toast.makeText(getActivity()  , "eror" , Toast.LENGTH_LONG).show();
            }
        });
    }

    public void currentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location1 = task.getResult();
                LatLng neww = new LatLng(location1.getLatitude(), location1.getLongitude());
                mMap.clear();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(neww, 18.5f));

            } else {
               Log.e("MapFragment", "currentLocation: ", task.getException());
            }
        });
    }


}