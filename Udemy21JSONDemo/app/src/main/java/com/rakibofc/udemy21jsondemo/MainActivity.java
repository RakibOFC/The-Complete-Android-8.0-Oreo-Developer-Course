package com.rakibofc.udemy21jsondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.appsearch.StorageInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView downloadedTextView;
    String content = "";

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls){

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader in  = new InputStreamReader(inputStream);
                BufferedReader read = new BufferedReader(in);

                String str = null;

                while((str = read.readLine()) != null){

                    content += str;

                }
                return content;

            } catch (IOException e) {

                downloadedTextView.setText(e.getMessage());
                return "Failed!";
            }
        }

        @Override
        protected void onPostExecute(String jsonContent) {
            super.onPostExecute(jsonContent);

            Log.i("Info", jsonContent);

            try {

                JSONObject jsonObject = new JSONObject(jsonContent);
                String weatherInfo = jsonObject.getString("weather");

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    downloadedTextView.setText(downloadedTextView.getText() + "\n" + jsonPart.getString("main"));
                    downloadedTextView.setText(downloadedTextView.getText() + "\n" + jsonPart.getString("description"));
                }

            } catch (JSONException e) {

                e.printStackTrace();
                downloadedTextView.setText("Exception: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadedTextView = findViewById(R.id.downloadedText);

        DownloadTask downloadTask = new DownloadTask();

        try {

            downloadTask.execute("https://api.openweathermap.org/data/2.5/weather?q=dhaka&appid=6414733b9d739200ad3ac7cc2e3057b3").get();

        } catch (InterruptedException | ExecutionException e) {

            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }


    }
}