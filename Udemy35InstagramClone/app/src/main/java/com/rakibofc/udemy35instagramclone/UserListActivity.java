package com.rakibofc.udemy35instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    ListView listViewUser;
    ArrayAdapter arrayAdapter;
    ArrayList<String> userID;
    ArrayList<String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listViewUser = findViewById(R.id.listViewUser);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        userID = new ArrayList<>();
        users = new ArrayList<>();

        Intent intentUserID = getIntent();
        String currentUserID = intentUserID.getStringExtra("userID");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    DataSnapshot value = dataSnapshot.child("fullName");

                    if (!dataSnapshot.getKey().equals(currentUserID)) {

                        userID.add(dataSnapshot.getKey());
                        users.add(value.getValue().toString());
                    }

                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);

        listViewUser.setAdapter(arrayAdapter);

        listViewUser.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(UserListActivity.this, UserProfileActivity.class);
            intent.putExtra("userID", userID.get(position));

            startActivity(intent);
        });
    }
}