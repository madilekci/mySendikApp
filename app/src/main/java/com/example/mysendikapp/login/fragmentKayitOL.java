package com.example.mysendikapp.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentKayitOL extends Fragment {


    public fragmentKayitOL() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kayit_ol, container, false);

        Button btnEkle =(Button) view.findViewById(R.id.btnKayitOl);
        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayitOl(v);
            }
        });

        return view;
    }

    public void kayitOl(View view) {

        EditText txtAd = (EditText) getActivity().findViewById(R.id.kayitOl_txtAd);
        EditText txtSoyad = (EditText) getActivity().findViewById(R.id.kayitOl_txtSoyad);
        EditText txtUsername = (EditText) getActivity().findViewById(R.id.kayitOl_txtUsername);
        EditText txtPassword = (EditText) getActivity().findViewById(R.id.kayitOl_txtPassword);
        EditText txtEmail = (EditText) getActivity().findViewById(R.id.kayitOl_txtEmail);

        final String ad = txtAd.getText().toString();
        final String soyad = txtSoyad.getText().toString();
        final String username = txtUsername.getText().toString();
        final String password = txtPassword.getText().toString();
        final String email = txtEmail.getText().toString();


        if (ad.equals("") == false && soyad.equals("") == false && username.equals("") == false && password.equals("") == false && email.equals("") == false) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());

            String url = getResources().getString(R.string.signInUrl);    // Post atılan adres.
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(), "Response" + response, Toast.LENGTH_LONG).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Error :" + error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("ad", ad);
                    params.put("soyad", soyad);
                    params.put("username", username);
                    params.put("password", password);
                    params.put("email", email);
                    return params;
                }
            };
            queue.add(postRequest);
        } else {
            Toast.makeText(getActivity(), "Lütfen Gerekli Alanları Doldurun", Toast.LENGTH_LONG).show();
        }

    }


}
