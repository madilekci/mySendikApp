package com.example.mysendikapp.icerikSayfalari;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class ActivitySozlesme extends AppCompatActivity {
    String userTokenPost;
    String sozlesmeLinki;
    String TAG = "ActivitySozlesme";

    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sozlesme);

        userTokenPost = getUserToken();
        fetchingJSON();
    }

    private void fetchingJSON() {
        Log.d(TAG, "Fetching JSON ....");
        Log.d(TAG, "userToken  --> " + userTokenPost);


        String url = getResources().getString(R.string.sozlesmeUrl);    // Post atılan adres.
        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response to etkinlikOlustur >> " + response);
                        parseJSONData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(ActivitySozlesme.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", userTokenPost);
                return params;
            }
        };

        // request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        jsonStringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));    // Yeniden istek gönderebilmek için uyulması gereken kurallar

        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);

    }

    public void parseJSONData(String response) {
        sozlesmeLinki = "";
        JSONObject dataobj = new JSONObject();
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray dataArray = obj.getJSONArray("data");


            dataobj = dataArray.getJSONObject(0);
            sozlesmeLinki = dataobj.getString("dosya");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        startDownload();

    }

    public void startDownload() {
        Log.d(TAG, "Sözlesme linki --> " + sozlesmeLinki);
        Uri uri = Uri.parse(sozlesmeLinki);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        this.onBackPressed();
    }




    public String getUserToken() {
        SharedPreferences xd = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();
        return xd.getString("userToken", "noTokens");
    }

}
