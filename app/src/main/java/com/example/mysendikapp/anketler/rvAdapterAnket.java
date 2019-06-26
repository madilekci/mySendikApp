package com.example.mysendikapp.anketler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mysendikapp.R;
import com.example.mysendikapp.haberler.RvAdapterHaber;
import com.example.mysendikapp.haberler.haberDetaylari;
import com.example.mysendikapp.haberler.haberModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class rvAdapterAnket extends RecyclerView.Adapter<rvAdapterAnket.MyViewHolder> {

    public String TAG="rvAdapterAnket";
    private LayoutInflater inflater;
    private ArrayList<anketModel> anketModelArrayList;

    public rvAdapterAnket(Context ctx, ArrayList<anketModel> anketModelArrayList){
        inflater = LayoutInflater.from(ctx);
        this.anketModelArrayList = anketModelArrayList;
    }

    @Override
    public rvAdapterAnket.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_four, parent, false);
        rvAdapterAnket.MyViewHolder holder = new rvAdapterAnket.MyViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(rvAdapterAnket.MyViewHolder holder, int position) {

        holder.title.setText(this.anketModelArrayList.get(position).getTitle() );
        holder.summary.setText(this.anketModelArrayList.get(position).getSummary() );
        holder.setAnketID(this.anketModelArrayList.get(position).id);

        Log.d(TAG,"onBindViewHolder -->>>>>>>"+this.anketModelArrayList.get(position).id);
    }

    @Override
    public int getItemCount() {
        return this.anketModelArrayList.size();
    }
    public void setItems(ArrayList<anketModel> anketModelArrayList){
        this.anketModelArrayList=anketModelArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, summary;
        String anketID;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.tv_title_rv_four);
            summary = (TextView) itemView.findViewById(R.id.tv_summary_rv_four);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            System.out.println("Clicked to rv item  --> "+anketID );
            Context ctx = v.getContext();
            Intent i = new Intent(ctx, anketDoldur.class);
            i.putExtra("anket_id",""+anketID);
            ctx.startActivity(i);
        }
        public void setAnketID(String gelenID){
            this.anketID=gelenID;
        }
    }
}
