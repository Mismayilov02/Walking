package com.mehemmed_i.walking;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

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
import com.mehemmed_i.walking.Room.History;
import com.mehemmed_i.walking.Room.MyroomDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class fragment_map extends Fragment {

    private GoogleMap mMap;
    // private FragmentMapsFragmentBinding binding;
    SearchView searchView;
    ImageView layer_image , exit_image ;
    Polyline polyline;
//    MyroomDatabase myroomDatabase;
    // Location current_location ;
    // LocationRequest locationRequest;
    //int REQUEST_CODE = 101;


    int position = 0;
    boolean go_history = false;
    SharedPreferenceManager sharedPreferenceManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    SharedPreferences sharedPreferenceskonum , go_map;


    Task<Location> locationTask;


    int izin_kontol = 0;

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
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            konumbilgisi();
            fragment_mapArgs args = fragment_mapArgs.fromBundle(getArguments());

            if(args.getRunOrMap()){
                Static.tableName = String.valueOf("table"+args.getId());
              MyroomDatabase myroomDatabase = MyroomDatabase.getINSTANCE(getActivity());
                History[] history = myroomDatabase.history_dao().read_history_all();
//13764    35
                int lengs = history.length;
                LatLng latLngg = null;
                LatLng start = null, stop  = null;
                List<LatLng> latLngList =new ArrayList<>();


                for (int i = 0; i < lengs - 1; i++) {
                    try {

                        if(i==0){
                            start = new LatLng(history[i].getLaitute() , history[i].getLongtitute());
                        }else if(i==lengs-2){
                            stop =new LatLng(history[i].getLaitute() , history[i].getLongtitute());
                        }

                        //  Toast.makeText(getApplicationContext(), String.valueOf(langtitue + " " + latitute), Toast.LENGTH_SHORT).show();
                        latLngg = new LatLng(history[i].getLaitute() , history[i].getLongtitute());

                        latLngList.add(latLngg);
                    } catch (Exception e) {
//                        Toast.makeText(getActivity() , String.valueOf(e), Toast.LENGTH_SHORT).show();
                    }

                }

               if(!latLngList.isEmpty() && start!= null && stop!= null) {

                    PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList);

                 polyline  =   mMap.addPolyline(polylineOptions);
                    mMap.addMarker(new MarkerOptions().position(start).title("start"));
                    mMap.addMarker(new MarkerOptions().position(stop).title("stop"));
                   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stop, 20f));
                }
                // LatLng home = new LatLng(-47.466331, 40.632236);
                // mMap.addMarker(new MarkerOptions().position(home).title("home in Sydney"));

            }else {
//
                now_conum();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.fragment_map, container, false);

        go_map = getActivity().getSharedPreferences("go_konum" , MODE_PRIVATE);
        go_history = go_map.getBoolean("true"  , false);
        position =   go_map.getInt("position" , 0);
        sharedPreferenceManager = new SharedPreferenceManager(getActivity());
//        Toast.makeText(getActivity()  , String.valueOf(position) , Toast.LENGTH_SHORT).show();


        if(izin_kontol != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity()   ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , 100);

            //getcurrent_location();
        }

        fusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(getActivity() );


        layer_image = v.findViewById(R.id.uydu_image);
        exit_image = v.findViewById(R.id.map_exit_image);
        searchView = v.findViewById(R.id.searchView);

        exit_image.setOnClickListener(view -> {
            Navigation.findNavController(view ).navigate(R.id.navhost_map_backHome);
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

              try {
                    if (searchView.getQuery() == null || searchView.getQuery().toString() == "") {

                    } else {
                        String get_konum = searchView.getQuery().toString();
                        List<Address> addressList = null;
                        Geocoder geocoder = new Geocoder(getActivity());
                        try {
                            addressList = geocoder.getFromLocationName(get_konum, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Address address = addressList.get(0);
                        LatLng deneme = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(deneme).title("position"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deneme, 15f));
                    }
                }catch (Exception e){

              }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });



        layer_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL){
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    layer_image.setColorFilter(Color.WHITE);
                    exit_image.setColorFilter(Color.WHITE);

                }else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    layer_image.setColorFilter(Color.BLACK);
                    exit_image.setColorFilter(Color.BLACK);

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


        if(requestCode ==100){
            izin_kontol = ContextCompat.checkSelfPermission(getActivity()  , Manifest.permission.ACCESS_FINE_LOCATION);

            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                System.out.println("izin succes");
                locationTask = fusedLocationProviderClient.getLastLocation();
                konumbilgisi();
            }
        }else {
            System.out.println("izin eror ");
        }
    }

    void  konumbilgisi(){
        fusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(getActivity() );
        izin_kontol = ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.ACCESS_FINE_LOCATION);
        locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnCompleteListener(task -> {
            if(task.isSuccessful() ){
                Location location1 = task.getResult();

            }else{
                System.out.println("false");
//                Toast.makeText(getActivity()  , "eror" , Toast.LENGTH_LONG).show();
            }
        });
    }

    public  void now_conum(){
        fusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(getActivity() );
        izin_kontol = ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.ACCESS_FINE_LOCATION);
        locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnCompleteListener(task -> {
            if(task.isSuccessful() ){
                Location location1 = task.getResult();
                LatLng neww = new LatLng(location1.getLatitude(), location1.getLongitude());
                mMap.clear();
                //  mMap.addMarker(new MarkerOptions().position(neww).title("now"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(neww, 18.5f));

            }else{
                System.out.println("false");
//                Toast.makeText(getActivity()  , "eror" , Toast.LENGTH_LONG).show();
            }
        });
    }





}