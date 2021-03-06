package com.example.mysendikapp.haberler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.mysendikapp.dashboard.ActivityDashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

public class haberAkisi extends AppCompatActivity {

    private static ProgressDialog mProgressDialog;
    ArrayList<haberModel> haberModelArrayList;
    LinearLayoutManager lManager;

    private RvAdapterHaber rvAdapter;
    private RecyclerView recyclerView;
    int postCount, postPage;
    public boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haber_akisi);

        this.postCount = 5;
        this.postPage = 0;
        this.haberModelArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.rv_newsFeed);
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

                    haberAkisi.this.postPage++;
                    fetchingJSON();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        deleteCache(this);
        super.onDestroy();
    }

    private void setupRecycler() {
        lManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lManager);
        rvAdapter = new RvAdapterHaber(this, haberModelArrayList);
        recyclerView.setAdapter(rvAdapter);
        isLoading = false;
    }




    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                        System.out.println("Response to haberAkisi >> " + response);
                        System.out.println("***********************************************");
                        parseJSONData(response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(haberAkisi.this, error.toString(), Toast.LENGTH_LONG).show();
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

    }       //getAllNews

    public void parseJSONData(String response) {
        isLoading = false;
        try {
//            removeSimpleProgressDialog();
//                            JSONObject obj = new JSONObject(response);
            JSONObject obj = new JSONObject(response);
            JSONArray dataArray = obj.getJSONArray("data");
            System.out.println("Gelen haber sayısı : " + dataArray.length());
            if (!(dataArray.length()>0) ) {
                Toasty.warning(haberAkisi.this, "Bütün haberler listelendi", Toasty.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < dataArray.length(); i++) {
                haberModel haberModel1 = new haberModel();
                JSONObject dataobj = dataArray.getJSONObject(i);
                haberModel1.setTitle(dataobj.getString("header"));
                haberModel1.setSummary(dataobj.getString("summary"));
                haberModel1.setUrl(dataobj.getString("picture"));
                haberModel1.setView(dataobj.getString("readed"));
                haberModel1.setId(dataobj.getString("id"));
                haberModelArrayList.add(haberModel1);
            }
            if(rvAdapter==null){
                setupRecycler();
            }else {
                rvAdapter.setItems(haberModelArrayList);
                rvAdapter.notifyItemRangeInserted(rvAdapter.getItemCount(), dataArray.length());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(haberAkisi.this , ActivityDashboard.class);
        startActivity(i);
    }


}