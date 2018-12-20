package com.example.jhonsalya.evist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class UploadReceiptActivity extends AppCompatActivity {
    private EditText editBank;
    private EditText editBankNumber;
    private EditText editBankAccoutName;

    private String post_key = null;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseEvent;
    private DatabaseReference mDatabaseTransaction;

    private StorageReference storageReference;
    private FirebaseDatabase database;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    //for opening gallery on android phone
    private static final int GALLERY_REQUEST = 2;
    private Uri uri = null;
    private ImageButton imageButton;

    private TextView detailPostBankAccount;
    private TextView detailPostAccountNumber;
    private TextView detailPostAccountOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_receipt);

        // yang ini buat cek user
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        editBank = (EditText) findViewById(R.id.edit_transffered_account_bank);
        editBankNumber = (EditText) findViewById(R.id.edit_transffered_account_number);
        editBankAccoutName = (EditText) findViewById(R.id.edit_transffered_account_name);

        post_key = getIntent().getExtras().getString("PostId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("UnpaidList");

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseTransaction = database.getInstance().getReference().child("Transaction");

        detailPostBankAccount = (TextView) findViewById(R.id.event_bank_account);
        detailPostAccountNumber = (TextView) findViewById(R.id.event_account_number);
        detailPostAccountOwner = (TextView) findViewById(R.id.event_account_owner_name);

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_event_id = (String) dataSnapshot.child("id").getValue();

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void receiptImageClicked(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            imageButton = (ImageButton) findViewById(R.id.receipt_image);
            imageButton.setImageURI(uri);
        }
    }

    public void uploadReceiptButtonClicked(View view){
        final ProgressDialog mDialog = new ProgressDialog(UploadReceiptActivity.this);
        mDialog.setMessage("Please Wait.....");
        mDialog.show();

        final String bankValue = editBank.getText().toString().trim();
        final String bankNumberValue = editBankNumber.getText().toString().trim();
        final String bankAccountNameValue = editBankAccoutName.getText().toString().trim();

        if(!TextUtils.isEmpty(bankValue) && !TextUtils.isEmpty(bankNumberValue) && !TextUtils.isEmpty(bankAccountNameValue) && uri != null){

            mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String post_title = (String) dataSnapshot.child("name").getValue();
                    final String post_location = (String) dataSnapshot.child("address").getValue();
                    final String post_total_price = (String) dataSnapshot.child("total").getValue();
                    final String post_start_date = (String) dataSnapshot.child("startdate").getValue();
                    final String post_start_time = (String) dataSnapshot.child("starttime").getValue();
                    final String post_quantity = (String) dataSnapshot.child("quantity").getValue();
                    final String post_event_id = (String) dataSnapshot.child("id").getValue();
                    final String post_buyer_id = (String) dataSnapshot.child("buyerid").getValue();
                    final String post_seller_id = (String) dataSnapshot.child("sellerid").getValue();
                    final String post_price = (String) dataSnapshot.child("price").getValue();

                    StorageReference filePath = storageReference.child("Receipt").child(uri.getLastPathSegment());
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Uri downloadurl = taskSnapshot.getDownloadUrl();
                            final DatabaseReference newPost = mDatabaseTransaction.push();

                            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    newPost.child("receipt").setValue(downloadurl.toString());
                                    newPost.child("statusseller").setValue("waiting_"+post_seller_id);
                                    newPost.child("statusbuyer").setValue("nostatus");
                                    newPost.child("unpaidid").setValue(post_key);
                                    newPost.child("name").setValue(post_title);
                                    newPost.child("address").setValue(post_location);
                                    newPost.child("total").setValue(post_total_price);
                                    newPost.child("startdate").setValue(post_start_date);
                                    newPost.child("starttime").setValue(post_start_time);
                                    newPost.child("quantity").setValue(post_quantity);
                                    newPost.child("id").setValue(post_event_id);
                                    newPost.child("buyerid").setValue(post_buyer_id);
                                    newPost.child("sellerid").setValue(post_seller_id);
                                    newPost.child("price").setValue(post_price);
                                    newPost.child("bankaccount").setValue(bankValue);
                                    newPost.child("bankaccountNumber").setValue(bankNumberValue);
                                    newPost.child("bankaccountName").setValue(bankAccountNameValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                mDialog.dismiss();
                                                Toast.makeText(UploadReceiptActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();
                                                Intent mainActivityIntent = new Intent(UploadReceiptActivity.this, MainActivity.class);
                                                mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(mainActivityIntent);
                                            }
                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else{
            Toast.makeText(this, "Please Fill All The Form", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
        }
    }
}
