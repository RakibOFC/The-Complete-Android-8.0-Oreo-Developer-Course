package com.rakibofc.udemy48uberclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button buttonGetStarted;
    Switch switchUserType;
    public static SharedPreferences sharedPreferences;

    public void buttonGetStarted(View view) {

        String userType;
        buttonGetStarted.setEnabled(false);

        if (switchUserType.isChecked()) {

            userType = "driver";

        } else {

            userType = "rider";
        }

        mAuth.signInAnonymously().addOnCompleteListener(this, task -> {

            if (task.isSuccessful()) {

                String currentUser = task.getResult().getUser().getUid();

                FirebaseDatabase.getInstance().getReference().child("anonymous").child(currentUser).child("riderOrDriver").setValue(userType);

                FirebaseUser user = mAuth.getCurrentUser();

                sharedPreferences.edit().putString("userType", userType).apply();
                // sharedPreferences.edit().putString("currentUser", currentUser).apply();

                if (userType.equals("rider")) {

                    startActivity(new Intent(this, RiderActivity.class));

                } else if (userType.equals("driver")) {

                    switchUserType.setChecked(true);
                    startActivity(new Intent(this, ViewRequestActivity.class));
                }

            } else {

                Toast.makeText(getApplicationContext(), "Please clear are data, and try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {

        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        String getUserType = sharedPreferences.getString("userType", "");

        if (currentUser != null) {

            sharedPreferences.edit().putString("currentUser", currentUser.getUid()).apply();

            if (getUserType.equals("rider")) {

                startActivity(new Intent(getApplicationContext(), RiderActivity.class));
                finish();

            } else if (getUserType.equals("driver")) {

                switchUserType.setChecked(true);
                startActivity(new Intent(this, ViewRequestActivity.class));
                finish();
            }

        } else {
            buttonGetStarted.setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = this.getSharedPreferences("com.rakibofc.udemy48uberclone", Context.MODE_PRIVATE);

        switchUserType = findViewById(R.id.switchUserType);
        buttonGetStarted = findViewById(R.id.buttonGetStarted);

        buttonGetStarted.setEnabled(false);

        ImageView imageViewLogo = findViewById(R.id.imageViewLogo);
        Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.uber);
        imageViewLogo.setImageBitmap(bitmap);
    }
}