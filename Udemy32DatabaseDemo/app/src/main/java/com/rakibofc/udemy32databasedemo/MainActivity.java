package com.rakibofc.udemy32databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        String tableRow = "";

        try {

            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleID INTEGER, articleTitle VARCHAR, articleURL VARCHAR)");

            int articleId = 12345;
            String articleTitle = "Spring 2022, 192 DB CSE GED 305", articleURL = "https://www.facebook.com/";

            sqLiteDatabase.execSQL("DELETE FROM articles");

            String sql = "INSERT INTO articles (articleID, articleTitle, articleURL) VALUES (?, ?, ?)";
            SQLiteStatement statement = sqLiteDatabase.compileStatement(sql);
            statement.bindString(1, String.valueOf(articleId));
            statement.bindString(2, articleTitle);
            statement.bindString(3, articleURL);

            statement.execute();


            /*
            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);

            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR, age INT(3))");

            // sqLiteDatabase.execSQL("INSERT INTO users (name, age) VALUES ('Rakib', 22)");
            // sqLiteDatabase.execSQL("INSERT INTO users (name, age) VALUES ('Yamin', 5)");

            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM users", null);

            int nameIndex = cursor.getColumnIndex("name");
            int ageIndex = cursor.getColumnIndex("age");

            cursor.moveToFirst();

            tableRow = "Name\t\tAge\n";

            while (!cursor.isAfterLast()) {

                Log.i("Name", cursor.getString(nameIndex));
                Log.i("Age", cursor.getString(ageIndex));

                tableRow += cursor.getString(nameIndex) + "\t\t" + cursor.getString(ageIndex) + "\n";

                cursor.moveToNext();
            }*/
        } catch (Exception e) {}

        textView.setText(tableRow);
    }
}