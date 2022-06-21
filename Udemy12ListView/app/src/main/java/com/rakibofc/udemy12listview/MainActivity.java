package com.rakibofc.udemy12listview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView my_list_view = findViewById(R.id.my_list_view);

        ArrayList<String> myFamily = new ArrayList<>();

        myFamily.add("Rakib");
        myFamily.add("Yamin");
        myFamily.add("Sathi");
        myFamily.add("Ajmira");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myFamily);

        my_list_view.setAdapter(arrayAdapter);

        my_list_view.setOnItemClickListener((parent, view, position, id) -> {

            Log.i("Person Selected", myFamily.get(position));

            Toast.makeText(this, myFamily.get(position), Toast.LENGTH_SHORT).show();
        });
    }
}