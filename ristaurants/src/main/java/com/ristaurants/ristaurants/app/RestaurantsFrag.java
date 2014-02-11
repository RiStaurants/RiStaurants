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
import android.location.*;
import android.content.*;
import android.graphics.drawable.*;

/**
 * Restaurants Fragment
 */
public class RestaurantsFrag extends Fragment {
	// instance variables
	private ListView mLvContent;
	private RestaurantsAdapter mAdapter;
	private final String BASE_URL = "https://dl.dropboxusercontent.com/u/27136243/RiStaurants/json/restaurants-view.json";

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
	}
}
