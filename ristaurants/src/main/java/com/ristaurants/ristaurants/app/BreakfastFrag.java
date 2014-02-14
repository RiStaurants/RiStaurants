package com.ristaurants.ristaurants.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ristaurants.ristaurants.misc.HelperClass;

import org.json.JSONObject;

/**
 * Fragment to display the breakfast menu of the restaurant
 */
public class BreakfastFrag extends Fragment {
    // instance variables
    private JSONObject mRestaurantMenu;

    /**
     * Constructor
     *
     * @param restaurantMenu The restaurants menu in JSON format.
     */
    public BreakfastFrag(JSONObject restaurantMenu) {
        mRestaurantMenu = restaurantMenu;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate layout
        return inflater.inflate(R.layout.frag_breakfast, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
