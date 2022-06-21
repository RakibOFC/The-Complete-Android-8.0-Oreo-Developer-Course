package com.rakibofc.udemy46snapchatclone;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SnapsActivity extends AppCompatActivity {

    TextView textViewUserInfo;
    ListView listViewSnaps;
    FirebaseAuth mAuth;
    ArrayList<DataSnapshot> snapsInfo;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {

            case R.id.createSnap:

                startActivity(new Intent(this, CreateSnapActivity.class));
                break;

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
        setContentView(R.layout.activity_main);
        setTitle("Snapchat");

        mAuth = FirebaseAuth.getInstance();

        textViewUserInfo = findViewById(R.id.textViewUserInfo);
        listViewSnaps = findViewById(R.id.listViewSnaps);

        ArrayList<String> snapSenders = new ArrayList<>();
        snapsInfo = new ArrayList<>();

        textViewUserInfo.setText("Email: " + mAuth.getCurrentUser().getEmail());

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, snapSenders);
        listViewSnaps.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("snaps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                snapSenders.add(snapshot.child("from").getValue() + "");
                snapsInfo.add(snapshot);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                int i = 0;
                for (DataSnapshot snap : snapsInfo) {

                    if (snap.getKey().equals(snapshot.getKey())) {

                        snapSenders.remove(i);
                        snapsInfo.remove(i);
                    }
                    i++;
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listViewSnaps.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(SnapsActivity.this, ViewSnapActivity.class);

            intent.putExtra("key", snapsInfo.get(position).getKey() + "");
            intent.putExtra("from", snapsInfo.get(position).child("from").getValue() + "");
            intent.putExtra("imageName", snapsInfo.get(position).child("imageName").getValue() + "");
            intent.putExtra("imageURL", snapsInfo.get(position).child("imageURL").getValue() + "");
            intent.putExtra("message", snapsInfo.get(position).child("message").getValue() + "");
            startActivity(intent);
        });
    }
}