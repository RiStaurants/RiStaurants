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
import android.view.MenuItem;

import com.ristaurants.ristaurants.misc.HelperClass;

/**
 *
 */
public class RestaurantMenuActivity extends FragmentActivity implements ActionBar.TabListener {
    // instance variables
    private ActionBar mActionBar;
    private ViewPager mViewPager;
	private String mRestaurantMenuUrl;
	private String mRestaurantName;
    private String[] mTabTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (getIntent().getExtras() != null) {
			// get data from previous activity
            mRestaurantMenuUrl = getIntent().getExtras().getString("jsonObjectUrl");
			mRestaurantName = getIntent().getExtras().getString("restaurantName");
        }

        // instantiate
        mViewPager = (ViewPager) findViewById(R.id.vp_restaurants_menus);
		mViewPager.setOffscreenPageLimit(1);
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
		mTabTitles = getResources().getStringArray(R.array.restaurants_menu_tab_titles);
        breakfastTab.setText(mTabTitles[0]);
        lunchTab.setText(mTabTitles[1]);
        dinnerTab.setText(mTabTitles[2]);
        dessertTab.setText(mTabTitles[3]);
        beverageTab.setText(mTabTitles[4]);

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
            // create instance of fragment
            RestaurantMenuFrag mFrag = new RestaurantMenuFrag();

            // create bundle to pass data
            Bundle args = new Bundle();
            args.putString("mRestaurantMenuUrl", mRestaurantMenuUrl);
            args.putString("mRestaurantName", mRestaurantName);
            args.putString("mMenuType", mTabTitles[item]);
            mFrag.setArguments(args);

            // return fragment and pass data
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
