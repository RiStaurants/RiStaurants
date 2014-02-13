package com.ristaurants.ristaurants.app;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.ristaurants.ristaurants.misc.HelperClass;

/**
 *
 */
public class MenuActivity extends FragmentActivity implements ActionBar.TabListener {
    // instance variables
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
        breakfastTab.setText("breakfast");
        lunchTab.setText("lunch");
        dinnerTab.setText("dinner");
        dessertTab.setText("dessert");
        beverageTab.setText("beverage");

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
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        switch (item.getItemId()) {
            case android.R.id.home:
                // go back to previous screens
                onBackPressed();

                // set activity animation
                this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
