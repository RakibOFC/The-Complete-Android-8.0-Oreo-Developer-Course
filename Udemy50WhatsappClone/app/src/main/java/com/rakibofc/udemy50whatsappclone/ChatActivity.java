package com.rakibofc.udemy50whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    ArrayList<String> messageHistory;
    ArrayAdapter arrayAdapter;
    ListView listViewMsgHistory;
    EditText editTextMsg;
    Button buttonSend;
    String sender, receiver;
    DatabaseReference databaseReference, senderDatabaseReference, receiverDatabaseReference;

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
                startActivity(new Intent(ChatActivity.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.setTitle(getIntent().getStringExtra("fullName"));

        // Value Initialization
        listViewMsgHistory = findViewById(R.id.listViewMsgHistory);
        editTextMsg = findViewById(R.id.editTextMsg);
        buttonSend = findViewById(R.id.buttonSend);

        messageHistory = new ArrayList<>();

        sender = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        receiver = getIntent().getStringExtra("receiver");

        // Manage Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        senderDatabaseReference = databaseReference.child(sender).child("chats").child(receiver);
        receiverDatabaseReference = databaseReference.child(receiver).child("chats").child(sender);

        // Control ListView
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, messageHistory);
        listViewMsgHistory.setAdapter(arrayAdapter);

        // Get Message History from Firebase Database
        getValueFromDatabase();

        // Clicked Send Button
        buttonSend.setOnClickListener(v -> {

            if (!editTextMsg.getText().toString().isEmpty()) {

                setValueInDatabase();

                getValueFromDatabase();

                editTextMsg.setText("");
            }
        });
    }

    private void setValueInDatabase() {

        // Generate Message ID
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmssddMMyyyy");
        Date date = new Date();
        String messageID = "" + simpleDateFormat.format(date);

        // Set Message Type in Database
        Message messageSenderSide = new Message(editTextMsg.getText().toString(), "send");
        Message messageReceiverSide = new Message(editTextMsg.getText().toString(), "receive");

        // Store Message in Database
        senderDatabaseReference.child(messageID).setValue(messageSenderSide);
        receiverDatabaseReference.child(messageID).setValue(messageReceiverSide);
    }

    private void getValueFromDatabase() {

        senderDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageHistory.clear();

                for (DataSnapshot messageID : snapshot.getChildren()) {

                    messageHistory.add(messageID.child("message").getValue() + "");
                }
                arrayAdapter.notifyDataSetChanged();
                // arrayAdapter.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}