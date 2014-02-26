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
    private final String BASE_URL = "https://dl.dropboxusercontent.com/u/27136243/RiStaurants/json/restaurants_list.json";

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

        // request a volley queue
        RequestQueue queue = SingletonVolley.getRequestQueue();

        // json to request
        JsonObjectRequest request = new JsonObjectRequest(BASE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                // instantiate ListView adapter
                mAdapter = new RestaurantsAdapter(getActivity(), jsonObject);

                // set adapter to ListView
                mLvContent.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // log errors
                VolleyLog.e("Volley Error @RestaurantsFrag: " + error.getMessage(), error.getMessage());
            }
        }
        );

        // add request to queue
        queue.add(request);

        // refresh action bar menu
        setHasOptionsMenu(true);

        // get data from database
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("restaurants");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> scoreList, ParseException e) {
                    if (e == null) {
                        for (int i = 0; i < scoreList.size(); i++) {
                            Log.d("Restaurant Name: ", scoreList.get(i).getString("restaurantName"));
                        }
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_restaurants, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // check which item in the menu was clicked
        switch (item.getItemId()) {
            case R.id.menu_restaurant_menu:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
