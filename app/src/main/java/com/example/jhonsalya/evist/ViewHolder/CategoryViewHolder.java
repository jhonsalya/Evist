package com.example.jhonsalya.evist.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jhonsalya.evist.R;
import com.squareup.picasso.Picasso;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public CategoryViewHolder(View itemView, View mView) {
        super(itemView);
        this.mView = mView;
    }

    public void setImage(Context ctx, String image){
        ImageView post_image = (ImageView) mView.findViewById(R.id.category_image);
        Picasso.with(ctx).load(image).into(post_image);
    }

    public void setName(String name){
        TextView post_title = (TextView) mView.findViewById(R.id.category_name);
        post_title.setText(name);
    }
}
