package com.rakibofc.udemy18downloadingimages;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ImageView image_view;

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();

                Bitmap bitmapFactory = BitmapFactory.decodeStream(inputStream);

                return bitmapFactory;

            } catch (Exception e) {

                e.printStackTrace();
            }

            return null;
        }
    }

    public void downloadImage(View view) {

        ImageDownloader imageDownloader = new ImageDownloader();

        try {

            Bitmap image = imageDownloader.execute("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRFZVPmLcsq3opmeec1hNy2PVAR_JnPEzy0MA&usqp=CAU").get();

            image_view.setImageBitmap(image);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image_view = findViewById(R.id.image_view);
    }
}
