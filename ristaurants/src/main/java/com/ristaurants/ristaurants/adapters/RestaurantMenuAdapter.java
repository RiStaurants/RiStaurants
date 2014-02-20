package com.ristaurants.ristaurants.adapters;

import android.animation.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.android.volley.toolbox.*;
import com.ristaurants.ristaurants.app.*;
import com.ristaurants.ristaurants.misc.*;
import org.json.*;

public class RestaurantMenuAdapter extends BaseAdapter {
	// instance variables
	private Context mContext;
	private JSONObject mData;
	private String mMenuType;
	private int mLastAnimPosition = 1;

	public RestaurantMenuAdapter(Context context, JSONObject data, String menuType){
		mContext = context;
		mData = data;
		mMenuType = menuType;
	}

	@Override
	public int getCount() {
		try {
			return mData.getJSONObject("menus").getJSONArray(mMenuType).length();
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		try {
			return mData.getJSONObject("menus").getJSONArray(mMenuType).getJSONObject(position);
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
            view = viewInflater.inflate(R.layout.row_restaurant_menu, null);

            // instantiate views
            mViewHolder = new ViewHolder();
            assert view != null;
            mViewHolder.mIvDishImage = (NetworkImageView) view.findViewById(R.id.niv_restaurant_menu_image);
			mViewHolder.mTvDishName = (TextView) view.findViewById(R.id.tv_restaurant_menu_name);
			mViewHolder.mTvDishDesc = (TextView) view.findViewById(R.id.tv_restaurant_menu_desc);
            mViewHolder.mTvDishReviewCount = (TextView) view.findViewById(R.id.tv_restaurant_menu_review_amount);

            // save view holder in tag
            view.setTag(mViewHolder);
        } else {
            // get view holder from tag
            mViewHolder = (ViewHolder) view.getTag();
        }

        // set data
        try {
			// set restaurant dish image
			mViewHolder.mIvDishImage.setImageUrl(mData.getJSONObject("menus").getJSONArray(mMenuType).getJSONObject(position).getString("image"), SingletonVolley.getImageLoader());

			// set restaurant dish name
			mViewHolder.mTvDishName.setText(mData.getJSONObject("menus").getJSONArray(mMenuType).getJSONObject(position).getString("name"));

			// set restaurant dish description
			mViewHolder.mTvDishDesc.setText(mData.getJSONObject("menus").getJSONArray(mMenuType).getJSONObject(position).getString("description"));

            // set restaurant dish review count
            final int reviewCount = mData.getJSONObject("menus").getJSONArray(mMenuType).getJSONObject(position).getJSONArray("reviews").length();
            mViewHolder.mTvDishReviewCount.setText(String.format("%d %s", reviewCount, "reviews"));

            // set view animation
            if (mLastAnimPosition < position) {
                ObjectAnimator.ofFloat(mViewHolder.mTvDishName, "alpha", 0f, 1f).setDuration(1000).start();
                ObjectAnimator.ofFloat(mViewHolder.mTvDishDesc, "alpha", 0f, 1f).setDuration(1000).start();
                ObjectAnimator.ofFloat(view, "translationY", 200, 0).setDuration(500).start();
                mLastAnimPosition = position;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // return view
        return view;
    }

    class ViewHolder {
        // instantiate views
        NetworkImageView mIvDishImage;
		TextView mTvDishName;
		TextView mTvDishDesc;
        TextView mTvDishReviewCount;
    }
}
