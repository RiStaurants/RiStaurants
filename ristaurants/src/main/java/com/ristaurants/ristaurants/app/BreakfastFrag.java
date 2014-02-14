package com.ristaurants.ristaurants.app;

import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.ristaurants.ristaurants.misc.*;
import org.json.*;
import com.ristaurants.ristaurants.adapters.*;

/**
 * Fragment to display the breakfast menu of the restaurant
 */
public class BreakfastFrag extends Fragment {
    // instance variables
	private ListView mLvContent;
	private BreakfastAdapter mAdapter;
    private JSONObject mRestaurantMenu;
	private String mRestaurantMenuUrl;
	private String mRestaurantName;

    /**
     * Constructor
     *
     * @param restaurantMenuUrl The restaurant menu in JSON file url.
	 * @param restaurantName The restaurant name.
     */
    public BreakfastFrag(String restaurantMenuUrl, String restaurantName) {
        mRestaurantMenuUrl = restaurantMenuUrl;
		mRestaurantName = restaurantName;
    }
	
	public BreakfastFrag(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate layout
        return inflater.inflate(R.layout.frag_breakfast, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
		
		// restaurant name text view
		final TextView mTvRestaurantName = (TextView) getActivity().findViewById(R.id.tv_restaurant_name);

		// menu list view
		mLvContent = (ListView) getActivity().findViewById(R.id.lv_content);
		
		if (savedInstanceState != null) {
			try {
				// restore data on device rotation
				mRestaurantMenu = new JSONObject(savedInstanceState.getString("mRestaurantMenu"));
				mRestaurantName = savedInstanceState.getString("mRestaurantName");
				
				// list view adapter 
				mAdapter = new BreakfastAdapter(getActivity(), mRestaurantMenu);
				mLvContent.setAdapter(mAdapter);
			} catch (JSONException e) {
				Log.e("Error @BreakfastFrag", e.getMessage());
			}
		} else {
			// request a volley queue
            RequestQueue queue = SingletonVolley.getRequestQueue();

            // json to request
            JsonObjectRequest request = new JsonObjectRequest(mRestaurantMenuUrl, null, new Response.Listener<JSONObject>(){
					@Override
					public void onResponse(JSONObject jsonObject) {
						// get the json file containing the menu data
						mRestaurantMenu = jsonObject;
						
						// list view adapter 
						mAdapter = new BreakfastAdapter(getActivity(), mRestaurantMenu);
						mLvContent.setAdapter(mAdapter);
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
		}
		
		// display restaurant name
		mTvRestaurantName.setText(mRestaurantName);
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// save instances
		outState.putString("mRestaurantMenu", mRestaurantMenu.toString());
		outState.putString("mRestaurantName", mRestaurantName);
	}
}
