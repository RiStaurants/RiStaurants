package com.ristaurants.ristaurants.adapters;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.parse.ParseObject;
import com.ristaurants.ristaurants.app.DishesReviews;
import com.ristaurants.ristaurants.app.R;
import com.ristaurants.ristaurants.misc.HelperClass;
import com.ristaurants.ristaurants.misc.SingletonVolley;

import java.text.NumberFormat;
import java.util.List;

/**
 * Dish adapter
 */
public class DishesAdapter extends BaseAdapter {
    // instance variables
    private List<ParseObject> mData;
    private Context mContext;
    private int mLastAnimPosition = 2;

    public DishesAdapter(Context context, List<ParseObject> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
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
            mViewHolder.mTvDishPrice = (TextView) view.findViewById(R.id.tv_restaurant_menu_price);
            mViewHolder.mTvDishDesc = (TextView) view.findViewById(R.id.tv_restaurant_menu_desc);
            mViewHolder.mTvDishReviewCount = (TextView) view.findViewById(R.id.tv_restaurant_menu_review_amount);

            // save view holder in tag
            view.setTag(mViewHolder);
        } else {
            // get view holder from tag
            mViewHolder = (ViewHolder) view.getTag();
        }

            // set restaurant dish image
            final String dishImageUrl = mData.get(position).getString("dishImage");
            mViewHolder.mIvDishImage.setImageUrl(dishImageUrl, SingletonVolley.getImageLoader());

            // set restaurant rate image
            mViewHolder.mIvDishRate.setImageUrl(HelperClass.getRateImage(mContext, mData.get(position).getInt("dishRate")), SingletonVolley.getImageLoader());

            // set restaurant dish name
            final String dishName = mData.get(position).getString("dishName");
            mViewHolder.mTvDishName.setText(dishName);

            // set restaurant dish price
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
            String dishPrice = currencyFormatter.format(mData.get(position).getDouble("dishPrice"));
            mViewHolder.mTvDishPrice.setText(dishPrice);

            // set restaurant dish description
            mViewHolder.mTvDishDesc.setText(mData.get(position).getString("dishDesc"));

            // set restaurant dish review count
            final int reviewCount = mData.get(position).getInt("dishReviewsCount");
            mViewHolder.mTvDishReviewCount.setText(String.format("%d %s", reviewCount, "reviews"));

            // on click listener
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // open reviews activity
                    Intent intent = new Intent(mContext, DishesReviews.class);
                    intent.putExtra("mDishImageUrl", dishImageUrl);
                    intent.putExtra("mDishName", dishName);
                    intent.putExtra("mDishID", mData.get(position).getObjectId());
                    mContext.startActivity(intent);

                    // set activity animation
                    ((Activity) mContext).overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_null);
                }
            });

            // set view animation
            if (mLastAnimPosition < position) {
                ObjectAnimator.ofFloat(mViewHolder.mTvDishName, "alpha", 0f, 1f).setDuration(1500).start();
                ObjectAnimator.ofFloat(mViewHolder.mTvDishDesc, "alpha", 0f, 1f).setDuration(1500).start();
                ObjectAnimator.ofFloat(view, "translationY", 200, 0).setDuration(500).start();
                mLastAnimPosition = position;
            }


        // return view
        return view;
    }

    class ViewHolder {
        // instantiate views
        NetworkImageView mIvDishImage;
        NetworkImageView mIvDishRate;
        TextView mTvDishName;
        TextView mTvDishPrice;
        TextView mTvDishDesc;
        TextView mTvDishReviewCount;
    }
}
