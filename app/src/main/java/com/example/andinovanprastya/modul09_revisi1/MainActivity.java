package com.example.andinovanprastya.modul09_revisi1;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements DapatkanAlamatTask.onTaskSelesai{

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Button mLocationButton;
    private TextView mLocationTextView;
    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private ImageView mAndroidImageView;
    private AnimatorSet mRotateAnim;
    private boolean mTrackingLocation;
    private FusedLocationProviderClient getmFusedLocationClient;
    private LocationCallback mLocationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationButton = (Button) findViewById(R.id.button_location);
        mLocationTextView = (TextView) findViewById(R.id.textview_location);
        mAndroidImageView = (ImageView) findViewById(R.id.imageview_android);

        //Animasi
        mRotateAnim = (AnimatorSet) AnimatorInflater.loadAnimator
                (this, R.animator.rotate);
        mRotateAnim.setTarget(mAndroidImageView);


        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (!mTrackingLocation) {
                    mulaiTrackingLokasi();
                }else{
                    stopTrackingLokasi();
                }
//                mulaiTrackingLokasi();
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // jika tracking aktif, proses reverse menjadi data alamat
                if (mTrackingLocation){
                    new DapatkanAlamatTask(MainActivity.this,
                            MainActivity.this)
                            .execute(locationResult.getLastLocation());
                }
            }
        };
    }


    private void mulaiTrackingLokasi() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
//            Log.d("GETPERMISI", "mulaiTrackingLokasi: permissions granted");
//
            mFusedLocationClient.requestLocationUpdates
                    (getLocationRequest(),mLocationCallback,null);
//            mFusedLocationClient.getLastLocation().addOnSuccessListener(
//                    new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            if (location != null) {
//                                // lakukan reverse geocode AsyncTask
//                                new DapatkanAlamatTask(MainActivity.this,
//                                        MainActivity.this).execute(location);
//                            } else {
//                                mLocationTextView.setText("Lokasi tidak tersedia");
//                            }
////                            if (location != null){
////                                mLastLocation = location;
////                                mLocationTextView.setText(
////                                        getString(R.string.location_text,
////                                                mLastLocation.getLatitude(),
////                                                mLastLocation.getLongitude(),
////                                                mLastLocation.getTime()));
////                            } else {
////                                mLocationTextView.setText("Lokasi tidak tersedia");
////                            }
//                        }
//                    }
//            );
        }

        mLocationTextView.setText(getString(R.string.alamat_text,
                "Sedang mencari alamat doi",
                System.currentTimeMillis()));

        mTrackingLocation = true;
        mLocationButton.setText("Stop Tracking Lokasi");
        mRotateAnim.start();

    }


    @Override

    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {

        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // jika permission diijinkan, mulaiTrackingLokasi()
                // jika tidak, tampilkan Toast

                if (grantResult.length > 0
                        && grantResult[0] == PackageManager.PERMISSION_GRANTED){
                    mulaiTrackingLokasi();
                } else {
                    Toast.makeText(this,
                            "Permission bapakknya gak dapet masbro, mesakke", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        // update ui dengan tampilan hasil alamat
//        mLocationTextView.setText(getString(R.string.alamat_text,result,System.currentTimeMillis()));
        if (mTrackingLocation) {
            mLocationTextView.setText(getString(R.string.alamat_text,
                    result, System.currentTimeMillis()));
        }
    }

    private void stopTrackingLokasi(){
        if (mTrackingLocation) {
            mTrackingLocation = false;
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            mLocationButton.setText("Mulai Tracking Lokasi");
            mLocationTextView.setText("Tracking sedang dihentikan");
            mRotateAnim.end();
        }
    }

    private LocationRequest getLocationRequest(){
        LocationRequest locationRequest = new LocationRequest();

        // digunakan untuk seberapa sering update lokasi yg diinginkan
        locationRequest.setInterval(10000);

        // adalah seberapa sering update lokasi dari app lain yg meminta req lokasi
        locationRequest.setFastestInterval(5000);

        // parameter untuk memilih akurasi dan akurasi tinggi menggunakan GPS
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }
}
