package com.ristaurants.ristaurants.app;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.ristaurants.ristaurants.misc.HelperClass;
import com.ristaurants.ristaurants.misc.SingletonVolley;
import com.ristaurants.ristaurants.views.QuickReturnScrollview;

import java.util.List;

/**
 * Restaurant Description Activity
 */
public class RestaurantDesc extends Activity implements QuickReturnScrollview.Callbacks {
    // instance variables
    private QuickReturnScrollview mScrollViewer;
    private boolean mIsFavorite;
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
            mRestaurantID = getIntent().getExtras().getString("mRestaurantID");
            mPhoneNumber = getIntent().getExtras().getString("mPhone");
            mLatitude = getIntent().getExtras().getDouble("mLatitude");
            mLongitude = getIntent().getExtras().getDouble("mLongitude");
            mIsFavorite = getIntent().getExtras().getBoolean("mIsFavorite");
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

            // add to favorites
            final ImageView mIvFavorites = (ImageView) findViewById(R.id.iv_favorites);
            mIvFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // check if the user is logged in
                    if (ParseUser.getCurrentUser() == null) {
                        // let user know to login
                        final AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantDesc.this);
                        builder.setMessage(R.string.please_login);
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                        // show dialog
                        builder.show();
                    }
                }
            });

            // check if user is logged in
            if (ParseUser.getCurrentUser() != null) {
                // get current object
                ParseQuery<ParseObject> query = ParseQuery.getQuery("RestaurantList");
                query.getInBackground(mRestaurantID, new GetCallback<ParseObject>() {
                    public void done(final ParseObject object, ParseException e) {
                        if (e == null) {
                            // check if restaurant is already a favorite
                            ParseQuery<ParseObject> relation = ParseUser.getCurrentUser().getRelation("favorites").getQuery();
                            relation.whereEqualTo("objectId", object.getObjectId());
                            relation.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(final List<ParseObject> parseObjectList, ParseException e) {
                                    if (e == null) {
                                        // set flag
                                        mIsFavorite = parseObjectList.size() == 1;

                                        // set image color
                                        if (mIsFavorite) {
                                            mIvFavorites.setImageResource(R.drawable.ic_action_rating_favorite_blue);
                                        } else {
                                            mIvFavorites.setImageResource(R.drawable.ic_action_rating_favorite_white);
                                        }

                                        // handle add/remove from favorites
                                        mIvFavorites.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                    // get relations
                                                    ParseRelation<ParseObject> relation = ParseUser.getCurrentUser().getRelation("favorites");

                                                    // add restaurant to object
                                                    if (!mIsFavorite) {
                                                        // add to favorites
                                                        relation.add(object);

                                                        // set favorite flag
                                                        mIsFavorite = true;

                                                        // let user know the restaurant was added to favorites
                                                        Toast.makeText(RestaurantDesc.this, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();

                                                        // change favorites icon color
                                                        mIvFavorites.setImageResource(R.drawable.ic_action_rating_favorite_blue);

                                                    } else if (mIsFavorite) {
                                                        // add to favorites
                                                        relation.remove(object);

                                                        // set favorite flag
                                                        mIsFavorite = false;

                                                        // let user know the restaurant was removed from favorites
                                                        Toast.makeText(RestaurantDesc.this, R.string.remove_to_favorites, Toast.LENGTH_SHORT).show();

                                                        // change favorites icon color
                                                        mIvFavorites.setImageResource(R.drawable.ic_action_rating_favorite_white);
                                                    }

                                                    // save user
                                                    ParseUser.getCurrentUser().saveInBackground();

                                            }
                                        });
                                    } else {
                                        // There was an error
                                        Toast.makeText(RestaurantDesc.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            // There was an error
                            Toast.makeText(RestaurantDesc.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

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
