package com.rakibofc.udemy30languagepreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_manu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.bengali:
                setLanguage("Bengali");
                return true;

            case R.id.english:
                setLanguage("English");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void setLanguage(String language) {

        sharedPreferences.edit().putString("language", language).apply();
        textView.setText(language);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = findViewById(R.id.textView);
        sharedPreferences = this.getSharedPreferences("com.rakibofc.udemy30languagepreferences", Context.MODE_PRIVATE);

        String language = sharedPreferences.getString("language", "N/A");

        if (language.equals("N/A")) {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_btn_speak_now)
                    .setTitle("Choose a language")
                    .setMessage("Which language  would you like to use?")
                    .setPositiveButton("Bengali", (dialog, which) -> {

                        // Set Bengali
                        setLanguage("Bengali");

                    })
                    .setNegativeButton("English", (dialog, which) -> {

                        // Set English
                        setLanguage("English");

                    }).show();
        } else {

            textView.setText(language);
        }
    }
}