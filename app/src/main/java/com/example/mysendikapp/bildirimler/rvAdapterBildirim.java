package com.example.mysendikapp.bildirimler;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mysendikapp.R;
import com.example.mysendikapp.etkinlik.etkinlikDetaylari;

import java.util.ArrayList;

public class rvAdapterBildirim extends RecyclerView.Adapter<rvAdapterBildirim.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<bildirimModel> bildirimModelArrayList;
    public String TAG="rvAdapterBildirim";

    public rvAdapterBildirim(Context ctx, ArrayList<bildirimModel> bildirimModelArrayList){
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
        holder.title.setText(this.bildirimModelArrayList.get(position).getTitle() );
        holder.content.setText(this.bildirimModelArrayList.get(position).getContent() );
        holder.date.setText(this.bildirimModelArrayList.get(position).getDate() );
        holder.setEtkinlikID(this.bildirimModelArrayList.get(position).getId() );

        Log.d(TAG,"onBind-->>>>>>>"+this.bildirimModelArrayList.get(position).id);
    }

    @Override
    public int getItemCount() {
        return this.bildirimModelArrayList.size();
    }
    public void setItems(ArrayList<bildirimModel> bildirimModelArrayList){
        this.bildirimModelArrayList=bildirimModelArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        String bildirimID;
        TextView title,content,date;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.tv_title_rv_three);
            content = (TextView) itemView.findViewById(R.id.tv_content_rv_three);
            date = (TextView) itemView.findViewById(R.id.tv_date_rv_three);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Log.d(TAG,"Clicked to rv item  --> "+ bildirimID);
            Context ctx = v.getContext();
            Intent i = new Intent(ctx, etkinlikDetaylari.class);
            i.putExtra("etkinlik_id",""+bildirimID);
            ctx.startActivity(i);
        }
        public void setEtkinlikID(String gelenID){
            this.bildirimID=gelenID;
        }
    }
}
