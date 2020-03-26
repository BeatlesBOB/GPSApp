package com.example.capteurapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ProxiActivity extends AppCompatActivity {

    // Valeur courante de la proximité
    float p;

    // Le sensor manager
    SensorManager sensorManager;
    TextView txtproxi;

    // Le capteur de proximité
    Sensor proximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxi);

        // Instancier le SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Instancier le capteur de lumière
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        txtproxi= findViewById(R.id.txtprox);
    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorListener, proximity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            // Mettre à jour uniquement dans le cas de notre capteur
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                // La valeur de la lumière
                p = event.values[0];

                if (p < 1){
                    Log.d("dist","pre");
                    txtproxi.setText("Vous etes pres");
                }else{
                    Log.d("dist","loin");
                    txtproxi.setText("Vous etes loin");
                }
                // faire autre chose&#8230;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
