package com.rakibofc.udemy29alertdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure!?")
                .setMessage("Do you definitely want to do this?")
                .setPositiveButton("Yes", (dialog, which) -> Toast.makeText(MainActivity.this, "It's done", Toast.LENGTH_SHORT).show())
                .setNegativeButton("No", (dialog, which) -> Toast.makeText(MainActivity.this, "Canceled!", Toast.LENGTH_SHORT).show()).show();

    }
}