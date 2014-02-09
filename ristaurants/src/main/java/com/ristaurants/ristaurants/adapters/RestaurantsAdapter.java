package com.ristaurants.ristaurants.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.ristaurants.ristaurants.app.R;
import com.ristaurants.ristaurants.misc.SingletonVolley;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.*;
import android.net.*;

public class RestaurantsAdapter extends BaseAdapter {
    // instance variables
    private final String API_KEY;
    private final String PHOTO_REFS_API = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&sensor=true&photoreference=";
    private final String DETAILS_REFS_API = "https://maps.googleapis.com/maps/api/place/details/json?sensor=true&reference=";
    private Context mContext;
    private JSONObject mData;

    public RestaurantsAdapter(Context context, JSONObject data, String apiKey) {
        // extract parameters
        this.API_KEY = apiKey;
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        try {
            return this.mData.getJSONArray("results").length();
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        try {
            return this.mData.getJSONArray("results").getJSONObject(position);
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
            mViewHolder.mIvRestaurantRate = (ImageView) view.findViewById(R.id.iv_restaurant_rate);
			mViewHolder.mTvRestaurantName = (TextView) view.findViewById(R.id.tv_restaurant_name);
			mViewHolder.mTvRestaurantPhone = (TextView) view.findViewById(R.id.tv_restaurant_phone);

            // save view holder in tag
            view.setTag(mViewHolder);
        } else {
            // get view holder from tag
            mViewHolder = (ViewHolder) view.getTag();
        }

        // set data
        try {
            // set restaurant name
            mViewHolder.mTvRestaurantName.setText(mData.getJSONArray("results").getJSONObject(position).getString("name").toLowerCase());

            // set restaurant phone
            String detailRefs = mData.getJSONArray("results").getJSONObject(position).getString("reference");
			//mViewHolder.mTvRestaurantPhone.setText(detailRefs);
			mViewHolder.mTvRestaurantPhone.setOnClickListener(new View.OnClickListener(){

					@Override
					public void onClick(View view) {
						// open phone dialer with phone number
						Intent intent = new Intent(Intent.ACTION_DIAL, null);
						intent.setData(Uri.parse("tel:" + ((TextView)view).getText().toString()));
						mContext.startActivity(intent);
					}
				});
			
            // set restaurant image
            String photoRefs = mData.getJSONArray("results").getJSONObject(position).getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            ImageLoader restaurantImage = SingletonVolley.getImageLoader();
            restaurantImage.setBatchedResponseDelay(0);
            restaurantImage.get( (PHOTO_REFS_API + photoRefs + API_KEY) , ImageLoader.getImageListener(mViewHolder.mIvRestaurantImage, R.drawable.ic_launcher, R.drawable.ic_launcher));

			// set restaurant rate image
            ImageLoader restaurantRate = SingletonVolley.getImageLoader();
            restaurantRate.setBatchedResponseDelay(0);
            //restaurantRate.get(mData.getJSONArray("restaurants").getJSONObject(position).getString("rate"), ImageLoader.getImageListener(mViewHolder.mIvRestaurantRate, R.drawable.ic_launcher, R.drawable.ic_launcher));
			
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // return view
        return view;
    }

    class ViewHolder {
        // instantiate views
        ImageView mIvRestaurantImage;
        ImageView mIvRestaurantRate;
		TextView mTvRestaurantName;
		TextView mTvRestaurantPhone;
    }
}
