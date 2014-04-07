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
import com.parse.ParseObject;
import com.ristaurants.ristaurants.app.MapLocation;
import com.ristaurants.ristaurants.app.R;
import com.ristaurants.ristaurants.app.RestaurantDesc;
import com.ristaurants.ristaurants.app.RestaurantMenuActivity;
import com.ristaurants.ristaurants.misc.HelperClass;
import com.ristaurants.ristaurants.misc.SingletonVolley;

import java.util.List;

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
            mViewHolder.mTvRestaurantPhoneText = (TextView) view.findViewById(R.id.tv_restaurant_phone_text);
            mViewHolder.mTvRestaurantPhone = (TextView) view.findViewById(R.id.tv_restaurant_phone);
            mViewHolder.mIvRestaurantAddress = (ImageView) view.findViewById(R.id.iv_restaurant_map);
            mViewHolder.mIvRestaurantMenu = (ImageView) view.findViewById(R.id.iv_restaurant_menu);

            // save view holder in tag
            view.setTag(mViewHolder);
        } else {
            // get view holder from tag
            mViewHolder = (ViewHolder) view.getTag();
        }

        // set restaurant image
        final String image = mDataList.get(position).getString("image");
        mViewHolder.mIvRestaurantImage.setImageUrl(image, SingletonVolley.getImageLoader());

        // set restaurant rate image
        final int rate = mDataList.get(position).getInt("rate");
        mViewHolder.mIvRestaurantRate.setImageUrl(HelperClass.getRateImage(mContext, rate), SingletonVolley.getImageLoader());

        // set restaurant name
        final String name = mDataList.get(position).getString("name");
        mViewHolder.mTvRestaurantName.setText(name);

        // get restaurant description
        final String desc = mDataList.get(position).getString("description");

        // set restaurant phone
        final String phone = mDataList.get(position).getString("phone");
        mViewHolder.mTvRestaurantPhone.setText(phone);
        mViewHolder.mTvRestaurantPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open phone dialer with phone number
                Intent intent = new Intent(Intent.ACTION_DIAL, null);
                intent.setData(Uri.parse("tel:" + ((TextView) view).getText().toString()));
                mContext.startActivity(intent);
            }
        });

        // set restaurant menu
        final String restaurantID = mDataList.get(position).getObjectId();
        mViewHolder.mIvRestaurantMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RestaurantMenuActivity.class);
                intent.putExtra("mRestaurantName", name);
                intent.putExtra("mRestaurantID", restaurantID);
                mContext.startActivity(intent);

                // set activity animation
                ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_null);
            }
        });


        // set restaurant address
        final String address = mDataList.get(position).getString("address");
        final ParseGeoPoint mGeoPoint = mDataList.get(position).getParseGeoPoint("coordinates");
        mViewHolder.mIvRestaurantAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open Google Map
                Intent intent = new Intent(mContext, MapLocation.class);
                intent.putExtra("mRestaurantName", name);
                intent.putExtra("mAddress", address);
                intent.putExtra("mLatitude", mGeoPoint.getLatitude());
                intent.putExtra("mLongitude", mGeoPoint.getLongitude());
                mContext.startActivity(intent);

                // set activity animation
                ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_null);
            }
        });

        // set item click listener
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pass data
                Intent intent = new Intent(mContext, RestaurantDesc.class);
                intent.putExtra("mImage", image);
                intent.putExtra("mName", name);
                intent.putExtra("mAddress", address);
                intent.putExtra("mPhone", phone);
                intent.putExtra("mDesc", desc);
                intent.putExtra("mRate", rate);
                intent.putExtra("mLatitude", mGeoPoint.getLatitude());
                intent.putExtra("mLongitude", mGeoPoint.getLongitude());
                mContext.startActivity(intent);

                // set activity animation
                ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_null);
            }
        });

        // set fade in animation
        if (mLastAnimPosition < position) {
            ObjectAnimator.ofFloat(mViewHolder.mTvRestaurantName, "translationX", -1000, 0).setDuration(700).start();
            ObjectAnimator.ofFloat(mViewHolder.mIvRestaurantRate, "translationX", 1000, 0).setDuration(700).start();
            ObjectAnimator.ofFloat(mViewHolder.mTvRestaurantPhoneText, "translationX", -1000, 0).setDuration(700).start();
            ObjectAnimator.ofFloat(mViewHolder.mTvRestaurantPhone, "translationX", -1000, 0).setDuration(700).start();
            ObjectAnimator.ofFloat(mViewHolder.mIvRestaurantMenu, "translationX", 1000, 0).setDuration(700).start();
            ObjectAnimator.ofFloat(mViewHolder.mIvRestaurantAddress, "translationX", 1000, 0).setDuration(700).start();
            mLastAnimPosition = position;
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
        TextView mTvRestaurantPhoneText;
        TextView mTvRestaurantPhone;
    }
}
