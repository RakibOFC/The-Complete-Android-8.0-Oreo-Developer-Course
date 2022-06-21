package com.rakibofc.udemy07animations;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    boolean bartIsShowing = true;

    public void translation(View view){

        ImageView iv_bart_image_view = findViewById(R.id.bart_image_view);

        Log.i("Info", "Image Clicked! fadeOut");

        iv_bart_image_view.animate().translationY(-500).setDuration(1000);
        iv_bart_image_view.animate().rotationX(1800).setDuration(2000);
        try {

            Thread.sleep(100);
            iv_bart_image_view.animate().translationY(500).setDuration(1000);

        } catch (InterruptedException e) { }

    }

    public void fade(View view){

        ImageView iv_bart_image_view = findViewById(R.id.bart_image_view);
        ImageView iv_homer_image_view = findViewById(R.id.homer_image_view);

        Log.i("Info", "Image Clicked! fadeOut");

        if (bartIsShowing){

            iv_bart_image_view.animate().alpha(0).setDuration(1000);
            iv_homer_image_view.animate().alpha(1).setDuration(1000);

            bartIsShowing = false;

        } else {

            iv_bart_image_view.animate().alpha(1).setDuration(1000);
            iv_homer_image_view.animate().alpha(0).setDuration(1000);

            bartIsShowing = true;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}