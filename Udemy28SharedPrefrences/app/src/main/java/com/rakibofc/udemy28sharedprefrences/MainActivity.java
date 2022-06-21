package com.rakibofc.udemy28sharedprefrences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.rakibofc.udemy28sharedprefrences", Context.MODE_PRIVATE);

        sharedPreferences.edit().putString("name", "Rakib").apply();

        String name = sharedPreferences.getString("name", "");


        /*ArrayList<String> family = new ArrayList<>();
        family.add("Rakib");
        family.add("Yamin");
        family.add("Azgar Ali");

        try {

            sharedPreferences.edit().putString("family", ObjectSerializer.serialize(family)).apply();
            Log.i("Family", ObjectSerializer.serialize(family));

        } catch (IOException e) {

            e.printStackTrace();
        }*/

        ArrayList<String> newFamily = new ArrayList<>();

        try {

            newFamily = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("family", ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {

            e.printStackTrace();
        }
        Log.i("New Family", newFamily.toString());
    }
}