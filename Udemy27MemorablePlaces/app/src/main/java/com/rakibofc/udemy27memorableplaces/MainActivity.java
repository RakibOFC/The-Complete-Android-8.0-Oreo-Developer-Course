package com.rakibofc.udemy27memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    static ArrayList<String> places;
    static ArrayList<LatLng> location;
    ArrayList<String> latitudes;
    ArrayList<String> longitudes;
    static ArrayAdapter arrayAdapter;
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        sharedPreferences = this.getSharedPreferences("com.rakibofc.udemy27memorableplaces", Context.MODE_PRIVATE);

        places = new ArrayList<>();
        location = new ArrayList<>();
        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();

        places.clear();
        location.clear();
        latitudes.clear();
        longitudes.clear();

        try {

            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places", ObjectSerializer.serialize(new ArrayList<>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("latitudes", ObjectSerializer.serialize(new ArrayList<>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longitudes", ObjectSerializer.serialize(new ArrayList<>())));


            Log.i("Info", places.toString());
            Log.i("Info", latitudes.toString());
            Log.i("Info", longitudes.toString());

        } catch (IOException e) {

            e.printStackTrace();
        }

        if (places.size() > 0 && latitudes.size() > 0 && longitudes.size() > 0) {

            if (places.size() == latitudes.size() && places.size() == longitudes.size()) {

                for (int i = 0; i < places.size(); i++) {

                    location.add(new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i))));
                }
            }
        } else {

            Log.i("Info", "First Time");
            places.add("Add a new places...");
            location.add(new LatLng(0, 0));
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

            intent.putExtra("placeNumber", position);

            startActivity(intent);
        });

    }
}