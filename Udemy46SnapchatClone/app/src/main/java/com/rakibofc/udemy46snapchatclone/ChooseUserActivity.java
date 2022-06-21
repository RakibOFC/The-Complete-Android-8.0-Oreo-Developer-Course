package com.rakibofc.udemy46snapchatclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class ChooseUserActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);
        this.setTitle("Choose User");

        ListView listViewUsers = findViewById(R.id.listViewUsers);

        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> emails = new ArrayList<>();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, emails);
        listViewUsers.setAdapter(arrayAdapter);

        // FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener

        FirebaseDatabase.getInstance().getReference().child("Users").addChildEventListener(new ChildEventListener(){

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String email = snapshot.child("email").getValue().toString();

                emails.add(email);
                keys.add(snapshot.getKey());
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listViewUsers.setOnItemClickListener((parent, view, position, id) -> {

            Map<String, String> snapMap = Map.of("from", FirebaseAuth.getInstance().getCurrentUser().getEmail(), "imageName", getIntent().getStringExtra("imageName"), "imageURL", getIntent().getStringExtra("imageURL"), "message", getIntent().getStringExtra("message"));

            FirebaseDatabase.getInstance().getReference().child("Users").child(keys.get(position)).child("snaps").push().setValue(snapMap);

            Toast.makeText(getApplicationContext(), "Image shared!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SnapsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}