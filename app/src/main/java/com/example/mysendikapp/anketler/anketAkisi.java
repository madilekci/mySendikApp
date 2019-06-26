package com.example.mysendikapp.anketler;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.R;
import com.example.mysendikapp.haberler.RvAdapterHaber;
import com.example.mysendikapp.haberler.haberAkisi;
import com.example.mysendikapp.haberler.haberModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

public class anketAkisi extends AppCompatActivity {

    private static ProgressDialog mProgressDialog;
    ArrayList<anketModel> anketModelArrayList;
    LinearLayoutManager lManager;

    private rvAdapterAnket rvAdapter;
    private RecyclerView recyclerView;
    int postCount, postPage;
    public boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anket_akisi);

        this.postCount = 5;
        this.postPage = 0;
        this.anketModelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_anketFeed);
        recyclerView.getAdapter();
        fetchingJSON();
        initScrollListener();

    }

    public void initScrollListener() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = lManager.getChildCount();
                int totalItemCount = lManager.getItemCount();
                int firstVisibleItemPosition = lManager.findFirstVisibleItemPosition();


                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >PAGE_SIZE && !isLoading) {

                    anketAkisi.this.postPage++;
                    fetchingJSON();
                }
            }
        });
    }

    private void setupRecycler() {
        lManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lManager);
        rvAdapter = new rvAdapterAnket(this, anketModelArrayList);
        recyclerView.setAdapter(rvAdapter);
        isLoading = false;
    }

    private void fetchingJSON() {
        isLoading = true;
        System.out.println("postpage : " + postPage);
        System.out.println("count : " + postCount);
//        showSimpleProgressDialog(this, "Loading...", "Fetching Json", false);

        String url = getResources().getString(R.string.haberListeleUrl);    // Post atılan adres.
        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("***********************************************");
                        System.out.println("Response to anketAkisi >> " + response);
                        System.out.println("***********************************************");
                        parseJSONData(response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(anketAkisi.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page", String.valueOf(postPage));
                params.put("count", String.valueOf(postCount));
                return params;
            }
        };

        // request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);

    }       //getAllSurveys

    public void parseJSONData(String response) {
        isLoading = false;
        try {
//            removeSimpleProgressDialog();
//                            JSONObject obj = new JSONObject(response);
            JSONObject obj = new JSONObject(response);
            JSONArray dataArray = obj.getJSONArray("data");
            System.out.println("Gelen haber sayısı : " + dataArray.length());
            if (!(dataArray.length()>0) ) {
                Toasty.warning(anketAkisi.this, "Bütün anketler listelendi", Toasty.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < dataArray.length(); i++) {
                anketModel anketModel1 = new anketModel();
                JSONObject dataobj = dataArray.getJSONObject(i);
                anketModel1.setTitle(dataobj.getString("header"));
                anketModel1.setSummary(dataobj.getString("summary"));
                anketModel1.setId(dataobj.getString("id"));
                anketModelArrayList.add(anketModel1);
            }
            if(rvAdapter==null){        //Data ilk kez yükleniyorsa ; bu data ile rv oluştur.
                setupRecycler();
            }else {                     // Else ; bu datayı var olan rv'ye ekle
                rvAdapter.setItems(anketModelArrayList);
                rvAdapter.notifyItemRangeInserted(rvAdapter.getItemCount(), dataArray.length());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
