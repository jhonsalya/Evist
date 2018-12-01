package com.example.jhonsalya.evist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.jhonsalya.evist.Model.Event;
import com.example.jhonsalya.evist.Model.Transaction;
import com.example.jhonsalya.evist.ViewHolder.EventViewHolder;
import com.example.jhonsalya.evist.ViewHolder.TransactionViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class UnpaidActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    //private EventAdapter adapter;
    private List<Event> eventList;

    FirebaseRecyclerAdapter<Event,EventViewHolder> adapter;

    //private RecyclerView mEventList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseDetail;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    private FirebaseAuth.AuthStateListener mAuthListener;

    String mainId = "uid";
    String intentQuery = "";
    Query dataQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("UnpaidList");
        mDatabaseDetail = mDatabase.child("event").child("0");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Transaction, TransactionViewHolder> FBRA = new FirebaseRecyclerAdapter<Transaction, TransactionViewHolder>(
                Transaction.class,
                R.layout.unpaid_confirm_card,
                TransactionViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(TransactionViewHolder viewHolder, Transaction model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getName());
                viewHolder.setLocation(model.getAddress());
                viewHolder.setDate(model.getStartdate());
                viewHolder.setTime(model.getStarttime());
                viewHolder.setTotal(model.getTotal());

                //viewHolder.setImage(getApplicationContext(),model.getImage());

                //Detail Unpaid
                viewHolder.btndetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent eventDetailActivity = new Intent(UnpaidActivity.this, UnpaidDetailActivity.class);
                        eventDetailActivity.putExtra("PostId", post_key);
                        startActivity(eventDetailActivity);
                    }
                });

            }
        };
        recyclerView.setAdapter(FBRA);
    }





//    public void checkoutButtonClicked(View view){
//        //Toast.makeText(this, "Try", Toast.LENGTH_SHORT).show();
//        Intent unpaidDetail = new Intent(UnpaidActivity.this, UnpaidDetailActivity.class);
//        unpaidDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(unpaidDetail);
//    }
}
