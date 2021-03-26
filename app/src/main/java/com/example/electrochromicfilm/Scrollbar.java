package com.example.electrochromicfilm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class Scrollbar extends AppCompatActivity {

    private BluetoothAdapter bt = null;
    private BluetoothSocket btSocket = null;
    private SeekBar seekbar;
    private ImageView setButton;
    int tintValue = 0;
    private OutputStream outputStream;
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    String deviceAddress;
    boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollbar);

        // Transfers data to this activity, but maybe we can still see this information when we go
        // the next activity instead of having to bundle everything again
        Bundle bundle = getIntent().getExtras();
        deviceAddress = bundle.getString("address");
        connect();
        initListeners();
    }

    protected void initListeners() {
        setButton = (ImageView) findViewById(R.id.settint);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Scrollbar.this, MainActivity.class);
//                startActivity(intent);
                sendData();
            }
        });
        seekbar=(SeekBar)findViewById(R.id.seekbar);
        // perform seek bar change listener event used for getting the progress value
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(Scrollbar.this, "Seek bar progress is: " + progressChangedValue + "%",
                        Toast.LENGTH_SHORT).show();
                tintValue = progressChangedValue;
            }
        });
    }

    // Setup a socket to communicate to the bluetooth module
    public void connect(){
        try {
            if (btSocket == null || !isConnected){
                bt = BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice hc = bt.getRemoteDevice(deviceAddress);
                btSocket = hc.createInsecureRfcommSocketToServiceRecord(PORT_UUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();
            }
        } catch (IOException e){
            Toast.makeText(getApplicationContext(), "Socket Failed to Connect", Toast.LENGTH_LONG).show();
        }
    }

    // Attempt to send the data to the bluetooth receiver.
    public void sendData(){
        if (btSocket != null) {
            try {
                // This may not work. May have to turn the tintValue into bytes (next line)
                btSocket.getOutputStream().write((byte) tintValue);
                //btSocket.getOutputStream().write(tintValue);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Write Attempt Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
