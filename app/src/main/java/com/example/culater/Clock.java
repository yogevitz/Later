package com.example.culater;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

public class Clock extends AppCompatActivity {

    private Chronometer chronometer;
    private int point;
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        chronometer = findViewById(R.id.chronometer);
        score = findViewById(R.id.score_TextView);
        chronometer.start();

    }

    /**
     * In every 30 second you earn 1 point.
     * now you just show it to screen by click the button
     * @param view
     */
    public void countPoint(View view){
        int elapsedMillis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
        System.out.println(elapsedMillis);
        point = elapsedMillis/30;
        score.setText("Point : "+point);

    }

    /**
     * back to Menu
     * @param v
     */
    public void backToMenu(View v){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}
