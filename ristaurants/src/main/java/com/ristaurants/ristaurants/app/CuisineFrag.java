package com.ristaurants.ristaurants.app;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.ListView;

import com.ristaurants.ristaurants.adapters.CuisineAdapter;
import com.ristaurants.ristaurants.app.*;
import com.ristaurants.ristaurants.misc.*;

public class CuisineFrag extends Fragment {
    // instance variables
    private CuisineAdapter mAdapter;
    private ListView mLvContent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// inflate layout
        View view = inflater.inflate(R.layout.frag_cuisine, container, false);

        // set action bar background
        HelperClass.setActionBarBackground(getActivity(), R.color.cuisine_bg);

        // set adapter
        String[] mTitles = getActivity().getResources().getStringArray(R.array.stub);
        mAdapter = new CuisineAdapter(getActivity(), mTitles);

        // set list view
        mLvContent = (ListView) view.findViewById(R.id.lv_content);
        mLvContent.setAdapter(mAdapter);

        // return view
        return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		

	}
}
