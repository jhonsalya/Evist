package com.example.jhonsalya.evist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class DetailTicketActivity extends AppCompatActivity {

    private String post_key = null;
    private DatabaseReference mDatabase;

    private TextView detailPostTitle;
    private TextView detailPostLocation;
    private TextView detailPostTotalPrice;
    private TextView detailPostStartDate;
    private TextView detailPostStartTime;
    private TextView detailQuantity;

    private TextView detailPostBankAccount;
    private TextView detailPostAccountNumber;
    private TextView detailPostAccountOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ticket);
    }

    public void orderButtonClicked(View view){
        Intent registerIntent = new Intent(this, UnpaidActivity.class);
        registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(registerIntent);
    }
}
