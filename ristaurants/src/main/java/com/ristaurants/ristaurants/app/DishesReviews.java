package com.ristaurants.ristaurants.app;

import android.animation.*;
import android.app.*;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.android.volley.toolbox.*;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.ristaurants.ristaurants.adapters.*;
import com.ristaurants.ristaurants.misc.*;

import java.util.List;

public class DishesReviews extends Activity {
    // instance variables
    private DishReviewsAdapter mAdapter;
    private String mDishName;
	private String mDishID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dishes_reviews);

        // set action bar background
        HelperClass.setActionBarBackground(this, R.color.restaurants_bg);

        // set-up action bar
        getActionBar().setTitle(HelperClass.setActionbarTitle(this, getResources().getString(R.string.ab_title_reviews)));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.ic_action_navigation_previous_item);

        // get data from previous activity
        if (getIntent().getExtras() != null) {
            // get dish review class name and ID
			mDishID = getIntent().getExtras().getString("mDishID");
			
            // display dish image
            String dishImageUrl = getIntent().getExtras().getString("mDishImageUrl");
            NetworkImageView mIvDishImage = (NetworkImageView) findViewById(R.id.niv_dish_reviews_image);
            mIvDishImage.setImageUrl(dishImageUrl, SingletonVolley.getImageLoader());
            
			// set image to black and white
			HelperClass.toGrayScale(mIvDishImage);

            // display dish name
            mDishName = getIntent().getExtras().getString("mDishName", "No Dish Name");
            final TextView mTvDishName = (TextView) findViewById(R.id.tv_dish_reviews_name);
            mTvDishName.setText(mDishName);

            // animate dish name
            ObjectAnimator.ofFloat(mTvDishName, "alpha", 0f, 1f).setDuration(1000).start();

            // get data from database
            makeNetworkCall("DishesReviews");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // get data from database
        makeNetworkCall("DishesReviews");
    }

    /**
     * Get the list of review from the database.
     *
     * @param className The class name to search for in the database.
     */
    private void makeNetworkCall(String className) {
        // get data from database
        ParseQuery<ParseObject> parseObject = ParseQuery.getQuery(className);
        parseObject.whereEqualTo("dishName", mDishName);
        parseObject.orderByDescending("createdAt");
        parseObject.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjectList, ParseException e) {
                if (e == null) {
                    // set list view adapter
                    mAdapter = new DishReviewsAdapter(DishesReviews.this, parseObjectList);

                    // set list view
                    final ListView mLvContent = (ListView) findViewById(R.id.lv_content);
                    mLvContent.setAdapter(mAdapter);
                } else {
                    Log.e("ParseObject", "Error: " + e.getMessage());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dish_review, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // go back to previous screens
                onBackPressed();
                break;
            case R.id.menu_dish_review_add:
                // open the add reviews activity
                Intent intent = new Intent(this, AddDishReview.class);
                intent.putExtra("mDishName", mDishName);
				intent.putExtra("mDishID", mDishID);
                startActivity(intent);

                // set activity animation
                this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // set activity animation
        this.overridePendingTransition(R.anim.anim_null, R.anim.anim_slide_out_right);
    }
}
