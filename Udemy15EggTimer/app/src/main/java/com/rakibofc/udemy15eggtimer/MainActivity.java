package com.rakibofc.udemy15eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    public CountDownTimer countDownTimer;
    SimpleDateFormat dateFormat;
    SeekBar timer_seek_bar;
    TextView tv_counter;
    Button counter_btn;
    MediaPlayer mediaPlayer;
    int setTime;
    boolean isCounting = false;

    public void onCounter(View view) {

        if (isCounting == false) {

            counter_btn.setText("Stop");
            isCounting = true;
            tv_counter.setText("");

            countDownTimer = new CountDownTimer(setTime, 1000) {

                public void onTick(long milliSecondsUntilDOne) {

                    // counting
                    timer_seek_bar.setProgress((int) milliSecondsUntilDOne);
                    tv_counter.setText(dateFormat.format(milliSecondsUntilDOne));
                }

                public void onFinish() {

                    // Finish message
                    mediaPlayer.start();
                    counter_btn.setText("Start");
                    isCounting = false;
                    Toast.makeText(getApplicationContext(), "Timer counting finished!", Toast.LENGTH_SHORT).show();
                }

            }.start();

        } else {

            setTime = 0;
            isCounting = false;
            tv_counter.setText("00:00");
            counter_btn.setText("Start");
            countDownTimer.cancel();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.siren);

        dateFormat = new SimpleDateFormat("mm:ss");

        counter_btn = findViewById(R.id.counter_btn);

        tv_counter = findViewById(R.id.counter);
        timer_seek_bar = findViewById(R.id.timer_seek_bar);

        timer_seek_bar.setMax(1000000);

        timer_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                setTime = progress;
                tv_counter.setText(dateFormat.format(setTime));
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