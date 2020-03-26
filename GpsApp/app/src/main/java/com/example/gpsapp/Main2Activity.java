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
    private Sensor accelerometer;
    private Sensor magnetic;

    float [] history = new float[2];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

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
        sensorManager.unregisterListener(sensorEventListener, stepCounter);
        sensorManager.unregisterListener(sensorEventListener, accelerometer);
        sensorManager.unregisterListener(sensorEventListener, magnetic);

    }


    private final SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            // Mettre à jour uniquement dans le cas de notre capteur
            if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

                Log.d("oui","oskour");

                TextView txtpas = findViewById(R.id.Podometre);
                txtpas.setText(String.valueOf(event.values[0]));
            }

            if (event.sensor == accelerometer) {
                TextView direction = findViewById(R.id.direction);

                float xChange = history[0] - event.values[0];
                float yChange = history[1] - event.values[1];

                history[0] = event.values[0];
                history[1] = event.values[1];

                if (xChange > 2.5){
                    direction.setText("Gauche");
                }
                else if (xChange < -2.5){
                    direction.setText("Droite");
                }

                if (yChange > 2.5){
                    direction.setText("Bas");
                }
                else if (yChange < -2.5){
                    direction.setText("Haut");
                }


            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    };




    @Override
    protected void onResume() {
        super.onResume();

        mLastAccelerometerSet = false;
        mLastMagnetometerSet = false;

        sensorManager.registerListener(sensorEventListener, stepCounter, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorEventListener, magnetic, SensorManager.SENSOR_DELAY_FASTEST);

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
