package com.example.mysendikapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mysendikapp.login.sifreDegistirActivity;

public class ayarlarActivity extends AppCompatActivity {
    private Button sifreDegistir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        sifreDegistir = (Button) findViewById(R.id.btn_sifreDegistir_Ayarlar);
        sifreDegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ayarlarActivity.this, sifreDegistirActivity.class);
                startActivity(i);
            }
        });
    }
}
