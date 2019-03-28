package com.example.culater;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class Clock extends AppCompatActivity {

    private Chronometer chronometer;
    private int point;
    private TextView score;
    private int userScoreBeforeAdding;
    private int count;

    private Thread t;
    private Thread tHours;
    private Thread tIdle;

//    private Thread tCoordinates;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        backButton = (Button) findViewById(R.id.backButton);
        chronometer = findViewById(R.id.chronometer);
        score = findViewById(R.id.score_TextView);
        count = 0;


        userScoreBeforeAdding = Integer.parseInt(getIntent().getStringExtra("USER_POINTS"));

        startClock();
        startTimer();
        t.start();
        startCheckHours();
        tHours.start();
//        startCheckCoordinates();
//        tCoordinates.start();
        startCheckidle();
        tIdle.start();


        //Thread t = new Thread(()->startClock());
        //t.start();

        for(int i=0 ;i <10;i++)
            System.out.println("Dor");
    }

    private void startClock() {
        chronometer.start();

    }

    private void startTimer(){

        t = new Thread(){

            public void run(){
                while(!isInterrupted()){
                    try{
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count++;
                                score.setText("Point : "+String.valueOf(count));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }

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
        score.setText("Point : "+point);


    }

    /**
     * back to Menu
     * @param v
     */
    public void backToMenu(View v){
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("POINTS_TO_ADD", (userScoreBeforeAdding+count)+"");
        startActivity(intent);
    }

    /**
     * Separated thread that checking the hours
     */
    private void startCheckHours(){

        tHours = new Thread(){

            public void run(){
                while(!isInterrupted()){
                    try{
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!availablehours()){
                                    backButton.setClickable(true);
                                    backToMenu(backButton);
                                }
                                else
                                    System.out.println("hours itay");
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
                while(!isInterrupted()){
                    try{
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(!idle()){
                                    backButton.setClickable(true);
                                    backToMenu(backButton);
                                }
                                else
                                    System.out.println("idle itay");
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }

//    /**
//     * Separated thread that checking the Coordinates
//     */
//    private void startCheckCoordinates(){
//
//        tCoordinates = new Thread(){
//
//            public void run(){
//                while(!isInterrupted()){
//                    try{
//                        Thread.sleep(1000);
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(!availablehours()){
//                                    backButton.setClickable(true);
//                                }
//                                else
//                                    System.out.println("itay");
//                            }
//                        });
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//
//    }

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
