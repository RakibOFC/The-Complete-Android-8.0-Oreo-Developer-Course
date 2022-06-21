package com.rakibofc.udemy26multipleactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public void goToNext(View view) {

        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);

        intent.putExtra("name", "Button Clicked!");
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        ArrayList<String> family = new ArrayList<>();

        family.add("Yamin");
        family.add("Rakib");
        family.add("Ajmira");
        family.add("Sathi");
        family.add("Fatema");
        family.add("Azgar");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, family);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);

            intent.putExtra("name", family.get(position));

            startActivity(intent);

        });
    }
}