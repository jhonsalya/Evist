package com.example.jhonsalya.evist;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.example.jhonsalya.evist.Model.Category;
import com.example.jhonsalya.evist.Model.Event;
import com.example.jhonsalya.evist.Model.EventAdapter;
import com.example.jhonsalya.evist.ViewHolder.CategoryViewHolder;
import com.example.jhonsalya.evist.ViewHolder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class EventByCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference eventList;

    String categoryId=""; //kemungkinan id kategori akan berupa namanya cth. education

    FirebaseRecyclerAdapter<Event, EventViewHolder> adapter;

    String mainId = "category";
    String intentQuery = "";
    Query dataQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_by_category);

        //Firebase
        database = FirebaseDatabase.getInstance();
        eventList = database.getReference("EventApp");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_event);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new EventByCategoryActivity.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Get Intent here
        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId != null){
            Toast.makeText(EventByCategoryActivity.this, categoryId, Toast.LENGTH_SHORT).show();
            setTitle('"'+categoryId+'"');
            loadListEvent(categoryId);
        }
    }

    private void loadListEvent(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(Event.class,
                R.layout.event_card,
                EventViewHolder.class,
                eventList.orderByChild("category").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(EventViewHolder viewHolder, Event model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getLocation());
                viewHolder.setImage(getApplicationContext(),model.getImage());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent eventDetailActivity = new Intent(EventByCategoryActivity.this, EventDetailActivity.class);
                        eventDetailActivity.putExtra("PostId", post_key);
                        startActivity(eventDetailActivity);
                    }
                });
            }
        };
        Log.d("TAG","" + adapter.getItemCount());
        recyclerView.setAdapter(adapter);
    }
    //recylerview item decoration - give equal margin around grid item
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge){
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
            int position = parent.getChildAdapterPosition(view); //item position
            int column = position % spanCount; // item column

            if(includeEdge){
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if(position < spanCount){ //top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            }
            else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if(position >= spanCount){ //top edge
                    outRect.top = spacing;
                }
            }
        }
    }
    //converting dp to pixel
    private int dpToPx(int dp){
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
