package com.ristaurants.ristaurants.app;

import android.animation.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.android.volley.toolbox.*;
import com.ristaurants.ristaurants.adapters.*;
import com.ristaurants.ristaurants.misc.*;
import org.json.*;

public class DishesReviews extends Activity {
	// instance variables
	private DishReviewsAdapter mAdapter;
	private JSONArray mReviews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dishes_reviews);

		// set action bar background
		HelperClass.setActionBarBackground(this, R.color.restaurants_bg);

		// set-up action bar
        getActionBar().setTitle(HelperClass.setActionbarTitle(this, getResources().getString(R.string.ab_title_reviews)));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

		// get data from previous activity
		if (getIntent().getExtras() != null) {
			try {
				// get json data of reviews
				mReviews = new JSONArray(getIntent().getExtras().getString("mReviews"));

				// display dish image
				String dishImageUrl = getIntent().getExtras().getString("mDishImageUrl");
				NetworkImageView mIvDishImage = (NetworkImageView) findViewById(R.id.niv_dish_reviews_image);
				mIvDishImage.setImageUrl(dishImageUrl, SingletonVolley.getImageLoader());

				// display dish name
				String dishName = getIntent().getExtras().getString("mDishName", "No Dish Name");
				final TextView mTvDishName = (TextView) findViewById(R.id.tv_dish_reviews_name);
				mTvDishName.setText(dishName);

				// animate dish name
				ObjectAnimator.ofFloat(mTvDishName, "alpha", 0f, 1f).setDuration(1000).start();
				
				// set list view adapter 
				mAdapter = new DishReviewsAdapter(this, mReviews);
				
				// set list view
				final ListView mLvContent = (ListView) findViewById(R.id.lv_content);
				mLvContent.setAdapter(mAdapter);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // go back to previous screens
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
    public void onBackPressed() {
        super.onBackPressed();

        // set activity animation
        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }
}
