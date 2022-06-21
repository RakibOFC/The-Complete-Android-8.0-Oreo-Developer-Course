package com.rakibofc.udemy16braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public Button btn_home, btn_start, button0, button1, button2, button3;
    public LinearLayout ll_upper_content, lower_content;
    public TextView tv_message, tv_timer, tv_display_question, tv_score, tv_status;
    public androidx.gridlayout.widget.GridLayout gl_options;

    public CountDownTimer countDownTimer;
    public Random random;

    int num1, num2, sum, score = 0, numberOfQuestion = 0;

    public boolean isPlaying = false;

    public static boolean isDuplicateNumber(ArrayList<Integer> numbers, int randomNumber) {

        for (int i = 0; i < numbers.size(); i++) {

            if (numbers.get(i) == randomNumber) {

                return true;
            }
        }

        return false;
    }

    public void setQuestionInDisplay() {

        num1 = random.nextInt(485);
        num2 = random.nextInt(485);

        tv_display_question.setText(num1 + " + " + num2);

        setOptionInDisplay();

    }

    public void setOptionInDisplay() {

        int uniqueNumberCount = 0, randomNumber, limit = 4;

        ArrayList<Integer> pastNumber = new ArrayList<>();

        while (uniqueNumberCount < limit) {

            randomNumber = random.nextInt(limit);

            if (!isDuplicateNumber(pastNumber, randomNumber)) {

                pastNumber.add(uniqueNumberCount++, randomNumber);
            }
        }

        sum = num1 + num2;

        int cloneSum;
        for (int i = 0, j = -10; i < pastNumber.size(); i++) {

            cloneSum = sum + j;

            if (pastNumber.get(i) == 0) {

                button0.setText("" + cloneSum);

            } else if (pastNumber.get(i) == 1) {

                button1.setText("" + cloneSum);

            } else if (pastNumber.get(i) == 2) {

                button2.setText("" + cloneSum);

            } else if (pastNumber.get(i) == 3) {

                button3.setText("" + cloneSum);

            }
            j += 10;
        }

    }

    private void setScoreInDisplay() {

        tv_score.setText(score + "/" + numberOfQuestion);

    }

    public void home(View view) {

        btn_home.setVisibility(View.INVISIBLE);
        ll_upper_content.setVisibility(View.VISIBLE);
        tv_message.setVisibility(View.VISIBLE);
        gl_options.setVisibility(View.VISIBLE);
        lower_content.setVisibility(View.VISIBLE);

    }

    // After pressing Start_Button
    public void startGame(View view) {

        // Initial score is zero (0)
        score = 0;
        numberOfQuestion = 0;

        if (isPlaying) {

            countDownTimer.cancel();
        }
        setQuestionInDisplay();

        isPlaying = true;
        btn_start.setText("Restart");

        countDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                int secondLeft = (int) millisUntilFinished / 1000;

                tv_timer.setText(secondLeft + "s");

            }

            public void onFinish() {

                isPlaying = false;
                tv_timer.setText("0s");
                tv_status.setText("Game Over!");
                Toast.makeText(getApplicationContext(), "Game Over!", Toast.LENGTH_SHORT).show();
            }

        }.start();
    }

    public void choice(View view) {

        Button btn_choice = (Button) view;

        if (isPlaying) {

            int tempSum = Integer.parseInt(btn_choice.getText().toString());

            if (tempSum == sum) {

                score++;
                tv_status.setText("Right!");

            } else {

                tv_status.setText("Wrong :(");

            }
            numberOfQuestion++;
            setQuestionInDisplay();
            setScoreInDisplay();

        } else {

            btn_start.setText("Start");
            tv_status.setText("Start the Game first!");
            Toast.makeText(getApplicationContext(), "Start the Game first!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_home = findViewById(R.id.home);
        btn_start = findViewById(R.id.start);
        ll_upper_content = findViewById(R.id.upper_content);
        tv_timer = findViewById(R.id.timer);
        tv_display_question = findViewById(R.id.display_question);
        tv_score = findViewById(R.id.score);
        tv_message = findViewById(R.id.message);
        gl_options = findViewById(R.id.options_area);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        tv_status = findViewById(R.id.status);
        lower_content = findViewById(R.id.lower_content);

        random = new Random();

    }
}