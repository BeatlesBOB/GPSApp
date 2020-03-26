package com.example.gpsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gpsapp.Service.GpsService;

public class Main2Activity extends AppCompatActivity  {

    private BroadcastReceiver broadcastReceiver;
    private SensorManager sensorManager;
    private Sensor stepCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCounter != null){
            Toast.makeText(this, "Podometre lancé", Toast.LENGTH_SHORT).show();
        }

    }




    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Main2Activity.this, GpsService.class);
                    startService(intent);
                    Toast.makeText(this, "Service GPS lancé", Toast.LENGTH_SHORT).show();

                } else {

                }
                return;
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(podometreListener, stepCounter);
    }


    private final SensorEventListener podometreListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.d("oui","oskour");

            Toast.makeText(getBaseContext(),"Les temps changent, les gens changent",Toast.LENGTH_SHORT).show();

            // Mettre à jour uniquement dans le cas de notre capteur
            if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

                Log.d("oui","oskour");

                TextView txtpas = findViewById(R.id.Podometre);
                txtpas.setText(String.valueOf(event.values[0]));
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    };




    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(podometreListener, stepCounter, SensorManager.SENSOR_DELAY_FASTEST);

        if (broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    float intDisttrack = (float) intent.getExtras().get("coordinates");
                    TextView distance = ((TextView) findViewById(R.id.Distance));
                    distance.setText(String.valueOf(intDisttrack));
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
    }


}
