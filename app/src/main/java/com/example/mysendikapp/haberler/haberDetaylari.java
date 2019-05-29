package com.example.mysendikapp.haberler;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class haberDetaylari extends AppCompatActivity {
    haberModel ne_haber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haber_detaylari);

        this.ne_haber=new haberModel();
        haberOlustur();
    }


    public void haberYazdirConsole(){
        System.out.println("*//--*------*-----*-------*-------//*");
        System.out.println(""+this.ne_haber.getContent());
        System.out.println(""+this.ne_haber.getTitle());
        System.out.println(""+this.ne_haber.getUrl());
        System.out.println(""+this.ne_haber.getDate());
        System.out.println(""+this.ne_haber.getView());
        System.out.println("*//--*------*-----*-------*-------//*");
        haberGoster();
    }
    public void haberGoster(){
        if(this.ne_haber.getDate().equals( null ) ){
            return;
        }
        Button btnDate = (Button) findViewById(R.id.btn_haberDetaylari_Date);
        btnDate.setText(""+this.ne_haber.getDate() );

        Button btnView = (Button) findViewById(R.id.btn_haberDetaylari_View);
        btnView.setText(""+this.ne_haber.getView()+" Kez görüntülendi" );

        TextView tv_baslik = (TextView) findViewById(R.id.tvHaberBaslik);
        tv_baslik.setText(this.ne_haber.getTitle() );

        ImageView iv = (ImageView) findViewById(R.id.haber_detaylari_imageView);
        Picasso.get().load("https://"+ this.ne_haber.getUrl() ).into(iv);

        TextView tv_content=(TextView) findViewById(R.id.tvHaberContent);
        tv_content.setText(this.ne_haber.getContent() );
    }

    public void haberOlustur() {
        String id =getIntent().getExtras().getString("haber_id");
        this.getNewDetails(id,this.ne_haber);
        final Handler handler = new Handler();

        final View contentView;
        final View loadingView;
        contentView = (LinearLayout) findViewById(R.id.contentView);
        loadingView = findViewById(R.id.haber_detaylari_progressBar);

        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                haberDetaylari.this.haberYazdirConsole();
                loadingView.setVisibility(View.GONE);
                contentView.setVisibility(View.VISIBLE);
            }
        }, 300);

    }

    public  void getNewDetails (final String haber_id, final  haberModel haberModel_neHaber){
        newsFeed.showSimpleProgressDialog(this, "Loading...", "Fetching Json", false);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.haberDetayUrl);    // Post atılan adres.

        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("Response getDetail id",">>"+haber_id);
                        Log.d("Response to getDetail", ">>" + response);
                        try {
                            newsFeed.removeSimpleProgressDialog();
                            JSONObject obj = new JSONObject(response);
                            JSONObject dataobj = obj.getJSONObject("data");

                            haberModel_neHaber.setTitle(dataobj.getString("header"));
                            haberModel_neHaber.setContent(dataobj.getString("content"));
                            haberModel_neHaber.setUrl(dataobj.getString("picture"));
                            haberModel_neHaber.setDate(dataobj.getString("date"));
                            haberModel_neHaber.setView(dataobj.getString("readed"));
                            Log.d("Response to getTitle" ,""+ne_haber.getTitle());


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
        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);
    }



}
