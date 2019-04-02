package com.example.culater;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
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
    private DataBaseHelper mDataBaseHelper;

    private String newPoints;
    private Activity a = this;

    // notification
    private String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
/*
        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
*/
        // initialize
        startClock = (Button) findViewById(R.id.getLocation_Btn);
        store = (Button) findViewById(R.id.store_Btn);
        flag = (Button) findViewById(R.id.inside_Btn);
        hourFlag = (Button) findViewById(R.id.hour_Btn);
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
                    Resources res = getResources();
                    Drawable drawable = res.getDrawable(R.drawable.gps_red);
                    Drawable drawable2 = res.getDrawable(R.drawable.gps);
                    Drawable drawable3 = res.getDrawable(R.drawable.time_access);
                    Drawable drawable4 = res.getDrawable(R.drawable.time_access_red);
                    if (availableCoordinates(Latitude, Longitude) && availablehours()) {


                        //flag.setBackground(drawable);

                        flag.setBackground(drawable2);
                        hourFlag.setBackground(drawable3);
                        startClock.setEnabled(true);
                    }
                    else {
                        showAvalabilityIndication(Latitude, Longitude);
                        flag.setBackground(drawable2);
                        startClock.setEnabled(true);
                    }
                }
            }
        });

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
        Resources res = getResources();
        Drawable gps_green = res.getDrawable(R.drawable.gps);
        Drawable gps_red = res.getDrawable(R.drawable.gps_red);

        Drawable time_green = res.getDrawable(R.drawable.time_access);
        Drawable time_red = res.getDrawable(R.drawable.time_access_red);
        //flag.setBackground(drawable);
        if (availableCoordinates(Latitude, Longitude)){
            flag.setBackground(gps_green);
            hourFlag.setBackground(time_red);
        }
        else if(availablehours()){
            flag.setBackground(gps_green);
            hourFlag.setBackground(time_green);
        }
        else {
            flag.setBackground(gps_green);
            hourFlag.setBackground(time_red);
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
//        if(currentHourIn24Format > 8 && currentHourIn24Format < 20)
//            return true;
//        return false;
        return true;
    }
}
