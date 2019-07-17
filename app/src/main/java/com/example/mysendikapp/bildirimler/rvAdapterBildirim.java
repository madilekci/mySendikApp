package com.example.mysendikapp.bildirimler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.R;
import com.example.mysendikapp.anketler.anketWebViev;
import com.example.mysendikapp.etkinlik.etkinlikDetaylari;
import com.example.mysendikapp.haberler.haberDetaylari;
import com.example.mysendikapp.icerikSayfalari.ActivitySozlesme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class rvAdapterBildirim extends RecyclerView.Adapter<rvAdapterBildirim.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<bildirimModel> bildirimModelArrayList;
    public String TAG = "rvAdapterBildirim";
    public String userTokenPost = "", bidPost, typePost;
    private Context ctx;

    public rvAdapterBildirim(Context ctx, ArrayList<bildirimModel> bildirimModelArrayList) {
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
        this.bildirimModelArrayList = bildirimModelArrayList;
    }

    @Override
    public rvAdapterBildirim.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_three, parent, false);
        rvAdapterBildirim.MyViewHolder holder = new rvAdapterBildirim.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull rvAdapterBildirim.MyViewHolder holder, int position) {
        holder.title.setText(this.bildirimModelArrayList.get(position).getTitle());
        holder.content.setText(this.bildirimModelArrayList.get(position).getContent());
        holder.setBildirimID(this.bildirimModelArrayList.get(position).getBid());
        holder.setID(this.bildirimModelArrayList.get(position).getId());
        holder.setBildirimType(this.bildirimModelArrayList.get(position).getType());
        holder.setReaded(this.bildirimModelArrayList.get(position).getReaded());

        if (Integer.parseInt(holder.readed) > 0) {
            Log.d(TAG, "onBind readed-->>>>>>>  " + this.bildirimModelArrayList.get(position).bid);
            holder.ll_holder.setBackgroundResource(R.color.color10);
            holder.title.setBackgroundColor(Color.parseColor("#77777777"));
            holder.content.setBackgroundColor(Color.parseColor("#77777777"));
        } else {
            Log.d(TAG, "onBind not readed -->>>>>>> " + this.bildirimModelArrayList.get(position).bid);
            holder.ll_holder.setBackgroundResource(R.color.color11);
            holder.title.setBackgroundColor(Color.parseColor("#F5F5F5"));
            holder.content.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }
    }


    @Override
    public int getItemCount() {
        return this.bildirimModelArrayList.size();
    }

    public void setItems(ArrayList<bildirimModel> bildirimModelArrayList) {
        this.bildirimModelArrayList = bildirimModelArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        String bildirimType, readed, ID, bildirimID;
        TextView title, content;
        LinearLayout ll_holder;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            title = (TextView) itemView.findViewById(R.id.tv_title_rv_three);
            content = (TextView) itemView.findViewById(R.id.tv_content_rv_three);
            ll_holder = (LinearLayout) itemView.findViewById(R.id.ll_holder_bildirimAkisi);
            itemView.setOnClickListener(this);

            itemView.findViewById(R.id.btn_okundu_bildirimAkisi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "<--  Okundu.OnClick  --> " + bildirimID);
                    Log.d(TAG, "Clicked to rv item   --> " + bildirimID);
                    Log.d(TAG, "readed --> " + readed + "\n\n");

                    if (Integer.parseInt(readed) == 0) {
                        ll_holder.setBackgroundResource(R.color.color10);
                        title.setBackgroundColor(Color.parseColor("#77777777"));
                        content.setBackgroundColor(Color.parseColor("#77777777"));

                        bidPost = bildirimID;
                        typePost = bildirimType;
                        userTokenPost = getUserToken();
                        sendStringRequest();
                    }
                }
            });


        }

        @Override
        public void onClick(View v) {

            Log.d(TAG, "Clicked to rv item  --> " + bildirimID);
            Log.d(TAG, "bağlı olan id --> " + ID);
            Log.d(TAG, "type --> " + bildirimType);
            Log.d(TAG, "readed --> " + readed + "\n\n");

            bidPost = bildirimID;
            typePost = bildirimType;
            userTokenPost = getUserToken();
            sendStringRequest();

            Context ctxx = v.getContext();
            Intent i;
            switch (Integer.parseInt(bildirimType)) {

                case 1:     //Haber
                    i = new Intent(ctxx, haberDetaylari.class);
                    i.putExtra("haber_id", "" + ID);
                    ctx.startActivity(i);
                    break;
                case 2:     //Etkinlik
                    i = new Intent(ctxx, etkinlikDetaylari.class);
                    i.putExtra("etkinlik_id", "" + ID);
                    ctx.startActivity(i);
                    break;
                case 3:     //Anket
                    i = new Intent(ctxx, anketWebViev.class);
                    ctx.startActivity(i);
                    break;
                case 4:     //Bildirim
                    i = new Intent(ctxx, bildirimAkisi.class);
                    ctx.startActivity(i);
                    break;
            }


        }

        public void setBildirimID(String gelenID) {
            this.bildirimID = gelenID;
        }

        public void setID(String gelenID) {
            this.ID = gelenID;
        }

        public void setBildirimType(String gelenType) {
            this.bildirimType = gelenType;
        }

        public void setReaded(String gelenReaded) {
            this.readed = gelenReaded;
        }
    }

    public void sendStringRequest() {
        Log.d(TAG, "Fetching JSON ....");
        Log.d(TAG, "userToken  --> " + userTokenPost);
        Log.d(TAG, "bid  --> " + bidPost);
        Log.d(TAG, "type  --> " + typePost);


        String url = ctx.getResources().getString(R.string.bildirimOkunduUrl);    // Post atılan adres.
        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response to bildirimOkundu >> " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(ctx, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", userTokenPost);
                params.put("id", bidPost);
                params.put("type", typePost);
                return params;
            }
        };

        // request queue
        RequestQueue queue = Volley.newRequestQueue(ctx);

        jsonStringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));    // Yeniden istek gönderebilmek için uyulması gereken kurallar

        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);

    }       //Bildirimin okunduğuna dair bilgi gönderir.

    public String getUserToken() {
        SharedPreferences xd = ctx.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();
        return xd.getString("userToken", "noTokens");
    }

}
