package com.ristaurants.ristaurants.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ristaurants.ristaurants.adapters.RestaurantsAdapter;
import com.ristaurants.ristaurants.misc.HelperClass;

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
        View view = inflater.inflate(R.layout.frag_restaurants, null);

        // set-up action bar
        getActivity().getActionBar().setTitle(HelperClass.setActionbarTitle(getActivity(), getResources().getString(R.string.ab_title_restaurants)));

        // instantiate views
        mLvContent = (ListView) view.findViewById(R.id.lv_content);

        // get data from database
        makeNetworkCall("RestaurantList");

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
        ParseQuery<ParseObject> parseObject = ParseQuery.getQuery(className);
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
    }
}

