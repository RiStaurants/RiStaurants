package com.ristaurants.ristaurants.app;

import android.app.*;
import android.os.*;
import android.util.*;
import android.widget.*;

import com.parse.*;
import com.ristaurants.ristaurants.adapters.RestaurantsAdapter;
import com.ristaurants.ristaurants.misc.*;

import java.util.*;

import android.view.*;

public class Cuisine extends Activity {
    // instance variables
    private RestaurantsAdapter mAdapter;
    private ListView mLvContent;
    private String mActionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_restaurants);

        // instantiate Parse Database
        Parse.initialize(Cuisine.this, "WB3Th85cP3viS7jJ5zkXzkZ2MTsFagIg0AKQeBpQ", "EGZKA60G8Iy4vVCPPvBDjn2XoeBbqQ1rtWReRvRh");

        // get data from intent
        if (getIntent().getExtras() != null) {
            mActionBarTitle = getIntent().getExtras().getString("mCuisine");
        }

        // set action bar background color
        HelperClass.setActionBarBackground(this, R.color.restaurants_bg);

        // set-up action bar
        getActionBar().setTitle(HelperClass.setActionbarTitle(this, mActionBarTitle));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.ic_action_navigation_previous_item);

        // get data from database
        ParseQuery<ParseObject> parseObject = ParseQuery.getQuery("Cuisine");
        parseObject.orderByAscending("cuisineCategory");
		parseObject.include("restaurantPointer");
        parseObject.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    // create new list with only the needed cuisine
                    List<ParseObject> tempList = new ArrayList<ParseObject>();

                    // extract restaurant object
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getString("cuisineCategory").equals(mActionBarTitle)) {
                            ParseObject restaurant = new ParseObject("Cuisine");
                            restaurant = list.get(i).getParseObject("restaurantPointer");
                            tempList.add(restaurant);
                        }
                    }

                    // set adapter
                    mAdapter = new RestaurantsAdapter(Cuisine.this, tempList);

                    // set list view
                    mLvContent = (ListView) findViewById(R.id.lv_content);
                    mLvContent.setAdapter(mAdapter);
                } else {
                    Log.e("ParseObject", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // go back to previous screens
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // set activity animation
        this.overridePendingTransition(R.anim.anim_null, R.anim.anim_slide_out_right);
    }
}
