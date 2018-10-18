package com.example.jhonsalya.evist.Model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.jhonsalya.evist.LoginActivity;
import com.example.jhonsalya.evist.MainActivity;
import com.example.jhonsalya.evist.R;
import com.example.jhonsalya.evist.RegisterActivity;
import com.example.jhonsalya.evist.UserProfileActivity;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    private Context mContext;
    private List<Event> eventList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    public EventAdapter(Context mContext, List<Event> eventList){
        this.mContext = mContext;
        this.eventList = eventList;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(final MyViewHolder holder, int position){
        Event event = eventList.get(position);
        holder.title.setText(event.getTitle());
        holder.count.setText(event.getLocation());

        //loading album cover using glide library
        Glide.with(mContext).load(event.getImage()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    //showing popup menu when taping icon
    private void showPopupMenu(View view){
        //Tutorial on 10.50 android card view and recycler view
        PopupMenu popup = new PopupMenu(mContext,view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_event, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    //click listener for popup menu items
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{
        public MyMenuItemClickListener(){

        }
        public boolean onMenuItemClick(MenuItem menuItem){
            switch(menuItem.getItemId()){
                case R.id.action_add_favourite:
                    Toast.makeText(mContext,"Edit Event", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Delete Event", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }
    public int getItemCount(){
        return eventList.size();
    }
}
