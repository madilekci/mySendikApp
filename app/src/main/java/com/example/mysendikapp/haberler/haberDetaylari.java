package com.example.mysendikapp.haberler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class haberDetaylari extends AppCompatActivity implements Html.ImageGetter {
    haberModel ne_haber;
    String TAG ="haberDetaylari";
    TextView mTv;
    LinearLayout ll_root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haber_detaylari);
        this.ne_haber=new haberModel();
        this.getNewDetails(getIntent().getExtras().getString("haber_id"),this.ne_haber);

        ll_root= (LinearLayout) findViewById(R.id.ll_root_haberDetaylari);
        ll_root.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(haberDetaylari.this , haberAkisi.class);
        startActivity(i);
    }

    public  void getNewDetails (final String haber_id, final  haberModel haberModel_neHaber) {
        haberAkisi.showSimpleProgressDialog(this, "Loading...", "Fetching Json", false);
        Log.d(TAG,"getNewDetails haber_id - > "+haber_id);
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
        btnView.setText(""+this.ne_haber.getView()+" Görüntüleme" );

        TextView tv_baslik = (TextView) findViewById(R.id.tv_baslik_haberDetaylari);
        tv_baslik.setText(this.ne_haber.getTitle() );

        ImageView iv = (ImageView) findViewById(R.id.iv_haberDetaylari);
        Picasso.get().load(""+this.ne_haber.getUrl() ).into(iv);

        Spanned spanned = Html.fromHtml(this.ne_haber.getContent(), this, null);
        mTv = (TextView) findViewById(R.id.tv_content_haberDetaylari);
        mTv.setText(spanned);
        ll_root.setVisibility(View.VISIBLE);
    }



    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.logo);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new haberDetaylari.LoadImage().execute(source, d);

        return d;
    }
    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "onPostExecute drawable " + mDrawable);
            Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = mTv.getText();
                mTv.setText(t);
            }
        }
    }



}
