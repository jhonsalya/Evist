package com.example.jhonsalya.evist;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
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
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity {

    private String post_key = null;
    private static final int GALLERY_REQUEST = 2;
    private Uri uri = null;
    private ImageButton imageButton;
    private EditText editName;
    private EditText editCategory;
    private EditText editLocation;
    private EditText editPrice;
    private EditText editDesc;
    private EditText editStartDate;
    private EditText editFinishDate;
    private EditText editStartTime;
    private EditText editNoOfParticipant;
    private EditText editTargetAge;
    private EditText editOrganizer;
    private EditText editContact;

    //bank info
    private EditText editBankAccount;
    private EditText editAccountNumber;
    private EditText editAccountOwner;

    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    Calendar mCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        post_key = getIntent().getExtras().getString("PostId");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("EventApp");
        storageReference = FirebaseStorage.getInstance().getReference().child("PostEvent");

        imageButton = (ImageButton) findViewById(R.id.addEventImage);
        editName = (EditText) findViewById(R.id.edit_event_name);
        editCategory = (EditText) findViewById(R.id.edit_category);
        editLocation  = (EditText) findViewById(R.id.edit_location);
        editPrice = (EditText) findViewById(R.id.edit_event_price);
        editDesc = (EditText) findViewById(R.id.edit_event_description);
        editStartDate = (EditText) findViewById(R.id.edit_start_date);
        editFinishDate = (EditText) findViewById(R.id.edit_finish_date);
        editStartTime = (EditText) findViewById(R.id.edit_start_time);
        editNoOfParticipant = (EditText) findViewById(R.id.edit_event_participant);
        editTargetAge = (EditText) findViewById(R.id.edit_target_age);
        editOrganizer = (EditText) findViewById(R.id.edit_event_organizer);
        editContact = (EditText) findViewById(R.id.edit_contact);

        //bank info
        editBankAccount = (EditText) findViewById(R.id.edit_event_bank_account);
        editAccountNumber = (EditText) findViewById(R.id.edit_event_bank_account_no);
        editAccountOwner = (EditText) findViewById(R.id.edit_event_bank_account_owner);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        //show calendar in start date text view event
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int month = mCurrentDate.get(Calendar.MONTH);
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(EditEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        editStartDate.setText(selectedDay+"/"+selectedMonth+"/"+selectedYear);
                        mCurrentDate.set(selectedYear, selectedMonth, selectedDay);
                    }
                }, year, month, day);
                mDatePicker.show();
            }
        });

        //show calendar in start date text view event
        editFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int month = mCurrentDate.get(Calendar.MONTH);
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(EditEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        editFinishDate.setText(selectedDay+"/"+selectedMonth+"/"+selectedYear);
                        mCurrentDate.set(selectedYear, selectedMonth, selectedDay);
                    }
                }, year, month, day);
                mDatePicker.show();
            }
        });

        //input detail activity into the edit text
        databaseReference.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_category = (String) dataSnapshot.child("category").getValue();
                String post_location = (String) dataSnapshot.child("location").getValue();
                String post_price = (String) dataSnapshot.child("price").getValue();
                String post_description = (String) dataSnapshot.child("description").getValue();
                String post_start_date = (String) dataSnapshot.child("start_date").getValue();
                String post_finish_date = (String) dataSnapshot.child("finish_date").getValue();
                String post_start_time = (String) dataSnapshot.child("start_time").getValue();
                String post_participant = (String) dataSnapshot.child("participant").getValue();
                String post_target_age = (String) dataSnapshot.child("target_age").getValue();
                String post_organizer = (String) dataSnapshot.child("organizer").getValue();
                String post_contact = (String) dataSnapshot.child("contact").getValue();
                String post_bank_account = (String) dataSnapshot.child("bank_account").getValue();
                String post_account_number = (String) dataSnapshot.child("account_number").getValue();
                String post_account_owner = (String) dataSnapshot.child("account_owner").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();

                Picasso.with(EditEventActivity.this).load(post_image).into(imageButton);
                editName.setText(post_title, TextView.BufferType.EDITABLE);
                editCategory.setText(post_category);
                editLocation.setText(post_location);
                editPrice.setText(post_price);
                editDesc.setText(post_description);
                editStartDate.setText(post_start_date);
                editFinishDate.setText(post_finish_date);
                editStartTime.setText(post_start_time);
                editNoOfParticipant.setText(post_participant);
                editTargetAge.setText(post_target_age);
                editOrganizer.setText(post_organizer);
                editContact.setText(post_contact);
                editBankAccount.setText(post_bank_account);
                editAccountNumber.setText(post_account_number);
                editAccountOwner.setText(post_account_owner);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void imageClicked(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            imageButton.setImageURI(uri);
        }
    }

    private void changeImage(){
        StorageReference filePath = storageReference.child("PostEvent").child(uri.getLastPathSegment());
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadurl = taskSnapshot.getDownloadUrl();
                final DatabaseReference newPost = databaseReference.child(post_key);
                newPost.child("image").setValue(downloadurl.toString());

            }
        });
    }

    public void editButtonClicked(View view){
        final ProgressDialog mDialog = new ProgressDialog(EditEventActivity.this);
        mDialog.setMessage("Please Wait.....");
        mDialog.show();

        if(uri != null){
            changeImage();
        }

        final String nameValue = editName.getText().toString().trim();
        final String categoryValue = editCategory.getText().toString().trim();
        final String locationValue = editLocation.getText().toString().trim();
        final String priceValue = editPrice.getText().toString().trim();
        final String descValue = editDesc.getText().toString().trim();
        final String startDateValue = editStartDate.getText().toString().trim();
        final String finishDateValue = editFinishDate.getText().toString().trim();
        final String startTimeValue = editStartTime.getText().toString().trim();
        final String participantValue = editNoOfParticipant.getText().toString().trim();
        final String targetAgeValue = editTargetAge.getText().toString().trim();
        final String organizerValue = editOrganizer.getText().toString().trim();
        final String contactValue =  editContact.getText().toString().trim();

        final String bankAccountValue = editBankAccount.getText().toString().trim();
        final String accountNumberValue = editAccountNumber.getText().toString().trim();
        final String accountOwnerValue = editAccountOwner.getText().toString().trim();

        if(!TextUtils.isEmpty(nameValue) &&
                !TextUtils.isEmpty(categoryValue) &&
                !TextUtils.isEmpty(locationValue) &&
                !TextUtils.isEmpty(priceValue) &&
                !TextUtils.isEmpty(descValue) &&
                !TextUtils.isEmpty(startDateValue) &&
                !TextUtils.isEmpty(finishDateValue) &&
                !TextUtils.isEmpty(startTimeValue) &&
                !TextUtils.isEmpty(participantValue) &&
                !TextUtils.isEmpty(targetAgeValue) &&
                !TextUtils.isEmpty(organizerValue) &&
                !TextUtils.isEmpty(contactValue) &&
                !TextUtils.isEmpty(bankAccountValue) &&
                !TextUtils.isEmpty(accountNumberValue) &&
                !TextUtils.isEmpty(accountOwnerValue)){

            final DatabaseReference newPost = databaseReference.push();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newPost.child("title").setValue(nameValue);
                    newPost.child("category").setValue(categoryValue);
                    newPost.child("location").setValue(locationValue);
                    newPost.child("price").setValue(priceValue);
                    newPost.child("description").setValue(descValue);
                    newPost.child("start_date").setValue(startDateValue);
                    newPost.child("finish_date").setValue(finishDateValue);
                    newPost.child("start_time").setValue(startTimeValue);
                    newPost.child("participant").setValue(participantValue);
                    newPost.child("target_age").setValue(targetAgeValue);
                    newPost.child("organizer").setValue(organizerValue);
                    newPost.child("contact").setValue(contactValue);
                    newPost.child("bank_account").setValue(bankAccountValue);
                    newPost.child("account_number").setValue(accountNumberValue);
                    newPost.child("account_owner").setValue(accountOwnerValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mDialog.dismiss();
                                Toast.makeText(EditEventActivity.this, "Upload Complete", Toast.LENGTH_SHORT).show();
                                Intent eventDetailActivity = new Intent(EditEventActivity.this, EventDetailActivity.class);
                                eventDetailActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                eventDetailActivity.putExtra("PostId", post_key);
                                startActivity(eventDetailActivity);
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
