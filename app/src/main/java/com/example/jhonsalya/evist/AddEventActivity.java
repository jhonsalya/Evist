package com.example.jhonsalya.evist;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jhonsalya.evist.Model.Category;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {

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
    private Spinner editSpinner;

    //bank info
    private EditText editBankAccount;
    private EditText editAccountNumber;
    private EditText editAccountOwner;

    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseReference categoryReference;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    Calendar mCurrentDate, mCurrentTime;
    private String startTimeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editName = (EditText) findViewById(R.id.edit_event_name);
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
        editSpinner = findViewById(R.id.spinner);

        //bank info
        editBankAccount = (EditText) findViewById(R.id.edit_event_bank_account);
        editAccountNumber = (EditText) findViewById(R.id.edit_event_bank_account_no);
        editAccountOwner = (EditText) findViewById(R.id.edit_event_bank_account_owner);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = database.getInstance().getReference().child("EventApp");
        categoryReference = FirebaseDatabase.getInstance().getReference();

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

                DatePickerDialog mDatePicker = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        selectedMonth++;
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

                DatePickerDialog mDatePicker = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        selectedMonth++;
                        editFinishDate.setText(selectedDay+"/"+selectedMonth+"/"+selectedYear);
                        mCurrentDate.set(selectedYear, selectedMonth, selectedDay);
                    }
                }, year, month, day);
                mDatePicker.show();
            }
        });

        //show dialog for picking time
        editStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String minuteConvert;
                        if(selectedMinute < 10){
                            minuteConvert = "0"+Integer.toString(selectedMinute);
                        }
                        else {
                            minuteConvert = Integer.toString(selectedMinute);
                        }
                        editStartTime.setText(selectedHour + ":" + minuteConvert);
                        //startTimeValue = selectedHour +":"+ minuteConvert;
                    }
                }, hour, minute, true); //24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //load category to the spinner
        categoryReference.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                final List<String> areas = new ArrayList<String>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("name").getValue(String.class);
                    areas.add(areaName);
                }

                Spinner areaSpinner = (Spinner) findViewById(R.id.spinner);
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AddEventActivity.this, android.R.layout.simple_spinner_item, areas);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(areasAdapter);
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
            imageButton = (ImageButton) findViewById(R.id.addEventImage);
            imageButton.setImageURI(uri);
        }
    }

    /*private void changeImage(){
        StorageReference filePath = storageReference.child("PostEvent").child(uri.getLastPathSegment());
        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri downloadurl = taskSnapshot.getDownloadUrl();
                final DatabaseReference newPost = databaseReference.push();
                newPost.child("image").setValue(downloadurl.toString());
                Toast.makeText(AddEventActivity.this, "Change Image Function", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    public void addButtonClicked(View view){
        final ProgressDialog mDialog = new ProgressDialog(AddEventActivity.this);
        mDialog.setMessage("Please Wait.....");
        mDialog.show();

        final String nameValue = editName.getText().toString().trim();
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
        final String categorySpinnerValue = editSpinner.getSelectedItem().toString();

        final String bankAccountValue = editBankAccount.getText().toString().trim();
        final String accountNumberValue = editAccountNumber.getText().toString().trim();
        final String accountOwnerValue = editAccountOwner.getText().toString().trim();

        if(!TextUtils.isEmpty(nameValue) &&
                !TextUtils.isEmpty(categorySpinnerValue) &&
                !TextUtils.isEmpty(locationValue) &&
                !TextUtils.isEmpty(priceValue) &&
                !TextUtils.isEmpty(descValue) &&
                !TextUtils.isEmpty(startDateValue) &&
                !TextUtils.isEmpty(finishDateValue) &&
                !TextUtils.isEmpty(startTimeValue) &&
                !TextUtils.isEmpty(participantValue) &&
                !TextUtils.isEmpty(targetAgeValue) &&
                !TextUtils.isEmpty(organizerValue) &&
                !TextUtils.isEmpty(contactValue)){

            StorageReference filePath = storageReference.child("PostEvent").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadurl = taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost = databaseReference.push();

                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("title").setValue(nameValue);
                            newPost.child("image").setValue(downloadurl.toString());
                            newPost.child("category").setValue(categorySpinnerValue);
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
                            newPost.child("account_owner").setValue(accountOwnerValue);
                            newPost.child("uid").setValue(mCurrentUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mDialog.dismiss();
                                        Toast.makeText(AddEventActivity.this, "Upload Complete", Toast.LENGTH_SHORT).show();
                                        Intent mainActivityIntent = new Intent(AddEventActivity.this, MainActivity.class);
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
        else{
            Toast.makeText(this, "Please Fill All Form", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
        }
    }
}
