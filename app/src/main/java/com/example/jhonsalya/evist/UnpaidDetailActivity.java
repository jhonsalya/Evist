package com.example.jhonsalya.evist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UnpaidDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid_detail);
    }

    public void uploadButtonClicked(View view){
        Intent receiptIntent = new Intent(UnpaidDetailActivity.this, UploadReceiptActivity.class);
        receiptIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(receiptIntent);
    }
}
