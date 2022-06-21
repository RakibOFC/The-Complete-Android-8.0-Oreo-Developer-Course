package com.rakibofc.udemy31notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class NoteEditorActivity extends AppCompatActivity {

    EditText editTextTitle;
    EditText editTextNote;

    int noteID;
    boolean isNewNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextNote = findViewById(R.id.editTextNote);

        Intent intent = getIntent();

        noteID = intent.getIntExtra("position", 0);

        if (noteID == -1) {

            isNewNote = true;
            noteID = MainActivity.noteTitles.size();

        } else {

            editTextTitle.setText(MainActivity.noteTitles.get(noteID));
            editTextNote.setText(MainActivity.notes.get(noteID));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!editTextTitle.getText().toString().equals("") && !editTextNote.getText().toString().equals("")) {

            if (isNewNote) {

                MainActivity.noteTitles.add(noteID, editTextTitle.getText().toString());
                MainActivity.notes.add(noteID, editTextNote.getText().toString());

            } else {

                MainActivity.noteTitles.set(noteID, editTextTitle.getText().toString());
                MainActivity.notes.set(noteID, editTextNote.getText().toString());
            }
            MainActivity.arrayAdapter.notifyDataSetChanged();

            try {

                MainActivity.sharedPreferences.edit().putString("title", ObjectSerializer.serialize(MainActivity.noteTitles)).apply();
                MainActivity.sharedPreferences.edit().putString("note", ObjectSerializer.serialize(MainActivity.notes)).apply();
                Toast.makeText(NoteEditorActivity.this, "Note Saved!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {

                Toast.makeText(NoteEditorActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}