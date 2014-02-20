package com.ristaurants.ristaurants.app;

import android.app.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.view.*;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.ristaurants.ristaurants.adapters.*;
import com.ristaurants.ristaurants.misc.*;
import org.json.*;

import android.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 *
 */
public class RestaurantMenuActivity extends FragmentActivity {
    // instance variables
    private ViewPager mViewPager;
	private JSONObject mRestaurantMenu;
	private String mRestaurantMenuUrl;
	private String mRestaurantName;
    private String[] mTabTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
		
		// set tab title
		mTabTitles = getResources().getStringArray(R.array.restaurants_menu_tab_titles);

        if (getIntent().getExtras() != null) {
			// get data from previous activity
            mRestaurantMenuUrl = getIntent().getExtras().getString("jsonObjectUrl");
			mRestaurantName = getIntent().getExtras().getString("restaurantName");
        }

		// check saveInstanceState
		if (savedInstanceState == null) {
			// request a volley queue
			RequestQueue queue = SingletonVolley.getRequestQueue();

			// json to request
			JsonObjectRequest request = new JsonObjectRequest(mRestaurantMenuUrl, null, new Response.Listener<JSONObject>(){

					@Override
					public void onResponse(JSONObject jsonObject) {
						// get the json file containing the menu data
						mRestaurantMenu = jsonObject;
						
						// instantiate view pager after getting json
						mViewPager = (ViewPager) findViewById(R.id.vp_restaurants_menus);
						mViewPager.setAdapter(new MenusPagerAdapter(getSupportFragmentManager()));
					}
				}, new Response.ErrorListener(){

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
				
				// instantiate view pager after getting json
				mViewPager = (ViewPager) findViewById(R.id.vp_restaurants_menus);
				mViewPager.setAdapter(new MenusPagerAdapter(getSupportFragmentManager()));
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

	/** ViewPager Adapter */
    private class MenusPagerAdapter extends FragmentStatePagerAdapter {

        public MenusPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int item) {
            // create instance of fragment
            Fragment mFrag = new RestaurantMenuFrag().newInstance(mRestaurantMenu, mTabTitles[item]);

            return mFrag;
        }

        @Override
        public int getCount() {
            return mTabTitles.length;
        }
		
		@Override
		public CharSequence getPageTitle(int position) {
			return mTabTitles[position].toUpperCase();
		}

    }
}
