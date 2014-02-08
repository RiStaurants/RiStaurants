package com.ristaurants.ristaurants.app;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.preference.PreferenceManager;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.ristaurants.ristaurants.adapters.NaviDrawerLeftAdapter;
import com.ristaurants.ristaurants.views.ActionBarFont;

import android.view.*;

public class BaseActivity extends FragmentActivity {
	// instance variables
	private static final String PREF_USER_DRAWER_LEARNED = "drawer_learned";
	private static final String STATE_SELECTED_POSITION = "drawer_selected_position";
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mLvDrawer;
	private Fragment mFrag;
    private String[] mDrawerTitles;
	private boolean mUserLearnedDrawer;
	private int mCurrentSelectedPosition;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);

        // create custom font for action bar
        SpannableString customActionBarFont = new SpannableString(getResources().getString(R.string.ab_title_restaurants));
        customActionBarFont.setSpan(new ActionBarFont(this, "Bender-Solid.otf"), 0, customActionBarFont.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		// set-up action bar
        getActionBar().setTitle(customActionBarFont);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

        // get the preferences for the activity.
        // check if the user is aware of the navigation drawer.
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserLearnedDrawer = mPreferences.getBoolean(PREF_USER_DRAWER_LEARNED, false);

        // set selected item to the latest know position
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }

        // get drawer icons
        Drawable[] mDrawerIcons = {
                getResources().getDrawable(R.drawable.restaurant_icon),
                getResources().getDrawable(R.drawable.dishes_icon),
                getResources().getDrawable(R.drawable.cuisine_icon),
                getResources().getDrawable(R.drawable.settings_icon)
        };

        // get drawer title list
        mDrawerTitles = getResources().getStringArray(R.array.drawer_title_list);

        // instantiate views
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_drawer);
        mLvDrawer = (ListView) findViewById(R.id.lv_drawer_left);

        // set navigation menu adapter
        mLvDrawer.setAdapter(new NaviDrawerLeftAdapter(this, mDrawerIcons, mDrawerTitles));
        //mDrawerLayout.openDrawer(mLvDrawer);
        mLvDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                // refresh action bar menu
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                if (!mUserLearnedDrawer) {
                    // the user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this);
                    sp.edit().putBoolean(PREF_USER_DRAWER_LEARNED, true).commit();
                }

                // refresh action bar menu
                invalidateOptionsMenu();
            }
        };

        // set action bar home icon
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // display default selected item (navigation drawer)
        if (savedInstanceState == null) {
            selectItem(mCurrentSelectedPosition);
        }
	}

    private void selectItem(int position) {
        // navigation menu item click
        switch (position) {
            case 0:
                // start Restaurants Fragment
                mFrag = new RestaurantsFrag();
                break;
				
			case 1:
				// start dishes Fragment
				mFrag = new DishesFrag();
				break;
				
			case 2:
                // start Cuisine Fragment
                mFrag = new CuisineFrag();
                break;

			case 3:
				// start Settings Fragment
				mFrag = new SettingsFrag();
				break;
        }

        // switch fragment with animation
        FragmentTransaction mFragTrans = this.getSupportFragmentManager().beginTransaction();
        mFragTrans.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        mFragTrans.replace(R.id.fl_drawer, mFrag);
        mFragTrans.commit();

        // update selected item
        mLvDrawer.setItemChecked(position, true);

        // close navigation menu
		closeLeftDrawer();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // if the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //return super.onPrepareOptionsMenu(menu);
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    /**
     * Check if the navigation drawer is open.
     *
     * @return Returns true if the navigation drawer is open, otherwise returns faults.
     */
    public boolean isLeftDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.START);
    }

    /**
     * Open the navigation drawer.
     */
    public void openLeftDrawer() {
        mDrawerLayout.openDrawer(Gravity.START);
    }

    /**
     * Close the navigation drawer.
     */
    public void closeLeftDrawer() {
        mDrawerLayout.closeDrawer(Gravity.START);
    }

    /**
     * Check if the user already learned the navigation drawer.
     *
     * @return Return true if the user already the navigation drawer.
     */
    public boolean getUserLearnedDrawer() {
        return mUserLearnedDrawer;
    }
}
