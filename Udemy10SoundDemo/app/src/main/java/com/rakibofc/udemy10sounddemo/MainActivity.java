package com.rakibofc.udemy10sounddemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public MediaPlayer mediaPlayer;
    public AudioManager audioManager;

    public void play(View view) {


        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pause(View view) {

        if (mediaPlayer.isPlaying()) {

            mediaPlayer.pause();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.surah_hasor);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);


        // Audio Sound
        SeekBar seekbar_control_audio = findViewById(R.id.control_audio);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);

        seekbar_control_audio.setMax(maxVolume);
        seekbar_control_audio.setProgress(currentVolume);

        seekbar_control_audio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.i("Audio Value", Integer.toString(progress));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // Audio Duration
        SeekBar seekbar_control_duration = findViewById(R.id.control_duration);

        seekbar_control_duration.setMax(mediaPlayer.getDuration());

        seekbar_control_duration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //  Duration Progress
        SeekBar seekbar_duration_progress = findViewById(R.id.duration_progress);
        seekbar_duration_progress.setMax(mediaPlayer.getDuration());

        seekbar_duration_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final Handler handler = new Handler();

        Runnable run = new Runnable() {
            @Override
            public void run() {

                seekbar_duration_progress.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 1000);

            }
        };
        handler.post(run);

        /*
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                seekbar_control_duration.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 1000);
        */

        // Audio Pitch
        SeekBar seekbar_control_pitch = findViewById(R.id.control_pitch);

        seekbar_control_pitch.setMin(1);
        seekbar_control_pitch.setMax(20);

        seekbar_control_pitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                Log.i("Pitch Value", Float.toString((float) progress/2f));

                PlaybackParams playbackParams = mediaPlayer.getPlaybackParams();
                playbackParams.setPitch((float) progress/2f);
                mediaPlayer.setPlaybackParams(playbackParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}