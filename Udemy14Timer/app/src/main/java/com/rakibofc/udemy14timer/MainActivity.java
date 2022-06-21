package com.rakibofc.udemy14timer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
		new CountDownTimer(progress, 1000) {

        public void onTick(long milliSecondsUntilDOne) {

            // counting
            tv_counter.setText(dateFormat.format(milliSecondsUntilDOne));
        }

        public void onFinish() {

            // Finish message
            timer_seek_bar.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Timer counting finished!", Toast.LENGTH_SHORT).show();
        }

    }.start();
*/

        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);

        // Another Runnable function
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                Date date = new Date(System.currentTimeMillis());
                Log.i("Time", formatter.format(date));
            }
        }, 0, 1000);

    }
}
