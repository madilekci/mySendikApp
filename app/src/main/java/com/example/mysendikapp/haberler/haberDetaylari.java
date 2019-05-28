package com.example.mysendikapp.haberler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

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

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class haberDetaylari extends AppCompatActivity {
    haberModel ne_haber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haber_detaylari);

        haberOlustur();

        LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayoutHaberDetayı);

    }


    public void haberYazdirConsole(){
        System.out.println("*//--*------*-----*-------*-------//*");
        System.out.println(""+this.ne_haber.getContent());
        System.out.println(""+this.ne_haber.getId());
        System.out.println(""+this.ne_haber.getSummary());
        System.out.println(""+this.ne_haber.getTitle());
        System.out.println(""+this.ne_haber.getUrl());
        System.out.println("*//--*------*-----*-------*-------//*");
    }

    public void haberOlustur() {
        String id =getIntent().getExtras().getString("haber_id");
        this.ne_haber= new haberModel();
        this.getNewDetails(id,ne_haber);
        this.haberYazdirConsole();
    }




    public  void getNewDetails (final String haber_id, final  haberModel haberModel1){
        newsFeed.showSimpleProgressDialog(this, "Loading...", "Fetching Json", false);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.haberDetayUrl);    // Post atılan adres.

        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response to getDetail", ">>" + response);
                        try {
                            newsFeed.removeSimpleProgressDialog();
                            JSONObject obj = new JSONObject(response);
                            JSONObject dataobj = obj.getJSONObject("data");

                                haberModel1.setTitle(dataobj.getString("header"));
                                haberModel1.setContent(dataobj.getString("content"));
                                haberModel1.setUrl(dataobj.getString("picture"));
                                haberModel1.setDate(dataobj.getString("date"));
                                haberModel1.setView(dataobj.getString("readed"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("news_id", haber_id);
                return params;
            }
        };
        // request queue
        queue.add(jsonStringRequest);
    }

}
