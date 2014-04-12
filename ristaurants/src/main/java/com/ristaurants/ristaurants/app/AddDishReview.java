package com.ristaurants.ristaurants.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.ristaurants.ristaurants.misc.HelperClass;

import android.widget.*;

public class AddDishReview extends Activity {
    // instance variables
    private EditText mEtDesc;
    private Spinner mNpRate;
    private String mDishName;
    private String mDishID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish_review);

        // instantiate Parse Database
        Parse.initialize(this, "WB3Th85cP3viS7jJ5zkXzkZ2MTsFagIg0AKQeBpQ", "EGZKA60G8Iy4vVCPPvBDjn2XoeBbqQ1rtWReRvRh");

        // set-up action bar
        getActionBar().setTitle(HelperClass.setActionbarTitle(this, getResources().getString(R.string.ab_title_add_dish_review)));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.ic_action_navigation_previous_item);

        // instantiate view
        mEtDesc = (EditText) findViewById(R.id.et_desc);
        mNpRate = (Spinner) findViewById(R.id.sp_rate);

        // get data from previous activity
        if (getIntent().getExtras() != null) {
            mDishName = getIntent().getExtras().getString("mDishName");
            mDishID = getIntent().getExtras().getString("mDishID");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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

    public void onAddReview(View view) {
        // check if fields are empty
        if (!mEtDesc.getText().toString().equals("")) {
            // link review to dish
            ParseObject dishPointerID = ParseObject.createWithoutData("RestaurantsMenus", mDishID);

            // create and upload review to parse
            ParseObject parseObjectReview = new ParseObject("DishesReviews");
            parseObjectReview.put("dishName", mDishName);
            parseObjectReview.put("username", ParseUser.getCurrentUser().getUsername());
            parseObjectReview.put("review", mEtDesc.getText().toString());
            parseObjectReview.put("rate", Integer.parseInt(mNpRate.getSelectedItem().toString()));
            parseObjectReview.put("dishPointer", dishPointerID);
            parseObjectReview.saveInBackground();

            // update dish review counter
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("RestaurantsMenus");
            parseQuery.getInBackground(mDishID, new GetCallback<ParseObject>() {
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        parseObject.increment("dishReviewsCount");
                        parseObject.saveInBackground();
                    }
                }
            });

            // return to previous activity
            onBackPressed();

            // let user know the review was added
            Toast.makeText(this, R.string.review_was_added, Toast.LENGTH_LONG).show();
        } else {
            // let user know to complete all fields
            Toast.makeText(this, R.string.please_fill_all_fields, Toast.LENGTH_LONG).show();
        }
    }
}
