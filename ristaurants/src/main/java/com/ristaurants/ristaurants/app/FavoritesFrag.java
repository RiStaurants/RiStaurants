package com.ristaurants.ristaurants.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.ristaurants.ristaurants.adapters.RestaurantsAdapter;
import com.ristaurants.ristaurants.misc.HelperClass;

import java.util.List;

/**
 * Favorites fragment
 */
public class FavoritesFrag extends Fragment {
    // instance variables
    private RestaurantsAdapter mAdapter;
    private ListView mLvContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        final View view = inflater.inflate(R.layout.frag_restaurants, null);

        // set-up action bar
        getActivity().getActionBar().setTitle(HelperClass.setActionbarTitle(getActivity(), getResources().getString(R.string.ab_title_favorites)));

        // check if user is logged in
        if (ParseUser.getCurrentUser() != null) {
            // get data from database
            ParseRelation<ParseObject> relation = ParseUser.getCurrentUser().getRelation("favorites");
            relation.getQuery().orderByDescending("rate");
            relation.getQuery().findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> results, ParseException e) {
                    if (e == null) {
                        // set adapter
                        mAdapter = new RestaurantsAdapter(getActivity(), results);

                        // set list view
                        mLvContent = (ListView) view.findViewById(R.id.lv_content);
                        mLvContent.setAdapter(mAdapter);
                    } else {
                        // There was an error
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        // return
        return view;
    }
}
