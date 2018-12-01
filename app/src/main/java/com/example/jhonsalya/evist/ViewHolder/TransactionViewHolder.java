package com.example.jhonsalya.evist.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jhonsalya.evist.Model.Event;
import com.example.jhonsalya.evist.Model.EventAdapter;
import com.example.jhonsalya.evist.R;
import com.example.jhonsalya.evist.UnpaidActivity;
import com.example.jhonsalya.evist.UnpaidDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    View mView;
    public ImageView overflow;
    public Button btndetail;
    public TransactionViewHolder(View itemView){
        super(itemView);
        mView = itemView;

        btndetail = (Button) itemView.findViewById(R.id.button4);
        overflow = (ImageView) itemView.findViewById(R.id.overflow);
    }

    public void setTitle(String title){
        TextView post_title = (TextView) mView.findViewById(R.id.event_name);
        post_title.setText(title);
    }

    public void setLocation(String location){
        TextView post_location = (TextView) mView.findViewById(R.id.event_location);
        post_location.setText(location);
    }

    public void setDate(String date){
        TextView post_date = (TextView) mView.findViewById(R.id.event_date);
        post_date.setText(date);
    }

    public void setTime(String time){
        TextView post_time = (TextView) mView.findViewById(R.id.event_time_value);
        post_time.setText(time);
    }

    public void setTotal(String total){
        TextView post_total = (TextView) mView.findViewById(R.id.event_price_num);
        post_total.setText(total);
    }

    public void setImage(Context ctx, String image){
        ImageView post_image = (ImageView) mView.findViewById(R.id.thumbnail);
        Picasso.with(ctx).load(image).into(post_image);
    }
}
