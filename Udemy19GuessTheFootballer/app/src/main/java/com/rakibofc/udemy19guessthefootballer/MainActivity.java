package com.rakibofc.udemy19guessthefootballer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    public TextView text_view_status, text_view_score;
    public ImageView image_view_playerImage;
    public Button button0, button1, button2, button3;

    public String htmlContent = "", imageUrl;

    public ArrayList<String> playerImageLink, playerName;
    public ArrayList<Integer> randomImageLinkPosition, randomOptions, randomOptionsPosition;

    public int index = 0, numberOfPlayers, correctAnswer, optionLength = 4, score = 0;

    public boolean isQuizFinished = false;

    // User option on button click
    public void userClicked(View view) {

        Button btn_choice = (Button) view;
        String userChoice = btn_choice.getText().toString();

        if (!isQuizFinished){
            if (playerName.get(randomImageLinkPosition.get(index)).equals(userChoice)) {

                score++;
                text_view_status.setText("Prev Status: Right");

            } else {

                text_view_status.setText("Prev Status: Wrong");
            }

            text_view_score.setText("Score: " + score + "/10");

            randomOptionsPosition.clear();
            randomOptions.clear();

            index++;
        }

        if (index < numberOfPlayers) {
            setImageOnDisplay();

        } else {

            isQuizFinished = true;
            Toast.makeText(this, "Quiz Finished!", Toast.LENGTH_SHORT).show();
        }
    }

    // Network Connection Check
    private boolean haveNetworkConnection() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;

        } else {

            return false;
        }
    }

    // Get players info - Player Image Link and Player Name
    public int getPlayerInfo() {

        try {

            InputStream inputStream = getAssets().open("football_player_list.html");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            htmlContent = new String(buffer);

            Pattern contentPattern = Pattern.compile("img src=\"(.*?)\"");
            Matcher matcher = contentPattern.matcher(htmlContent);

            while (matcher.find()) {

                playerImageLink.add(matcher.group(1));
            }

            contentPattern = Pattern.compile("<h1>(.*?)</h1>");
            matcher = contentPattern.matcher(htmlContent);

            while (matcher.find()) {

                playerName.add(matcher.group(1));
            }

        } catch (IOException e) {

            e.printStackTrace();
        }

        return playerImageLink.size();

    }

    // Generate unique random numbers for set the questions randomly
    public void generateUniqueRandomNumber(ArrayList<Integer> uniqueNumbers, int limit) {

        Random random = new Random();

        int uniqueNumberCount = 0, randomNumber;

        while (uniqueNumberCount < limit) {

            randomNumber = random.nextInt(limit);

            if (!isDuplicate(uniqueNumbers, randomNumber)) {

                uniqueNumbers.add(uniqueNumberCount++, randomNumber);
            }
        }

    }

    // Duplicate number check for random numbers
    public boolean isDuplicate(@NonNull ArrayList<Integer> numbers, int randomNumber) {

        for (int i = 0; i < numbers.size(); i++) {

            if (numbers.get(i) == randomNumber) {

                return true;
            }
        }
        return false;
    }

    // Set image on display
    public void setImageOnDisplay() {

        try {

            if (haveNetworkConnection()) {

                ImageDownloader getImage = new ImageDownloader();

                imageUrl = playerImageLink.get(randomImageLinkPosition.get(index));

                Bitmap image = getImage.execute(imageUrl).get();
                image_view_playerImage.setImageBitmap(image);

            } else {

                Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException | ExecutionException e) {

            e.printStackTrace();

            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();

        }
        setOptionOnDisplay();

    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... imageUrls) {

            try {

                URL url = new URL(imageUrls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();

                return BitmapFactory.decodeStream(inputStream);

            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }
    }

    public void setOptionOnDisplay() {

        generateUniqueRandomNumber(randomOptionsPosition, optionLength);

        correctAnswer = randomImageLinkPosition.get(index);
        randomOptions.add(correctAnswer);

        for (int j = 1; j <= 3; j++) {

            if ((correctAnswer - j) >= 0) {

                randomOptions.add(correctAnswer - j);

            } else {

                randomOptions.add(correctAnswer + j);
            }

        }

        for (int i = 0; i < optionLength; i++) {

            if (randomOptionsPosition.get(i) == 0) {

                button0.setText(playerName.get(randomOptions.get(i)));

            } else if (randomOptionsPosition.get(i) == 1) {

                button1.setText(playerName.get(randomOptions.get(i)));

            } else if (randomOptionsPosition.get(i) == 2) {

                button2.setText(playerName.get(randomOptions.get(i)));

            } else if (randomOptionsPosition.get(i) == 3) {

                button3.setText(playerName.get(randomOptions.get(i)));

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_view_status = findViewById(R.id.status);
        text_view_score = findViewById(R.id.score);

        image_view_playerImage = findViewById(R.id.imageView);

        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        playerImageLink = new ArrayList<>();
        playerName = new ArrayList<>();
        randomImageLinkPosition = new ArrayList<>();
        randomOptions = new ArrayList<>();
        randomOptionsPosition = new ArrayList<>();

        numberOfPlayers = getPlayerInfo();

        generateUniqueRandomNumber(randomImageLinkPosition, numberOfPlayers);

        setImageOnDisplay();
    }
}