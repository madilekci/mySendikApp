package com.example.mysendikapp.sosyal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.example.mysendikapp.R;

public class ActivityTwitter extends AppCompatActivity {


    private WebView webViewHeroes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        String yourHTML = "<a class=\"twitter-timeline\" data-theme=\"light\" data-link-color=\"#f67280\" href=\"https://twitter.com/madilekci/lists/e-stk?ref_src=twsrc%5Etfw\">A twitter list by e-STK</a> <script async src=\"https://platform.twitter.com/widgets.js\" charset=\"utf-8\"></script>";

        webViewHeroes = (WebView) findViewById(R.id.webviewTwitter);
        webViewHeroes.getSettings().setJavaScriptEnabled(true);
        webViewHeroes.loadData(yourHTML, "text/html", "utf-8");

    }


}



