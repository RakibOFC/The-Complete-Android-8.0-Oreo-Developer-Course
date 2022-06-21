package com.rakibofc.udemy43pictureinpicture;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    Button button;
    TextView textView;

    public ActivityManager.MemoryInfo memoryInfo;
    public ActivityManager activityManager;
    public double availMem;
    public double totalMem;

    public void goPIP(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPictureInPictureMode();
        } else {
            Toast.makeText(getApplicationContext(), "This device not support PIP mode", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);

        ramInfo();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        if (isInPictureInPictureMode) {

            Log.e("Info", "Checking...");
            button.setVisibility(View.INVISIBLE);
            getSupportActionBar().hide();

            ramInfo();

        } else {

            Objects.requireNonNull(getSupportActionBar()).show();
            button.setVisibility(View.VISIBLE);
        }
    }

    public void ramInfo() {
        memoryInfo = new ActivityManager.MemoryInfo();
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        availMem = (double) memoryInfo.availMem / 1073741824;
        totalMem = (double) memoryInfo.totalMem / 1073741824;
        textView.setText(String.format("%.2f", (totalMem - availMem)) + "GB / " + (int) Math.ceil(totalMem) + "GB");

        // Check every second for RAM
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                handler.postDelayed(this, 500);

                memoryInfo = new ActivityManager.MemoryInfo();
                activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                activityManager.getMemoryInfo(memoryInfo);

                availMem = (double) memoryInfo.availMem / 1073741824;
                textView.setText(String.format("%.4f", (totalMem - availMem)) + "GB / " + (int) Math.ceil(totalMem) + "GB");
            }
        };

        handler.post(runnable);
    }
}