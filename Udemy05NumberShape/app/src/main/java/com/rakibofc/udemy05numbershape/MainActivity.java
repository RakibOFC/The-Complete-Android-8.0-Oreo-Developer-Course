package com.rakibofc.udemy05numbershape;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void checkANumber(View view){

        Log.i("Info", "Button Pressed!");

        EditText et_number = findViewById(R.id.number);

        String message;

        if(et_number.getText().toString().isEmpty()){

            message = "Please enter a num";

        } else {

            Logic logic = new Logic();

            logic.number = Integer.parseInt(et_number.getText().toString());

            if (logic.isTriangular() && logic.isSquare()){

                message = logic.number + " is triangular and square number.";

            } else if(logic.isTriangular()){

                message = logic.number + " is triangular number.";

            } else if(logic.isSquare()){

                message = logic.number + " is square number.";

            } else {

                message = logic.number + " is neither triangular nor square number.";
            }

        }

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}