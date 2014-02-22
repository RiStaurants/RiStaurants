package com.ristaurants.ristaurants.adapters;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;
import com.android.volley.toolbox.*;
import com.ristaurants.ristaurants.app.*;
import com.ristaurants.ristaurants.misc.*;
import org.json.*;

public class DishReviewsAdapter extends BaseAdapter {
	// instance variables
	private Context mContext;
	private JSONArray mData;

	public DishReviewsAdapter(Context context, JSONArray data) {
		mContext = context;
		mData = data;
	}

	@Override
	public int getCount() {
		return mData.length();
	}

	@Override
	public Object getItem(int position) {
		try {
			return mData.getJSONObject(position);
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
	public View getView(final int position, View view, ViewGroup viewGroup) {
		// get view
        ViewHolder mViewHolder;

        // check if view needs to get inflated
        if (view == null) {
            // inflate view
            LayoutInflater viewInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = viewInflater.inflate(R.layout.row_dish_reviews, null);

            // instantiate views
            mViewHolder = new ViewHolder();
            assert view != null;
            mViewHolder.mIvDishReviewRate = (NetworkImageView) view.findViewById(R.id.niv_dish_review_rate);
			mViewHolder.mTvDishReviewAuthor = (TextView) view.findViewById(R.id.tv_dish_review_author);
			mViewHolder.mTvDishReviewDate = (TextView) view.findViewById(R.id.tv_dish_review_date);
            mViewHolder.mTvDishReviewDesc = (TextView) view.findViewById(R.id.tv_dish_review_review);

            // save view holder in tag
            view.setTag(mViewHolder);
        } else {
            // get view holder from tag
            mViewHolder = (ViewHolder) view.getTag();
        }
		
		// set data
		try {
			// set author name
			String author = mData.getJSONObject(position).getString("author");
			mViewHolder.mTvDishReviewAuthor.setText(author);
			
			// set author name
			String date = mData.getJSONObject(position).getString("date");
			mViewHolder.mTvDishReviewDate.setText(date);
			
			// set author name
			String review = mData.getJSONObject(position).getString("review");
			mViewHolder.mTvDishReviewDesc.setText(review);
			
			// set restaurant rate image
			mViewHolder.mIvDishReviewRate.setImageUrl(mData.getJSONObject(position).getString("rate"), SingletonVolley.getImageLoader());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

        // return view
        return view;
    }

    class ViewHolder {
        // instantiate views
        NetworkImageView mIvDishReviewRate;
		TextView mTvDishReviewAuthor;
		TextView mTvDishReviewDate;
        TextView mTvDishReviewDesc;
    }
}
