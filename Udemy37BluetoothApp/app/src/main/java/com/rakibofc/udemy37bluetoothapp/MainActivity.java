package com.rakibofc.udemy37bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView lv_deviceList;
    TextView tv_searching;
    Button btn_search;
    ArrayList<String> bluetoothDevice;
    ArrayList<String> deviceAddress;
    ArrayAdapter arrayAdapter;

    BluetoothAdapter bluetoothAdapter;

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                tv_searching.setText("Finished");
                btn_search.setEnabled(true);

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));

                String deviceString;

                if (name == null || name.equals("")) {

                    deviceString = address + " - RSSI " + rssi + "dBm";
                    
                } else {
                    deviceString = name + " - RSSI " + rssi + "dBm";
                }
                bluetoothDevice.add(deviceString);
                arrayAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_deviceList = findViewById(R.id.lv_deviceList);
        tv_searching = findViewById(R.id.tv_searching);
        btn_search = findViewById(R.id.btn_search);

        bluetoothDevice = new ArrayList<>();
        deviceAddress = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, bluetoothDevice);
        lv_deviceList.setAdapter(arrayAdapter);

        btn_search.setOnClickListener(this);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onClick(View view) {

        bluetoothDevice.clear();

        tv_searching.setText("Searching...");
        btn_search.setEnabled(false);
        bluetoothAdapter.startDiscovery();
    }
}