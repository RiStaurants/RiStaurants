package com.ristaurants.ristaurants.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.ristaurants.ristaurants.misc.HelperClass;
import android.widget.*;

public class AddDishReview extends Activity {
    // instance variables
    private EditText mEtAuthor;
    private EditText mEtDesc;
	private Spinner mNpRate;
    private String mDishReviewClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish_review);

        // instantiate Parse Database
        Parse.initialize(this, "WB3Th85cP3viS7jJ5zkXzkZ2MTsFagIg0AKQeBpQ", "EGZKA60G8Iy4vVCPPvBDjn2XoeBbqQ1rtWReRvRh");

        // set action bar background
        HelperClass.setActionBarBackground(this, R.color.restaurants_bg);

        // set-up action bar
        getActionBar().setTitle(HelperClass.setActionbarTitle(this, getResources().getString(R.string.ab_title_add_dish_review)));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // instantiate view
        mEtAuthor = (EditText) findViewById(R.id.et_author);
        mEtDesc = (EditText) findViewById(R.id.et_desc);
		mNpRate = (Spinner) findViewById(R.id.sp_rate);

        if (getIntent().getExtras() != null) {
            mDishReviewClassName = getIntent().getExtras().getString("mDishReviewClassName");
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
        this.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    public void onAddReview(View view) {
        try {
			// check if fields are empty
			if (!mEtAuthor.getText().toString().equals("") && !mEtDesc.getText().toString().equals("")) {
				// upload review to parse
				ParseObject mParse = new ParseObject(mDishReviewClassName);
				mParse.put("dishReviewAuthor", mEtAuthor.getText().toString());
				mParse.put("dishReviewDesc", mEtDesc.getText().toString());
				mParse.put("dishReviewRate", Integer.parseInt(mNpRate.getSelectedItem().toString()));
				mParse.saveInBackground();

				// return to previous activity
				onBackPressed();

				// let user know the review was added
				Toast.makeText(this, R.string.review_was_added, Toast.LENGTH_LONG).show();
			} else {
				// let user know the review was added
				Toast.makeText(this, R.string.please_fill_all_fields, Toast.LENGTH_LONG).show();
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
