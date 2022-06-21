package com.rakibofc.udemy31notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.shadow.ShadowRenderer;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    ListView myNotesListView;

    public static ArrayList<String> noteTitles;
    public static ArrayList<String> notes;
    public static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.add_new_note:

                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);

                intent.putExtra("position", -1);

                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myNotesListView = findViewById(R.id.myNotesListView);

        sharedPreferences = this.getSharedPreferences("com.rakibofc.udemy31notes", Context.MODE_PRIVATE);

        noteTitles = new ArrayList<>();
        notes  = new ArrayList<>();

        // noteTitles.add("Example Note");
        noteTitles.clear();
        notes.clear();

        try {

            noteTitles = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("title", ObjectSerializer.serialize(new ArrayList<>())));
            notes = (ArrayList<String>) ObjectSerializer.deserialize(MainActivity.sharedPreferences.getString("note", ObjectSerializer.serialize(new ArrayList<>())));

        } catch (IOException e) {

            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, noteTitles);
        myNotesListView.setAdapter(arrayAdapter);

        myNotesListView.setOnItemClickListener((parent, view, position, id) -> {
            // Selected item
            Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);

            intent.putExtra("position", position);

            startActivity(intent);

        });

        myNotesListView.setOnItemLongClickListener((parent, view, position, id) -> {

            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_delete)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        noteTitles.remove(position);
                        arrayAdapter.notifyDataSetChanged();

                        notes.remove(position);

                        try {

                            sharedPreferences.edit().putString("title", ObjectSerializer.serialize(MainActivity.noteTitles)).apply();
                            sharedPreferences.edit().putString("note", ObjectSerializer.serialize(MainActivity.notes)).apply();
                            Toast.makeText(MainActivity.this, "Delete selected item", Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {

                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    })
                    .setNegativeButton("No", null).show();

            return true;
        });
    }
}