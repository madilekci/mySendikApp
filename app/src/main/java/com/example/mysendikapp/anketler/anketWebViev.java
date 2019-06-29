package com.example.mysendikapp.anketler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mysendikapp.R;
import com.example.mysendikapp.sosyal.ActivityFacebook;

public class anketWebViev extends AppCompatActivity {
    private WebView webViewHeroes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anket_web_viev);
        webViewHeroes = (WebView) findViewById(R.id.webviewAnketDoldur);
        webViewHeroes.setWebViewClient(new anketWebViev.Callback());

        webViewHeroes.getSettings().setJavaScriptEnabled(true);
        webViewHeroes.loadUrl(getResources().getString(R.string.webVievanketDoldurUrl) );

    }

    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
}
