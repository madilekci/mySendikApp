package com.example.mysendikapp.haberler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysendikapp.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<haberModel> haberModelArrayList;

    public RvAdapter(Context ctx, ArrayList<haberModel> haberModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.haberModelArrayList = haberModelArrayList;
    }

    @Override
    public RvAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_one, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(RvAdapter.MyViewHolder holder, int position) {

        Picasso.get().load("https://"+this.haberModelArrayList.get(position).getUrl() ).into(holder.pic);
        holder.title.setText(this.haberModelArrayList.get(position).getTitle() );
        holder.summary.setText(this.haberModelArrayList.get(position).getSummary() );
        holder.btn_view.setText(this.haberModelArrayList.get(position).getView() +" Kez Görüntülendi" );
        holder.setHaberID(this.haberModelArrayList.get(position).id);
        System.out.println("-->>>>>>>"+this.haberModelArrayList.get(position).id);
    }
    @Override
    public int getItemCount() {
        return this.haberModelArrayList.size();
    }

    public void setItems(ArrayList<haberModel> haberModelArrayList){
        this.haberModelArrayList=haberModelArrayList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, summary;
        ImageView pic;
        Button btn_view;
        String haberID;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.tv_rv_one_title);
            summary = (TextView) itemView.findViewById(R.id.tv_rv_one_summary);
            pic = (ImageView) itemView.findViewById(R.id.NewsFeedPic);
            btn_view = (Button) itemView.findViewById(R.id.rv_one_btn_view);
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

