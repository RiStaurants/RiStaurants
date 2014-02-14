package com.ristaurants.ristaurants.adapters;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.net.*;
import android.view.*;
import android.widget.*;
import com.android.volley.toolbox.*;
import com.ristaurants.ristaurants.app.*;
import com.ristaurants.ristaurants.misc.*;
import org.json.*;

public class BreakfastAdapter extends BaseAdapter {
	// instance variables
	private Context mContext;
	private JSONObject mData;
	private int mLastAnimPosition = -1;
	
	public BreakfastAdapter(Context context, JSONObject data){
		mContext = context;
		mData = data;
	}

	@Override
	public int getCount() {
		try {
			return mData.getJSONObject("menus").getJSONArray("breakfast").length();
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		try {
			return mData.getJSONObject("menus").getJSONArray("breakfast").getJSONObject(position);
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
            view = viewInflater.inflate(R.layout.row_breakfast, null);

            // instantiate views
            mViewHolder = new ViewHolder();
            assert view != null;
            mViewHolder.mIvDishImage = (NetworkImageView) view.findViewById(R.id.niv_restaurant_menu_image);
			mViewHolder.mTvDishName = (TextView) view.findViewById(R.id.tv_restaurant_menu_name);
			mViewHolder.mTvDishDesc = (TextView) view.findViewById(R.id.tv_restaurant_menu_desc);

            // save view holder in tag
            view.setTag(mViewHolder);
        } else {
            // get view holder from tag
            mViewHolder = (ViewHolder) view.getTag();
        }

        // set data
        try {
			// set restaurant dish image
			mViewHolder.mIvDishImage.setImageUrl(mData.getJSONObject("menus").getJSONArray("breakfast").getJSONObject(position).getString("image"), SingletonVolley.getImageLoader());
			
			// set restaurant dish name
			mViewHolder.mTvDishName.setText(mData.getJSONObject("menus").getJSONArray("breakfast").getJSONObject(position).getString("name"));
			
			// set restaurant dish name
			mViewHolder.mTvDishDesc.setText(mData.getJSONObject("menus").getJSONArray("breakfast").getJSONObject(position).getString("description"));
			
            // set fade in animation
            if (mLastAnimPosition < position) {
				ObjectAnimator.ofFloat(mViewHolder.mIvDishImage, "alpha", 0f, 1f).setDuration(200).start();
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
    }
}
