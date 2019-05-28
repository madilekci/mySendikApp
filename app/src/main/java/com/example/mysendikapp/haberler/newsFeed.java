package com.example.mysendikapp.haberler;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class newsFeed extends AppCompatActivity {

    private static ProgressDialog mProgressDialog;
    ArrayList<haberModel> haberModelArrayList;

    private RvAdapter rvAdapter;
    private RecyclerView recyclerView;

    String postCount, postPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        this.postCount = "2";
        this.postPage = "0";

        recyclerView = (RecyclerView) findViewById(R.id.rv_newsFeed);
        recyclerView.getAdapter();
        fetchingJSON();


    }

    @Override
    protected void onDestroy() {
        deleteCache(this);
        super.onDestroy();
    }
    private void fetchingJSON() {

        showSimpleProgressDialog(this, "Loading...", "Fetching Json", false);

        String url = getResources().getString(R.string.haberUrl);    // Post atılan adres.
        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
            public void onResponse(String response) {

                        System.out.println("***********************************************");
                        System.out.println("Response to newsFeed >> " + response.toString());
                        System.out.println("***********************************************");
                        try {

                            removeSimpleProgressDialog();
//                            JSONObject obj = new JSONObject(response);
                            haberModelArrayList = new ArrayList<>();
                            JSONObject obj = new JSONObject(response);
                            JSONArray dataArray = obj.getJSONArray("data");
                            System.out.println("Gelen haber sayısı : " + dataArray.length());

                            for (int i = 0; i < dataArray.length(); i++) {
                                haberModel haberModel1 = new haberModel();
                                JSONObject dataobj = dataArray.getJSONObject(i);

                                haberModel1.setTitle(dataobj.getString("header"));
                                haberModel1.setSummary(dataobj.getString("summary"));
                                haberModel1.setUrl(dataobj.getString("picture"));
                                haberModel1.setView(dataobj.getString("readed"));
                                haberModelArrayList.add(haberModel1);
                                setupRecycler();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(newsFeed.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page", getResources().getString(R.string.page));
                params.put("count", getResources().getString(R.string.count));
                return params;
            }
        };

        // request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);


    }       //getAllNews

    private void setupRecycler() {

        rvAdapter = new RvAdapter(this, haberModelArrayList);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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

    public void haberOnClick(RecyclerView rv) {
        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}