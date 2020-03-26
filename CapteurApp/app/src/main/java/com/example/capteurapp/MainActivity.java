package com.example.capteurapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> sensorlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> liste = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorlist = new ArrayList<String>();
        for (int i = 0 ; i < liste.size() ; i++){
            sensorlist.add(liste.get(i).getName());
            Log.d("value" , liste.get(i).getName().toString());
        }
        ListView listview = findViewById(R.id.lstsensor);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1,sensorlist);
        listview.setAdapter(adapter);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            Toast.makeText(this, "Vous avez le sensor : température ambiante", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Vous n'avez pas le sensor : température ambiante", Toast.LENGTH_SHORT).show();

        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            Toast.makeText(this, "Vous avez le sensor : ACCELEROMETER", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Vous n'avez pas le sensor : TYPE_ACCELEROMETER", Toast.LENGTH_SHORT).show();
        }

        Button btn3 = findViewById(R.id.btneexo3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccelActivity.class);
                startActivity(intent);
            }
        });

    }

}
