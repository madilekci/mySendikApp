package com.example.mysendikapp.icerikSayfalari;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mysendikapp.R;

public class ActivityVideolar extends AppCompatActivity {
    private WebView webViewHeroes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolar);

        webViewHeroes = (WebView) findViewById(R.id.webviewVideolar);
        webViewHeroes.setWebViewClient(new ActivityVideolar.Callback());

        webViewHeroes.getSettings().setJavaScriptEnabled(true);
        webViewHeroes.loadUrl(getResources().getString(R.string.webVievVideolarUrl) );
    }
    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
}
