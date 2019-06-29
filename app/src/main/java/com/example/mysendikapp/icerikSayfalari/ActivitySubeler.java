package com.example.mysendikapp.icerikSayfalari;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mysendikapp.R;
import com.example.mysendikapp.sosyal.ActivityFacebook;

public class ActivitySubeler extends AppCompatActivity {
    private WebView webViewHeroes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subeler);

        webViewHeroes = (WebView) findViewById(R.id.webVievSubeler);
        webViewHeroes.setWebViewClient(new ActivitySubeler.Callback());

        webViewHeroes.getSettings().setJavaScriptEnabled(true);
        webViewHeroes.loadUrl(getResources().getString(R.string.webVievSubelerUrl) );

    }

    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
}
