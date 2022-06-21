package com.rakibofc.udemy35instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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
        this.setTitle("Instagram Clone");
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = this.getSharedPreferences("com.rakibofc.udemy35instagramclone", Context.MODE_PRIVATE);
        boolean loggedIn = sharedPreferences.getBoolean("loggedIn", false); 

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewResetPassword = findViewById(R.id.textViewResetPassword);
        textViewSignup = findViewById(R.id.textViewSignup);

        if (loggedIn) {

            startActivity(new Intent(this, MainActivity.class));

        } else {

            buttonLogin.setOnClickListener(this);
            textViewResetPassword.setOnClickListener(this);
            textViewSignup.setOnClickListener(this);

            editTextPassword.setOnKeyListener(this);
        }
/*
        // database check
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        databaseReference.child("dduh8dKUYMcgHfPrETKCcwPtR9D3").child("imageUrl").child("image1").setValue("https://rakibofc.blogspot.com/");

        HashMap<String, Object> user = new HashMap<>();
        user.put("Full Name", "Yamin Hasan");
        user.put("Username", "rakibofc");
        user.put("Email", "rakib@gmail.com");
        user.put("Password", "123456");
        databaseReference.push().setValue(user);

        DatabaseReference getDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        getDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Users users = dataSnapshot.getValue(Users.class);
                    String info = users.getFullName() + " : " + users.getEmail();
                    DataSnapshot value = dataSnapshot.child("Email");

                    Log.i("Info", value.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/

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

                if (firebaseUser.isEmailVerified()) {

                    sharedPreferences.edit().putBoolean("loggedIn", true).apply();
                    startActivity(new Intent(this, MainActivity.class));

                } else {
                    firebaseUser.sendEmailVerification();
                    Toast.makeText(LoginActivity.this, "Check your email to verify your account", Toast.LENGTH_SHORT).show();
                }

            } else {

                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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