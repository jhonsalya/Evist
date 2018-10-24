package com.example.jhonsalya.evist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jhonsalya.evist.Model.Category;
import com.example.jhonsalya.evist.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference category;

    RecyclerView recycler_event;

    FirebaseRecyclerAdapter<Category,CategoryViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        //load menu
        recycler_event = (RecyclerView) findViewById(R.id.recycler_event);
        recycler_event.setHasFixedSize(true);
        recycler_event.setLayoutManager(new LinearLayoutManager(this));

        loadMenu();
    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class,
                R.layout.category_card,
                CategoryViewHolder.class,
                category) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, Category model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setName(model.getName());
                viewHolder.setImage(getApplicationContext(),model.getImage());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        //get categoryID and send to new activity
//                        Intent eventList = new Intent(CategoryActivity.this, EventByCategoryActivity.class);
//                        //because categoryID is key, so we just get key of this item
//                        //eventList.putExtra("CategoryId", adapter.getRef(position).getKey());
//                        eventList.putExtra("CategoryId", post_key);
//                        startActivity(eventList);
                        Toast.makeText(CategoryActivity.this, post_key, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        Log.d("TAG","" + adapter.getItemCount());
        recycler_event.setAdapter(adapter);

    }
}
