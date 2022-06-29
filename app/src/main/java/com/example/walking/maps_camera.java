package com.example.walking;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class maps_camera extends Fragment {

    TextView camera_date , camera_location;
    String date_c  = "Date: " , Location_c = "Location: " , stamp = "" , location_text , current_ddate;
    Bitmap bitmap;
    Date date;
    FusedLocationProviderClient fusedLocationProviderClient;
    // List<LatLng> latLng;
    Task<Location> locationTask;
    Location location1;
    boolean konum = false , handlar_boolean = false , flash = true;
    int izin_kontol = 0;
    LocationRequest locationRequest;
    int time = 0 , x , y;
    private GoogleMap mMap;

CameraManager cameraManager;
    private ImageView btnCapture , flash_camera , galeri_camera  , open_gps;
    private TextureView textureView;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   // Date date = new Date();

    //Check state orientation of output image
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static{
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }

    private String cameraId;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    String mCameraId;

    //Save to FILE
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            cameraDevice.close();
            cameraDevice=null;
        }
    };



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

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
//
                     date = new Date();
                     current_ddate = simpleDateFormat.format(date.getTime());
                    camera_date.setText(date_c +current_ddate);
                konumbilgisi();
//                if(location1.getLongitude()!=0) {
                    try {
                         location_text = Location_c + String.valueOf(location1.getLatitude()) + "; " + String.valueOf(location1.getLongitude());
                        camera_location.setText(location_text);
                    }catch (Exception e) {
                        System.out.println(e);
                        location_text = Location_c +"null";
                        camera_location.setText(location_text );


                    }
                    stamp = String.valueOf(date_c+current_ddate) +System.lineSeparator() +String.valueOf(location_text);
                    //System.out.println(stamp);
//                }
//
                    handler.postDelayed(this , 1000);
                }
            };handler.post(runnable);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.fragment_maps_camera, container, false);

        cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        fusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(getActivity());

        textureView = v.findViewById(R.id.textureView);
        flash_camera = v.findViewById(R.id.flash_camera);
        galeri_camera = v.findViewById(R.id.galeri_camera);
        camera_date = v.findViewById(R.id.camera_time);
        camera_location = v.findViewById(R.id.camera_location);
        open_gps = v.findViewById(R.id.open_gps);
        //From Java 1.4 , you can use keyword 'assert' to check expression true or false
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        btnCapture = v.findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });


        //gps_open method
        open_gps.setOnClickListener(view -> {
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
                        Toast.makeText(getActivity() , "GPS is already enable" , Toast.LENGTH_SHORT).show();


                    }catch (ApiException e){
                        if(e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            try {
                                resolvableApiException.startResolutionForResult(getActivity() , 101);
                            } catch (IntentSender.SendIntentException sendIntentException) {
                                sendIntentException.printStackTrace();
                            }
                        }

                        if(e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE){
                            Toast.makeText(getActivity() , "settings no avaible" , Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });

        });

        flash_camera.setOnClickListener(view -> {
//            Camera cam= Camera.open();
//            Camera.Parameters p = cam.getParameters();
            if(flash){
                try {

//                    cam = Camera.open();
//                    p = cam.getParameters();
//                    p.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
//                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                    cam.setParameters(p);
//                    cam.startPreview();
                   // cameraManager.setTorchMode(cameraId, false);
                   flash_camera.setImageResource(R.drawable.bolt_on);

                    flash = false;
                    //System.out.println("false");
                } catch (Exception e) {
                  //  e.printStackTrace();
                    System.out.println(e);
                }
            }else {
                try {
//                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                    cam.setParameters(p);
//                    cam.stopPreview();
//                    cam.release();
                   // cameraManager.setTorchMode(cameraId , true);
                    flash = true;
                    //System.out.println("ture");
                   flash_camera.setImageResource(R.drawable.bolt);
                } catch (Exception e) {
                   // e.printStackTrace();
                    System.out.println(e);
                }
            }
        });

        galeri_camera.setOnClickListener(view -> {
            Intent galery= new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galery , REQUEST_CAMERA_PERMISSION);
        });

        return  v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_camera);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void takePicture() {
        if(cameraDevice == null)
            return;
        CameraManager manager = (CameraManager)getActivity().getSystemService(Context.CAMERA_SERVICE);
        try{
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if(characteristics != null)
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);

            //Capture image with custom size
            int width = 640;
            int height = 480;
            if(jpegSizes != null && jpegSizes.length > 0)
            {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            final ImageReader reader = ImageReader.newInstance(width,height,ImageFormat.JPEG,1);
            List<Surface> outputSurface = new ArrayList<>(2);
            outputSurface.add(reader.getSurface());
            outputSurface.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            //Check orientation base on device
            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION,ORIENTATIONS.get(rotation));

            file = new File(Environment.getExternalStorageDirectory()+"/"+ UUID.randomUUID().toString()+".jpg");
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Image image = null;
                    try{
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    } finally {
                        {
                            if(image != null)
                                //image=null;
                                image.close();
                            // image.close();
                            // image.close();
                        }
                    }
                }


                private void save(byte[] bytes) {

                    Uri images;

                    ContentResolver contentResolver = getActivity().getContentResolver();

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                    }else{
                        images  = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    }

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.Images.Media.DISPLAY_NAME , System.currentTimeMillis()+".jpg");
                    contentValues.put(MediaStore.Images.Media.MIME_TYPE , "images/*");
                    Uri uri = contentResolver.insert(images , contentValues);
                    Bitmap bitmap1  = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


                    // conver bitmap
                    try {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(0);
                        bitmap = drawTextToBitmap(Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true) , stamp);
                      //  bitmap =  ;
                    }catch (Exception e){
                        System.out.println(e);
                    }


                    OutputStream outputStream = null;
                    try {
                        outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
                    } catch (FileNotFoundException e) {
                      //  e.printStackTrace();
                    }
                    bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , outputStream);

                    Objects.requireNonNull(outputStream);
                    Toast.makeText(getActivity() , "save" , Toast.LENGTH_SHORT).show();

                    try {
                        outputStream.close();
                    } catch (IOException e) {
                       // e.printStackTrace();
                    }


//                    OutputStream outputStream = null;
//                    try{
//                        outputStream = new FileOutputStream(file);
//                        outputStream.write(bytes);
//                    }finally {
//                        if(outputStream != null)
//                            outputStream.close();
//                    }
                }
            };

            reader.setOnImageAvailableListener(readerListener,mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    // Toast.makeText(getActivity(), "Saved "+file, Toast.LENGTH_SHORT).show();
                    createCameraPreview();
                }
            };

            cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try{
                        cameraCaptureSession.capture(captureBuilder.build(),captureListener,mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                }
            },mBackgroundHandler);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createCameraPreview() {
        try{
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert  texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(),imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if(cameraDevice == null)
                        return;
                    try {
                        cameraCaptureSessions = cameraCaptureSession;
                        updatePreview();

                    }catch (Exception e){
                        Toast.makeText(getActivity() , String.valueOf(e) , Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    try {
                        Toast.makeText(getActivity(), "Changed", Toast.LENGTH_SHORT).show();
                        createCameraPreview();
                    }catch (Exception e){
                        Toast.makeText(getActivity() , String.valueOf(e) , Toast.LENGTH_SHORT).show();
                    }

                }
            },null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if(cameraDevice == null)
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE,CaptureRequest.CONTROL_MODE_AUTO);
        try{
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(),null,mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void openCamera() {
        CameraManager manager = (CameraManager)getActivity().getSystemService(Context.CAMERA_SERVICE);
        try{
            cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            //Check realtime permission if run higher API 23
            if(ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(getActivity(),new String[]{
                        android.Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId,stateCallback,null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION)
        {
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getActivity(), "You can't use camera without permission", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
            startBackgroundThread();
            if(textureView.isAvailable())
                openCamera();
            else
                textureView.setSurfaceTextureListener(textureListener);

        }catch (Exception e){
            Toast.makeText(getActivity() , String.valueOf(e) , Toast.LENGTH_SHORT).show();
        }

    }

//    @Override
//    public void onPause() {
//        stopBackgroundThread();
//        super.onPause();
//    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread= null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void startBackgroundThread() {
//        mBackgroundThread = new HandlerThread("Camera Background");
//        mBackgroundThread.start();
//        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }


    void konumbilgisi() {
        try {
            fusedLocationProviderClient  = LocationServices.getFusedLocationProviderClient(getActivity());
            izin_kontol = ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.ACCESS_FINE_LOCATION);
            locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    location1 = task.getResult();
                   // Toast.makeText(getActivity(),String.valueOf(location1.getLatitude()) , Toast.LENGTH_SHORT).show();
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

    public Bitmap drawTextToBitmap(Bitmap bitmap , String gggtext) {

        String gText  =String.valueOf(date_c+current_ddate);
        Bitmap bitmapp = bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        Resources resources = getActivity().getResources();
        float scale = resources.getDisplayMetrics().density;
        // Bitmap bitmap =  BitmapFactory.decodeResource(resources, gResId);

        android.graphics.Bitmap.Config bitmapConfig =
                bitmapp.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmapp = bitmapp.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmapp);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.RED);
        // text size in pixels
//        if(bitmapp.getHeight() < 800){
//            paint.setTextSize((int) (6 * scale));
//            x =50;
//            y = 50;
//        }else if(bitmapp.getHeight()> 3500){
//            paint.setTextSize((int) (32 * scale));
//             x =100;
//             y = 120;
//        }
        paint.setTextSize((int) ((55 * scale)));
        x =(bitmapp.getWidth()/120)*4;
        y = (bitmapp.getHeight()/120)*5;

        // text shadow
        //paint.setShadowLayer(0f, 0f, 0f, Color.);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        Toast.makeText(getActivity() , String.valueOf(bitmapp.getHeight()+ " " + bitmapp.getWidth()) , Toast.LENGTH_SHORT).show();

        // (bitmapp.getHeight() + bounds.height()-15);

        canvas.drawText(gText, x, y, paint);

       return drawTextToBitmap22(bitmapp , String.valueOf(location_text));
    }
    public Bitmap drawTextToBitmap22(Bitmap bitmap , String gggtext) {

        String gText  =String.valueOf(location_text);
        Bitmap bitmapp = bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        Resources resources = getActivity().getResources();
        float scale = resources.getDisplayMetrics().density;
        // Bitmap bitmap =  BitmapFactory.decodeResource(resources, gResId);

        android.graphics.Bitmap.Config bitmapConfig =
                bitmapp.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmapp = bitmapp.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmapp);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.RED);
        // text size in pixels
//        if(bitmapp.getHeight() < 800){
//            paint.setTextSize((int) (6 * scale));
//            x =50;
//            y = 50;
//        }else if(bitmapp.getHeight()> 3500){
//            paint.setTextSize((int) (32 * scale));
//             x =100;
//             y = 120;
//        }
        paint.setTextSize((int) ((55 * scale)));
        x =(bitmapp.getWidth()/120)*4;
        y = ((bitmapp.getHeight()/120)*5)+110;

        // text shadow
        //paint.setShadowLayer(0f, 0f, 0f, Color.);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        Toast.makeText(getActivity() , String.valueOf(bitmapp.getHeight()+ " " + bitmapp.getWidth()) , Toast.LENGTH_SHORT).show();

        // (bitmapp.getHeight() + bounds.height()-15);

        canvas.drawText(gText, x, y, paint);

        return bitmapp;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // open location permition reguest
        if(requestCode ==101){
            if(requestCode ==RESULT_OK)
            {
                Toast.makeText(getActivity() , " now GPS is enable" , Toast.LENGTH_SHORT).show();

            }
            if(requestCode == RESULT_CANCELED){
                Toast.makeText(getActivity() , " denied GPS  enable" , Toast.LENGTH_SHORT).show();

            }
        }

    }
}