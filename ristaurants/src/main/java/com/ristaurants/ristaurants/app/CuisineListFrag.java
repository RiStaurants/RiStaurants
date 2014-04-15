package com.ristaurants.ristaurants.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ristaurants.ristaurants.adapters.CuisineAdapter;
import com.ristaurants.ristaurants.misc.HelperClass;

import java.util.List;

public class CuisineListFrag extends Fragment {
    // instance variables
    private CuisineAdapter mAdapter;
    private ListView mLvContent;
    private String[] mTitles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate layout
        View view = inflater.inflate(R.layout.frag_cuisine, container, false);

        // set list view and adapter
        mLvContent = (ListView) view.findViewById(R.id.lv_content);

        // get data from database
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("CuisineCategories");
        parseQuery.orderByAscending("type");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    // extract data
                    mTitles = new String[parseObjects.size()];
                    for (int i = 0; i < parseObjects.size(); i ++) {
                        mTitles[i] = parseObjects.get(i).getString("type");
                    }

                    // set adapter
                    mAdapter = new CuisineAdapter(getActivity(), mTitles);
                    mLvContent.setAdapter(mAdapter);
                } else {
                    Log.e("ParseObject", "Error: " + e.getMessage());
                }
            }
        });

        // set list view listener
        mLvContent.setOnItemClickListener(new OnItemClickListener() {

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

        // return view
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // refresh action bar menu
        setHasOptionsMenu(true);
    }
}
