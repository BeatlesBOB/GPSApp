package com.example.capteurapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

public class AccelActivity extends AppCompatActivity {

    private SensorManager sensorManager = null;
    private Sensor accelerometre = null;

    final SensorEventListener mSensorEventListener = new SensorEventListener() {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Que faire en cas de changement de précision ?
        }

        public void onSensorChanged(SensorEvent sensorEvent) {

            float x, y, z;
            LinearLayout bglinear = findViewById(R.id.coloraccel);


            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                x = sensorEvent.values[0];
                y = sensorEvent.values[1];
                z = sensorEvent.values[2];

                Log.d("accel","z: "+z);
                Log.d("accel","y: "+y);
                Log.d("accel","x: "+x);

                float acceleration = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);


                if (acceleration< 1.5){
                    bglinear.setBackgroundColor(Color.parseColor("#7FFF00"));

                }else if (acceleration< 3.5){
                    bglinear.setBackgroundColor(Color.parseColor("#00BFFF"));

                }else {
                    bglinear.setBackgroundColor(Color.parseColor("#FF0000"));
                }





            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accel);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Button btn4 = findViewById(R.id.btneexo4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccelActivity.this, DirectionActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(mSensorEventListener, accelerometre);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(mSensorEventListener, accelerometre, SensorManager.SENSOR_DELAY_NORMAL);
    }
}








