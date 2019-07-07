package com.example.mysendikapp.bildirimler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

public class bildirimAkisi extends AppCompatActivity {
    private static ProgressDialog mProgressDialog;
    ArrayList<bildirimModel> bildirimModelArrayList;
    LinearLayoutManager lManager;

    private rvAdapterBildirim rvAdapter;
    private RecyclerView recyclerView;
    int postCount, postPage;
    public boolean isLoading = false;
    public String TAG = "bildirimAkisi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildirim_akisi);

        this.postCount = 5;
        this.postPage = 0;
        this.bildirimModelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_bildirimFeed);
        recyclerView.getAdapter();
        fetchingJSON();
        //initScrollListener();

    }

    /*public void initScrollListener() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = lManager.getChildCount();
                int totalItemCount = lManager.getItemCount();
                int firstVisibleItemPosition = lManager.findFirstVisibleItemPosition();


                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE && !isLoading) {

                    bildirimAkisi.this.postPage++;
                    fetchingJSON();

                }
            }
        });
    }*/

    @Override
    protected void onDestroy() {
        deleteCache(this);
        super.onDestroy();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void setupRecycler() {
        lManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lManager);
        rvAdapter = new rvAdapterBildirim(this, bildirimModelArrayList);
        recyclerView.setAdapter(rvAdapter);
        isLoading = false;
    }



    private void fetchingJSON() {
        isLoading = true;
        Log.d(TAG, "postpage : " + postPage);
        Log.d(TAG, "count : " + postCount);

        String url = getResources().getString(R.string.bildirimListeleUrl);    // Post atılan adres.
        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response to bildirimAkisi >> " + response);
                        parseJSONData(response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(bildirimAkisi.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", getUserToken());
                return params;
            }
        };

        // request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);

    }       //getAllEvents

    public void parseJSONData(String response) {
        isLoading=false;
        try {
//                            JSONObject obj = new JSONObject(response);
            JSONObject obj = new JSONObject(response);
            JSONArray dataArray = obj.getJSONArray("data");
            Log.d(TAG, "Gelen bildirim sayısı : " + dataArray.length());
            if (!(dataArray.length() > 0)) {
                Toasty.warning(bildirimAkisi.this, "Bütün bildirimler listelendi", Toasty.LENGTH_LONG).show();
                return;
            }

            for (int i = 0; i < dataArray.length(); i++) {
                bildirimModel bildirimModel1 = new bildirimModel();
                JSONObject dataobj = dataArray.getJSONObject(i);

                bildirimModel1.setTitle(dataobj.getString("title"));
                bildirimModel1.setContent(dataobj.getString("message"));
                bildirimModel1.setId(dataobj.getString("id"));
                bildirimModel1.setBid(dataobj.getString("bid"));
                bildirimModel1.setType(dataobj.getString("type"));
                bildirimModel1.setReaded(dataobj.getString("read"));

                bildirimModelArrayList.add(bildirimModel1);
            }
            if (rvAdapter == null) {
                setupRecycler();
            } else {
                int itemCount = rvAdapter.getItemCount();
                // TODO: add method setItems() to your adapter
                rvAdapter.setItems(bildirimModelArrayList);
                rvAdapter.notifyItemRangeInserted(itemCount, dataArray.length());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUserToken() {
        SharedPreferences xd = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();
        return xd.getString("userToken", "noTokens");
    }
}
