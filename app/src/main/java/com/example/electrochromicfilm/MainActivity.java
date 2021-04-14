package com.example.electrochromicfilm;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUGGING_TAG";
    private ImageView front_driver_window;
    private ImageView front_passenger_window;
    private ImageView back_driver_window;
    private ImageView back_passenger_window;
    String deviceAddress;
    String deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        // Transfers data to this activity, but maybe we can still see this information when we go
        // the next activity instead of having to bundle everything again
        Bundle bundle = getIntent().getExtras();
        deviceName = bundle.getString("device");
        deviceAddress = bundle.getString("address");
        Log.i(TAG, deviceName + ": " + deviceAddress);
        initListeners();
    }

    protected void initListeners(){
        front_driver_window = (ImageView) findViewById(R.id.front_driver_window);
        front_driver_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Scrollbar.class);
                Bundle bundle = new Bundle();
                bundle.putString("address", deviceAddress);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        front_passenger_window = (ImageView) findViewById(R.id.front_passenger_window);
        front_passenger_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Scrollbar.class);
                startActivity(intent);
            }
        });
        back_driver_window = (ImageView) findViewById(R.id.back_driver_window);
        back_driver_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Scrollbar.class);
                startActivity(intent);
            }
        });
        back_passenger_window = (ImageView) findViewById(R.id.back_passenger_window);
        back_passenger_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Scrollbar.class);
                startActivity(intent);
            }
        });

    }
}