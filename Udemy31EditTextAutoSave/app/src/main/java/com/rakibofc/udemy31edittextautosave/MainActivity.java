package com.rakibofc.udemy31edittextautosave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    SharedPreferences sharedPreferences;

    public void setTextInNote(int noteID) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.rakibofc.udemy31edittextautosave", Context.MODE_PRIVATE);
        editText = findViewById(R.id.editText);

        //HashSet<String> textString = new HashSet<>();

        String textString = "";

        editText.setText(sharedPreferences.getString("text", null));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                sharedPreferences.edit().putString("text", editText.getText().toString()).apply();
                Log.i("Info", editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}