package com.ristaurants.ristaurants.app;

import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.view.*;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.ristaurants.ristaurants.misc.*;

import org.json.*;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.Arrays;
import java.util.Collections;

/**
 *
 */
public class RestaurantMenuActivity extends FragmentActivity {
    // instance variables
    private ViewPager mViewPager;
    private JSONObject mRestaurantMenu;
    private String mRestaurantMenuUrl;
    private String[] mPagerTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // instantiate view pager after getting json
        mViewPager = (ViewPager) findViewById(R.id.vp_restaurants_menus);

        if (getIntent().getExtras() != null) {
            // get data from previous activity
            mRestaurantMenuUrl = getIntent().getExtras().getString("jsonObjectUrl");
        }

        // check saveInstanceState
        if (savedInstanceState == null) {
            // request a volley queue
            RequestQueue queue = SingletonVolley.getRequestQueue();

            // json to request
            JsonObjectRequest request = new JsonObjectRequest(mRestaurantMenuUrl, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject jsonObject) {
                    // get the json file containing the menu data
                    mRestaurantMenu = jsonObject;

                    try {
                        // get menu categories
                        getMenuCategories(mRestaurantMenu.getJSONObject("menus").names());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // log errors
                    VolleyLog.e("Volley Error: " + error.getMessage(), error.getMessage());
                }
            }
            );

            // add request to queue
            queue.add(request);

        } else {
            try {
                // get json from saved instance
                mRestaurantMenu = new JSONObject(savedInstanceState.getString("mRestaurantMenu", null));

                // get menu categories
                getMenuCategories(mRestaurantMenu.getJSONObject("menus").names());

            } catch (JSONException e) {
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
     * @param categories The JSONArray containing all categories
     */
    private void getMenuCategories(JSONArray categories) {
        try {
            // set categories size
            mPagerTitles = new String[categories.length()];

            // convert json array to normal array of strings
            for (int i = 0; i < categories.length(); i++) {
                mPagerTitles[i] = categories.getString(i);
            }

            // reverse the array
            Collections.reverse(Arrays.asList(mPagerTitles));

            // set view pager adapter
            mViewPager.setAdapter(new MenusPagerAdapter(getSupportFragmentManager()));
        } catch (JSONException e) {
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
