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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class RestaurantMenuActivity extends FragmentActivity {
    // instance variables
    private ViewPager mViewPager;
    private JSONObject mRestaurantMenu;
    private String mRestaurantMenuString;
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
            try {
                // get menu categories
                //getMenuCategories(mRestaurantMenu.getJSONObject("menus").names());

                // get data from database
                ParseQuery<ParseObject> parseObject = ParseQuery.getQuery(getIntent().getExtras().getString("menuClassName"));
                parseObject.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null) {
                            // get menu categories
                            getMenuCategories(list.get(0).getInt("dishCategoriesCount"));

                            Toast.makeText(RestaurantMenuActivity.this, list.get(0).getObjectId(), Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("ParseObject", "Error: " + e.getMessage());
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        // set action bar background color
        HelperClass.setActionBarBackground(this, R.color.menus_bg);

        // set-up action bar
        getActionBar().setTitle(HelperClass.setActionbarTitle(this, getResources().getString(R.string.ab_title_menu)));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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

    /**
     * Get restaurant menu categories. It also sets the ViewPager adapter and PagerTitleStrip
     *
     * @param categoriesCount The amount of menu categories
     */
    private void getMenuCategories(int categoriesCount) {
        try {
            // set categories size
            mPagerTitles = new String[categoriesCount];

            // convert json array to normal array of strings
            for (int i = 0; i < categoriesCount; i++) {
                //mPagerTitles[i] = categories.getString(i);
            }

            // reverse the array
            Collections.reverse(Arrays.asList(mPagerTitles));

            // set view pager adapter
            mViewPager.setAdapter(new MenusPagerAdapter(getSupportFragmentManager()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save state
        outState.putString("mRestaurantMenu", mRestaurantMenu.toString());
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
            return new RestaurantMenuFrag().newInstance(mRestaurantMenu, mPagerTitles[item]);
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
