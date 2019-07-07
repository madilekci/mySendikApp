package com.example.mysendikapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mysendikapp.login.sifreDegistirActivity;

public class ActivityAyarlar extends AppCompatActivity {

    private String TAG = "ActivityAyarlar";
    private Button sifreDegistir;
    private Switch bildirimKapat;
    private CharSequence mail = "", adress = "", website = "";
    private TextView tv_mail, tv_adress, tv_website;

    private SharedPreferences xd;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        xd = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editor = xd.edit();

        setupBanner();
        setupSettings();
        initSwitchStates();

    }


    @Override
    public void onPause() {
        editor.apply();
        super.onPause();
    }

    public void initSwitchStates() {
        boolean nAble = xd.getBoolean("isNotifyable", true);
        boolean sAble = xd.getBoolean("isSoundable", true);
        boolean vAble = xd.getBoolean("isVibratable", true);

        if (nAble) {
            bildirimKapat.setChecked(true);
        } else {
            bildirimKapat.setChecked(false);
        }

    }

    public void setupBanner() {
        mail = getResources().getString(R.string.companyMail);
        adress = getResources().getString(R.string.companyAdress);
        website = getResources().getString(R.string.companyWebSite);

        tv_mail = findViewById(R.id.tv_mail_ayarlar);
        tv_adress = findViewById(R.id.tv_adress_ayarlar);
        tv_website = findViewById(R.id.tv_website_ayarlar);

        tv_mail.append(" " + mail);
        tv_adress.append(" " + adress);
        tv_website.append(" " + website);
    }

    public void setupSettings() {
        sifreDegistir = (Button) findViewById(R.id.btn_sifreDegistir_Ayarlar);
        sifreDegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityAyarlar.this, sifreDegistirActivity.class);
                startActivity(i);
            }
        });

        bildirimKapat = (Switch) findViewById(R.id.sw_bildirimKapat_ayarlar);
        bildirimKapat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {                //  The isChecked will be true if the switch is in the On position
                editor.remove("isNotifyable");
                editor.putBoolean("isNotifyable", isChecked);
                Log.d(TAG, "isNotifyable --> " + isChecked);
            }
        });

    }

    public void onClickAdress(View v){
        //        40°42'14.5"N 29°53'00.3"E
        String uri = "http://maps.google.com/maps?daddr=" +getResources().getString(R.string.companyAdressCoordinates) ;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

}
