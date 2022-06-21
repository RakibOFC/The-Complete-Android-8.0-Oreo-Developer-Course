package com.rakibofc.udemy03currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void currencyConverter(View view){

        EditText et_amount = findViewById(R.id.amount);
        String str;
        double amountInPound = 0.0;

        str = et_amount.getText().toString();

        if (str.isEmpty()){

            str = "0";
        }

        amountInPound = Double.parseDouble(str);

        double amountInDollar = amountInPound * 1.3;

        String amountInDollarStr = String.format("%.2f", amountInDollar);

        Log.i("Info", "Button Pressed!");

        Toast.makeText(getApplicationContext(),"Amount: " + amountInDollarStr,Toast. LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}