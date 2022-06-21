package com.rakibofc.udemy44splitscreendemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);

        Toast.makeText(getApplicationContext(), "onMultiWindowModeChanged", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (isInMultiWindowMode()) {

                Toast.makeText(getApplicationContext(), "isInMultiWindowMode", Toast.LENGTH_SHORT).show();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (isInPictureInPictureMode()) {

                // picture in picture mode
            }
        }
    }
}