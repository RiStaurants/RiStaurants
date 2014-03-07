package com.ristaurants.ristaurants.app;
import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.parse.*;
import com.ristaurants.ristaurants.adapters.*;
import com.ristaurants.ristaurants.misc.*;
import java.util.*;

public class CuisineListFrag extends Fragment {
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
		String[] mTitles = getActivity().getResources().getStringArray(R.array.cuisine_categories);
		mAdapter = new CuisineAdapter(getActivity(), mTitles);
		
		// set list view and adapter
		mLvContent = (ListView) view.findViewById(R.id.lv_content);
		mLvContent.setAdapter(mAdapter);
		mLvContent.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// get item name
					String cuisine = parent.getItemAtPosition(position).toString();
					
					// pass data to next activity
					getActivity().startActivity(new Intent(getActivity(), Cuisine.class).putExtra("mCuisine", cuisine));

					// set activity animation
					getActivity().overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_null);
					
				}
			});
		
        // refresh action bar menu
        setHasOptionsMenu(true);
        
        // return view
        return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
