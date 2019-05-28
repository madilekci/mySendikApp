package com.example.mysendikapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ActivityClaimComplaint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_complaint);

        Button sendButton = (Button) findViewById(R.id.btnTalepGonder);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talepGonder(v);
            }
        });

    }

    public String getUsername(){
        SharedPreferences xd = getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();
        return xd.getString("username","NoUsername");
    }
    public String getPassword(){
        SharedPreferences xd = getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();
        return xd.getString("password","NoPassword");
    }

    public void talepGonder(View v) {

        EditText txt_1 = (EditText) findViewById(R.id.claimComplaint_txt);
        final String talep = txt_1.getText().toString();

        final String username=getUsername();
        final String password=getPassword();


        if (!(talep.equals(""))) {
            RequestQueue queue = Volley.newRequestQueue(ActivityClaimComplaint.this);

            String url = "https://www.yucelyavuz.com/muhammed/talepSikayetGonder.php";    // Post atılan adres.
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Toasty.success(ActivityClaimComplaint.this, response.toString(), Toast.LENGTH_LONG, true).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toasty.error(ActivityClaimComplaint.this, "Error :" + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("talep", talep);
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };
            queue.add(postRequest);
        } else {
            Toast.makeText(this, "Lütfen Gerekli Alanları Doldurun", Toast.LENGTH_LONG).show();
        }
    }
}


