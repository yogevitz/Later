package com.example.culater;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Store extends AppCompatActivity {

    // Coupons buttons
    private Button coupon1;
    private Button coupon2;
    private Button coupon3;
    private Button coupon4;
    private Button coupon5;
    private Button coupon6;

    // Coupon prices text view
    private TextView coupon1_price_text;
    private TextView coupon2_price_text;
    private TextView coupon3_price_text;
    private TextView coupon4_price_text;
    private TextView coupon5_price_text;
    private TextView coupon6_price_text;

    // Coupon prices
    private int coupon1_price = 5;
    private int coupon2_price = 7;
    private int coupon3_price = 10;
    private int coupon4_price = 12;
    private int coupon5_price = 15;
    private int coupon6_price = 22;

    // Current number of points
    private int points = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        // Coupons buttons
        coupon1 = (Button) findViewById(R.id.coupon1);
        coupon2 = (Button) findViewById(R.id.coupon2);
        coupon3 = (Button) findViewById(R.id.coupon3);
        coupon4 = (Button) findViewById(R.id.coupon4);
        coupon5 = (Button) findViewById(R.id.coupon5);
        coupon6 = (Button) findViewById(R.id.coupon6);

        // Set the prices of the coupons
        coupon1_price_text.setText(coupon1_price);
        coupon2_price_text.setText(coupon2_price);
        coupon3_price_text.setText(coupon3_price);
        coupon4_price_text.setText(coupon4_price);
        coupon5_price_text.setText(coupon5_price);
        coupon6_price_text.setText(coupon6_price);

        // Disable all coupons
        coupon1.setEnabled(false);
        coupon2.setEnabled(false);
        coupon3.setEnabled(false);
        coupon4.setEnabled(false);
        coupon5.setEnabled(false);
        coupon6.setEnabled(false);

        // Enable the relevant coupons
        // Relevant coupons are coupons that don't cost
        // more than the current number of points
        enableRelevantCoupons();

    }

    protected void enableRelevantCoupons() {
        if (coupon1_price <= points)
            coupon1.setEnabled(true);
        if (coupon2_price <= points)
            coupon2.setEnabled(true);
        if (coupon3_price <= points)
            coupon3.setEnabled(true);
        if (coupon4_price <= points)
            coupon4.setEnabled(true);
        if (coupon5_price <= points)
            coupon5.setEnabled(true);
        if (coupon6_price <= points)
            coupon6.setEnabled(true);
    }
}
