package com.ristaurants.ristaurants.app;

import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.view.*;
import android.widget.*;
import com.ristaurants.ristaurants.adapters.*;
import com.ristaurants.ristaurants.app.*;
import org.json.*;

/**
 * Fragment to display the breakfast menu of the restaurant
 */
public class RestaurantMenuFrag extends Fragment {
    // instance variables
	private ListView mLvContent;
	private RestaurantMenuAdapter mAdapter;
    private JSONObject mRestaurantMenu;
	private String mMenuType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_restaurant_menu, container, false);
		
		// restaurant name text view
		TextView mTvRestaurantName = (TextView) view.findViewById(R.id.tv_restaurant_name);
		mTvRestaurantName.setText(mMenuType);
		
		// setup adapter
		mAdapter = new RestaurantMenuAdapter(getActivity(), mRestaurantMenu, mMenuType);

		// menu list view
		mLvContent = (ListView) view.findViewById(R.id.lv_content);
		mLvContent.setAdapter(mAdapter);
		
		
		PagerTitleStrip _Title = (PagerTitleStrip) getActivity().findViewById(R.id.pager_title_strip);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Bender-Solid.otf"); 
		for (int counter = 0 ; counter<_Title.getChildCount(); counter++) {

			if (_Title.getChildAt(counter) instanceof TextView) {
				((TextView)_Title.getChildAt(counter)).setTypeface(font);
				((TextView)_Title.getChildAt(counter)).setTextSize(25);
			}

		}
		
        // inflate layout
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
	
	public static Fragment newInstance(JSONObject menu, String category){
        RestaurantMenuFrag mFrag = new RestaurantMenuFrag();
		mFrag.mRestaurantMenu = menu;
		mFrag.mMenuType = category;
        return mFrag;
    }
}
