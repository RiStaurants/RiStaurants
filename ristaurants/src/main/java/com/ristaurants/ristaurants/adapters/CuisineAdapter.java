package com.ristaurants.ristaurants.adapters;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.parse.ParseGeoPoint;
import com.ristaurants.ristaurants.app.MapLocation;
import com.ristaurants.ristaurants.app.R;
import com.ristaurants.ristaurants.app.RestaurantDesc;
import com.ristaurants.ristaurants.app.RestaurantMenuActivity;
import com.ristaurants.ristaurants.misc.HelperClass;
import com.ristaurants.ristaurants.misc.SingletonVolley;

/**
 * Created by cs15 on 3/4/14.
 */
public class CuisineAdapter extends BaseAdapter {
    // instance variables
    private Context mContext;
    private String[] mTitles;

    public CuisineAdapter(Context context, String[] titles){
        mContext = context;
        mTitles = titles;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        // get view
        ViewHolder mViewHolder;

        // check if view needs to get inflated
        if (view == null) {
            // inflate view
            LayoutInflater viewInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = viewInflater.inflate(R.layout.row_cuisines, null);

            // instantiate views
            mViewHolder = new ViewHolder();
            assert view != null;
            mViewHolder.mTvTitle = (TextView) view.findViewById(R.id.tv_cuisine_title);

            // save view holder in tag
            view.setTag(mViewHolder);
        } else {
            // get view holder from tag
            mViewHolder = (ViewHolder) view.getTag();
        }

        // set title
        mViewHolder.mTvTitle.setText(mTitles[position]);

        // return view
        return view;
    }

    class ViewHolder {
        // instantiate views
        TextView mTvTitle;
    }
}
