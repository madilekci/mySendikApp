package com.example.mysendikapp.etkinlik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.R;
import com.example.mysendikapp.haberler.haberModel;
import com.example.mysendikapp.haberler.newsFeed;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class etkinlikDetaylari extends AppCompatActivity {
    etkinlikModel ne_etkinlik;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etkinlik_detaylari);
        this.ne_etkinlik = new etkinlikModel();
        this.getEtkinlikDetails(getIntent().getExtras().getString("etkinlik_id"), this.ne_etkinlik);
    }

    public void getEtkinlikDetails(final String etkinlik_id, final etkinlikModel etkinlikModel_ne_etkinlik) {
        newsFeed.showSimpleProgressDialog(this, "Loading...", "Fetching Json", false);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.etkinlikDetayUrl);    // Post atılan adres.

        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response to getDetail", etkinlik_id+">>" + response);
                        parseJson(response, etkinlikModel_ne_etkinlik);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("event_id", etkinlik_id);
                return params;
            }
        };
        // request queue
        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);
    }

    public void parseJson(String response, etkinlikModel etkinlikModel_ne_etkinlik) {

        try {
            newsFeed.removeSimpleProgressDialog();
            JSONObject obj = new JSONObject(response);
            JSONObject dataobj = obj.getJSONObject("data");

            etkinlikModel_ne_etkinlik.setTitle(dataobj.getString("header"));
            etkinlikModel_ne_etkinlik.setContent(dataobj.getString("content"));
            etkinlikModel_ne_etkinlik.setUrl(dataobj.getString("picture"));
            etkinlikModel_ne_etkinlik.setDate(dataobj.getString("date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        etkinlikYazdirConsole();
        etkinlikGoster();


    }

    public void etkinlikYazdirConsole() {
        System.out.println("*//--*------*-----*-------*-------//*");
        System.out.println("" + this.ne_etkinlik.getContent());
        System.out.println("" + this.ne_etkinlik.getTitle());
        System.out.println("" + this.ne_etkinlik.getUrl());
        System.out.println("" + this.ne_etkinlik.getDate());
        System.out.println("*//--*------*-----*-------*-------//*");
    }

    public void etkinlikGoster() {
        if (this.ne_etkinlik.equals(null)) {
            return;
        }
        Button btnDate = (Button) findViewById(R.id.btn_etkinlikDetaylari_Date);
        btnDate.setText("" + this.ne_etkinlik.getDate());

        TextView tv_baslik = (TextView) findViewById(R.id.tvEtkinlikBaslik);
        tv_baslik.setText(this.ne_etkinlik.getTitle());

        ImageView iv = (ImageView) findViewById(R.id.etkinlik_detaylari_imageView);
        Picasso.get().load("https://" + this.ne_etkinlik.getUrl()).into(iv);

        TextView tv_content = (TextView) findViewById(R.id.tvEtkinlikContent);
        tv_content.setText(this.ne_etkinlik.getContent());
    }
}
