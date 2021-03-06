package com.example.mysendikapp.dashboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.anketler.anketWebViev;
import com.example.mysendikapp.ActivityAyarlar;
import com.example.mysendikapp.anlasmaliYerlerActivity;
import com.example.mysendikapp.bildirimler.bildirimAkisi;
import com.example.mysendikapp.etkinlik.etkinlikAkisi;
import com.example.mysendikapp.etkinlik.etkinlikOlustur;
import com.example.mysendikapp.ActivityTalepSikayet;
import com.example.mysendikapp.R;
import com.example.mysendikapp.haberler.*;
import com.example.mysendikapp.icerikSayfalari.ActivityHakkinda;
import com.example.mysendikapp.icerikSayfalari.ActivitySozlesme;
import com.example.mysendikapp.icerikSayfalari.ActivitySubeler;
import com.example.mysendikapp.icerikSayfalari.ActivityVideolar;
import com.example.mysendikapp.icerikSayfalari.ActivityYonetim;
import com.example.mysendikapp.icerikSayfalari.ActivityGallery;
import com.example.mysendikapp.sosyal.*;

import com.example.mysendikapp.sosyalTesisler.sosyalTesisler;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityDashboard extends AppCompatActivity  {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private  String[] haber_id;
    private  String[] haber_basligi;
    private TextView tvBaslik;
    private  String[] urls;
    SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        this.urls = new String[5];
        this.haber_id = new String[5];
        this.haber_basligi = new String[6];

        //SlidingImages
        fetchingJSON();
        initMenuButtons();
        initOptionMenu();
        tvBaslik=(TextView)findViewById(R.id.tv_haberBasligi_dashboard);

    }


    public void initOptionMenu(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_app_bar);
        setSupportActionBar(toolbar);
    }

    //Main menu
    public void initMenuButtons() {

        //Anket Doldur
        findViewById(R.id.iv_anketdoldur_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, anketWebViev.class);
                startActivity(i);
            }
        });
        //sosyal
        findViewById(R.id.iv_sosyalmedya_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, hangiSosyalActivity.class);
                startActivity(i);
            }
        });


        //Videolar
        findViewById(R.id.iv_videolar_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivityVideolar.class);
                startActivity(i);
            }
        });
        //Videolar\\

        //Galeri
        findViewById(R.id.iv_galeri_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivityGallery.class);
                startActivity(i);
            }
        });
        //Galeri\\

        //Sozlesme
        findViewById(R.id.iv_sozlesmem_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivitySozlesme.class);
                startActivity(i);
            }
        });
        //Sozlesme\\

        //Etkinlikler
        findViewById(R.id.iv_etkinlikListele_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, etkinlikAkisi.class);
                startActivity(i);
            }
        });

        findViewById(R.id.iv_etkinlikOlustur_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, etkinlikOlustur.class);
                startActivity(i);
            }
        });
        //Etkinlikler\\

        //Talep Şikayet
        findViewById(R.id.iv_talepSikayet_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivityTalepSikayet.class);
                startActivity(i);
            }
        });
        //Talep Şikayet\\


        //Bildirimler
        findViewById(R.id.iv_bildirimler_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, bildirimAkisi.class);
                startActivity(i);
            }
        });
        //Bildirimler\\



        //BUTONLAR
        //Haberler
        findViewById(R.id.btn_butunHaberleriGor_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, haberAkisi.class);
                startActivity(i);
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_topmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent();
        switch (item.getItemId()) {
            case R.id.opMenuYonetim:
                i = new Intent(ActivityDashboard.this, ActivityYonetim.class);
                startActivity(i);
                break;
            case R.id.opMenuHakkinda:
                i = new Intent(ActivityDashboard.this,ActivityHakkinda.class);
                startActivity(i);
                break;
            case R.id.opMenuSubeler:
                i = new Intent(ActivityDashboard.this, ActivitySubeler.class);
                startActivity(i);
                break;
            case R.id.opMenuSosyalTesisler:
                i = new Intent(ActivityDashboard.this, sosyalTesisler.class);
                startActivity(i);
                break;
            case R.id.opMenuDiscount:
                i = new Intent(ActivityDashboard.this, anlasmaliYerlerActivity.class);
                startActivity(i);
                break;
            case R.id.opMenuAyarlar:
                i = new Intent(ActivityDashboard.this, ActivityAyarlar.class);
                startActivity(i);
                break;
            case R.id.opMenuLogout:
                this.logout();
                break;
        }

        return true;
    }

    //Initialize the slider images
    private void initImages() {


        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator_dashboard);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = urls.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 6000, 6000);
        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                Log.d("ActivityDashboard","Selected currentPage --> "+currentPage);
            }
            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
                Log.d("ActivityDashboard","Scrolled currentPage --> "+currentPage);
                if(haber_basligi[currentPage] != null )
                tvBaslik.setText(""+Html.fromHtml(haber_basligi[currentPage]).toString());
            }
            @Override
            public void onPageScrollStateChanged(int pos) {
                Log.d("ActivityDashboard","ScrollStateChanged currentPage --> "+currentPage);
            }
        });


    }
    public void ImageOnClick(View v) {
        Log.d("Image onClick","haber_id = "+this.haber_id[currentPage]);
        Intent i = new Intent(ActivityDashboard.this, haberDetaylari.class);
        i.putExtra("haber_id", "" + this.haber_id[currentPage]);
        startActivity(i);
    }
    private void fetchingJSON() {

        String url = ActivityDashboard.this.getResources().getString(R.string.haberSlideUrl);    // Post atılan adres.
        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJSONData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(ActivityDashboard.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("count", String.valueOf(5));
                return params;
            }
        };

        // request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);


    }       //getAllNews
    public void parseJSONData(String response){
        Log.d("ActivityDashboard","-- Response to getLastNews ----> "+response);
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray dataArray = obj.getJSONArray("data");
            System.out.println("Gelen slide sayısı" + dataArray.length());

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataobj = dataArray.getJSONObject(i);

                urls[i]=dataobj.getString("picture");
                haber_id[i]=dataobj.getString("id");
                haber_basligi[i]=dataobj.getString("adi");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

                mPager = (ViewPager) findViewById(R.id.pager_dashboard);
                mPager.setAdapter(new SlidingImage_Adapter(ActivityDashboard.this, urls));
                initImages();

    }
    public void logout() {
        Log.d("logout Function", "Logout function");

        sp = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("isLogged");
        editor.remove("username");
        editor.remove("password");
        editor.putBoolean("isLogged", false);
        editor.apply();

        Intent i = new Intent(this, com.example.mysendikapp.login.loginActivity.class);
        startActivity(i);
    }


}






