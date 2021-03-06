package com.example.mysendikapp.etkinlik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.R;
import com.example.mysendikapp.bildirimler.bildirimAkisi;
import com.example.mysendikapp.dashboard.ActivityDashboard;
import com.example.mysendikapp.haberler.haberAkisi;
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

public class etkinlikDetaylari extends AppCompatActivity implements Html.ImageGetter {
    etkinlikModel ne_etkinlik;
    String bid = "";
    
    String TAG = "etkinlikDetaylari";
    TextView mTv, olusturanTv;
    LinearLayout ll_root;
    String adSoyad = "", firma = "", telNo = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etkinlik_detaylari);
        this.ne_etkinlik = new etkinlikModel();
        this.getEtkinlikDetails(getIntent().getExtras().getString("etkinlik_id"), this.ne_etkinlik);
    
        try {
            String bid = (getIntent().getExtras().getString("notification_id") );
            if(bid != null){
                bildirimdenAcildimOkundum( (getIntent().getExtras().getString("notification_id") ) , "2");
            }
        }catch (NullPointerException ex){
            Log.d(TAG,"bildirimdenAcildimOkundum Error --> "+ex.getMessage() );
        }
        
        olusturanTv = (TextView) findViewById(R.id.tv_olusturan_etkinlikDetaylari);
        
        ll_root = (LinearLayout) findViewById(R.id.ll_root_etkinlikDetay);
        ll_root.setVisibility(View.INVISIBLE);
    }
    
    @Override
    public void onBackPressed() {
        Intent i = new Intent(etkinlikDetaylari.this, etkinlikAkisi.class);
        startActivity(i);
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
                        Log.d(TAG, "Response to getDetail " + etkinlik_id + " ->>" + response);
                        parseJson(response, etkinlikModel_ne_etkinlik);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Log.d(TAG,"Error while fetching JSON");
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
            
            if(dataobj.toString().equals("{\"error\":\"0\"}")) {          //Etkinlik silinmiş mi ?
                Log.d(TAG,"Etkinlik silinmiş. ");
                return;
            }
            etkinlikModel_ne_etkinlik.setTitle(dataobj.getString("header"));
            etkinlikModel_ne_etkinlik.setContent(dataobj.getString("content"));
            etkinlikModel_ne_etkinlik.setUrl(dataobj.getString("picture"));
            etkinlikModel_ne_etkinlik.setDate(dataobj.getString("date"));
            
            
            adSoyad = dataobj.getString("adi");
            firma = dataobj.getString("firma");
            telNo = dataobj.getString("telefon");
            
            Log.d(TAG, "adSoyad --> " + adSoyad);
            if (adSoyad.equals("null")) {
                Log.d(TAG, "IF IF IF IF ");
                olusturanTv.setVisibility(View.INVISIBLE);
            } else {
                olusturanTv.setText(olusturanTv.getText() + "\n" + adSoyad);
                if (!firma.equals("")) {
                    olusturanTv.setText(olusturanTv.getText() + "\n" + firma);
                }
                if (!telNo.equals("")) {
                    olusturanTv.setText(olusturanTv.getText() + "\n" + telNo);
                }
            }
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
    public void etkinlikSilinmiş(){
        TextView tv_baslik = (TextView) findViewById(R.id.tv_baslik_etkinlikDetaylari);
        tv_baslik.setText("Görmek istediğiniz etkinlik silinmiş.");
    }
    public void etkinlikGoster() {
        if (this.ne_etkinlik.equals(null)) {
            return;
        }else if(this.ne_etkinlik.title.equals("null")){
            etkinlikSilinmiş();
        }else {
            Button btnDate = (Button) findViewById(R.id.btn_date_etkinlikDetaylari);
            btnDate.setText("" + this.ne_etkinlik.getDate());
    
            TextView tv_baslik = (TextView) findViewById(R.id.tv_baslik_etkinlikDetaylari);
            tv_baslik.setText(this.ne_etkinlik.getTitle());

            ImageView iv = (ImageView) findViewById(R.id.iv_etkinlikDetaylari);
            Picasso.get().load("" + this.ne_etkinlik.getUrl()).into(iv);
    
            Spanned spanned = Html.fromHtml(this.ne_etkinlik.getContent(), this, null);
            mTv = (TextView) findViewById(R.id.tv_content_etkinlikDetaylari);
            mTv.setText(spanned);
        }
        ll_root.setVisibility(View.VISIBLE);
    }
    
    public void bildirimdenAcildimOkundum(final String bid, final String btype) {
        
        Log.d(TAG,"bildirimdenAcildimOkundum");
        
        final String bUserToken = getUserToken();
        Log.d(TAG, "\n\nFetching JSON ....");
        Log.d(TAG, "userToken  --> " + bUserToken);
        Log.d(TAG, "bid  --> " + bid);
        Log.d(TAG, "type  --> " + btype);
        
        
        String url = getResources().getString(R.string.bildirimOkunduUrl);    // Post atılan adres.
        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response to bildirimdenAcildimOkundum >> " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(etkinlikDetaylari.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", bUserToken);
                params.put("id", bid);
                params.put("type", btype);
                return params;
            }
        };
        
        // request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        
        jsonStringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));    // Yeniden istek gönderebilmek için uyulması gereken kurallar
        
        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);
        
    }       //Bildirimin okunduğuna dair bilgi gönderir.
    
    
    public String getUserToken() {
        SharedPreferences xd = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();
        return xd.getString("userToken", "noTokens");
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
