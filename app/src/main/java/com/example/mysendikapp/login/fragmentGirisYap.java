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
import android.widget.EditText;
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

        view.findViewById(R.id.btnGirisYap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisYap(v);
            }
        });

        return view;
    }

    public void girisYap(View v) {

        EditText txtUsername = (EditText) getView().findViewById(R.id.tvUsernameGirisYap);
        EditText txtPassword = (EditText) getView().findViewById(R.id.tvPasswordGirisYap);

        final String username = "" + txtUsername.getText().toString();
        final String password = "" + txtPassword.getText().toString();


        if ( !( username.equals("") || password.equals("") ) ) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());

            String url = getResources().getString(R.string.loginUrl);    // Post atılan adres.
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int switchCase = 0;
                            try {
                                System.out.println("Response :" + response);
                                JSONObject veri_json = new JSONObject(response);
                                switchCase = veri_json.getInt("state");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            switch (switchCase) {

                                case 1:
                                    System.out.println("Giris basarili.");
                                    saveUserInfo(username,password);
                                    goToMenuActivity();
                                    break;
                                case 2:
                                    Log.d("girisYap", "Sifre hatali");
                                    Toasty.error(getContext(), "Şifrenizi yanlış girdiniz. Lütfen tekrar deneyin", Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Log.d("girisYap", "user_id hatali");
                                    Toasty.error(getContext(), "Böyle bir kullanıcı bulunamadı", Toast.LENGTH_SHORT).show();
                                    break;
                                case 4:
                                    Log.d("girisYap", "Onay bekleniyor");
                                    Toasty.error(getContext(), "Şubenizden onay bekleniyor. Lütfen daha sonra tekrar deneyin", Toast.LENGTH_SHORT).show();

                            }
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
            queue.add(postRequest);

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

    public void saveUserInfo(String username, String password){
        SharedPreferences xd = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();

        editor.remove("username");
        editor.remove("password");

        editor.putString("username",username);
        editor.putString("password",password);
        editor.apply();
    }

}
