package com.ristaurants.ristaurants.app;

import android.os.*;
import android.support.v4.app.*;
import android.util.Log;
import android.view.*;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ristaurants.ristaurants.adapters.DishesAdapter;
import com.ristaurants.ristaurants.adapters.RestaurantMenuAdapter;
import com.ristaurants.ristaurants.adapters.RestaurantsAdapter;
import com.ristaurants.ristaurants.misc.*;

import java.util.List;

public class DishesFrag extends Fragment {
    // instance variables
    private DishesAdapter mAdapter;
    private ListView mLvContent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// inflate layout
		View view = inflater.inflate(R.layout.frag_dishes, null);

        // instantiate views
        mLvContent = (ListView) view.findViewById(R.id.lv_content);

        // make network call
        makeNetworkCall("RestaurantsMenus");

        // return view
        return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        // refresh action bar menu
        setHasOptionsMenu(true);
	}

    /**
     * Get the list of restaurants from the database.
     *
     * @param className The class name to search for in the database.
     */
    private void makeNetworkCall(String className) {
        // get data from database
        final ParseQuery<ParseObject> parseObject = ParseQuery.getQuery(className);
        parseObject.orderByDescending("rate");
        parseObject.include("restaurantPointer");
        parseObject.setLimit(50);
        parseObject.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjectList, ParseException e) {
                if (e == null) {
                    // instantiate ListView adapter
                    mAdapter = new DishesAdapter(getActivity(), parseObjectList);

                    // set adapter to ListView
                    mLvContent.setAdapter(mAdapter);

                } else {
                    Log.e("ParseObject", "Error: " + e.getMessage());
                }
            }
        });
    }
}
