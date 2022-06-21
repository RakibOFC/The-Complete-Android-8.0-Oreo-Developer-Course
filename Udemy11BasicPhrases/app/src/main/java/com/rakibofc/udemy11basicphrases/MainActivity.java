package com.rakibofc.udemy11basicphrases;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

public class MainActivity extends AppCompatActivity {

    public MediaPlayer mediaPlayer;

    public void playPhrase(View view) {

        Button button = (Button) view;

        String btnGetTag = button.getTag().toString();

        mediaPlayer = MediaPlayer.create(this, getResources().getIdentifier(btnGetTag, "raw", getPackageName()));
        mediaPlayer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}