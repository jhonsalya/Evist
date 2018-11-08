package com.example.jhonsalya.evist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class UnpaidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid);
    }

    public void checkoutButtonClicked(View view){
        //Toast.makeText(this, "Try", Toast.LENGTH_SHORT).show();
        Intent unpaidDetail = new Intent(UnpaidActivity.this, UnpaidDetailActivity.class);
        unpaidDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(unpaidDetail);
    }
}
