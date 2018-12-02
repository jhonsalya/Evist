package com.example.jhonsalya.evist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.jhonsalya.evist.Model.Transaction;
import com.example.jhonsalya.evist.ViewHolder.TransactionViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SalesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transaction");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Transaction, TransactionViewHolder> FBRA = new FirebaseRecyclerAdapter<Transaction, TransactionViewHolder>(
                Transaction.class,
                R.layout.unpaid_confirm_card,
                TransactionViewHolder.class,
                //dataQuery
                mDatabase.orderByChild("statusseller").equalTo("confirmed_"+mCurrentUser.getUid())
        ) {
            @Override
            protected void populateViewHolder(TransactionViewHolder viewHolder, Transaction model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getName());
                viewHolder.setLocation(model.getAddress());
                viewHolder.setDate(model.getStartdate());
                viewHolder.setTime(model.getStarttime());
                viewHolder.setTotal(model.getTotal());
                viewHolder.setButtonVisibility();

                //viewHolder.setImage(getApplicationContext(),model.getImage());

                //Detail Unpaid button
//                viewHolder.btndetail.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent eventDetailActivity = new Intent(ConfirmPaymentActivity.this, ConfirmPaymentDetailActivity.class);
//                        eventDetailActivity.putExtra("PostId", post_key);
//                        startActivity(eventDetailActivity);
//                    }
//                });

            }
        };
        recyclerView.setAdapter(FBRA);
    }
}
