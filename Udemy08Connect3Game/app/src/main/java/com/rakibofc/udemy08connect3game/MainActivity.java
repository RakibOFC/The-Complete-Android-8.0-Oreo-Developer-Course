package com.rakibofc.udemy08connect3game;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public boolean playerTraceValueCheck(int playerWinningPosition, List<Integer> playerTrace) {

        for (int position : playerTrace) {

            if (playerWinningPosition == position) {

                return true;

            }
        }
        return false;
    }

    public boolean isGameOver = false;

    // Initially all position is empty or Zero (0)
    public int[] positionIsFill = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    // Set the winning position
    public int[][] playerWinningPosition = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    // payer = 0 for red, and 1 for yellow
    public int player = 0;

    // playerTraceRed-Yellow = By tracing which position the player has filled
    List<Integer> playerTraceRed = new ArrayList<>();
    List<Integer> playerTraceYellow = new ArrayList<>();

    // redScore = 0, yellowScore = 0; Initially player score is zero(0)
    public int redScore = 0, yellowScore = 0;

    public void reAssignValue(){

        androidx.gridlayout.widget.GridLayout gridLayout = findViewById(R.id.board);
        isGameOver = false;
        redScore = 0;
        yellowScore = 0;
        playerTraceRed.removeAll(playerTraceRed);
        playerTraceYellow.removeAll(playerTraceYellow);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {

            ImageView count = (ImageView) gridLayout.getChildAt(i);

            count.setImageDrawable(null);

        }

        for (int x = 0; x < positionIsFill.length; x++) {

            positionIsFill[x] = 0;

        }

    }

    public void dropIn(View view) {

        ImageView count = (ImageView) view;
        Button btn_play = findViewById(R.id.play);
        TextView tv_winning_msg = findViewById(R.id.winning_msg);

        int tagPosition = Integer.parseInt(count.getTag().toString());

        Log.i("Info:", "ImageView Pressed!");

        if (!isGameOver) {

            if (positionIsFill[tagPosition] == 0) {

                positionIsFill[tagPosition] = 1;

                count.setTranslationY(-1500);

                if (player == 0) {

                    player = 1;
                    count.setImageResource(R.drawable.red);
                    playerTraceRed.add(tagPosition);

                } else {

                    player = 0;
                    count.setImageResource(R.drawable.yellow);
                    playerTraceYellow.add(tagPosition);

                }

                count.animate().translationYBy(1500).rotationY(1800).setDuration(500);

                for (int i = 0; i < 8; i++) {

                    for (int j = 0; j < 3; j++) {

                        if (playerTraceValueCheck(playerWinningPosition[i][j], playerTraceRed) && player == 1) {

                            redScore++;

                        } else if (playerTraceValueCheck(playerWinningPosition[i][j], playerTraceYellow) && player == 0) {

                            yellowScore++;
                        }
                    }

                    if (redScore >= 3) {

                        // Red is win and game over
                        isGameOver = true;
                        tv_winning_msg.setVisibility(View.VISIBLE);
                        btn_play.setText("Play Again");
                        tv_winning_msg.setText("Red is win");

                    } else if (yellowScore >= 3) {

                        // Yellow is win and game over
                        isGameOver = true;
                        tv_winning_msg.setVisibility(View.VISIBLE);
                        btn_play.setText("Play Again");
                        tv_winning_msg.setText("Yellow is win");

                    }
                    redScore = 0;
                    yellowScore = 0;
                }

            } else {

                int x;

                for (x = 0; x < positionIsFill.length; x++) ;

                if (positionIsFill[x - 1] == 1) {

                    reAssignValue();
                }
            }

        } else {

            Toast.makeText(this, "Game Finished, Play again!", Toast.LENGTH_SHORT).show();

        }

    }

    public void playAgain(View view) {


        Button btn_play = findViewById(R.id.play);
        TextView tv_winning_msg = findViewById(R.id.winning_msg);

        btn_play.setText("Restart");
        tv_winning_msg.setVisibility(View.INVISIBLE);

        player = 0;
        reAssignValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}