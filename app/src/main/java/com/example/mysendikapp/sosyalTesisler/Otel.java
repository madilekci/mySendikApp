package com.example.mysendikapp.sosyalTesisler;

import android.app.DatePickerDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mysendikapp.R;

import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class Otel extends AppCompatActivity {
    String TAG = "Otel";
    String selectedDate1 = "", selectedDate2 = "";      //Secilen tarih ve saat degerleri

    Button btnTarihSec, btnSend;
    EditText etKisiSayisi;
    Spinner sp;

    ArrayList<String> OtelSecenekleri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otel);

        btnTarihSec = (Button) findViewById(R.id.btn_tarihsec_otel);
        btnTarihSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(1);
            }
        });

        btnSend = (Button) findViewById(R.id.btn_send_otel);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReq();
            }
        });

        ///
        OtelSecenekleri = new ArrayList<String>();
        sp = (Spinner) findViewById(R.id.sp_otelSec);

        etKisiSayisi = (EditText) findViewById(R.id.et_kisiSayisi_otel);
    }

    public void sendReq() {
        if (checkConditions()) {
            Toasty.warning(this, "Rezervasyon talebiniz alındı..", Toasty.LENGTH_LONG).show();
            clearPage();
        } else {
            Toasty.error(this, "Lütfen gerekli alanları doldurun !", Toasty.LENGTH_LONG).show();
        }
    }

    public void pickDate(final int whichDate) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Otel.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Log.d(TAG,"Selected dates --> \n1- "+selectedDate1+"\n 2- "+selectedDate2);
                        if (whichDate == 1) {
                            selectedDate1 = "" + day + "." + (month + 1) + "." + year;
                            btnTarihSec.setText(selectedDate1);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    // Actions to do after 5 seconds
                                    pickDate(2);
                                }
                            }, 500);
                        } else if (whichDate == 2) {
                            selectedDate2 = "" + day + "." + (month + 1) + "." + year;
                            btnTarihSec.setText(selectedDate1+" - "+selectedDate2);
                        }
                        Log.d(TAG,"Selected dates --> \n1- "+selectedDate1+"\n 2- "+selectedDate2);

                    }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-10000);
        if (whichDate == 1){
            datePickerDialog.setTitle("Başlangıç tarihi seçin");
        }else if (whichDate == 2){
            datePickerDialog.setTitle("Bitiş tarihi seçin");
        }
        datePickerDialog.show();
    }


    public void clearPage() {
        finish();
        startActivity(getIntent());
    }

    public boolean checkConditions() {
        Log.d(TAG, "etGetText --> " + etKisiSayisi.getText());
        if (sp.getSelectedItem().equals("")) {
            return false;
        }
        if (etKisiSayisi.getText().toString().equals("")) {
            return false;
        }
        if (selectedDate1.equals("") || selectedDate2.equals("")) {
            return false;
        }

        return true;
    }

}
