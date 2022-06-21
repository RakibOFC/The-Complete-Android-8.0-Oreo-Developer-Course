package com.rakibofc.udemy13timestables;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar seekBar = findViewById(R.id.seek_bar);

        TextView tv_seek_bar_value = findViewById(R.id.seek_bar_value);

        ListView my_list_view = findViewById(R.id.list_view);

        ArrayList<Integer> timesTableContent = new ArrayList<>();

        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timesTableContent);

        for (int i = 0; i < 10; i++) {

            timesTableContent.add(i, i + 1);

        }
        my_list_view.setAdapter(arrayAdapter);

        // seekBar.setMin(1);
        seekBar.setMax(10);
        seekBar.setProgress(1);
        tv_seek_bar_value.setText("1");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                int min = 1;
                int timesTableNumber;

                if (progress < min) {

                    timesTableNumber = min;
                    seekBar.setProgress(min);
                } else {

                    timesTableNumber = progress;

                }
                tv_seek_bar_value.setText(timesTableNumber + "");
                arrayAdapter.clear();

                for (int i = 0; i < 10; i++) {

                    timesTableContent.add(i, (i + 1) * timesTableNumber);

                }

                my_list_view.setAdapter(arrayAdapter);

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