package com.example.culater;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Menu extends AppCompatActivity {

    private Button startClock;
    private Button store;
    private Button flag;
    private TextView coordinates_TextView;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        startClock = (Button) findViewById(R.id.getLocation_Btn);
        store = (Button) findViewById(R.id.store_Btn);
        flag = (Button) findViewById(R.id.inside_Btn);
        coordinates_TextView = (TextView) findViewById(R.id.location_TextView);

        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(Menu.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(Menu.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double Latitude = location.getLatitude();
                    double Longitude = location.getLongitude();
                    coordinates_TextView.setText(Latitude + " " + Longitude);
                    if (availableCoordinates(Latitude, Longitude)) {
                        flag.setBackgroundColor(Color.GREEN);
                        startClock.setEnabled(true);
                    } else {
                        flag.setBackgroundColor(Color.RED);
                    }
                }
            }
        });

        startClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClock();
            }
        });

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStore();
            }
        });
    }

    /**
     * Get permission from user to get phone location
     */
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
    }

    /**
     *  check if Coordinates is inside in the university
     *  get university coordinates from google
     * @param latit
     * @param longit
     * @return true - inside , false - outside
     */
    private boolean availableCoordinates(double latit, double longit) {
        double North = 31.264972441750654; // latitude
        double South = 31.260913230180165; // latitude
        double East = 34.80587469500858; // longitude
        double West = 34.798327688240306; // longitude

        if(North > latit && South < latit && East > longit && West < longit)
            return true;
        return false;

    }

    /**
     * Move to clock class
     */
    public void openClock() {
        Intent intent = new Intent(this, Clock.class);
        startActivity(intent);
    }

    public void openStore() {
        Intent intent = new Intent(this, Store.class);
        startActivity(intent);
    }
}
