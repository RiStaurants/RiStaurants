package com.ristaurants.ristaurants.app;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.ristaurants.ristaurants.misc.HelperClass;
import com.ristaurants.ristaurants.misc.SingletonVolley;
import com.ristaurants.ristaurants.views.QuickReturnScrollview;

/**
 * Restaurant Description Activity
 */
public class RestaurantDesc extends Activity implements QuickReturnScrollview.Callbacks {
    // instance variables
    private QuickReturnScrollview mScrollViewer;
    private String mName;
    private String mPhoneNumber;
    private String mAddress;
	private String mRestaurantID;
    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_desc);

        // get data from previous activity
        if (getIntent().getExtras() != null) {
            String image = getIntent().getExtras().getString("mImage");
            mName = getIntent().getExtras().getString("mName");
            mAddress = getIntent().getExtras().getString("mAddress");
			mRestaurantID = getIntent().getExtras().getString("mRestaurantId");
            mPhoneNumber = getIntent().getExtras().getString("mPhone");
            mLatitude = getIntent().getExtras().getDouble("mLatitude");
            mLongitude = getIntent().getExtras().getDouble("mLongitude");
            String desc = getIntent().getExtras().getString("mDesc");
            int rate = getIntent().getExtras().getInt("mRate");

            // set-up action bar
            getActionBar().setTitle(HelperClass.setActionbarTitle(this, mName));
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.ic_action_navigation_previous_item);

            // scroll viewer
            mScrollViewer = (QuickReturnScrollview) findViewById(R.id.sv_scroll_container);
            mScrollViewer.setCallbacks(this);

            // display main image
            NetworkImageView ivRestaurantImage = (NetworkImageView) findViewById(R.id.niv_restaurant_image);
            ivRestaurantImage.setImageUrl(image, SingletonVolley.getImageLoader());
        
            // display main image
            NetworkImageView ivRestaurantRate = (NetworkImageView) findViewById(R.id.niv_restaurant_rate);
            ivRestaurantRate.setImageUrl(HelperClass.getRateImage(this, rate), SingletonVolley.getImageLoader());

            // add to favorites
            final ImageView mIvFavorites = (ImageView) findViewById(R.id.iv_favorites);
            mIvFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIvFavorites.setImageResource(R.drawable.ic_action_rating_favorite_blue);

                    // get restaurant object
                    ParseObject parseObject = new ParseObject("");

                    ParseUser user = ParseUser.getCurrentUser();

                    Toast.makeText(RestaurantDesc.this, mRestaurantID, Toast.LENGTH_LONG).show();
                }
            });

            // display restaurant name
            TextView tvRestaurantName = (TextView) findViewById(R.id.tv_restaurant_name);
            tvRestaurantName.setText(mName);

            // display restaurant phone
            TextView tvRestaurantPhone = (TextView) findViewById(R.id.tv_restaurant_phone);
            tvRestaurantPhone.setText("phone: " + mPhoneNumber);

            // display restaurant address
            TextView tvRestaurantAddress = (TextView) findViewById(R.id.tv_restaurant_address);
            tvRestaurantAddress.setText(formatString(mAddress));

            // display restaurant address
            TextView tvRestaurantDesc = (TextView) findViewById(R.id.tv_restaurant_desc);
            tvRestaurantDesc.setText(formatString(desc));

            // set animation
            ObjectAnimator.ofFloat(tvRestaurantDesc, "alpha", 0f, 1f).setDuration(1500).start();
        }
    }

    /**
     * Replace \n in the string with a line separator
     *
     * @param string string to be formatted.
     * @return Return the formatted string.
     */
    private String formatString(String string) {
        if (string.contains("\\n")) {
            string = string.replace("\\n", System.getProperty("line.separator"));
        }
        return string;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_restaurant_desc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // variable
        Intent intent;

        switch (item.getItemId()) {
            case android.R.id.home:
                // go back to previous screens
                onBackPressed();
                break;
            case R.id.menu_call:
                // open phone dialer with phone number
                intent = new Intent(Intent.ACTION_DIAL, null);
                intent.setData(Uri.parse("tel:" + mPhoneNumber));
                startActivity(intent);
                break;
			case R.id.menu_menu:
				intent = new Intent(RestaurantDesc.this, RestaurantMenuActivity.class);
				intent.putExtra("mRestaurantName", mName);
				startActivity(intent);
				
				// animation
				overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_null);
				break;
            case R.id.menu_location:
                // open Google Map
                intent = new Intent(RestaurantDesc.this, MapLocation.class);
                intent.putExtra("mRestaurantName", mName);
                intent.putExtra("mAddress", mAddress);
                intent.putExtra("mLatitude", mLatitude);
                intent.putExtra("mLongitude", mLongitude);
                startActivity(intent);
				
				// animation
				overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_null);
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

    @Override
    public void onScrollChanged(int scrollY) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent() {

    }
}
