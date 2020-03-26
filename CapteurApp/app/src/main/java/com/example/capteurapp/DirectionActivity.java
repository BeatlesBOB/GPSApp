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
import android.widget.TextView;

public class DirectionActivity extends AppCompatActivity {

        private SensorManager sensorManager = null;
        private Sensor accelerometre = null;
        private TextView txtdirection;
        float [] history = new float[2];



    final SensorEventListener mSensorEventListener = new SensorEventListener() {


            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {

                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                    float xChange = history[0] - sensorEvent.values[0];
                    float yChange = history[1] - sensorEvent.values[1];

                    history[0] = sensorEvent.values[0];
                    history[1] = sensorEvent.values[1];

                    if (xChange > 2.5){
                        txtdirection.setText("Gauche");
                    }
                    else if (xChange < -2.5){
                        txtdirection.setText("Droite");
                    }

                    if (yChange > 2.5){
                        txtdirection.setText("Bas");
                    }
                    else if (yChange < -2.5){
                        txtdirection.setText("Haut");
                    }

                }

            }
        };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_direction);
            sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
            accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            Button btn5 = findViewById(R.id.btnexo5);
            btn5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DirectionActivity.this, LightActivity.class);
                    startActivity(intent);
                }
            });

            txtdirection = findViewById(R.id.txtdirection);
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










