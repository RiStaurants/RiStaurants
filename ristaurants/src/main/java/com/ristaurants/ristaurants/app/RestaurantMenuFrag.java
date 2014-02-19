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
public class RestaurantMenuFrag extends Fragment {
    // instance variables
	private ListView mLvContent;
	private RestaurantMenuAdapter mAdapter;
    private JSONObject mRestaurantMenu;
	private static String mRestaurantMenuUrl;
	private static String mRestaurantName;
	private static String mMenuType;

	//public RestaurantMenuFrag(){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate layout
        return inflater.inflate(R.layout.frag_restaurant_menu, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

		// restaurant name text view
		final TextView mTvRestaurantName = (TextView) getActivity().findViewById(R.id.tv_restaurant_name);

		// menu list view
		mLvContent = (ListView) getActivity().findViewById(R.id.lv_content);

		mRestaurantMenuUrl = getArguments().getString("mRestaurantMenuUrl", null);
		mRestaurantName = getArguments().getString("mRestaurantName", null);
		mMenuType = getArguments().getString("mMenuType", null);

		// request a volley queue
		RequestQueue queue = SingletonVolley.getRequestQueue();

		// json to request
		JsonObjectRequest request = new JsonObjectRequest(mRestaurantMenuUrl, null, new Response.Listener<JSONObject>(){
				@Override
				public void onResponse(JSONObject jsonObject) {
					// get the json file containing the menu data
					mRestaurantMenu = jsonObject;

					// list view adapter 
					mAdapter = new RestaurantMenuAdapter(getActivity(), mRestaurantMenu, mMenuType);
					mLvContent.setAdapter(mAdapter);

					Log.i("@@@@@@@", mMenuType);
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

		// display restaurant name
		mTvRestaurantName.setText(mMenuType);
    }

	public static RestaurantMenuFrag newInstance(String restaurantMenuUrl, String restaurantName, String menuType) {
		RestaurantMenuFrag mFrag = new RestaurantMenuFrag();

		Bundle args = new Bundle();
		args.putString("mRestaurantMenuUrl", restaurantMenuUrl);
		args.putString("mRestaurantName", restaurantName);
		args.putString("mMenuType", menuType);
		mFrag.setArguments(args);

		return mFrag;
	}
}
