package com.example.culater;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Menu extends AppCompatActivity {

    private Button startClock;
    private Button store;
    private Button flag; // red : outside, green : inside
    private Button hourFlag;
    private TextView coordinates_TextView;
    private TextView userPoints;
    private FusedLocationProviderClient client;
    private DataBaseHelper mDataBaseHelper ;

    private String newPoints;
    private Activity a = this;
//    private Thread tHours;

    private String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // initialize
        startClock = (Button) findViewById(R.id.getLocation_Btn);
        store = (Button) findViewById(R.id.store_Btn);
        flag = (Button) findViewById(R.id.inside_Btn);
        hourFlag = (Button) findViewById(R.id.hour_Btn);
        coordinates_TextView = (TextView) findViewById(R.id.location_TextView);
        userPoints = (TextView) findViewById(R.id.userPoints_textView);
        mDataBaseHelper = new DataBaseHelper(this);

        getUserEmail();
        getUserPoints();


        requestPermission(); // get permission for location

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
                    if (availableCoordinates(Latitude, Longitude) && availablehours()) {
                        flag.setBackgroundColor(Color.GREEN);
                        hourFlag.setBackgroundColor(Color.GREEN);
                        startClock.setEnabled(true);
//                        startCheckHours();
//                        tHours.start();
                    }
                    else {
                        showAvalabilityIndication(Latitude, Longitude);
                    }
                }
            }
        });
//        Handler handler = new Handler();
//        int delay = 10000; //milliseconds
//        handler.postDelayed(new Runnable(){
//            public void run(){
//                requestPermission();
//
//                client = LocationServices.getFusedLocationProviderClient(a);
//                if (ActivityCompat.checkSelfPermission(Menu.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                client.getLastLocation().addOnSuccessListener(Menu.this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
//                            double Latitude = location.getLatitude();
//                            double Longitude = location.getLongitude();
//                            System.out.println(Latitude);
//                            System.out.println(Longitude);
//                            coordinates_TextView.setText(Latitude + " " + Longitude);
//                            if (availableCoordinates(Latitude, Longitude)) {
//                                flag.setBackgroundColor(Color.GREEN);
//                                startClock.setEnabled(true);
//                            } else {
//                                flag.setBackgroundColor(Color.RED);
//                            }
//                        }
//                    }
//                });
//                handler.postDelayed(this, delay);
//            }
//        }, delay);
        // move to clock class
        startClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClock();
            }
        });
        // move to store class
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStore();
            }
        });
    }

    /**
     * get user point from logIn DB
     * get point to add to user from clock
     */
    private void getUserPoints() {

        newPoints = mDataBaseHelper.getPoints(userEmail)+"";
        userPoints.setText("Points : " +newPoints);
    }

    private void getUserEmail(){
        if (getIntent().getStringExtra("EMAIL") != null){
            userEmail = getIntent().getStringExtra("EMAIL");
        }
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

        return North > latit && South < latit && East > longit && West < longit;

    }

    /**
     * Show user status indication
     * why he is not available to use in app
     * @param Latitude
     * @param Longitude
     */
    private void showAvalabilityIndication(double Latitude, double Longitude){
        if (availableCoordinates(Latitude, Longitude)){
            flag.setBackgroundColor(Color.GREEN);
            hourFlag.setBackgroundColor(Color.RED);
        }
        else if(availablehours()){
            flag.setBackgroundColor(Color.RED);
            hourFlag.setBackgroundColor(Color.GREEN);
        }
        else {
            flag.setBackgroundColor(Color.RED);
            hourFlag.setBackgroundColor(Color.RED);
        }
    }

    /**
     * Move to clock class
     */
    public void openClock() {
        Intent intent = new Intent(this, Clock.class);
        intent.putExtra("USER_POINTS", newPoints);
        intent.putExtra("EMAIL", userEmail);
        startActivity(intent);
    }

    /**
     * Move to Store class with user points
     */
    public void openStore() {
        Intent intent = new Intent(this, Store.class);
        intent.putExtra("POINTS", newPoints);
        intent.putExtra("EMAIL", userEmail);
        startActivity(intent);
    }


    /**
     *  check if hours is available
     * @return true - available , false - not available
     */
    private boolean availablehours() {
        Calendar rightNow = Calendar.getInstance();
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
        if(currentHourIn24Format > 8 && currentHourIn24Format < 20)
            return true;
        return false;

    }

//    /**
//     * Separated thread that checking the hours
//     */
//    private void startCheckHours(){
//
//        tHours = new Thread(){
//
//            public void run(){
//                while(availablehours()){
//                    try{
//                        Thread.sleep(1000);
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        startClock.setEnabled(false);
//
//                    }
//                }
//            }
//        };
//
//    }
}
