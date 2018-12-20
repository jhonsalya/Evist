package com.example.jhonsalya.evist;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ConfirmPaymentDetailActivity extends AppCompatActivity {

    private String post_key = null;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUnpaid;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private StorageReference storageReference;

    private ImageView detailReceiptImage;
    private TextView detailPostTitle;
    private TextView detailPostLocation;
    private TextView detailPostQuantity;
    private TextView detailPostTotalPrice;
    private TextView detailPostBankAccount;
    private TextView detailPostBankAccountNumber;
    private TextView detailPostBankAccountName;

    Dialog myDialog;
    private static final int GALLERY_REQUEST = 2;
    private Uri uri = null;

    Button btnTicket;
    ImageButton btnPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment_detail);

        post_key = getIntent().getExtras().getString("PostId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Transaction");
        mDatabaseUnpaid = FirebaseDatabase.getInstance().getReference().child("UnpaidList");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        storageReference = FirebaseStorage.getInstance().getReference();

        detailReceiptImage = (ImageView) findViewById(R.id.receipt_image);
        detailPostTitle = (TextView) findViewById(R.id.event_name);
        detailPostLocation = (TextView) findViewById(R.id.event_location);
        detailPostQuantity = (TextView) findViewById(R.id.event_ticket_amount);
        detailPostTotalPrice = (TextView) findViewById(R.id.event_total_price);
        detailPostBankAccount = (TextView) findViewById(R.id.event_bank_account);
        detailPostBankAccountNumber = (TextView) findViewById(R.id.event_account_number);
        detailPostBankAccountName = (TextView) findViewById(R.id.event_account_owner_name);

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_image = (String) dataSnapshot.child("receipt").getValue();
                String post_title = (String) dataSnapshot.child("name").getValue();
                String post_location = (String) dataSnapshot.child("address").getValue();
                String post_quantity = (String) dataSnapshot.child("quantity").getValue();
                String post_total_price= (String) dataSnapshot.child("total").getValue();
                String post_bank_account = (String) dataSnapshot.child("bankaccount").getValue();
                String post_bank_account_number = (String) dataSnapshot.child("bankaccountNumber").getValue();
                String post_bank_account_name = (String) dataSnapshot.child("bankaccountName").getValue();

                Picasso.with(ConfirmPaymentDetailActivity.this).load(post_image).into(detailReceiptImage);
                detailPostTitle.setText(post_title);
                detailPostLocation.setText(post_location);
                detailPostQuantity.setText(post_quantity);
                detailPostTotalPrice.setText(post_total_price);
                detailPostBankAccount.setText(post_bank_account);
                detailPostBankAccountNumber.setText(post_bank_account_number);
                detailPostBankAccountName.setText(post_bank_account_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void confirmPayment(View view){
        final ProgressDialog mDialog = new ProgressDialog(ConfirmPaymentDetailActivity.this);
        mDialog.setMessage("Please Wait.....");
        mDialog.show();

        if(uri != null){
            changeImage();
        }
        else{
            Toast.makeText(this, "Please Select Ticket First", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
        }

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String post_buyerid = (String) dataSnapshot.child("buyerid").getValue();
                final String post_sellerid = (String) dataSnapshot.child("sellerid").getValue();
                final String unpaidid = (String) dataSnapshot.child("unpaidid").getValue();

                final DatabaseReference newPost = mDatabase.child(post_key);

                //edit status of the transaction to confirmed
                mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        newPost.child("statusbuyer").setValue("confirmed_"+post_buyerid);
                        newPost.child("statusseller").setValue("confirmed_"+post_sellerid).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    mDialog.dismiss();
                                    Toast.makeText(ConfirmPaymentDetailActivity.this, "Payment Confirmed", Toast.LENGTH_SHORT).show();
                                    Intent salesIntnet = new Intent(ConfirmPaymentDetailActivity.this, SalesActivity.class);
                                    salesIntnet.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(salesIntnet);
                                    mDatabaseUnpaid.child(unpaidid).removeValue();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void selectTicket(View view) {
        myDialog = new Dialog(this);
        TextView txtclose;
        myDialog.setContentView(R.layout.popup_upload_ticket);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        btnPhoto = (ImageButton) myDialog.findViewById(R.id.addTicketImage);
        btnTicket = (Button) myDialog.findViewById(R.id.setTicket);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClicked();
            }
        });

        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void imageClicked() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            btnPhoto.setImageURI(uri);
        }
    }

    private void changeImage(){
        StorageReference filePath = storageReference.child("TransactionTicket").child(uri.getLastPathSegment());
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadurl = taskSnapshot.getDownloadUrl();
                final DatabaseReference newPost = mDatabase.child(post_key);
                newPost.child("ticket").setValue(downloadurl.toString());

            }
        });
    }
}
