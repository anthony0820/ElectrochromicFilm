package com.example.electrochromicfilm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    private ImageView bluetoothButton;
    private ImageView chooseWindowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);
        initListeners();
    }

    protected void initListeners() {
        bluetoothButton = (ImageView) findViewById(R.id.connectBluetooth);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Bluetooth.class);
                startActivity(intent);
            }
        });

        chooseWindowButton = (ImageView) findViewById(R.id.chooseWindow);
        chooseWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
