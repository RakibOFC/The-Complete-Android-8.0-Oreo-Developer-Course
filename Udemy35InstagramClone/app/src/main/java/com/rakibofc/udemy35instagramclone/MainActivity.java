package com.rakibofc.udemy35instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public TextView textViewUserProfile;
    public FirebaseUser firebaseUser;
    public DatabaseReference databaseReference;
    public StorageReference uploadInStorage;
    public String userID, fileName;
    public ImageView imageView;
    public LinearLayout layoutStoryList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.sharePhoto:
                sharePhoto();
                break;

            case R.id.userList:
                Intent intent = new Intent(MainActivity.this, UserListActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
                break;

            case R.id.logout:
                LoginActivity.sharedPreferences.edit().putBoolean("loggedIn", false).apply();
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = firebaseUser.getUid();

        textViewUserProfile = findViewById(R.id.textViewUserProfile);

        /*// firebase get values by key
        databaseReference.child(userID).child("fullName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {
                            textViewUserProfile.setText("Name: " + snapshot.getValue().toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", " it's null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

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
                            String email = user.email;

                            textViewUserProfile.setText("Name: " + fullName + "\nUsername: " + username + "\nEmail: " + email);

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

                        imageView = new ImageView(MainActivity.this);
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

        /*
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {

                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String userName = userProfile.username;

                    // rakibull2000@gmail.com
                    textViewUserProfile.setText(userProfile.toString());
                    // textViewUserProfile.setText("userID: " + userID + "\nName: " + fullName + "\nUsername: " + userName + "\nEmail: " + email);
                }

                Log.i("Info", firebaseUser.getEmail() + "\nName: " + firebaseUser.getDisplayName());

                // Get ALl User
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    User users = dataSnapshot.getValue(User.class);
                    String info = users.getFullName() + " : " + users.getEmail();
                    DataSnapshot value = dataSnapshot.child("email");

                    Log.i("Info", value.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */
    }


    private void sharePhoto() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            try {

                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                // imageViewPhoto = findViewById(R.id.imageViewPhoto);

                // imageViewPhoto.setImageBitmap(bitmapImage);

                // upload start here
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageRef = firebaseStorage.getReference();

                SimpleDateFormat formatter = new SimpleDateFormat("HHmmssddMMyyyy");
                Date date = new Date();
                fileName = "" + formatter.format(date);

                uploadInStorage = storageRef.child("InstagramClone/" + fileName);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageData = byteArrayOutputStream.toByteArray();

                UploadTask uploadTask = uploadInStorage.putBytes(imageData);
                uploadTask.addOnFailureListener(exception -> {

                    // Failure Message
                    Toast.makeText(getApplicationContext(), "Image Upload failed", Toast.LENGTH_SHORT).show();

                }).addOnSuccessListener(taskSnapshot -> {

                    // Getting url and store data in Database
                    uploadInStorage.getDownloadUrl().addOnSuccessListener(uri -> {

                        databaseReference.child(userID).child("imageUrl").child(fileName).setValue(uri.toString());
                        Toast.makeText(getApplicationContext(), "Image shared!", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(exception -> Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show());
                });

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Multiple ImageView Layout
    private void addVIew(ImageView imageView, int width, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

        imageView.setLayoutParams(params);
        layoutStoryList.addView(imageView);
    }
}