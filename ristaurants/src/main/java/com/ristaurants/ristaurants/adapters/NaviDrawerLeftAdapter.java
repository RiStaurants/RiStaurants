package com.ristaurants.ristaurants.adapters;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.*;
import android.view.*;

import com.ristaurants.ristaurants.app.R;

public class NaviDrawerLeftAdapter extends BaseAdapter {
    // instance variables
    private Context mContext;
    private Drawable[] mIcons;
    private String[] mTitles;

    public NaviDrawerLeftAdapter(Context mContext, Drawable[] mIcons, String[] mTitles) {
        // extract parameters
        this.mContext = mContext;
        this.mIcons = mIcons;
        this.mTitles = mTitles;
    }

    @Override
    public int getCount() {
        return this.mTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return this.mTitles[position];
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
            view = viewInflater.inflate(R.layout.row_drawer_left, null);

            // instantiate views
            mViewHolder = new ViewHolder();
            assert view != null;
            mViewHolder.mIvImage = (ImageView) view.findViewById(R.id.iv_drawer_left_icon);
            mViewHolder.mTvTitles = (TextView) view.findViewById(R.id.tv_drawer_left);

            // save view holder in tag
            view.setTag(mViewHolder);
        } else {
            // get view holder from tag
            mViewHolder = (ViewHolder) view.getTag();
        }

        // set data
        mViewHolder.mIvImage.setImageDrawable(this.mIcons[position]);
        mViewHolder.mTvTitles.setText(this.mTitles[position]);

        // return view
        return view;
    }

    class ViewHolder {
        // instantiate views
        ImageView mIvImage;
        TextView mTvTitles;
    }
}
