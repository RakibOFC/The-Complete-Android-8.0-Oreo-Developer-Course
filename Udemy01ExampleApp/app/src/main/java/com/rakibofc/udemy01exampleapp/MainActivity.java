package com.rakibofc.udemy01exampleapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public void clickFunction(View view){

        EditText editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        EditText editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        EditText editTextTextPassword = findViewById(R.id.editTextTextPassword);

        Log.i("Info", "Button Pressed!");
        Log.i("Person Name", editTextTextPersonName.getText().toString());
        Log.i("Email", editTextTextEmailAddress.getText().toString());
        Log.i("Password", editTextTextPassword.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}