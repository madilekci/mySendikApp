package com.example.mysendikapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Space;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class SplashAct extends AppCompatActivity {
    String TAG="splashAct";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        updateNotificationToken();
        Intent i = new Intent(this, ActivityDashboard.class);
        startActivity(i);

    }
    public void updateNotificationToken() {
        SharedPreferences xd = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        final String userToken = xd.getString("userToken", "noTokens");
        final String notificationToken = xd.getString("notificationToken", "noNotificationTokens");
        Log.d(TAG, "notificationToken : " + notificationToken);
        Log.d(TAG, "userToken : " + userToken);

        String url = getResources().getString(R.string.notificationTokenGuncelleUrl);    // Post atılan adres.
        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"response :"+response);
                        if(isNotificationTokenUpdated(response)){
                            Log.d(TAG,"notification Token updated successfullys");
                        }else {
                            Log.d(TAG,"unhandled error while updating notification token");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_token", userToken);
                params.put("notification_token", notificationToken);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);

    }
    public boolean isNotificationTokenUpdated(String response){
        try {
            JSONObject obj = new JSONObject(response);
            int error= obj.getInt("error");
            if(error==0){
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }


}
