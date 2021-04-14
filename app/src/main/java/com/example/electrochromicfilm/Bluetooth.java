package com.example.electrochromicfilm;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;

public class Bluetooth extends AppCompatActivity {

    ImageView b1,b3;
    Button b2, b4;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ListView lv;
    private ArrayList list;
    private ArrayList addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.bluetoothactivity);

        b1 = (ImageView) findViewById(R.id.turn_on_button);
//        b2 = (Button) findViewById(R.id.get_visible_button);
        b3 = (ImageView) findViewById(R.id.list_devices_button);
//        b4 = (Button) findViewById(R.id.off_button);
        BA = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView) findViewById(R.id.devicelist);
    }

    // Enable the Bluetooth
    public void on(View v) {
        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
        }
    }

//    // Disable the bluetooth module
//    public void off(View v) {
//        BA.disable();
//        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
//    }

//    // Get all visible bluetooth modules around
//    public void visible(View v) {
//        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        startActivityForResult(getVisible, 0);
//    }

    // Creates a list of all visible bluetooth modules
    // Will later add buttons to each in order to click on one we wish to connect to
    public void list(View v) {
        pairedDevices = BA.getBondedDevices();

        list = new ArrayList();
        addresses = new ArrayList();

        for (BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
            addresses.add(bt.getAddress());
        }
        Toast.makeText(getApplicationContext(), "Showing Paired Devices", Toast.LENGTH_SHORT).show();

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
        setDeviceListener();
    }

    // This is what happens when a device from the list is selected
    public void setDeviceListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String deviceName = ((TextView)v).getText().toString();
                int index = list.indexOf(deviceName);
                String deviceAddress = addresses.get(index).toString();
                Intent intent = new Intent(Bluetooth.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("device",deviceName);
                bundle.putString("address", deviceAddress);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}




