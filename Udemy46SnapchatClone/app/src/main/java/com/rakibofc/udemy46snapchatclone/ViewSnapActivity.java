package com.rakibofc.udemy46snapchatclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class ViewSnapActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_snap);
        this.setTitle("View Snap");

        mAuth = FirebaseAuth.getInstance();

        TextView textViewFrom = findViewById(R.id.textViewFrom);
        TextView textViewMessage = findViewById(R.id.textViewMessage);
        ImageView imageViewSnap = findViewById(R.id.imageViewSnap);

        textViewFrom.setText("From: " + getIntent().getStringExtra("from"));
        textViewMessage.setText("Message: " + getIntent().getStringExtra("message"));
        Picasso.get().load(getIntent().getStringExtra("imageURL")).into(imageViewSnap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("snaps").child(getIntent().getStringExtra("key")).removeValue();

        FirebaseStorage.getInstance().getReference().child("SnapchatClone").child(getIntent().getStringExtra("imageName")).delete();
    }
}