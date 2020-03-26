package com.example.capteurapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LightActivity extends AppCompatActivity {

    private CameraManager mCameraManager;
    private String mCameraId;
    private SensorManager sensorManager;
    private Sensor accelerometre = null;
    private boolean isOn = false;
    private static final float SHAKE_THRESHOLD_GRAVITY = 1.7F;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        boolean isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {
            showNoFlashError();
        }
        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometre = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //getting the camera manager and camera id
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        Button btn6 = findViewById(R.id.btnexo6);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LightActivity.this, ProxiActivity.class);
                startActivity(intent);
            }
        });

    }

    public void showNoFlashError() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Oops!");
        alert.setMessage("Flash not available in this device...");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }

    public void flashLight(boolean status) {
        try {
            mCameraManager.setTorchMode(mCameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorListener, accelerometre);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, accelerometre, SensorManager.SENSOR_DELAY_NORMAL);
    }
    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];


            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                if (isOn == false){
                    flashLight(true);
                    isOn = true;
                }else {
                    flashLight(false);
                    isOn = false;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
