package com.example.mysendikapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.bildirimler.bildirimAkisi;
import com.example.mysendikapp.etkinlik.etkinlikAkisi;
import com.example.mysendikapp.etkinlik.etkinlikOlustur;
import com.example.mysendikapp.icerikSayfalari.ActivityHakkinda;
import com.example.mysendikapp.icerikSayfalari.ActivityYonetim;
import com.example.mysendikapp.icerikSayfalari.ActivitySubeler;
import com.example.mysendikapp.ActivityTalepSikayet;
import com.example.mysendikapp.R;
import com.example.mysendikapp.haberler.*;
import com.example.mysendikapp.sosyal.*;
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
    private  String[] urls;
    SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        this.urls = new String[5];
        this.haber_id = new String[5];

        //SlidingImages
        fetchingJSON();
        setMenuActivites();

    }

    //Main menu
    public void setMenuActivites() {

        //Facebook
        findViewById(R.id.ivFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivityFacebook.class);
                startActivity(i);
            }
        });
        //Facebook\\


        //Twitter
        findViewById(R.id.ivTwitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivityTwitter.class);
                startActivity(i);
            }
        });
        //Twitter\\

        //Etkinlikler
        findViewById(R.id.ivEtkinlikler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, etkinlikAkisi.class);
                startActivity(i);
            }
        });
        //Etkinlikler\\

        //Talep Şikayet
        findViewById(R.id.ivTalep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivityTalepSikayet.class);
                startActivity(i);
            }
        });
        //Talep Şikayet\\

        //Hakkında
        findViewById(R.id.ivHakkinda).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivityHakkinda.class);
                startActivity(i);
            }
        });
        //Hakkında\\

        //Yönetim
        findViewById(R.id.ivYonetim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivityYonetim.class);
                startActivity(i);
            }
        });
        //Yönetim\\

        //Şubeler
        findViewById(R.id.ivSubeler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, ActivitySubeler.class);
                startActivity(i);
            }
        });
        //Şubeler\\

        //Bildirimler
        findViewById(R.id.ivBildirimler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, bildirimAkisi.class);
                startActivity(i);
            }
        });
        //Bildirimler\\



        //BUTONLAR
        //Haberler
        findViewById(R.id.btn_butunHaberleriGor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, haberAkisi.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btn_etkinlikOlusturabilirMiyim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityDashboard.this, etkinlikOlustur.class);
                startActivity(i);
            }
        });



    }

    @Override
    public void onBackPressed() {
    } //Do Nothing
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is presents
        getMenuInflater().inflate(R.menu.menu_topmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_icon:
                this.logout();
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
        }, 1500, 1500);
        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }
            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int pos) {
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
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray dataArray = obj.getJSONArray("data");
            System.out.println("Gelen slide sayısı" + dataArray.length());

            for (int i = 0; i < dataArray.length(); i++) {
                haberModel haberModel1 = new haberModel();
                JSONObject dataobj = dataArray.getJSONObject(i);

                urls[i]=dataobj.getString("picture");
                haber_id[i]=dataobj.getString("id");
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






