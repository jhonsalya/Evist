package com.example.jhonsalya.evist;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jhonsalya.evist.Model.Event;
import com.example.jhonsalya.evist.Model.EventAdapter;
import com.example.jhonsalya.evist.Sidebar.Constant;
import com.example.jhonsalya.evist.Sidebar.SidebarAdapter;
import com.example.jhonsalya.evist.ViewHolder.EventViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity
        implements ExpandableListView.OnGroupClickListener,
        ExpandableListView.OnChildClickListener {

    private ExpandableListView sidebarList;
    private SidebarAdapter mAdapter;
    private DrawerLayout drawer;
    private List<String> listParentSidebar;
    private List<String> sortChild;
    private List<String> purchaseChild;
    private List<String> salesChild;
    private HashMap<String, List<String>> listChildSidebar;

    //drawer for navigation
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //end of drawer

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;

    //private RecyclerView mEventList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    //for recommendation event
    private DatabaseReference recDatabase;
    Dialog myDialog;
    private ImageView detailPostImage;

    private FirebaseAuth.AuthStateListener mAuthListener;

    String mainId = "category";
    String intentQuery = "";
    Query dataQuery;

    MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userProfileActivity = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(userProfileActivity);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        searchView=(MaterialSearchView)findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String sort = "title";
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("idPass", sort);
                intent.putExtra("query", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Get Intent here for search
        Intent intent = getIntent();
        if(intent != null){
            mainId = intent.getStringExtra("idPass");
            intentQuery = intent.getStringExtra("query");
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setListData(); // load data
        mAdapter = new SidebarAdapter(this, listParentSidebar, listChildSidebar); // init adapter

        // initialize expandable list
        sidebarList = (ExpandableListView) findViewById(R.id.sidebar_list);
        sidebarList.setAdapter(mAdapter);
        sidebarList.setOnGroupClickListener(this);
        sidebarList.setOnChildClickListener(this);

        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        eventList = new ArrayList<>();
        adapter = new EventAdapter(this, eventList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(adapter);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("EventApp");

        //prepareEvents();

        /**
         * get random event to be posted in pop up menu
         *
         * */
        recDatabase = FirebaseDatabase.getInstance().getReference().child("EventApp");
        myDialog = new Dialog(this);
        recDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Random random = new Random();

                int questionCount = (int) dataSnapshot.getChildrenCount();
                int rand = random.nextInt(questionCount);
                Iterator itr = dataSnapshot.getChildren().iterator();

                for(int i = 0; i < rand; i++) {
                    itr.next();
                }
                DataSnapshot childSnapshot = (DataSnapshot) itr.next();

                TextView txtclose;
                Button btnFollow;
                myDialog.setContentView(R.layout.custompopup);

                detailPostImage = (ImageView) myDialog.findViewById(R.id.imagePopUp);
                String post_image = (String) childSnapshot.child("image").getValue();
                Picasso.with(MainActivity.this).load(post_image).into(detailPostImage);

                txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
                txtclose.setText("X");
                btnFollow = (Button) myDialog.findViewById(R.id.btnDetail);
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();

                //question = childSnapshot.getValue(Question.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mainId == null){
            mainId = "category";
        }
        if(intentQuery != null){
            dataQuery = mDatabase.orderByChild(mainId).startAt(intentQuery).endAt(intentQuery+"\uf8ff");
        }
        else{
            dataQuery = mDatabase.orderByChild(mainId);
        }

        FirebaseRecyclerAdapter <Event, EventViewHolder> FBRA = new FirebaseRecyclerAdapter<Event, EventViewHolder>(
                Event.class,
                R.layout.event_card,
                EventViewHolder.class,
                dataQuery
                //mDatabase.orderByChild(mainId)
        ) {
            @Override
            protected void populateViewHolder(EventViewHolder viewHolder, Event model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getLocation());
                viewHolder.setImage(getApplicationContext(),model.getImage());

                //Detail Event
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent eventDetailActivity = new Intent(MainActivity.this, EventDetailActivity.class);
                        eventDetailActivity.putExtra("PostId", post_key);
                        //Toast.makeText(MainActivity.this, post_key, Toast.LENGTH_SHORT).show();
                        startActivity(eventDetailActivity);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(searchView.isSearchOpen()){
            searchView.closeSearch();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setListData() {
        listParentSidebar = new ArrayList<String>();
        listChildSidebar = new HashMap<String, List<String>>();

        // Adding parent data
        listParentSidebar.add(Constant.S_POS_ACCOUNT, "Account Profile");
        listParentSidebar.add(Constant.S_POS_CATEGORY, "Category");
        listParentSidebar.add(Constant.S_POS_SORT, "Sort");
        listParentSidebar.add(Constant.S_POS_CART, "Cart");
        listParentSidebar.add(Constant.S_POS_PURCHASE, "Purchase");
        listParentSidebar.add(Constant.S_POS_SALES, "Sales");
        listParentSidebar.add(Constant.S_POS_LOGOUT, "Logout");

        // Adding child data
        sortChild = new ArrayList<String>();
        sortChild.add("Sort Alphabetically");
        sortChild.add("Sort by Time");

        //Child Manage
        purchaseChild = new ArrayList<String>();
        purchaseChild.add("Unpaid");
        purchaseChild.add("Purchased");

        //Child Sales
        salesChild = new ArrayList<String>();
        salesChild.add("Confirm");
        salesChild.add("Sales List");

        // Set child to particular parent
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_ACCOUNT), new ArrayList<String>()); // adding empty child
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_CATEGORY), new ArrayList<String>()); // adding empty child
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_SORT), sortChild);
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_CART), new ArrayList<String>()); // adding empty child
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_PURCHASE), purchaseChild);
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_SALES), salesChild);
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_LOGOUT), new ArrayList<String>()); // adding empty child
    }

    // Handling click on child item
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (groupPosition == 2) { // index program
            showToast(sortChild.get(childPosition));
            if(childPosition == 0){
                String sort = "title";
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("idPass", sort);
                startActivity(intent);
            }
            else if(childPosition == 1){
                String sort = "start_date";
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("idPass", sort);
                startActivity(intent);
            }
        }
        if (groupPosition == 4){
            //Unpaid List
            if (childPosition == 0) {
                Intent intent = new Intent(MainActivity.this, UnpaidActivity.class);
                startActivity(intent);
            }
            //Purchased
            else if (childPosition == 1) {
                Intent intent = new Intent(MainActivity.this, PurchasedActivity.class);
                startActivity(intent);
            }
        }
        if (groupPosition == 5){
            //Confirm payment
            if (childPosition == 0) {
                Intent intent = new Intent(MainActivity.this, ConfirmPaymentActivity.class);
                startActivity(intent);
            }
            //ticket sold
            else if (childPosition == 1) {
                Intent intent = new Intent(MainActivity.this, SalesActivity.class);
                startActivity(intent);
            }
        }

        drawer.closeDrawer(GravityCompat.START); // close drawer
        return true;
    }

    // Handling click on parent item
    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        // Flag for group that have child, then parent will expand or collapse
        boolean isHaveChild = false;

        switch (groupPosition) {
            case Constant.S_POS_ACCOUNT:
                if(mAuth.getCurrentUser() != null){
                    showToast("User Profile");
                    Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                }
                else{
                    showToast("Please Login First");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;

            case Constant.S_POS_CATEGORY:
                showToast("Event Category");
                Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(categoryIntent);
                break;

            case Constant.S_POS_SORT:
                isHaveChild = true; // have child

                if (parent.isGroupExpanded(groupPosition)) // if parent expanded
                    parent.collapseGroup(groupPosition); // collapse parent
                else
                    parent.expandGroup(groupPosition); // expand parent
                break;

            case Constant.S_POS_CART:
                showToast("Cart");
                Intent cartIntent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(cartIntent);
                break;

            case Constant.S_POS_PURCHASE:
                isHaveChild = true; // have child

                if (parent.isGroupExpanded(groupPosition)) // if parent expanded
                    parent.collapseGroup(groupPosition); // collapse parent
                else
                    parent.expandGroup(groupPosition); // expand parent
                break;

            case Constant.S_POS_SALES:
                isHaveChild = true; // have child

                if (parent.isGroupExpanded(groupPosition)) // if parent expanded
                    parent.collapseGroup(groupPosition); // collapse parent
                else
                    parent.expandGroup(groupPosition); // expand parent
                break;

            case Constant.S_POS_LOGOUT:
                showToast("Logged Out");
                mAuth.signOut();
                Intent LogoutIntent = new Intent(MainActivity.this, LoginActivity .class);
                startActivity(LogoutIntent);
                break;

            default:
                break;
        }

        if (!isHaveChild) { // if don't have child, close drawer
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/
}
