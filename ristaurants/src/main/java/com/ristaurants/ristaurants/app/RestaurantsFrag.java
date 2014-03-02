package com.ristaurants.ristaurants.app;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.os.*;
import android.widget.*;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ristaurants.ristaurants.misc.*;

import org.json.*;

import com.ristaurants.ristaurants.adapters.*;

import android.content.*;

import java.util.List;

/**
 * Restaurants Fragment
 */
public class RestaurantsFrag extends Fragment {
    // instance variables
    private ListView mLvContent;
    private RestaurantsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate layout
        return inflater.inflate(R.layout.frag_restaurants, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // instantiate Parse Database
        Parse.initialize(getActivity(), "WB3Th85cP3viS7jJ5zkXzkZ2MTsFagIg0AKQeBpQ", "EGZKA60G8Iy4vVCPPvBDjn2XoeBbqQ1rtWReRvRh");

        // set action bar background
        HelperClass.setActionBarBackground(getActivity(), R.color.restaurants_bg);

        // instantiate views
        mLvContent = (ListView) getActivity().findViewById(R.id.lv_content);

		// get data from database
		ParseQuery<ParseObject> parseObject = ParseQuery.getQuery("RestaurantList");
        parseObject.orderByDescending("rate");
		parseObject.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> restaurantList, ParseException e) {
					if (e == null) {
						// instantiate ListView adapter
						mAdapter = new RestaurantsAdapter(getActivity(), restaurantList);

						// set adapter to ListView
						mLvContent.setAdapter(mAdapter);
					} else {
						Log.e("ParseObject", "Error: " + e.getMessage());
					}
				}
			});

        // refresh action bar menu
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_restaurants, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // check which item in the menu was clicked
        switch (item.getItemId()){
            case R.id.menu_restaurants_random:

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

