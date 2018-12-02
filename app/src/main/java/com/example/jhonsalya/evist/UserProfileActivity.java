package com.example.jhonsalya.evist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jhonsalya.evist.Model.Event;
import com.example.jhonsalya.evist.Model.EventAdapter;
import com.example.jhonsalya.evist.ViewHolder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    //private EventAdapter adapter;
    private List<Event> eventList;

    FirebaseRecyclerAdapter<Event,EventViewHolder> adapter;

    //private RecyclerView mEventList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    private FirebaseAuth.AuthStateListener mAuthListener;

    String mainId = "uid";
    String intentQuery = "";
    Query dataQuery;

    private String post_key = null;
    private ImageView detailUserImage;
    private TextView detailUserName;
    private TextView detailUserEmail;
    private TextView detailUserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        eventList = new ArrayList<>();
        //adapter = new EventAdapter(this, eventList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("EventApp");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        intentQuery = mCurrentUser.getUid();

        detailUserImage = (ImageView) findViewById(R.id.user_image);
        detailUserName = (TextView) findViewById(R.id.user_name_value);
        detailUserEmail = (TextView) findViewById(R.id.user_email_value);
        detailUserPhone = (TextView) findViewById(R.id.user_phone_value);

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user_image = (String) dataSnapshot.child("image").getValue();
                String user_name = (String) dataSnapshot.child("name").getValue();
                String user_email = (String) dataSnapshot.child("email").getValue();
                String user_phone = (String) dataSnapshot.child("phone").getValue();

                Picasso.with(UserProfileActivity.this).load(user_image).into(detailUserImage);
                detailUserName.setText(user_name);
                detailUserEmail.setText(user_email);
                detailUserPhone.setText(user_phone);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //prepareEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mainId == null){
            mainId = "uid";
        }
        if(intentQuery != null){
            dataQuery = mDatabase.orderByChild(mainId).startAt(intentQuery).endAt(intentQuery+"\uf8ff");
        }
        else{
            dataQuery = mDatabase.orderByChild(mainId);
        }

        FirebaseRecyclerAdapter<Event, EventViewHolder> FBRA = new FirebaseRecyclerAdapter<Event, EventViewHolder>(
                Event.class,
                R.layout.event_card_user,
                EventViewHolder.class,
                dataQuery
                //mDatabase.orderByChild(user)
        ) {
            @Override
            protected void populateViewHolder(final EventViewHolder viewHolder, Event model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getLocation());
                viewHolder.setImage(getApplicationContext(),model.getImage());

                //Detail Event
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent eventDetailActivity = new Intent(UserProfileActivity.this, EventDetailActivity.class);
                        eventDetailActivity.putExtra("PostId", post_key);
                        startActivity(eventDetailActivity);
                    }
                });
                viewHolder.overflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenu(viewHolder.overflow);
                    }

                    //showing popup menu when taping icon
                    private void showPopupMenu(View view){
                        //Tutorial on 10.50 android card view and recycler view
                        PopupMenu popup = new PopupMenu(getApplicationContext(),view);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.menu_event, popup.getMenu());
                        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
                        popup.show();
                    }

                    //click listener for popup menu items
                    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{
                        public MyMenuItemClickListener(){

                        }
                        public boolean onMenuItemClick(MenuItem menuItem){
                            switch(menuItem.getItemId()){
                                case R.id.action_add_favourite:
                                    Toast.makeText(getApplicationContext(),"Edit Event", Toast.LENGTH_SHORT).show();
                                    Intent editActivityIntent = new Intent(UserProfileActivity.this, EditEventActivity.class);
                                    editActivityIntent.putExtra("PostId",post_key);
                                    startActivity(editActivityIntent);
                                    return true;
                                case R.id.action_play_next:
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfileActivity.this);
                                    alertDialog.setMessage("Are You Sure Want to Delete?");
                                    alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

                                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(UserProfileActivity.this, "Event Deleted", Toast.LENGTH_SHORT).show();
                                            mDatabase.child(post_key).removeValue();
                                        }
                                    });

                                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(UserProfileActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    alertDialog.show();
                                    return true;
                                default:
                            }
                            return false;
                        }
                    }
                });
            }
        };
        recyclerView.setAdapter(FBRA);
    }



    //adding few albums for testing
    /*private void prepareEvents(){
        int[] covers = new int[]{
                R.drawable.event1,
        };

        Event a = new Event("Bakso Bakar Festival", "Denpasar", "10000", covers[0]);
        eventList.add(a);

        a = new Event("Bakso Festival", "Denpasar", "10000", covers[0]);
        eventList.add(a);

        a = new Event("Bakso Festival", "Denpasar", "10000", covers[0]);
        eventList.add(a);

        a = new Event("Bakso Festival", "Denpasar", "10000", covers[0]);
        eventList.add(a);

        a = new Event("Bakso Festival", "Denpasar", "10000", covers[0]);
        eventList.add(a);

        adapter.notifyDataSetChanged();
    }*/

    //recylerview item decoration - give equal margin around grid item
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge){
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
            int position = parent.getChildAdapterPosition(view); //item position
            int column = position % spanCount; // item column

            if(includeEdge){
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if(position < spanCount){ //top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            }
            else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if(position >= spanCount){ //top edge
                    outRect.top = spacing;
                }
            }
        }
    }
    //converting dp to pixel
    private int dpToPx(int dp){
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Toast.makeText(UserProfileActivity.this, "Add Event", Toast.LENGTH_LONG).show();
            Intent addIntent = new Intent(UserProfileActivity.this, AddEventActivity .class);
            startActivity(addIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void settingButtonClicked(View view){
        Intent editUserIntent = new Intent(UserProfileActivity.this, EditUserActivity.class);
        editUserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(editUserIntent);
    }
}
