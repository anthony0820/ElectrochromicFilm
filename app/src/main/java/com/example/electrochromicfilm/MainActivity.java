package com.example.electrochromicfilm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private ImageView front_driver_window;
    private ImageView front_passenger_window;
    private ImageView back_driver_window;
    private ImageView back_passenger_window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListeners();
    }

    protected void initListeners(){
        front_driver_window = (ImageView) findViewById(R.id.front_driver_window);
        front_driver_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Scrollbar.class);
                startActivity(intent);
            }
        });
        front_passenger_window = (ImageView) findViewById(R.id.front_passenger_window);
        front_passenger_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Bluetooth.class);
                startActivity(intent);
            }
        });

    }
}