package com.example.mysendikapp.sosyal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mysendikapp.R;


public class ActivityFacebook extends AppCompatActivity {

    private WebView webViewHeroes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);


        webViewHeroes = (WebView) findViewById(R.id.webviewFacebook);
        webViewHeroes.setWebViewClient(new Callback());

        webViewHeroes.getSettings().setJavaScriptEnabled(true);
        webViewHeroes.loadUrl(getResources().getString(R.string.webViewFacebookUrl) );

    }

    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
}




//http://touch.facebook.com/HARBISSENDIKASI