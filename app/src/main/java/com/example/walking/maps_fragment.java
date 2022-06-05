package com.example.walking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class maps_fragment extends Fragment  {

      GoogleMap mMap;
    Button btn_location;
    ImageView konum_image , uydu_image;
    int izin_kontrolu = 0;
    EditText location_text;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<LatLng> latLng;
    Task<Location> locationTask;
    Polyline polyline;
    PolylineOptions polylineOptions;
    List<Marker>markers ;
   // View v;


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

            mMap = googleMap;
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
          //  markers.add(latLng);
            LatLng home = new LatLng(-40.632236, 47.466331);
            googleMap.addMarker(new MarkerOptions().position(home).title("home in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(home));

             polylineOptions = new PolylineOptions().add(sydney).add(home);

             polyline = mMap.addPolyline(polylineOptions);


        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
      View v=   inflater.inflate(R.layout.fragment_maps_fragment, container, false);

        btn_location = v.findViewById(R.id.btn_location);
        location_text = v.findViewById(R.id.location_text);
        uydu_image = v.findViewById(R.id.uydu_image);
        konum_image = v.findViewById(R.id.konum_image);
      //  fusedLocationProviderClient =




        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // real time konum and permition konum service
        konum_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
               if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                       PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                       Manifest.permission.ACCESS_COARSE_LOCATION )== PackageManager.PERMISSION_GRANTED){

                   LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                   if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER )|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                       fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                           @Override
                           public void onComplete(@NonNull Task<Location> task) {
                               Location location = task.getResult();


                               if (location != null) {
                                   LatLng now = new LatLng(location.getLatitude(), location.getLongitude());
                                   mMap.addMarker(new MarkerOptions().position(now).title("hhomeeeeeey"));
                                   mMap.moveCamera(CameraUpdateFactory.newLatLng(now));
                               } else {
                                   LocationRequest locationRequest;
                                  // locationRequest = new LocationRequest;
                               }
                           }
                       });
                   }
               }*/
            }
        });


        // uydu gorunum
        uydu_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL){
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    uydu_image.setImageResource(R.drawable.signal_satellite);
                }else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    uydu_image.setImageResource(R.drawable.satellite);
                }
            }
        });


        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(location_text == null || location_text.getText().toString() ==""){

                }else{
                    String get_konum = location_text.getText().toString();
                    List<Address> addressList = null;
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(get_konum , 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address = addressList.get(0);
                    LatLng deneme = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(deneme).title("thiss deneme"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deneme ,10f));

                }




            }
        });

        return v;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {

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

 /*   public void onLocationChanged(Location location) {
        polylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude());
    } */


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100)
        {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                System.out.println("verildi");
                mMap.setMyLocationEnabled(true);
            }else
            {
                System.out.println("izin yoxdu");
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

  /*  @SuppressLint("MissingPermission")
    public  void getCurrentlocation(){

        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

          fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
              @Override
              public void onComplete(@NonNull Task<Location> task) {
                  Location location= task.getResult();
                  LatLng now = new LatLng(location.getLatitude(), location.getLongitude());
                  mMap.addMarker(new MarkerOptions().position(now).title("hhomeeeeeey"));
                  mMap.moveCamera(CameraUpdateFactory.newLatLng(now));
              }
          });


    }

    @SuppressLint("MissingPermission")
    public   void  getCurrentlocation(){

        }

    } */
}