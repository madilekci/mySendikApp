package com.example.mysendikapp.login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.R;
import com.example.mysendikapp.dashboard.SplashAct;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentGirisYap extends Fragment {

    SharedPreferences sp = null;


    public fragmentGirisYap() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_giris_yap, container, false);


        sp = this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        if (sp.getBoolean("isLogged", false)) {
            goToMenuActivity();
        }else {

            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isLogged", false);
            editor.apply();
        }

        view.findViewById(R.id.btn_girisYap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisYap(v);
            }
        });

        return view;
    }

    public void girisYap(View v) {
        final TextView txtUsername = (TextView) getView().findViewById(R.id.tv_username_girisYap);
        final TextView txtPassword = (TextView) getView().findViewById(R.id.tv_password_girisYap);

        final String username = "" + txtUsername.getText().toString();
        final String password = "" + txtPassword.getText().toString();
        Log.d("login","username: "+username);
        Log.d("login","password: "+password);


        if ( !( username.equals("") || password.equals("") ) ) {
            String url = getResources().getString(R.string.loginUrl);    // Post atılan adres.

            StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Log coming response :"+response);
                            parseJson(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toasty.error(getContext(), "Error :" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", username);
                    params.put("password", password);
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
            queue.add(jsonStringRequest);

        } else {
            Toasty.error(getContext(), "Lütfen gerekli alanları doldurun.", Toast.LENGTH_LONG, true).show();
        }
    }

    public void goToMenuActivity() {

        SharedPreferences xd = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();
        editor.remove("isLogged");
        editor.putBoolean("isLogged", true);
        editor.apply();
        Toasty.success(getContext(), "Giriş başarılı.", Toast.LENGTH_SHORT, true).show();

        Intent i = new Intent(getActivity(), SplashAct.class);
        startActivity(i);

    }

    public void saveUserInfo(String userToken){
        SharedPreferences xd = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();

        editor.remove("userToken");
        editor.putString("userToken",userToken);
        editor.apply();

    }

    public void parseJson(String response ){

        final String[] userToken = new String[1];
        int isActive;
        int error;

        try {
            System.out.println("Response :" + response);
            JSONObject obj = new JSONObject(response);
            error = obj.getInt("error");
            switch (error) {
                case 1:
                    Log.d("girisYap", "Kullanıcı adı veya Sifre hatali");
                    Toasty.error(getContext(), "Kullanıcı adınızı veya şifrenizi yanlış girdiniz. Lütfen tekrar deneyin", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    JSONObject dataobj = obj.getJSONObject("data");
                    System.out.println("Giris basarili.");
                    userToken[0] = dataobj.getString("user_token");
                    isActive= dataobj.getInt("is_active");
                    if (isActive==1){
                        Log.d("frGirisYap","giden UserToken :"+userToken[0]);
                        saveUserInfo(userToken[0]);
                        goToMenuActivity();
                        break;
                    }else if(isActive==0){      //Kullanıcı ilk kez giriş yapıyorsa
                        Intent i= new Intent(getContext(),sifreDegistirActivity.class);
                        startActivity(i);
                        break;
                    }

                default:
                    Log.d("girisYap", "Bilinmeyen bir hata oluştu");
                    Toasty.error(getContext(), "Bilinmeyen bir hata oluştu ...", Toast.LENGTH_SHORT).show();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }





    }

}
