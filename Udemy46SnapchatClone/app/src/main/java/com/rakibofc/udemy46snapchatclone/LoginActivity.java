package com.rakibofc.udemy46snapchatclone;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    public EditText editTextEmail, editTextPassword;
    public Button buttonLogin;
    public TextView textViewResetPassword, textViewSignup;
    private FirebaseAuth mAuth;
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Snapchat");
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = this.getSharedPreferences("com.rakibofc.udemy35instagramclone", Context.MODE_PRIVATE);
        boolean loggedIn = sharedPreferences.getBoolean("loggedIn", false);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewResetPassword = findViewById(R.id.textViewResetPassword);
        textViewSignup = findViewById(R.id.textViewSignup);

        if (loggedIn) {

            Intent intent = new Intent(this, SnapsActivity.class);
            intent.putExtra("userID", "");
            startActivity(intent);

        } else {

            buttonLogin.setOnClickListener(this);
            textViewResetPassword.setOnClickListener(this);
            textViewSignup.setOnClickListener(this);

            editTextPassword.setOnKeyListener(this);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonLogin:

                userLogin();
                break;

            case R.id.textViewSignup:

                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                break;

            case R.id.textViewResetPassword:

                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                break;
        }
    }

    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            editTextEmail.setError("Enter a valid email address");
            editTextEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {

            editTextPassword.setError("Minimum length of a password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                // if (firebaseUser.isEmailVerified()) {

                sharedPreferences.edit().putBoolean("loggedIn", true).apply();
                sharedPreferences.edit().putString("userID", firebaseUser.getUid()).apply();
                startActivity(new Intent(this, SnapsActivity.class));

                /*} else {
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(LoginActivity.this, "Check your email to verify your account", Toast.LENGTH_SHORT).show();
                }*/

            } else {

                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER) {

            userLogin();
        }
        return false;
    }
}