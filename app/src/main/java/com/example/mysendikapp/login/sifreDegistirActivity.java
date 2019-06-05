package com.example.mysendikapp.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.R;
import com.example.mysendikapp.dashboard.SplashAct;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class sifreDegistirActivity extends AppCompatActivity {
String TAG= "sifreDegistirActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_degistir);
    }

    public void btnSifreDegistirOnclick(View v){
        EditText sifretxt= (EditText) findViewById(R.id.tv_yeniSifre_sifreDegistir);
        EditText sifreTekrartxt= (EditText) findViewById(R.id.tv_sifreTekrar_sifreDegistir);

        String sifre= ""+sifretxt.getText().toString();
        String sifreTekrar= ""+sifreTekrartxt.getText().toString();

        if(sifre.equals("")){
            Toasty.info(this,"Lütfen yeni şifrenizi girin",Toasty.LENGTH_SHORT).show();
        }else if (sifreTekrar.equals("") ){
            Toasty.info(this,"Lütfen yeni şifrenizi tekrar girin",Toasty.LENGTH_SHORT).show();
        }else if (!(sifre.equals(sifreTekrar)) ){
            Toasty.info(this,"Girilen şifreler uyuşmuyor",Toasty.LENGTH_SHORT).show();
        }else {
            changePassword(sifre);
        }
    }

    public void changePassword(final String sifre){
        SharedPreferences xd = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        final String userToken = xd.getString("userToken", "noTokens");
        Log.d(TAG, "userToken : "+userToken);
        Log.d(TAG, "Yeni Şifre: "+sifre);


        String url = getResources().getString(R.string.sifreGuncelleUrl);    // Post atılan adres.
        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"response :"+response);
                            if(isPasswordChanged(response)){
                                Toasty.info(sifreDegistirActivity.this,"Şifreniz değiştirildi.",Toasty.LENGTH_SHORT).show();
                                goToMenuActivity();
                            }else {
                                Toasty.info(sifreDegistirActivity.this,"Bilinmeyen bir hata oluştu.",Toasty.LENGTH_SHORT).show();
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_token", userToken);
                params.put("new_pass", sifre);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);
    }

    public boolean isPasswordChanged(String response){
        try {
            JSONObject obj = new JSONObject(response);
            int error= obj.getInt("error");
            if(error==0){
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void goToMenuActivity() {

        SharedPreferences xd = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();
        editor.remove("isLogged");
        editor.putBoolean("isLogged", true);
        editor.apply();
        Toasty.success(this, "Giriş başarılı.", Toast.LENGTH_SHORT, true).show();

        Intent i = new Intent(this, SplashAct.class);
        startActivity(i);

    }

    @Override
    public void onBackPressed() {
    }
}
