package com.example.gpsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gpsapp.Service.GpsService;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        Button btn2 = findViewById(R.id.btnexo2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(MainActivity.this, GpsService.class);
                    startService(intent);
                    Toast.makeText(this, "Service GPS lancé", Toast.LENGTH_SHORT).show();

                } else {

                }
                return;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Location locatrack = (Location) intent.getExtras().get("location");
                    double latitude = locatrack.getLatitude();
                    double longitude = locatrack.getLongitude();
                    TextView distance = ((TextView) findViewById(R.id.Location));
                    distance.setText("Latitude :"+latitude+" Longitude :"+longitude);
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
        Log.d("aled","stpo");
        Intent intent = new Intent(MainActivity.this, GpsService.class);
        stopService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("aled","pause");
        Intent intent = new Intent(MainActivity.this, GpsService.class);
        stopService(intent);
    }
}
