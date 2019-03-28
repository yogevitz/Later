package com.example.culater;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Store extends AppCompatActivity {

    // Coupons buttons
    public Button coupon1;
    public Button coupon2;
    public Button coupon3;
    public Button coupon4;
    public Button coupon5;
    public Button coupon6;

    // Coupon prices text view
    private TextView coupon1_price_text;
    private TextView coupon2_price_text;
    private TextView coupon3_price_text;
    private TextView coupon4_price_text;
    private TextView coupon5_price_text;
    private TextView coupon6_price_text;

    // Points label
    private TextView points_number;

    // Coupon prices
    private int coupon1_price = 5;
    private int coupon2_price = 7;
    private int coupon3_price = 10;
    private int coupon4_price = 12;
    private int coupon5_price = 15;
    private int coupon6_price = 22;

    // Current number of points
    private int points;

    // Users email address
    private String userEmail = "";

    // Database helper for updating
    private DataBaseHelper mDataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        // Get db helper settings
        mDataBaseHelper = new DataBaseHelper(this);
        userEmail = getIntent().getStringExtra("EMAIL");

        // Set points label
        points = Integer.parseInt(getIntent().getStringExtra("POINTS"));
        points_number = (TextView) findViewById(R.id.points_number);
        points_number.setText(points + "");

        // Coupons buttons
        coupon1 = (Button) findViewById(R.id.coupon1);
        coupon2 = (Button) findViewById(R.id.coupon2);
        coupon3 = (Button) findViewById(R.id.coupon3);
        coupon4 = (Button) findViewById(R.id.coupon4);
        coupon5 = (Button) findViewById(R.id.coupon5);
        coupon6 = (Button) findViewById(R.id.coupon6);

        // Coupon prices
        coupon1_price_text = (TextView) findViewById(R.id.coupon1_price);
        coupon2_price_text = (TextView) findViewById(R.id.coupon2_price);
        coupon3_price_text = (TextView) findViewById(R.id.coupon3_price);
        coupon4_price_text = (TextView) findViewById(R.id.coupon4_price);
        coupon5_price_text = (TextView) findViewById(R.id.coupon5_price);
        coupon6_price_text = (TextView) findViewById(R.id.coupon6_price);

        // Set the prices of the coupons
        coupon1_price_text.setText(coupon1_price + "");
        coupon2_price_text.setText(coupon2_price + "");
        coupon3_price_text.setText(coupon3_price + "");
        coupon4_price_text.setText(coupon4_price + "");
        coupon5_price_text.setText(coupon5_price + "");
        coupon6_price_text.setText(coupon6_price + "");

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

        coupon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Store.this);
                builder.setMessage("Espresso Coupon\nBuy an espresso for 1$ instead of 3$\n" +
                        "Do you want to use the coupon?")
                        .setPositiveButton("Use Coupon", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onClickUseCoupon(1);
                            }
                        })
                        .setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        coupon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Store.this);
                builder.setMessage("Americano Coupon\nBuy an americano for 1$ instead of 3$\n" +
                        "Do you want to use the coupon?")
                        .setPositiveButton("Use Coupon", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onClickUseCoupon(2);
                            }
                        })
                        .setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        coupon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Store.this);
                builder.setMessage("Latte Coupon\nBuy a latte for 1$ instead of 3$\n" +
                        "Do you want to use the coupon?")
                        .setPositiveButton("Use Coupon", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onClickUseCoupon(3);
                            }
                        })
                        .setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        coupon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Store.this);
                builder.setMessage("Coffee and Pastry Coupon\nBuy coffee and pastry for 1$" +
                        "instead of 3$\n" +
                        "Do you want to use the coupon?")
                        .setPositiveButton("Use Coupon", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onClickUseCoupon(4);
                            }
                        })
                        .setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        coupon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Store.this);
                builder.setMessage("Chocolate Coupon\nBuy chocolate for 1$ instead of 3$\n" +
                        "Do you want to use the coupon?")
                        .setPositiveButton("Use Coupon", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onClickUseCoupon(5);
                            }
                        })
                        .setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        coupon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Store.this);
                builder.setMessage("Sandwich Coupon\nBuy a Sandwich for 1$ instead of 3$\n" +
                        "Do you want to use the coupon?")
                        .setPositiveButton("Use Coupon", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onClickUseCoupon(6);
                            }
                        })
                        .setNegativeButton("Cancel", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void onClickUseCoupon(int coupon_number) {
        int current_price = 0;
        switch (coupon_number) {
            case 1: current_price = coupon1_price; break;
            case 2: current_price = coupon2_price; break;
            case 3: current_price = coupon3_price; break;
            case 4: current_price = coupon4_price; break;
            case 5: current_price = coupon5_price; break;
            case 6: current_price = coupon6_price; break;
        }
        points = points - current_price;
        points_number.setText(points + "");
        updateInDB(- current_price, userEmail);
        disableNotRelevantCoupons();
        enableRelevantCoupons();
    }

    // Enable the relevant coupons
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

    // Disable not relevant coupons
    protected void disableNotRelevantCoupons() {
        if (points < coupon1_price)
            coupon1.setEnabled(false);
        if (points < coupon2_price)
            coupon2.setEnabled(false);
        if (points < coupon3_price)
            coupon3.setEnabled(false);
        if (points < coupon4_price)
            coupon4.setEnabled(false);
        if (points < coupon5_price)
            coupon5.setEnabled(false);
        if (points < coupon6_price)
            coupon6.setEnabled(false);
    }

    // Update points in DB
    private void updateInDB(int count, String userEmail) {
        mDataBaseHelper.updatePoints(count, userEmail);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(this, Menu.class);
            intent.putExtra("EMAIL", (userEmail));
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

}
