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

/**
 * Restaurants Fragment
 */
public class RestaurantsFrag extends Fragment {
	// instance variables
    private static final String API_KEY = "&key=AIzaSyB48wXIVLTIamI2z1eixGyXkEzp9SVeizA";
	private ListView mLvContent;
	private RestaurantsAdapter mAdapter;
	//private final String BASE_URL = "https://dl.dropboxusercontent.com/u/27136243/RiStaurants/json/restaurants-view.json";
    private final String BASE_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=best+restaurants+in+providence+rhode+island&sensor=false&types=restaurant&location=41.8238889,-71.4133333&radius=500" + API_KEY;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// inflate layout
		return inflater.inflate(R.layout.frag_restaurants, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// instantiate views
		mLvContent = (ListView) getActivity().findViewById(R.id.lv_content);
		
		// request a volley queue
		RequestQueue queue = SingletonVolley.getRequestQueue();
		
		// json to request
		JsonObjectRequest request = new JsonObjectRequest(BASE_URL, null, new Response.Listener<JSONObject>(){
				@Override
				public void onResponse(JSONObject jsonObject) {
					// instantiate ListView adapter
					mAdapter = new RestaurantsAdapter(getActivity(), jsonObject, API_KEY);

					// set adapter to ListView
					mLvContent.setAdapter(mAdapter);
				}
			}, new Response.ErrorListener(){

				@Override
				public void onErrorResponse(VolleyError error) {
					// log errors
					VolleyLog.e("Volley Error @RestaurantsFrag: ", error.getMessage());
				}
			}
		);
		
		// add request to queue
		queue.add(request);
	}
	
	private long[] getGpsCoordinates(){
		// variables
		LocationManager locMang;
		long[] coordinates = new long[2];
		
		//
		locMang = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		locMang.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, new LocationListener(){

				@Override
				public void onLocationChanged(Location p1) {
					// TODO: Implement this method
				}

				@Override
				public void onStatusChanged(String p1, int p2, Bundle p3) {
					// TODO: Implement this method
				}

				@Override
				public void onProviderEnabled(String p1) {
					// TODO: Implement this method
				}

				@Override
				public void onProviderDisabled(String p1) {
					// TODO: Implement this method
				}
			});
		
		
		// return
		return coordinates;
	}
	
}
