package com.ristaurants.ristaurants.app;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ristaurants.ristaurants.adapters.RestaurantsAdapter;
import com.ristaurants.ristaurants.misc.HelperClass;
import com.ristaurants.ristaurants.misc.SingletonVolley;

import org.json.JSONObject;

/**
 *
 */
public class RestaurantMenuActivity extends FragmentActivity implements ActionBar.TabListener {
    // instance variables
    private ActionBar mActionBar;
    private ViewPager mViewPager;
    private String mRestaurantMenuUrl;
    private JSONObject mJsonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (getIntent().getExtras() != null) {
            mRestaurantMenuUrl = getIntent().getExtras().getString("jsonObjectUrl");

            // request a volley queue
            RequestQueue queue = SingletonVolley.getRequestQueue();

            // json to request
            JsonObjectRequest request = new JsonObjectRequest(mRestaurantMenuUrl, null, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject jsonObject) {
                    //
                    mJsonMenu = jsonObject;

                    Log.d("JSON MENU", mJsonMenu.toString());
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    // log errors
                    VolleyLog.e("Volley Error @RestaurantsMenuActivity: " + error.getMessage(), error.getMessage());
                }
            }
            );

            // add request to queue
            queue.add(request);
        }

        // instantiate
        mViewPager = (ViewPager) findViewById(R.id.vp_restaurants_menus);
        mViewPager.setAdapter(new MenusPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == ViewPager.SCROLL_STATE_IDLE){

                }

                if (i == ViewPager.SCROLL_STATE_DRAGGING) {

                }

                if (i == ViewPager.SCROLL_STATE_SETTLING) {

                }

            }
        });

        // set action bar background color
        HelperClass.setActionBarBackground(this, R.color.menus_bg);

        // set-up action bar
        getActionBar().setTitle(HelperClass.setActionbarTitle(this, getResources().getString(R.string.ab_title_menu)));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // action bar instance
        mActionBar = getActionBar();

        // change tab background colors
        mActionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.menus_tab_bg)));

        // create action bar tabs
        ActionBar.Tab breakfastTab = mActionBar.newTab();
        ActionBar.Tab lunchTab = mActionBar.newTab();
        ActionBar.Tab dinnerTab = mActionBar.newTab();
        ActionBar.Tab dessertTab = mActionBar.newTab();
        ActionBar.Tab beverageTab = mActionBar.newTab();

        // set tab title
		String[] tabTitles = getResources().getStringArray(R.array.restaurants_menu_tab_titles);
        breakfastTab.setText(tabTitles[0]);
        lunchTab.setText(tabTitles[1]);
        dinnerTab.setText(tabTitles[2]);
        dessertTab.setText(tabTitles[3]);
        beverageTab.setText(tabTitles[4]);

        // set tab listener
        breakfastTab.setTabListener(this);
        lunchTab.setTabListener(this);
        dinnerTab.setTabListener(this);
        dessertTab.setTabListener(this);
        beverageTab.setTabListener(this);

        // add tabs to action bar
        mActionBar.addTab(breakfastTab);
        mActionBar.addTab(lunchTab);
        mActionBar.addTab(dinnerTab);
        mActionBar.addTab(dessertTab);
        mActionBar.addTab(beverageTab);

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    private class MenusPagerAdapter extends FragmentPagerAdapter {

        public MenusPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int item) {
            Fragment mFrag = null;

            switch (item) {
                case 0:
                    mFrag = new BreakfastFrag();
                    break;
                case 1:
                    mFrag = new LunchFrag();
                    break;
                case 2:
                    mFrag = new DinnerFrag();
                    break;
                case 3:
                    mFrag = new DessertsFrag();
                    break;
                case 4:
                    mFrag = new BeverageFrag();
                    break;
            }

            return mFrag;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
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
        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}
