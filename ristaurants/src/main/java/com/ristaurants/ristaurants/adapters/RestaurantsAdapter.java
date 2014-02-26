package com.ristaurants.ristaurants.adapters;

import android.animation.*;
import android.app.Activity;
import android.content.*;
import android.net.*;
import android.view.*;
import android.widget.*;
import com.android.volley.toolbox.*;
import com.ristaurants.ristaurants.app.*;
import com.ristaurants.ristaurants.misc.*;
import org.json.*;
import com.parse.ParseObject;
import java.util.*;

public class RestaurantsAdapter extends BaseAdapter {
    // instance variables
    private Context mContext;
	private List<ParseObject> mDataList;
    private int mLastAnimPosition = -1;

    public RestaurantsAdapter(Context context, List<ParseObject> dataList) {
        // extract parameters
        this.mContext = context;
		this.mDataList = dataList;
    }

    @Override
    public int getCount() {
		return this.mDataList.size();
    }

    @Override
    public Object getItem(int position) {
		return this.mDataList.get(position);
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
            mViewHolder.mIvRestaurantImage = (NetworkImageView) view.findViewById(R.id.niv_restaurant_image);
            mViewHolder.mIvRestaurantRate = (NetworkImageView) view.findViewById(R.id.niv_restaurant_rate);
			mViewHolder.mTvRestaurantName = (TextView) view.findViewById(R.id.tv_restaurant_name);
			mViewHolder.mTvRestaurantPhone = (TextView) view.findViewById(R.id.tv_restaurant_phone);
            mViewHolder.mIvRestaurantAddress = (ImageView) view.findViewById(R.id.iv_restaurant_map);
            mViewHolder.mIvRestaurantMenu = (ImageView) view.findViewById(R.id.iv_restaurant_menu);

            // save view holder in tag
            view.setTag(mViewHolder);
        } else {
            // get view holder from tag
            mViewHolder = (ViewHolder) view.getTag();
        }

        // set data
        try {
			// set restaurant image
			mViewHolder.mIvRestaurantImage.setImageUrl(mDataList.get(position).getString("restaurantImage"), SingletonVolley.getImageLoader());
			//mViewHolder.mIvRestaurantImage.setImageUrl(mData.getJSONArray("restaurants").getJSONObject(position).getString("image"), SingletonVolley.getImageLoader());
			
			// set restaurant rate image
			mViewHolder.mIvRestaurantRate.setImageUrl(HelperClass.getRateImage(mContext, mDataList.get(position).getInt("restaurantRate")), SingletonVolley.getImageLoader());
			
            // set restaurant name
			//final String restaurantName = mData.getJSONArray("restaurants").getJSONObject(position).getString("name").toLowerCase();
            mViewHolder.mTvRestaurantName.setText(mDataList.get(position).getString("restaurantName"));

            // set restaurant phone
			mViewHolder.mTvRestaurantPhone.setText(mDataList.get(position).getString("restaurantPhone"));
			mViewHolder.mTvRestaurantPhone.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View view) {
						// open phone dialer with phone number
						Intent intent = new Intent(Intent.ACTION_DIAL, null);
						intent.setData(Uri.parse("tel:" + ((TextView)view).getText().toString()));
						mContext.startActivity(intent);
					}
				});

            // set restaurant menu
            final String jsonObjectUrl = "https://dl.dropboxusercontent.com/u/27136243/RiStaurants/json/los_andes_menu.json";
            mViewHolder.mIvRestaurantMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, RestaurantMenuActivity.class);
                    intent.putExtra("jsonObjectUrl", jsonObjectUrl);
                    mContext.startActivity(intent);

                    // set activity animation
                    ((Activity)mContext).overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                }
            });
			
            // set restaurant address
            final String address = mDataList.get(position).getString("restaurantAddress").toLowerCase();
            mViewHolder.mIvRestaurantAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // open phone dialer with phone number
                    Intent intent = new Intent(Intent.ACTION_VIEW, null);
                    intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + address));
                    mContext.startActivity(intent);
                }
            });

            // set fade in animation
            if (mLastAnimPosition < position) {
				ObjectAnimator.ofFloat(mViewHolder.mIvRestaurantImage, "alpha", 0f, 1f).setDuration(200).start();
                mLastAnimPosition = position;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // return view
        return view;
    }

    class ViewHolder {
        // instantiate views
        NetworkImageView mIvRestaurantImage;
        NetworkImageView mIvRestaurantRate;
        ImageView mIvRestaurantAddress;
        ImageView mIvRestaurantMenu;
		TextView mTvRestaurantName;
		TextView mTvRestaurantPhone;
    }
}
