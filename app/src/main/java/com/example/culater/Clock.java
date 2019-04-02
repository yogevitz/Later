package com.example.culater;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import me.itangqi.waveloadingview.WaveLoadingView;

public class Clock extends AppCompatActivity {

    private Chronometer chronometer;
    private TextView score;
    private DataBaseHelper mDataBaseHelper ;
    private WaveLoadingView waveLoadingView;

    private Button backButton;

    private String userEmail;
    private int count;
    private int point;

    // Threads
    private Thread t;
    private Thread tHours;
    private Thread tIdle;
    private Thread tAnimation;

    private volatile boolean runningThread = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        waveLoadingView = findViewById(R.id.waveLoadingView);
        waveLoadingView.setProgressValue(10);




        // initialize DB
        mDataBaseHelper = new DataBaseHelper(this);

        // initialize
        backButton = (Button) findViewById(R.id.backButton);
        chronometer = findViewById(R.id.chronometer);
        score = findViewById(R.id.score_TextView);

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        // count per clock is 0
        count = 0;
        userEmail = getIntent().getStringExtra("EMAIL"); // getUserMail

        startClock(); // clock start
        startTimer(); // new Thread, counter of points
        t.start(); // run counter
        startCheckHours(); // check if time between 8 - 20
        tHours.start(); // run hour Thread
        startCheckidle(); // Thread of idle ( check if user inside the app)
        tIdle.start(); // run Thread of Idle
        Animation();
        tAnimation.start();

    }

    private void Animation(){

        tAnimation = new Thread(){

            public void run(){
                while(runningThread){
                    try{
                        Thread.sleep(1000);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                waveLoadingView.setProgressValue((count*5)%100);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }



    /**
     * Start Timer
     */
    private void startClock() {
        chronometer.start();
    }

    /**
     * When you leave app
     * Update Points, stop timer, stop all Thread
     */
    protected void onUserLeaveHint(){
        mDataBaseHelper.updatePoints(count/20,userEmail);
        chronometer.stop();
        runningThread = false;
        super.onUserLeaveHint();
    }

    /**
     * Start Thread of points
     */
    private void startTimer(){

        t = new Thread(){

            public void run(){
                while(runningThread){
                    try{
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count++;
                                score.setText(String.valueOf(count/20) + "\n" +"earned" );
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    /**
     * check if user is inside of app screen
     * @return true if inside, otherwise false
     */
    private boolean idle (){
        ActivityManager am =(ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo task = tasks.get(0); // current task
        ComponentName rootActivity = task.baseActivity;

        String currentPackageName = rootActivity.getPackageName();
        if (!currentPackageName.equals("com.example.culater")) {
            System.out.println("Out");
            return false;
        }
        return true;
    }

    /**
     * In every 30 second you earn 1 point.
     * now you just show it to screen by click the button
     * @param view
     */
    public void countPoint(View view){
        int elapsedMillis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
        System.out.println(elapsedMillis);
        point = elapsedMillis;
        score.setText("Point : "+(point/20));
    }

    /**
     * back to Menu
     * @param v
     */
    public void backToMenu(View v){
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("EMAIL", userEmail);
        startActivity(intent);
    }


    /**
     * Separated thread that checking the hours
     */
    private void startCheckHours(){

        tHours = new Thread(){

            public void run(){
                while(runningThread){
                    try{
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!availablehours()){
                                    runningThread = false;
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }
    /**
     * Separated thread that checking the idle
     */
    private void startCheckidle(){

        tIdle = new Thread(){

            public void run(){
                while(runningThread){
                    try{
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!idle()){
                                    runningThread = false;
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }

    /**
     * When click on back button in your phone
     * @param keyCode
     * @param event
     * @return true
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME ) {
            mDataBaseHelper.updatePoints(point,userEmail);
        }
        return super.onKeyDown(keyCode, event);
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

//    /**
//     *  check if Coordinates is inside in the university
//     *  get university coordinates from google
//     * @param latit
//     * @param longit
//     * @return true - inside , false - outside
//     */
//    private boolean availableCoordinates(double latit, double longit) {
//        double North = 31.264972441750654; // latitude
//        double South = 31.260913230180165; // latitude
//        double East = 34.80587469500858; // longitude
//        double West = 34.798327688240306; // longitude
//
//        return North > latit && South < latit && East > longit && West < longit;
//
//    }
}
