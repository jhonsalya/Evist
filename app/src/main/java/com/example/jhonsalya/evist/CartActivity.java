package com.example.jhonsalya.evist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jhonsalya.evist.Common.Common;
import com.example.jhonsalya.evist.Database.Database;
import com.example.jhonsalya.evist.Model.Order;
import com.example.jhonsalya.evist.Model.Request;
import com.example.jhonsalya.evist.ViewHolder.CartAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("UnpaidList");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mDatabase = FirebaseDatabase.getInstance().getReference().child("EventApp");

        //Init
        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace = (Button)findViewById(R.id.btnPlaceOrder);

        //klik tombol place order ketika cart kosong
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cart.size() > 0)
                    showAlertDialog();
                else
                    Toast.makeText(CartActivity.this, "Your Cart is Empty", Toast.LENGTH_SHORT).show();
            }
        });

        loadListFood();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        //alertDialog.setTitle("One More Step");
        alertDialog.setMessage("Are You Sure Want to Checkout?");

//        final EditText edtAddress = new EditText(CartActivity.this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//        );
//        edtAddress.setLayoutParams(lp);
//        alertDialog.setView(edtAddress); // add edit text to alert dialog
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //create new request

                Request request = new Request(
                        //Common.currentUser.getPhone(),
                        //Common.currentUser.getName(),
                        //edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart,
                        mCurrentUser.getUid()
                );

                //submit to firebase
                //using system.currentMilli to key
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                //delete cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(CartActivity.this, "Thank You, Order Place", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged(); //refresh data cart kalo di delete
        recyclerView.setAdapter(adapter);

        //calculate total price
        int total = 0;
        for(Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }

    //press ctrl+o

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        //we will remove item at List<Order> by position
        cart.remove(position);
        //after that, we will delete all old data from SQLite
        new Database(this).cleanCart();
        //and final, we will update new data from List<Order> to SQLite
        for(Order item:cart)
            new Database(this).addToCart(item);
        //Refresh
        loadListFood();
    }

//    public void checkoutButtonClicked(View view){
//        Intent registerIntent = new Intent(CartActivity.this, CheckoutActivity.class);
//        registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(registerIntent);
//    }
}
