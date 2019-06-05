package com.example.mysendikapp.etkinlik;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysendikapp.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class rvAdapterEtkinlik extends RecyclerView.Adapter<rvAdapterEtkinlik.MyViewHolder>{
    private LayoutInflater inflater;
    private ArrayList<etkinlikModel> etkinlikModelArrayList;
    public String TAG="rvAdapterEtkinlik";

    public rvAdapterEtkinlik(Context ctx, ArrayList<etkinlikModel> etkinlikModelArrayList){
        inflater = LayoutInflater.from(ctx);
        this.etkinlikModelArrayList = etkinlikModelArrayList;
    }


    @Override
    public rvAdapterEtkinlik.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_two, parent, false);
        rvAdapterEtkinlik.MyViewHolder holder = new rvAdapterEtkinlik.MyViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull rvAdapterEtkinlik.MyViewHolder holder, int position) {
        Picasso.get().load("https://"+this.etkinlikModelArrayList.get(position).getUrl() ).into(holder.pic);
        holder.title.setText(this.etkinlikModelArrayList.get(position).getTitle() );
        holder.summary.setText(this.etkinlikModelArrayList.get(position).getSummary() );
        holder.btn_date.setText(this.etkinlikModelArrayList.get(position).getDate() );
        holder.setEtkinlikID(this.etkinlikModelArrayList.get(position).id);

        Log.d(TAG,"onBind-->>>>>>>"+this.etkinlikModelArrayList.get(position).id);
    }

    @Override
    public int getItemCount() {
        return this.etkinlikModelArrayList.size();
    }
    public void setItems(ArrayList<etkinlikModel> etkinlikModelArrayList){
        this.etkinlikModelArrayList=etkinlikModelArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        String etkinlikID;
        TextView title,summary;
        ImageView pic;
        Button btn_date;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.tv_title_rv_two);
            summary = (TextView) itemView.findViewById(R.id.tv_summary_rv_two);
            pic = (ImageView) itemView.findViewById(R.id.iv_rv_two);
            btn_date = (Button) itemView.findViewById(R.id.btn_date_rv_two);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Log.d(TAG,"Clicked to rv item  --> "+ etkinlikID);
            Context ctx = v.getContext();
            Intent i = new Intent(ctx, etkinlikDetaylari.class);
            i.putExtra("etkinlik_id",""+etkinlikID);
            ctx.startActivity(i);
        }
        public void setEtkinlikID(String gelenID){
            this.etkinlikID=gelenID;
        }
    }
}
