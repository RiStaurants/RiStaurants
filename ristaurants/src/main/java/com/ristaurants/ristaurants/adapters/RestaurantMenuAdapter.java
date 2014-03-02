package com.ristaurants.ristaurants.adapters;

import android.animation.*;
import android.app.Activity;
import android.content.*;
import android.view.*;
import android.widget.*;

import com.android.volley.toolbox.*;
import com.parse.ParseObject;
import com.ristaurants.ristaurants.app.*;
import com.ristaurants.ristaurants.misc.*;

import java.util.ArrayList;

public class RestaurantMenuAdapter extends BaseAdapter {
    // instance variables
    private Context mContext;
    private ArrayList<ParseObject> mDataList;
    private String mMenuCategory;
    private int mLastAnimPosition = 1;

    public RestaurantMenuAdapter(Context context, ArrayList<ParseObject> data, String menuCategory) {
        mContext = context;
        mDataList = data;
        mMenuCategory = menuCategory;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        // get view
        ViewHolder mViewHolder;

        // check if view needs to get inflated
        if (view == null) {
            // inflate view
            LayoutInflater viewInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = viewInflater.inflate(R.layout.row_restaurant_menu, null);

            // instantiate views
            mViewHolder = new ViewHolder();
            assert view != null;
            mViewHolder.mIvDishImage = (NetworkImageView) view.findViewById(R.id.niv_restaurant_menu_image);
			mViewHolder.mIvDishRate = (NetworkImageView) view.findViewById(R.id.niv_restaurant_menu_rate);
            mViewHolder.mTvDishName = (TextView) view.findViewById(R.id.tv_restaurant_menu_name);
            mViewHolder.mTvDishDesc = (TextView) view.findViewById(R.id.tv_restaurant_menu_desc);
            mViewHolder.mTvDishReviewCount = (TextView) view.findViewById(R.id.tv_restaurant_menu_review_amount);

            // save view holder in tag
            view.setTag(mViewHolder);
        } else {
            // get view holder from tag
            mViewHolder = (ViewHolder) view.getTag();
        }

        if (mDataList.get(position).getString("dishCategories").equals(mMenuCategory)) {
            // set restaurant dish image
            final String dishImageUrl = mDataList.get(position).getString("dishImage");
            mViewHolder.mIvDishImage.setImageUrl(dishImageUrl, SingletonVolley.getImageLoader());
			
			// set restaurant rate image
			mViewHolder.mIvDishRate.setImageUrl(HelperClass.getRateImage(mContext, mDataList.get(position).getInt("dishRate")), SingletonVolley.getImageLoader());

            // set restaurant dish name
            final String dishName = mDataList.get(position).getString("dishName");
            mViewHolder.mTvDishName.setText(dishName);

            // set restaurant dish description
            mViewHolder.mTvDishDesc.setText(mDataList.get(position).getString("dishDesc"));

            // set restaurant dish review count
            final int reviewCount = mDataList.get(position).getInt("dishReviewsCount");
            mViewHolder.mTvDishReviewCount.setText(String.format("%d %s", reviewCount, "reviews"));

            // on click listener
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // variable to create the dish name class name for Parse.com
                    String dishNameClassName = mDataList.get(position).getString("dishName");

                    // replace empty spaces and append the (Reviews) String.
                    dishNameClassName = dishNameClassName.replace(" ", "");
                    dishNameClassName += "DishReviews";

                    // open reviews activity
                    Intent intent = new Intent(mContext, DishesReviews.class);
                    intent.putExtra("mDishReviewClassName", dishNameClassName);
                    intent.putExtra("mDishImageUrl", dishImageUrl);
                    intent.putExtra("mDishName", dishName);
                    intent.putExtra("mDishID", mDataList.get(position).getObjectId());
                    intent.putExtra("mMenuClassName", mDataList.get(position).getClassName());
                    mContext.startActivity(intent);

                    // set activity animation
                    ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                }
            });

            // set view animation
            if (mLastAnimPosition < position) {
                ObjectAnimator.ofFloat(mViewHolder.mTvDishName, "alpha", 0f, 1f).setDuration(1500).start();
                ObjectAnimator.ofFloat(mViewHolder.mTvDishDesc, "alpha", 0f, 1f).setDuration(1000).start();
                ObjectAnimator.ofFloat(view, "translationY", 200, 0).setDuration(500).start();
                mLastAnimPosition = position;
            }

        }
        // return view
        return view;
    }

    class ViewHolder {
        // instantiate views
        NetworkImageView mIvDishImage;
		NetworkImageView mIvDishRate;
        TextView mTvDishName;
        TextView mTvDishDesc;
        TextView mTvDishReviewCount;
    }
}
