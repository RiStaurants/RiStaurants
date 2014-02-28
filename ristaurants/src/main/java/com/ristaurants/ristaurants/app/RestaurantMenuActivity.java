package com.ristaurants.ristaurants.app;

import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.util.Log;
import android.view.*;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ristaurants.ristaurants.adapters.RestaurantsAdapter;
import com.ristaurants.ristaurants.misc.*;

import org.json.*;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class RestaurantMenuActivity extends FragmentActivity {
    // instance variables
    private ViewPager mViewPager;
    private List<ParseObject> mParseObjectList;
    private String[] mPagerTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // instantiate Parse Database
        Parse.initialize(this, "WB3Th85cP3viS7jJ5zkXzkZ2MTsFagIg0AKQeBpQ", "EGZKA60G8Iy4vVCPPvBDjn2XoeBbqQ1rtWReRvRh");

        // instantiate view pager after getting json
        mViewPager = (ViewPager) findViewById(R.id.vp_restaurants_menus);

        if (getIntent().getExtras() != null) {
            // get data from database
            ParseQuery<ParseObject> parseObject = ParseQuery.getQuery(getIntent().getExtras().getString("menuClassName"));
            parseObject.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> parseObjectList, ParseException e) {
                    if (e == null) {
                        // get response
                        mParseObjectList = parseObjectList;

                        // get menu categories
                        getMenuCategories(parseObjectList.size());
                    } else {
                        Log.e("ParseObject", "Error: " + e.getMessage());
                    }
                }
            });
        }

        // set action bar background color
        HelperClass.setActionBarBackground(this, R.color.menus_bg);

        // set-up action bar
        getActionBar().setTitle(HelperClass.setActionbarTitle(this, getResources().getString(R.string.ab_title_menu)));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Get restaurant menu categories. It also sets the ViewPager adapter and PagerTitleStrip
     *
     * @param categoriesCount The amount of menu categories
     */
    private void getMenuCategories(int categoriesCount) {
        try {
            // local variables
            ArrayList<String> categories = new ArrayList<String>();
            int indexTracker = 1;

            // add first item
            categories.add(mParseObjectList.get(0).getString("dishCategories"));

            // create non-duplicate category items
            for (int i = 1; i < categoriesCount; i++) {
                if (!mParseObjectList.get(i).getString("dishCategories").equals(categories.get(indexTracker - 1))) {
                    categories.add(mParseObjectList.get(i).getString("dishCategories"));
                    indexTracker++;
                }
            }

            // convert ArrayList to array and set PagerTitleStrip titles
            mPagerTitles = categories.toArray(new String[categories.size()]);

            // set view pager adapter
            mViewPager.setAdapter(new MenusPagerAdapter(getSupportFragmentManager()));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // set activity animation
        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    /**
     * ViewPager Adapter
     */
    private class MenusPagerAdapter extends FragmentStatePagerAdapter {

        public MenusPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int item) {
            // create instance of fragment
            return new RestaurantMenuFrag().newInstance(mParseObjectList, mPagerTitles[item]);
        }

        @Override
        public int getCount() {
            return mPagerTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPagerTitles[position].toUpperCase();
        }

    }
}
