package com.rakibofc.udemy46snapchatclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateSnapActivity extends AppCompatActivity {

    Bitmap bitmapImage;
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_snap);
        setTitle("Create Snap");

        Button buttonChooseImage = findViewById(R.id.buttonChooseImage);
        TextView editTextMessage = findViewById(R.id.editTextMessage);
        Button buttonNext = findViewById(R.id.buttonNext);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        buttonChooseImage.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        buttonNext.setOnClickListener(v -> {

            // If image uploaded successfully, then goto ChooseUserActivity
            buttonNext.setEnabled(false);
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageRef = firebaseStorage.getReference();

            SimpleDateFormat formatter = new SimpleDateFormat("HHmmssddMMyyyy");
            Date date = new Date();
            String fileName = "" + formatter.format(date);

            StorageReference uploadInStorage = storageRef.child("SnapchatClone/" + fileName);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] imageData = byteArrayOutputStream.toByteArray();

            UploadTask uploadTask = uploadInStorage.putBytes(imageData);
            uploadTask.addOnFailureListener(e -> {

                // Failure Message
                Toast.makeText(getApplicationContext(), "Image Upload failed", Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(taskSnapshot -> {

                // Success Message
                uploadInStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        imageURL = uri.toString();

                        Intent intent = new Intent(getApplicationContext(), ChooseUserActivity.class);
                        intent.putExtra("imageName", fileName);
                        intent.putExtra("imageURL", imageURL);
                        intent.putExtra("message", editTextMessage.getText().toString());
                        startActivity(intent);
                    }
                });
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ImageView imageViewPickedImage = findViewById(R.id.imageViewPickedImage);
                imageViewPickedImage.setImageBitmap(bitmapImage);

            } catch (Exception e) {}
        }
    }
}