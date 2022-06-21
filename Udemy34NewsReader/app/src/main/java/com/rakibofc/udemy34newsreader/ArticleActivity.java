package com.rakibofc.udemy34newsreader;

import static android.webkit.WebSettings.FORCE_DARK_OFF;
import static android.webkit.WebSettings.FORCE_DARK_ON;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ArticleActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint({"WrongConstant", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        WebView articleView = findViewById(R.id.articleView);

        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    WebSettingsCompat.setForceDark(articleView.getSettings(), FORCE_DARK_ON);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    WebSettingsCompat.setForceDark(articleView.getSettings(), FORCE_DARK_OFF);
                    break;
            }
        }
        articleView.getSettings().setJavaScriptEnabled(true);

        articleView.setWebViewClient(new WebViewClient());

        Intent intent = getIntent();
        articleView.loadUrl(intent.getStringExtra("url"));

    }
}