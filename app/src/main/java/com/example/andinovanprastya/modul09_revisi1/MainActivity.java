package com.example.andinovanprastya.modul09_revisi1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mLocationButton;
    private static final int REQUEST_LOCATION_PERMISSION =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mLocationButton = (Button) findViewById(R.id.button_location);

        mLocationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getLocation();
            }
        });
    }

    private void getLocation(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            Log.d("GETPERMISI", "getLocation: permissions granted");
        }
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
                    getLocation();
                } else {
                    Toast.makeText(this,
                            "Permission bapakknya gak dapet masbro, mesakke", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
