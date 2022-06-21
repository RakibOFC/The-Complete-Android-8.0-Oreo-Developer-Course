package com.rakibofc.udemy33webviews;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        // webView.loadUrl("https://rakibofc.blogspot.com/");

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Test Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Hello World</h1>\n" +
                "    <h2>Java WebView Layout</h2>\n" +
                "    <h3>Boom Boom Boom!</h3>\n" +
                "</body>\n" +
                "</html>";

        webView.loadData(html, "text/html", "UTF-8");
    }
}