package com.rakibofc.udemy35instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    TextView textViewUserProfile;
    public LinearLayout layoutStoryList;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textViewUserProfile  = findViewById(R.id.textViewUserProfile);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        // User Profile Info, Name, Username, Email
        databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {

                            User user = snapshot.getValue(User.class);
                            String fullName = user.fullName;
                            String username = user.username;

                            textViewUserProfile.setText("Name: " + fullName + "\nUsername: " + username);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // User Post List
        databaseReference.child(userID).child("imageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    for (DataSnapshot imageName : snapshot.getChildren()) {

                        // Multiple ImageView Layout
                        layoutStoryList = findViewById(R.id.layoutStoryList);

                        String imageUrl = snapshot.child(imageName.getKey()).getValue().toString();

                        imageView = new ImageView(UserProfileActivity.this);
                        Picasso.get().load(imageUrl).into(imageView);
                        addVIew(imageView, 400, 400);

                    }

                } catch (Exception e) {
                    Log.e("Error", "" + e.getMessage());
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addVIew(ImageView imageView, int width, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

        imageView.setLayoutParams(params);
        layoutStoryList.addView(imageView);
    }
}