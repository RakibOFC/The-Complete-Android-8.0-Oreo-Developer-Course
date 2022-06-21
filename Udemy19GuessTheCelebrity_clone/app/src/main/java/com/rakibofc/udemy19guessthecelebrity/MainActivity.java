package com.rakibofc.udemy19guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    ImageView image_view_playerImage;

    TextView text_view_score;

    String content = "";

    public class ImageDownloader extends AsyncTask <String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... imageUrls) {

            try {

                URL url = new URL(imageUrls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap getImage = BitmapFactory.decodeStream(inputStream);

                return getImage;

            } catch (IOException e) {

                e.printStackTrace();
            }

            return null;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image_view_playerImage = findViewById(R.id.imageView);
        text_view_score = findViewById(R.id.score);

        ImageDownloader getImage = new ImageDownloader();

        try {

            /*Bitmap bitmapImage = getImage.execute("https://a.espncdn.com/photo/2021/1113/r936950_400x600_2-3.jpg").get();
            image_view_playerImage.setImageBitmap(bitmapImage);
            */

            Bitmap image = getImage.execute("https://scontent.fcgp17-1.fna.fbcdn.net/v/t1.6435-9/154234056_285169036300480_5742804475050203773_n.jpg?_nc_cat=110&ccb=1-5&_nc_sid=09cbfe&_nc_ohc=hccg0B2Grk0AX8U2B4a&tn=-fRHN-CGKS8gTjLh&_nc_ht=scontent.fcgp17-1.fna&oh=00_AT-37KnKhT7o3kPR4TBIuhdOcqlE67ic9pHOZvvRVO4kSg&oe=623852A8").get();
            // https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRFZVPmLcsq3opmeec1hNy2PVAR_JnPEzy0MA&usqp=CAU
            image_view_playerImage.setImageBitmap(image);

            Log.i("Result", "Success!");
            text_view_score.setText("Image Success!");

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

            text_view_score.setText("Image Error!");
            Log.i("Result", "Error");

        }


        String string = "";
        try {
            InputStream inputStream = getAssets().open("find_the_football_player_and_image.txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            string = new String(buffer);

            Log.i("Size", size + ", Content:" + string);

        } catch (IOException e) {

            e.printStackTrace();
            Log.i("Content", "Failed");
        }
    }
}