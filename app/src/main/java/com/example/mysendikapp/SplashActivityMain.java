package com.example.mysendikapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivityMain extends AppCompatActivity {
    String TAG = "SplashActivityMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(SplashActivityMain.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
