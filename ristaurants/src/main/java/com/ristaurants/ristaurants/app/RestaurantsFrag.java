package com.ristaurants.ristaurants.app;

import android.support.v4.app.Fragment;
import android.view.*;
import android.os.*;
import android.widget.*;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.ristaurants.ristaurants.misc.*;
import org.json.*;
import com.ristaurants.ristaurants.adapters.*;

import android.content.*;

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
		
		// set action bar background
		HelperClass.setActionBarBackground(getActivity(), R.color.restaurants_bg);
		
		// instantiate views
		mLvContent = (ListView) getActivity().findViewById(R.id.lv_content);

		// request a volley queue
		RequestQueue queue = SingletonVolley.getRequestQueue();
		
		// json to request
		JsonObjectRequest request = new JsonObjectRequest(BASE_URL, null, new Response.Listener<JSONObject>(){
				@Override
				public void onResponse(JSONObject jsonObject) {
					// instantiate ListView adapter
					mAdapter = new RestaurantsAdapter(getActivity(), jsonObject);

					// set adapter to ListView
					mLvContent.setAdapter(mAdapter);
				}
			}, new Response.ErrorListener(){

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
            case R.id.menu_restaurant_menu:
                // start the menu activity
                startActivity(new Intent(getActivity(), RestaurantMenuActivity.class));

                // set activity animation
                getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
