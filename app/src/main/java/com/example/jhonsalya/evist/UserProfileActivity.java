package com.example.jhonsalya.evist;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jhonsalya.evist.Model.Event;
import com.example.jhonsalya.evist.Model.EventAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        eventList = new ArrayList<>();
        adapter = new EventAdapter(this, eventList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareEvents();
    }

    //adding few albums for testing
    private void prepareEvents(){
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
    }

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
