package com.rakibofc.udemy22whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editTextTextCityName;
    TextView textViewWeather;
    Button goButton;

    public class WebContent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String currentContent = null, content = "";

                while ((currentContent = reader.readLine()) != null){

                    content += currentContent;

                }

                return content;

            } catch (Exception e) {

                e.printStackTrace();

                return null;
            }

        }
    }

    public void identifyWeather(String fullContent) {

        try {

            String result = "";

            JSONObject jsonObject = new JSONObject(fullContent);
            String getString = jsonObject.getString("weather");

            JSONArray arrayWeather = new JSONArray(getString);

            JSONObject objectPath;

            for (int i = 0; i < arrayWeather.length(); i++) {

                objectPath = arrayWeather.getJSONObject(i);

                String main = objectPath.getString("main");
                String description = objectPath.getString("description");

                result = result +" " + main + " : " + description;

            }

            // temperature
            String tempStr =  jsonObject.getString("main");

            JSONObject json = new JSONObject(tempStr);

            double temp = Double.parseDouble(json.getString("temp"));
            double feels_like = Double.parseDouble(json.getString("feels_like"));

            textViewWeather.setText(result + "\nTemp: " + (temp - 273) + "\nFeels Like: " + (feels_like - 273));

            // About Sun
            String sunStr =  jsonObject.getString("sys");

            JSONObject jsonSys = new JSONObject(sunStr);

            DateFormat dateFormat = new SimpleDateFormat("hh.mm.ss");
            String sunrise = dateFormat.format(Integer.parseInt(jsonSys.getString("sunrise")));
            String sunset = dateFormat.format(Integer.parseInt(jsonSys.getString("sunset")));

            textViewWeather.setText(textViewWeather.getText() + "\n" + "Sunrise: " + sunrise + "\nSunset: " + sunset);


        } catch (JSONException e) {

            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTextCityName = findViewById(R.id.editTextTextCityName);
        textViewWeather = findViewById(R.id.textViewWeather);
        goButton = findViewById(R.id.buttonGo);

        goButton.setOnClickListener(view -> {

            WebContent getContent = new WebContent();

            String cityName = editTextTextCityName.getText().toString();

            try {

                String fullContent = getContent.execute("https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=6414733b9d739200ad3ac7cc2e3057b3").get();

                try {

                    identifyWeather(fullContent);

                } catch (Exception e) {

                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            } catch (ExecutionException | InterruptedException e) {

                e.printStackTrace();
            }

        });
    }
}