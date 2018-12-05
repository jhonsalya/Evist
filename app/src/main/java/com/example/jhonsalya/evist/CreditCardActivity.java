package com.example.jhonsalya.evist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.craftman.cardform.Card;
import com.craftman.cardform.CardForm;
import com.craftman.cardform.OnPayBtnClickListner;

public class CreditCardActivity extends AppCompatActivity {

    private String post_key = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);

        post_key = getIntent().getExtras().getString("PostId");

        CardForm cardForm=(CardForm)findViewById(R.id.cardform);
        TextView txtDes= (TextView)findViewById(R.id.payment_amount);
        Button btnPay= (Button)findViewById(R.id.btn_pay);

        txtDes.setText(post_key);
        btnPay.setText(String.format("Payer  %s", txtDes.getText()));

        cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
            @Override
            public void onClick(Card card) {
                Toast.makeText(CreditCardActivity.this, "Payment Success"+card.getLast4(),
                        Toast.LENGTH_SHORT).show();
                Toast.makeText(CreditCardActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();
                Intent mainActivityIntent = new Intent(CreditCardActivity.this, UnpaidActivity.class);
                startActivity(mainActivityIntent);
            }
        });
    }
}
