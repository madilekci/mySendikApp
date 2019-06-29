package com.example.mysendikapp.icerikSayfalari;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mysendikapp.R;
import com.example.mysendikapp.sosyal.ActivityFacebook;

public class ActivityHakkinda extends AppCompatActivity {
    private WebView webViewHeroes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hakkinda);

        webViewHeroes = (WebView) findViewById(R.id.webViewHakkinda);
        webViewHeroes.setWebViewClient(new ActivityHakkinda.Callback());

        webViewHeroes.getSettings().setJavaScriptEnabled(true);
        webViewHeroes.loadUrl(getResources().getString(R.string.webVievHakkimizdaUrl) );

    }

    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
}
