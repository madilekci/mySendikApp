package com.example.mysendikapp.haberler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

public class RvAdapterHaber extends RecyclerView.Adapter<RvAdapterHaber.MyViewHolder> {
    public String TAG="rvAdapterHaber";
    private LayoutInflater inflater;
    private ArrayList<haberModel> haberModelArrayList;

    public RvAdapterHaber(Context ctx, ArrayList<haberModel> haberModelArrayList){
        inflater = LayoutInflater.from(ctx);
        this.haberModelArrayList = haberModelArrayList;
    }

    @Override
    public RvAdapterHaber.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_one, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(RvAdapterHaber.MyViewHolder holder, int position) {

        Picasso.get().load(this.haberModelArrayList.get(position).getUrl() ).into(holder.pic);
        holder.title.setText(this.haberModelArrayList.get(position).getTitle() );
        holder.summary.setText(Html.fromHtml(this.haberModelArrayList.get(position).getSummary()).toString());
        holder.btn_view.setText(this.haberModelArrayList.get(position).getView() +" Kez Görüntülendi" );
        holder.setHaberID(this.haberModelArrayList.get(position).id);

        Log.d(TAG,"-->>>>>>>"+this.haberModelArrayList.get(position).id);
    }

    @Override
    public int getItemCount() {
        return this.haberModelArrayList.size();
    }
    public void setItems(ArrayList<haberModel> haberModelArrayList){
        this.haberModelArrayList=haberModelArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, summary;
        ImageView pic;
        Button btn_view;
        String haberID;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.tv_title_rv_one);
            summary = (TextView) itemView.findViewById(R.id.tv_summary_rv_one);
            pic = (ImageView) itemView.findViewById(R.id.iv_rv_one);
            btn_view = (Button) itemView.findViewById(R.id.btn_view_rv_one);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            System.out.println("Clicked to rv item  --> "+ haberID);
            Context ctx = v.getContext();
            Intent i = new Intent(ctx,haberDetaylari.class);
            i.putExtra("haber_id",""+haberID);
            ctx.startActivity(i);
        }
        public void setHaberID(String gelenID){
            this.haberID=gelenID;
        }
    }

}

