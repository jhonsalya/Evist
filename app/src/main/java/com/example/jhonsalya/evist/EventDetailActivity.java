package com.example.jhonsalya.evist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EventDetailActivity extends AppCompatActivity {

    private String post_key = null;
    private DatabaseReference mDatabase;
    private ImageView detailPostImage;
    private TextView detailPostTitle;
    private TextView detailPostCategory;
    private TextView detailPostLocation;
    private TextView detailPostPrice;
    private TextView detailPostDescription;
    private TextView detailPostStartDate;
    private TextView detailPostFinishDate;
    private TextView detailPostStartTime;
    private TextView detailPostParticipant;
    private TextView detailPostTargetAge;
    private TextView detailPostOrganizer;
    private TextView detailPostContact;

    private TextView detailPostBankAccount;
    private TextView detailPostAccountNumber;
    private TextView detailPostAccountOwner;

    private Button deleteButton;
    private Button editButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        post_key = getIntent().getExtras().getString("PostId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("EventApp");

        detailPostImage = (ImageView) findViewById(R.id.event_image);
        detailPostTitle = (TextView) findViewById(R.id.event_title);
        //detailPostCategory = (TextView) findViewById(R.id.detailCategory);
        detailPostLocation = (TextView) findViewById(R.id.event_location);
        detailPostPrice = (TextView) findViewById(R.id.event_price);
        detailPostDescription = (TextView) findViewById(R.id.event_description);
        detailPostStartDate = (TextView) findViewById(R.id.start_date_value);
        detailPostFinishDate = (TextView) findViewById(R.id.finish_date_value);
        detailPostStartTime = (TextView) findViewById(R.id.event_time_value);
        detailPostParticipant = (TextView) findViewById(R.id.event_participant_value);
        detailPostTargetAge = (TextView) findViewById(R.id.event_target_age_value);
        detailPostOrganizer = (TextView) findViewById(R.id.event_organizer_value);
        detailPostContact = (TextView) findViewById(R.id.event_organizer_phone_value);

//        detailPostBankAccount = (TextView) findViewById(R.id.detailParticipant);
//        detailPostAccountNumber = (TextView) findViewById(R.id.detailParticipant);
//        detailPostAccountOwner = (TextView) findViewById(R.id.detailParticipant);

        //untuk di detail user yang isi delete dan edit button
        /*mAuth = FirebaseAuth.getInstance();
        deleteButton = (Button) findViewById(R.id.detailDeleteButton);
        deleteButton.setVisibility(View.INVISIBLE);

        editButton = (Button) findViewById(R.id.editEventButton);
        editButton.setVisibility(View.INVISIBLE);*/

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
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

                Picasso.with(EventDetailActivity.this).load(post_image).into(detailPostImage);
                detailPostTitle.setText(post_title);
                //detailPostCategory.setText(post_category);
                detailPostLocation.setText(post_location);
                detailPostPrice.setText(post_price);
                detailPostDescription.setText(post_description);
                detailPostStartDate.setText(post_start_date);
                detailPostFinishDate.setText(post_finish_date);
                detailPostStartTime.setText(post_start_time);
                detailPostParticipant.setText(post_participant);
                detailPostTargetAge.setText(post_target_age);
                detailPostOrganizer.setText(post_organizer);
                detailPostContact.setText(post_contact);

                //show delete button if the user authenticated
                /*if(mAuth.getCurrentUser() != null){
                    if(mAuth.getCurrentUser().getUid().equals(post_uid)){
                        deleteButton.setVisibility(View.VISIBLE);
                        editButton.setVisibility(View.VISIBLE);
                    }
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_three_dots) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
