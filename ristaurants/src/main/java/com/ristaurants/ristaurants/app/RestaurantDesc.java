package com.ristaurants.ristaurants.app;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ristaurants.ristaurants.misc.HelperClass;
import com.ristaurants.ristaurants.misc.SingletonVolley;

/**
 * Restaurant Description Activity
 */
public class RestaurantDesc extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_desc);

        // get data from previous activity
        if (getIntent().getExtras() != null) {
            String image = getIntent().getExtras().getString("mImage");
            String name = getIntent().getExtras().getString("mName");
            String address = getIntent().getExtras().getString("mAddress");
            String phone = getIntent().getExtras().getString("mPhone");
            String desc = getIntent().getExtras().getString("mDesc", getResources().getString(R.string.no_description_available));
            int rate = getIntent().getExtras().getInt("mRate");

            // set action bar background color
            HelperClass.setActionBarBackground(this, R.color.menus_bg);

            // set-up action bar
            getActionBar().setTitle(HelperClass.setActionbarTitle(this, name));
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setIcon(R.drawable.ic_action_navigation_previous_item);

            // display main image
            NetworkImageView ivRestaurantImage = (NetworkImageView) findViewById(R.id.niv_restaurant_image);
            ivRestaurantImage.setImageUrl(image, SingletonVolley.getImageLoader());
            //HelperClass.toGrayScale(ivRestaurantImage);

            // display main image
            NetworkImageView ivRestaurantRate = (NetworkImageView) findViewById(R.id.niv_restaurant_rate);
            ivRestaurantRate.setImageUrl(HelperClass.getRateImage(this, rate), SingletonVolley.getImageLoader());

            // display restaurant name
            TextView tvRestaurantName = (TextView) findViewById(R.id.tv_restaurant_name);
            tvRestaurantName.setText(name);

            // display restaurant phone
            TextView tvRestaurantPhone = (TextView) findViewById(R.id.tv_restaurant_phone);
            tvRestaurantPhone.setText("phone: " + phone);

            // display restaurant address
            TextView tvRestaurantAddress = (TextView) findViewById(R.id.tv_restaurant_address);
            tvRestaurantAddress.setText(formatString(address));

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
