package com.rakibofc.udemy09video;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView vv_video = findViewById(R.id.videoView);

        vv_video.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.hbd_arha_2021);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mediaController);

        vv_video.setMediaController(mediaController);
        vv_video.start();
    }
}