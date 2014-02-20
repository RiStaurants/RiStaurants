package com.ristaurants.ristaurants.app;

import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.view.*;
import android.widget.*;
import com.ristaurants.ristaurants.adapters.*;
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

		// setup adapter
        if (savedInstanceState != null){
            try {
                mRestaurantMenu = new JSONObject(savedInstanceState.getString("mRestaurantMenu", null));
                mMenuType = savedInstanceState.getString("mMenuType", null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

		mAdapter = new RestaurantMenuAdapter(getActivity(), mRestaurantMenu, mMenuType);

		// menu list view
        assert view != null;
        mLvContent = (ListView) view.findViewById(R.id.lv_content);
		mLvContent.setAdapter(mAdapter);
		
		// set PagerTitleStrip style
		PagerTitleStrip mPagerTitle = (PagerTitleStrip) getActivity().findViewById(R.id.pager_title_strip);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Bender-Solid.otf"); 
		for (int i = 0; i < mPagerTitle.getChildCount(); i++) {
			if (mPagerTitle.getChildAt(i) instanceof TextView) {
				((TextView)mPagerTitle.getChildAt(i)).setTypeface(font);
			}
		}
		
        // inflate layout
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("mRestaurantMenu", mRestaurantMenu.toString());
        outState.putString("mMenuType", mMenuType);
    }

    public static Fragment newInstance(JSONObject menu, String category){
        RestaurantMenuFrag mFrag = new RestaurantMenuFrag();
		mFrag.mRestaurantMenu = menu;
		mFrag.mMenuType = category;
        return mFrag;
    }
}
