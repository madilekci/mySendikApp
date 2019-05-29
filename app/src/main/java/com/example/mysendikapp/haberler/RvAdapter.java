package com.example.mysendikapp.haberler;

import android.content.Context;
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

//        Picasso.get().load("https://demonuts.com/Demonuts/SampleImages/W-03.JPG").into(holder.pic);
//        Picasso.get().load("https://www.dogruweb.com/assets/images/haber/d7742305d5b840373db6fe06828d68f1-taseron-isci-dha-1_16_9_1524634621.jpg").into(holder.pic);
        Picasso.get().load("https://"+haberModelArrayList.get(position).getUrl() ).into(holder.pic);
        holder.title.setText(haberModelArrayList.get(position).getTitle() );
        holder.summary.setText(haberModelArrayList.get(position).getSummary() );
        holder.btn_view.setText(haberModelArrayList.get(position).getView() +" Kez Görüntülendi" );
        holder.id=haberModelArrayList.get(position).id;

    }

    @Override
    public int getItemCount() {
        return haberModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, summary;
        ImageView pic;
        Button btn_view;
        String id;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.tv_rv_one_title);
            summary = (TextView) itemView.findViewById(R.id.tv_rv_one_summary);
            pic = (ImageView) itemView.findViewById(R.id.NewsFeedPic);
            btn_view = (Button) itemView.findViewById(R.id.rv_one_btn_view);

        }

        @Override
        public void onClick(View v) {
            System.out.println("Clicked to rv item");
        }
    }
}

