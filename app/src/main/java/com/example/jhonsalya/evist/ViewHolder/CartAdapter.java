package com.example.jhonsalya.evist.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.jhonsalya.evist.Common.Common;
import com.example.jhonsalya.evist.Interface.ItemClickListener;
import com.example.jhonsalya.evist.Model.Order;
import com.example.jhonsalya.evist.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jhonsalya on 19/11/18.
 */

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    ,View.OnCreateContextMenuListener{

    public TextView txt_cart_name, txt_location, txt_date, txt_time, txt_price, txt_quantity;
    //public ImageView img_cart_count;

    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);
        txt_cart_name = (TextView)itemView.findViewById(R.id.cart_event_name);
        txt_location = (TextView)itemView.findViewById(R.id.cart_event_location);
        txt_date = (TextView)itemView.findViewById(R.id.cart_event_date);
        txt_time = (TextView)itemView.findViewById(R.id.cart_event_time_value);
        txt_price = (TextView)itemView.findViewById(R.id.cart_event_price_num);
        txt_quantity = (TextView)itemView.findViewById(R.id.cart_event_quantity_value);
        //img_cart_count = (ImageView)itemView.findViewById(R.id.cart_item_count);

        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("select action");
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{
    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(""+listData.get(position).getQuantity(), Color.RED);
//        holder.img_cart_count.setImageDrawable(drawable);

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_price.setText(fmt.format(price));

        holder.txt_cart_name.setText(listData.get(position).getEventName());
        holder.txt_location.setText(listData.get(position).getEventAddress());
        holder.txt_date.setText(listData.get(position).getEventStartDate());
        holder.txt_time.setText(listData.get(position).getEventStartTime());
        holder.txt_quantity.setText(listData.get(position).getQuantity());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
