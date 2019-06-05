package com.example.mysendikapp.haberler;

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
        this.getNewDetails(getIntent().getExtras().getString("haber_id"),this.ne_haber);

    }

    public  void getNewDetails (final String haber_id, final  haberModel haberModel_neHaber) {
        haberAkisi.showSimpleProgressDialog(this, "Loading...", "Fetching Json", false);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.haberDetayUrl);    // Post atılan adres.

        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response to getDetail", ">>" + response);
                        parseJson(response , haberModel_neHaber);
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
    public void parseJson(String response , haberModel haberModel_neHaber){

        try {
            haberAkisi.removeSimpleProgressDialog();
            JSONObject obj = new JSONObject(response);
            JSONObject dataobj = obj.getJSONObject("data");

            haberModel_neHaber.setTitle(dataobj.getString("header"));
            haberModel_neHaber.setContent(dataobj.getString("content"));
            haberModel_neHaber.setUrl(dataobj.getString("picture"));
            haberModel_neHaber.setDate(dataobj.getString("date"));
            haberModel_neHaber.setView(dataobj.getString("readed"));
            Log.d("Response to getTitle", "" + ne_haber.getTitle());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        haberYazdirConsole();
        haberGoster();


    }
    public void haberYazdirConsole(){
        System.out.println("*//--*------*-----*-------*-------//*");
        System.out.println(""+this.ne_haber.getContent());
        System.out.println(""+this.ne_haber.getTitle());
        System.out.println(""+this.ne_haber.getUrl());
        System.out.println(""+this.ne_haber.getDate());
        System.out.println(""+this.ne_haber.getView());
        System.out.println("*//--*------*-----*-------*-------//*");
    }
    public void haberGoster(){
        if(this.ne_haber.equals( null ) ){
            return;
        }
        Button btnDate = (Button) findViewById(R.id.btn_date_haberDetaylari);
        btnDate.setText(""+this.ne_haber.getDate() );

        Button btnView = (Button) findViewById(R.id.btn_view_haberDetaylari);
        btnView.setText(""+this.ne_haber.getView()+" Kez görüntülendi" );

        TextView tv_baslik = (TextView) findViewById(R.id.tv_baslik_haberDetaylari);
        tv_baslik.setText(this.ne_haber.getTitle() );

        ImageView iv = (ImageView) findViewById(R.id.iv_haberDetaylari);
        Picasso.get().load("https://"+ this.ne_haber.getUrl() ).into(iv);

        TextView tv_content=(TextView) findViewById(R.id.tv_content_haberDetaylari);
        tv_content.setText(this.ne_haber.getContent() );
    }



}
