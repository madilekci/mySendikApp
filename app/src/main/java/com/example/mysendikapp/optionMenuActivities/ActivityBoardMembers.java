package com.example.mysendikapp.optionMenuActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.mysendikapp.R;

public class ActivityBoardMembers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_members);

        WebView webview = (WebView) findViewById(R.id.webviewBoardMembers);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(getResources().getString(R.string.webViewBoardMembersUrl) );
    }
}
