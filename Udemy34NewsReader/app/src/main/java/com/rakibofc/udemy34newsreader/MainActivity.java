package com.rakibofc.udemy34newsreader;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public ArrayList<String> articleID;
    public ArrayList<String> articleTitle;
    public ArrayList<String> articleUrl;
    public ArrayList<String> articleHtmlContent;
    public View progressBar;
    public ListView topStoriesListVIew;
    public SQLiteDatabase articleDB;
    public SharedPreferences sharedPreferences;
    public int size = 20;
    public ArrayAdapter arrayAdapter;

    public boolean isDataStore = false;

    public void setProgressBar() {

        runOnUiThread(() -> {

            arrayAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        });

    }

    public class TopStories extends AsyncTask<String, Void, String> {

        URL url;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;

        @Override
        protected String doInBackground(String... urls) {

            try {

                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                try {
                    inputStream = httpURLConnection.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);

                } catch (Exception e) {

                    // connection problem
                    return null;
                }

                String jsonTopStoryIDs = "", newContent;

                while ((newContent = bufferedReader.readLine()) != null) {

                    jsonTopStoryIDs += newContent;
                }

                String jsonArticle = "", tempArticleID, tempArticleTitle, tempArticleUrl;

                JSONArray jsonArray = new JSONArray(jsonTopStoryIDs);

                for (int i = 0; i < size; i++) {

                    tempArticleID = jsonArray.getString(i);

                    url = new URL("https://hacker-news.firebaseio.com/v0/item/" + tempArticleID + ".json?print=pretty");

                    httpURLConnection = (HttpURLConnection) url.openConnection();

                    inputStream = httpURLConnection.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    bufferedReader = new BufferedReader(inputStreamReader);

                    while ((newContent = bufferedReader.readLine()) != null) {

                        jsonArticle += newContent;
                    }

                    JSONObject jsonObject = new JSONObject(jsonArticle);

                    try {

                        tempArticleTitle = jsonObject.getString("title");
                        tempArticleUrl = jsonObject.getString("url");

                        articleID.add(tempArticleID);
                        articleTitle.add(tempArticleTitle);
                        articleUrl.add(tempArticleUrl);

                    } catch (Exception e) {

                        ++i;
                        size++;
                    }
                    jsonArticle = "";
                }

                return jsonTopStoryIDs;

            } catch (IOException | JSONException e) {

                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TopStories getContent = new TopStories();

        progressBar = findViewById(R.id.progressBar);
        topStoriesListVIew = findViewById(R.id.topStoriesListVIew);

        articleID = new ArrayList<>();
        articleTitle = new ArrayList<>();
        articleUrl = new ArrayList<>();
        articleHtmlContent = new ArrayList<>();

        sharedPreferences = this.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        isDataStore = sharedPreferences.getBoolean("isDataStore", false);

        articleDB = this.openOrCreateDatabase("Articles", MODE_PRIVATE, null);
        articleDB.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY, articleID INTEGER, articleTitle VARCHAR, articleURL VARCHAR)");

        Thread thread = new Thread(() -> {

            if (!isDataStore) {

                articleID.add("0");
                articleTitle.add("RakibOFC: A Resource for Online Coding Practice");
                articleUrl.add("https://rakibofc.blogspot.com/");

                try {

                    String data = getContent.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty").get();

                    if (data != null){

                        articleDB.execSQL("DELETE FROM articles");

                        for (int i = 0; i < articleID.size(); i++) {

                            String sql = "INSERT INTO articles (articleID, articleTitle, articleURL) VALUES (?, ?, ?)";
                            SQLiteStatement statement = articleDB.compileStatement(sql);
                            statement.bindString(1, articleID.get(i));
                            statement.bindString(2, articleTitle.get(i));
                            statement.bindString(3, articleUrl.get(i));

                            statement.execute();
                        }
                        setProgressBar();

                        isDataStore = true;
                        sharedPreferences.edit().putBoolean("isDataStore", isDataStore).apply();
                    }

                } catch (ExecutionException | InterruptedException e) {

                    e.printStackTrace();
                }

            } else {
                setProgressBar();
            }
        });
        thread.start();

        articleID.clear();
        articleTitle.clear();
        articleUrl.clear();

        Cursor cursor = articleDB.rawQuery("SELECT * FROM articles", null);

        int titleIndex = cursor.getColumnIndex("articleTitle");
        int urlIndex = cursor.getColumnIndex("articleURL");

        while (cursor.moveToNext()) {

            articleTitle.add(cursor.getString(titleIndex));
            articleUrl.add(cursor.getString(urlIndex));
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, articleTitle);
        topStoriesListVIew.setAdapter(arrayAdapter);

        topStoriesListVIew.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);

            intent.putExtra("url", articleUrl.get(position));
            startActivity(intent);

        });
    }
}