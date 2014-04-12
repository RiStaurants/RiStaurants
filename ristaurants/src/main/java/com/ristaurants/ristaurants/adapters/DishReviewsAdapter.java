package com.ristaurants.ristaurants.adapters;

import android.content.*;
import android.view.*;
import android.widget.*;

import com.android.volley.toolbox.*;
import com.parse.ParseObject;
import com.ristaurants.ristaurants.app.*;
import com.ristaurants.ristaurants.misc.*;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class DishReviewsAdapter extends BaseAdapter {
    // instance variables
    private Context mContext;
    private List<ParseObject> mData;

    public DishReviewsAdapter(Context context, List<ParseObject> data) {
        mContext = context;
        mData = data;
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

        // set review author name
        String author = mData.get(position).getString("username");
        mViewHolder.mTvDishReviewAuthor.setText(author);

        // set review date
        Date date = mData.get(position).getUpdatedAt();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(mContext);
        mViewHolder.mTvDishReviewDate.setText(dateFormat.format(date));

        // set review desc
        String review = mData.get(position).getString("review");
        mViewHolder.mTvDishReviewDesc.setText(review);

        // set restaurant rate image
        mViewHolder.mIvDishReviewRate.setImageUrl(HelperClass.getRateImage(mContext, mData.get(position).getInt("rate")), SingletonVolley.getImageLoader());

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
