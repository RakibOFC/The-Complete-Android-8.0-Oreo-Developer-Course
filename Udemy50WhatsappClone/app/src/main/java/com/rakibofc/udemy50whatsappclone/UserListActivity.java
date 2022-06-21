package com.rakibofc.udemy50whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class UserListActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ListView listViewUser;
    ArrayList<String> userList;
    ArrayList<String> userKey;
    ArrayList<String> fullName;
    ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logout:

                LoginActivity.sharedPreferences.edit().putBoolean("loggedIn", false).apply();
                LoginActivity.sharedPreferences.edit().putString("userID", null).apply();
                FirebaseAuth.getInstance().signOut();
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        this.setTitle("Whatsapp Users");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        listViewUser = findViewById(R.id.listViewUser);
        userList = new ArrayList<>();
        userKey = new ArrayList<>();
        fullName = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userList);
        listViewUser.setAdapter(arrayAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (!Objects.equals(snapshot.getKey(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {

                    userList.add(snapshot.child("email").getValue() + "");
                    userKey.add(snapshot.getKey());
                    fullName.add(snapshot.child("fullName").getValue() + "");
                }
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

        listViewUser.setOnItemClickListener((parent, view, position, id) -> {

            // If Current_User choose a user, then go to the ChatActivity

            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra("sender", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            intent.putExtra("receiver", userKey.get(position));
            intent.putExtra("fullName", fullName.get(position));
            startActivity(intent);
        });
    }
}