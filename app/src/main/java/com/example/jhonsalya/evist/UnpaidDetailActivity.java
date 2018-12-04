package com.example.jhonsalya.evist;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UnpaidDetailActivity extends AppCompatActivity {

    private String post_key = null;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseEvent;

    private TextView detailPostTitle;
    private TextView detailPostLocation;
    private TextView detailPostTotalPrice;
    private TextView detailPostStartDate;
    private TextView detailPostStartTime;
    private TextView detailQuantity;
//    private TextView detailEventId;
//    private TextView detailPrice;
//    private TextView detailBuyerId;

    private TextView detailPostBankAccount;
    private TextView detailPostAccountNumber;
    private TextView detailPostAccountOwner;

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid_detail);

        post_key = getIntent().getExtras().getString("PostId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("UnpaidList");

        detailPostTitle = (TextView) findViewById(R.id.event_name);
        detailPostLocation = (TextView) findViewById(R.id.event_location);
        detailPostTotalPrice = (TextView) findViewById(R.id.event_total_price);
        detailPostStartDate = (TextView) findViewById(R.id.event_date);
        detailPostStartTime = (TextView) findViewById(R.id.event_time);
        detailQuantity = (TextView) findViewById(R.id.event_ticket_amount);

        detailPostBankAccount = (TextView) findViewById(R.id.event_bank_account);
        detailPostAccountNumber = (TextView) findViewById(R.id.event_account_number);
        detailPostAccountOwner = (TextView) findViewById(R.id.event_account_owner_name);

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("name").getValue();
                String post_location = (String) dataSnapshot.child("address").getValue();
                String post_total_price = (String) dataSnapshot.child("total").getValue();
                String post_start_date = (String) dataSnapshot.child("startdate").getValue();
                String post_start_time = (String) dataSnapshot.child("starttime").getValue();
                String post_quantity = (String) dataSnapshot.child("quantity").getValue();
                String post_event_id = (String) dataSnapshot.child("id").getValue();
                String post_buyer_id = (String) dataSnapshot.child("buyerid").getValue();
                String post_price = (String) dataSnapshot.child("price").getValue();

                //For searching the payment detail on event database
                mDatabaseEvent = FirebaseDatabase.getInstance().getReference().child("EventApp").child(post_event_id);
                mDatabaseEvent.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String post_bank_account = (String) dataSnapshot.child("bank_account").getValue();
                        String post_account_number = (String) dataSnapshot.child("account_number").getValue();
                        String post_account_owner = (String) dataSnapshot.child("account_owner").getValue();

                        detailPostBankAccount.setText(post_bank_account);
                        detailPostAccountNumber.setText(post_account_number);
                        detailPostAccountOwner.setText(post_account_owner);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                detailPostTitle.setText(post_title);
                detailPostLocation.setText(post_location);
                detailPostTotalPrice.setText(post_total_price);
                detailPostStartDate.setText(post_start_date);
                detailPostStartTime.setText(post_start_time);
                detailQuantity.setText(post_quantity);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uploadButtonClicked(View view){
//        Intent receiptIntent = new Intent(UnpaidDetailActivity.this, UploadReceiptActivity.class);
//        receiptIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        receiptIntent.putExtra("PostId", post_key);
//        startActivity(receiptIntent);
        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_total_price = (String) dataSnapshot.child("total").getValue();

                showPopUp(post_total_price);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showPopUp(final String total){
        myDialog = new Dialog(this);
        TextView txtclose;
        Button btnCredit;
        Button btnBank;
        myDialog.setContentView(R.layout.popup_payment);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        btnCredit = (Button) myDialog.findViewById(R.id.btnCredit);
        btnBank = (Button) myDialog.findViewById(R.id.btnBankTransfer);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        btnCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //OPEN CREDIT CARD PAGE
                Intent creditCardActivity = new Intent(UnpaidDetailActivity.this, CreditCardActivity.class);
                creditCardActivity.putExtra("PostId", total);
                //Toast.makeText(MainActivity.this, post_key, Toast.LENGTH_SHORT).show();
                startActivity(creditCardActivity);
            }
        });
        btnBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receiptIntent = new Intent(UnpaidDetailActivity.this, UploadReceiptActivity.class);
                receiptIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                receiptIntent.putExtra("PostId", post_key);
                startActivity(receiptIntent);
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
