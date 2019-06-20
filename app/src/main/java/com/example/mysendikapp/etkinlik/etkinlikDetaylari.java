package com.example.mysendikapp.etkinlik;

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
import com.example.mysendikapp.haberler.haberAkisi;
import com.example.mysendikapp.haberler.haberDetaylari;
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

public class etkinlikDetaylari extends AppCompatActivity implements Html.ImageGetter{
    etkinlikModel ne_etkinlik;

    String TAG ="etkinlikDetaylari";
    TextView mTv;
    LinearLayout ll_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etkinlik_detaylari);
        this.ne_etkinlik = new etkinlikModel();
        this.getEtkinlikDetails(getIntent().getExtras().getString("etkinlik_id"), this.ne_etkinlik);

        ll_root=(LinearLayout) findViewById(R.id.ll_root_etkinlikDetay);
        ll_root.setVisibility(View.INVISIBLE);
    }

    public void getEtkinlikDetails(final String etkinlik_id, final etkinlikModel etkinlikModel_ne_etkinlik) {
        haberAkisi.showSimpleProgressDialog(this, "Loading...", "Fetching Json", false);

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
            haberAkisi.removeSimpleProgressDialog();
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
        Button btnDate = (Button) findViewById(R.id.btn_date_etkinlikDetaylari);
        btnDate.setText("" + this.ne_etkinlik.getDate());

        TextView tv_baslik = (TextView) findViewById(R.id.tv_baslik_etkinlikDetaylari);
        tv_baslik.setText(this.ne_etkinlik.getTitle());

        ImageView iv = (ImageView) findViewById(R.id.iv_etkinlikDetaylari);
        Picasso.get().load(""+this.ne_etkinlik.getUrl()).into(iv);

        Spanned spanned = Html.fromHtml(this.ne_etkinlik.getContent(), this, null);
        mTv = (TextView) findViewById(R.id.tv_content_etkinlikDetaylari);
        mTv.setText(spanned);
        ll_root.setVisibility(View.VISIBLE);
    }


    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.logo);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new etkinlikDetaylari.LoadImage().execute(source, d);

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
