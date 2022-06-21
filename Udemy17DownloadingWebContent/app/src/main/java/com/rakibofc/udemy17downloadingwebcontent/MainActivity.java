package com.rakibofc.udemy17downloadingwebcontent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView downloadedTextView;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls){

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

               while (data != -1) {

                    char currentData = (char) data;
                    result += currentData;

                    downloadedTextView.setText(result);
                    Log.i("ResultIndividual:" , result);

                    data = reader.read();

                }
                return result;

            } catch (Exception e) {

                return "Failed!";
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadedTextView = findViewById(R.id.downloadedText);

        DownloadTask downloadTask = new DownloadTask();
        String result = null;

        try {

            result = downloadTask.execute("https://api.openweathermap.org/data/2.5/onecall?lat=33.44&lon=-94.04&exclude=hourly,daily&appid=6414733b9d739200ad3ac7cc2e3057b3").get();

            downloadedTextView.setText(result);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}