package com.example.electrochromicfilm;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Scrollbar extends AppCompatActivity {

    private SeekBar seekbar;
    private ImageView setButton;
    private Button stateButton;
    private EditText stateView;
    int tintValue = 0;
    int maxTint = 0;
    private OutputStream outputStream;
    String [][] states;
    String state;

    // GPS vars
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    double latitude, longitude;



    // Bluetooth vars
    private BluetoothAdapter bt = null;
    private BluetoothSocket btSocket = null;
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    String deviceAddress;
    boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrollbar);

        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        createStates();
        stateView = (EditText) findViewById(R.id.state);

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
                sendData(false);
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

        stateButton = (Button) findViewById(R.id.specific_state);
        stateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = stateView.getText().toString();
                getTintValues();
                sendData(true);
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
    public void sendData(Boolean specificState){
        if (!specificState) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            } else {
                getLocation();
            }
            getTintValues();
        }
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write((byte) tintValue); // Do math with maxTint and tintValue
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Write Attempt Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createStates(){
        states = new String[][]{
                {"AL", "Alabama", "32", "32", "32"},
                {"AK", "Alaska", "70", "40", "40"},
                {"AZ", "Arizona", "33", "100", "100"},
                {"AR", "Arkansas", "25", "25", "10"},
                {"CA", "California", "70", "100", "100"},
                {"CO", "Colorado", "27", "27", "27"},
                {"CT", "Connecticut", "35", "35", "100"},
                {"DC", "Washington D.C.", "70", "50", "50"},
                {"DE", "Delaware", "70", "100", "100"},
                {"FL", "Florida", "28", "15", "15"},
                {"GA", "Georgia", "32", "32", "32"},
                {"HI", "Hawaii", "35", "35", "35"},
                {"ID", "Idaho", "35", "20", "35"},
                {"IL", "Illinois", "35", "35", "35"},
                {"IN", "Indiana", "30", "30", "30"},
                {"IA", "Iowa", "70", "100", "100"},
                {"KS", "Kansas", "35", "35", "35"},
                {"KY", "Kentucky", "35", "18", "18"},
                {"LA", "Louisiana", "40", "25", "12"},
                {"ME", "Maine", "35", "100", "100"},
                {"MD", "Maryland", "35", "35", "35"},
                {"MA", "Massachusetts", "35", "35", "35"},
                {"MI", "Michigan", "100", "100", "100"},
                {"MN", "Minnesota", "50", "50", "50"},
                {"MS", "Mississippi", "28", "28", "28"},
                {"MO", "Missouri", "35", "100", "100"},
                {"MT", "Montana", "24", "14", "14"},
                {"NE", "Nebraska", "35", "20", "20"},
                {"NV", "Nevada", "35", "100", "100"},
                {"NH", "New Hampshire", "0", "35", "35"},
                {"NJ", "New Jersey", "0", "100", "100"},
                {"NM", "New Mexico", "20", "20", "20"},
                {"NY", "New York", "70", "70", "100"},
                {"NC", "North Carolina", "35", "35", "35"},
                {"ND", "North Dakota", "50", "100", "100"},
                {"OH", "Ohio", "50", "100", "100"},
                {"OK", "Oklahoma", "25", "25", "25"},
                {"OR", "Oregon", "35", "35", "35"},
                {"PA", "Pennsylvania", "70", "70", "70"},
                {"RI", "Rhode Island", "70", "70", "70"},
                {"SC", "South Carolina", "27", "27", "27"},
                {"SD", "South Dakota", "35", "20", "20"},
                {"TN", "Tennessee", "35", "35", "35"},
                {"TX", "Texas", "25", "25", "100"},
                {"UT", "Utah", "43", "100", "100"},
                {"VT", "Vermont", "0", "100", "100"},
                {"VA", "Virginia", "50", "35", "35"},
                {"WA", "Washington", "24", "24", "24"},
                {"WV", "West Virginia", "35", "35", "35"},
                {"WI", "Wisconsin", "50", "35", "35"},
                {"WY", "Wyoming", "28", "28", "28"}
        };
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                Scrollbar.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                Scrollbar.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                latitude = locationGPS.getLatitude();
                longitude = locationGPS.getLongitude();
//                showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                state = addresses.get(0).getAdminArea();
                stateView.setText("State: " + state);
            } catch (IOException e) {
                e.printStackTrace();
                stateView.setText("Failure");
            }
        }
    }

    public void getTintValues() {
        for (int i = 0; i < 51; i++){
            if (states[i][0].equalsIgnoreCase(state) || states[i][1].equalsIgnoreCase(state)){
                maxTint = Integer.parseInt(states[i][2]);
                break;
            }
        }
        Toast.makeText(this, "Max Tint: " + maxTint, Toast.LENGTH_SHORT).show();
    }
}
