package com.example.mysendikapp.sosyal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mysendikapp.R;
import com.example.mysendikapp.dashboard.ActivityDashboard;

public class hangiSosyalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangi_sosyal);

        //Facebook
        findViewById(R.id.iv_facebook_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(hangiSosyalActivity.this, ActivityFacebook.class);
                startActivity(i);
            }
        });
        //Facebook\\


        //Twitter
        findViewById(R.id.iv_twitter_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(hangiSosyalActivity.this, ActivityTwitter.class);
                startActivity(i);
            }
        });
    }
}
