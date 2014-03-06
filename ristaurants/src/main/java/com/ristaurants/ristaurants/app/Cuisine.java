package com.ristaurants.ristaurants.app;

import android.app.*;
import android.os.*;
import android.util.*;
import android.widget.*;

import com.parse.*;
import com.ristaurants.ristaurants.misc.*;

import java.util.*;

import android.view.*;

public class Cuisine extends Activity {
    // instance variables
    private String mActionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // instantiate Parse Database
        Parse.initialize(this, "WB3Th85cP3viS7jJ5zkXzkZ2MTsFagIg0AKQeBpQ", "EGZKA60G8Iy4vVCPPvBDjn2XoeBbqQ1rtWReRvRh");

        // get data from intent
        if (getIntent().getExtras() != null) {
            mActionBarTitle = getIntent().getExtras().getString("mCuisine");
        }

        // set action bar background color
        HelperClass.setActionBarBackground(this, R.color.cuisine_bg);

        // set-up action bar
        getActionBar().setTitle(HelperClass.setActionbarTitle(this, mActionBarTitle));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.ic_action_navigation_previous_item);

        // get data from database
        ParseQuery<ParseObject> parseObject = ParseQuery.getQuery("Cuisine");
        parseObject.orderByAscending("cuisineCategory");
        parseObject.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    // get cuisine pointer id list
                    ArrayList<String> pointerID = new ArrayList<String>();

                    // extract pointers needed
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getString("cuisineCategory").equals(mActionBarTitle)) {
                            pointerID.add(list.get(i).getString("restaurantPointer"));
                        }
                    }

                    Toast.makeText(Cuisine.this, pointerID.get(0), Toast.LENGTH_SHORT).show();

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
