package com.example.culater;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.List;
import java.util.Properties;

public class Clock extends AppCompatActivity {

    private Chronometer chronometer;
    private int point;
    private TextView score;
    private int userScoreBeforeAdding;
    private int count;
    private DataBaseHelper mDataBaseHelper ;
    private String userEmail;

    private Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        chronometer = findViewById(R.id.chronometer);
        score = findViewById(R.id.score_TextView);
        count = 0;
        userEmail = getIntent().getStringExtra("EMAIL");
        mDataBaseHelper = new DataBaseHelper(this);


        userScoreBeforeAdding = Integer.parseInt(getIntent().getStringExtra("USER_POINTS"));

        startClock();
        startTimer();
        t.start();


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

    private int idle (){
        ActivityManager am =(ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo task = tasks.get(0); // current task
        ComponentName rootActivity = task.baseActivity;

        String currentPackageName = rootActivity.getPackageName();
        if (!currentPackageName.equals("com.example.culater")) {
            System.out.println("Out");
        }
        return 2;
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
        updateInDB();
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("POINTS_TO_ADD", (userScoreBeforeAdding+count)+"");
        startActivity(intent);
    }

    private void updateInDB() {
        mDataBaseHelper.updatePoints(count,userEmail);
    }
}
