package com.rakibofc.udemy04higherorlower;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public void guessNumber(View view){

        Log.i("Info", "Button Pressed!");

        EditText et_number = findViewById(R.id.number);

        Random rand = new Random();
        int randNumber = rand.nextInt(20) + 1;

        int inputNum = Integer.parseInt(et_number.getText().toString());

        if (inputNum > randNumber){

            Toast.makeText(getApplicationContext(), "Lower : " + randNumber, Toast.LENGTH_SHORT).show();
        }
        else if (inputNum < randNumber){

            Toast.makeText(getApplicationContext(), "Higher : " + randNumber, Toast.LENGTH_SHORT).show();
        }
        else {

            Toast.makeText(getApplicationContext(), "You get it! : " + randNumber, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}