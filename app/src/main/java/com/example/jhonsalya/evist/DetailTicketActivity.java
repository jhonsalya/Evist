package com.example.jhonsalya.evist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailTicketActivity extends AppCompatActivity {

    private String post_key = null;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;

    private TextView detailPostTitle;
    private TextView detailPostLocation;
    private TextView detailPostTotalPrice;
    private TextView detailPostStartDate;
    private TextView detailPostStartTime;
    private TextView detailQuantity;
    private ImageView detailTicketImage;

    private TextView detailPostBankAccount;
    private TextView detailPostAccountNumber;
    private TextView detailPostAccountOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ticket);

        post_key = getIntent().getExtras().getString("PostId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transaction");

        storageReference = FirebaseStorage.getInstance().getReference();

        detailPostTitle = (TextView) findViewById(R.id.event_name);
        detailPostLocation = (TextView) findViewById(R.id.event_location);
        detailPostTotalPrice = (TextView) findViewById(R.id.event_total_price);
        detailPostStartDate = (TextView) findViewById(R.id.event_date);
        detailPostStartTime = (TextView) findViewById(R.id.event_time);
        detailQuantity = (TextView) findViewById(R.id.event_ticket_amount);
        detailTicketImage = (ImageView) findViewById(R.id.ticket_image);

        detailPostBankAccount = (TextView) findViewById(R.id.event_bank_account);
        detailPostAccountNumber = (TextView) findViewById(R.id.event_account_number);
        detailPostAccountOwner = (TextView) findViewById(R.id.event_account_owner_name);

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_image = (String) dataSnapshot.child("ticket").getValue();
                String post_title = (String) dataSnapshot.child("name").getValue();
                String post_location = (String) dataSnapshot.child("address").getValue();
                String post_quantity = (String) dataSnapshot.child("quantity").getValue();
                String post_total_price= (String) dataSnapshot.child("total").getValue();
                String post_start_date = (String) dataSnapshot.child("startdate").getValue();
                String post_start_time = (String) dataSnapshot.child("starttime").getValue();

                String post_bank_account = (String) dataSnapshot.child("bankaccount").getValue();
                String post_bank_account_number = (String) dataSnapshot.child("bankaccountNumber").getValue();
                String post_bank_account_name = (String) dataSnapshot.child("bankaccountName").getValue();

                Picasso.with(DetailTicketActivity.this).load(post_image).into(detailTicketImage);
                detailPostTitle.setText(post_title);
                detailPostLocation.setText(post_location);
                detailQuantity.setText(post_quantity);
                detailPostTotalPrice.setText(post_total_price);
                detailPostStartDate.setText(post_start_date);
                detailPostStartTime.setText(post_start_time);

                detailPostBankAccount.setText(post_bank_account);
                detailPostAccountNumber.setText(post_bank_account_number);
                detailPostAccountOwner.setText(post_bank_account_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void orderButtonClicked(View view){
        Intent registerIntent = new Intent(this, UnpaidActivity.class);
        registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(registerIntent);
    }
}
