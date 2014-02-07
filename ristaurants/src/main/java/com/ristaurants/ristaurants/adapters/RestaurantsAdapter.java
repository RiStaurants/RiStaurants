package com.ristaurants.ristaurants.adapters;

import android.content.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import com.ristaurants.ristaurants.app.*;
import org.json.*;
import com.android.volley.toolbox.*;
import com.ristaurants.ristaurants.misc.*;

public class RestaurantsAdapter extends BaseAdapter
{
	// instance variables
    private Context mContext;
    private JSONObject mData;

    public RestaurantsAdapter(Context context, JSONObject data) {
        // extract parameters
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        try {
			return this.mData.getJSONArray("restaurants").length();
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
    }

    @Override
    public Object getItem(int position) {
        try {
			return this.mData.getJSONArray("restaurants").getJSONObject(position);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
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
            view = viewInflater.inflate(R.layout.row_restaurants, null);

            // instantiate views
            mViewHolder = new ViewHolder();
            assert view != null;
            mViewHolder.mIvRestaurantImage = (ImageView) view.findViewById(R.id.iv_restaurant_image);
            mViewHolder.mTvRestaurantName = (TextView) view.findViewById(R.id.tv_restaurant_name);

            // save view holder in tag
            view.setTag(mViewHolder);
        } else {
            // get view holder from tag
            mViewHolder = (ViewHolder) view.getTag();
        }

        // set data
        try {
			// set restaurant name
			mViewHolder.mTvRestaurantName.setText(mData.getJSONArray("restaurants").getJSONObject(position).getString("name"));
			
			// set restaurant image
			ImageLoader imageLoader = SingletonVolley.getImageLoader();
			imageLoader.setBatchedResponseDelay(0);
			imageLoader.get(mData.getJSONArray("restaurants").getJSONObject(position).getString("image"), ImageLoader.getImageListener(mViewHolder.mIvRestaurantImage, R.drawable.ic_launcher, R.drawable.ic_launcher));
			
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
		}

        // return view
        return view;
    }

    class ViewHolder {
        // instantiate views
        ImageView mIvRestaurantImage;
        TextView mTvRestaurantName;
    }
}
