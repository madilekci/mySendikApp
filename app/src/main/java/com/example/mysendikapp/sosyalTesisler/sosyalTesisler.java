package com.example.mysendikapp.sosyalTesisler;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.example.mysendikapp.R;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class sosyalTesisler extends AppCompatActivity {
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosyal_tesisler);

        findViewById(R.id.btn_otel_sosyalTesisler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i= new Intent(sosyalTesisler.this,Otel.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btn_tesis_sosyalTesisler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i= new Intent(sosyalTesisler.this,Tesis.class);
                startActivity(i);
            }
        });


    }


}

