package com.ristaurants.ristaurants.app;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import com.ristaurants.ristaurants.misc.*;

public class DishesFrag extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// inflate layout
		return inflater.inflate(R.layout.frag_dishes, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// set action bar background
		HelperClass.setActionBarBackground(getActivity(), R.color.dishes_bg);
	}
}
