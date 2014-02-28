package com.ristaurants.ristaurants.app;

import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.view.*;
import android.widget.*;

import com.parse.Parse;
import com.parse.ParseObject;
import com.ristaurants.ristaurants.adapters.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display the breakfast menu of the restaurant
 */
public class RestaurantMenuFrag extends Fragment {
    // instance variables
    private ListView mLvContent;
    private RestaurantMenuAdapter mAdapter;
    private List<ParseObject> mParseObjectList;
    private String mMenuCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_restaurant_menu, container, false);

        // setup adapter
        if (savedInstanceState != null) {
            mMenuCategory = savedInstanceState.getString("mMenuCategory", null);
        }

        // set adapter
        mAdapter = new RestaurantMenuAdapter(getActivity(), getMenuList(mParseObjectList, mMenuCategory), mMenuCategory);

        // menu list view
        assert view != null;
        mLvContent = (ListView) view.findViewById(R.id.lv_content);
        mLvContent.setAdapter(mAdapter);

        // set PagerTitleStrip style
        PagerTitleStrip mPagerTitle = (PagerTitleStrip) getActivity().findViewById(R.id.pager_title_strip);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Bender-Solid.otf");
        for (int i = 0; i < mPagerTitle.getChildCount(); i++) {
            if (mPagerTitle.getChildAt(i) instanceof TextView) {
                ((TextView) mPagerTitle.getChildAt(i)).setTypeface(font);
            }
        }

        // inflate layout
        return view;
    }

    /**
     * Extract the menu list for the current category
     *
     * @param parseObjectList ParseObject list containing all menu items.
     * @param menuCategory  The current category.
     * @return Returns an ArrayList<ParseObject> containing only the menu item for the current category. </>
     */
    private ArrayList<ParseObject> getMenuList(List<ParseObject> parseObjectList, String menuCategory) {
        // local variable
        ArrayList<ParseObject> menuList = new ArrayList<ParseObject>();

        // extract menu list for this category
        for (int i = 0; i < parseObjectList.size(); i++) {
            if (parseObjectList.get(i).getString("dishCategories").equals(menuCategory)) {
                menuList.add(parseObjectList.get(i));
            }
        }

        // return menu list
        return menuList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("mMenuCategory", mMenuCategory);
    }

    public static Fragment newInstance(List<ParseObject> parseObjectList, String menuCategory) {
        RestaurantMenuFrag mFrag = new RestaurantMenuFrag();
        mFrag.mParseObjectList = parseObjectList;
        mFrag.mMenuCategory = menuCategory;
        return mFrag;
    }
}
