package com.ristaurants.ristaurants.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.ristaurants.ristaurants.misc.HelperClass;

import android.widget.*;

import java.io.ByteArrayOutputStream;

public class AddDishReview extends Activity {
    // instance variables
    private ParseFile mParseFileImage;
    private EditText mEtDesc;
    private Spinner mNpRate;
    private Spinner mNpFlavors;
    private boolean mContainImage;
    private String mDishName;
    private String mDishID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish_review);

        // set-up action bar
        getActionBar().setTitle(HelperClass.setActionbarTitle(this, getResources().getString(R.string.ab_title_add_dish_review)));
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.ic_action_navigation_previous_item);

        // instantiate view
        mEtDesc = (EditText) findViewById(R.id.et_desc);
        mNpRate = (Spinner) findViewById(R.id.sp_rate);
        mNpFlavors = (Spinner) findViewById(R.id.sp_flavors);

        // get data from previous activity
        if (getIntent().getExtras() != null) {
            mDishName = getIntent().getExtras().getString("mDishName");
            mDishID = getIntent().getExtras().getString("mDishID");
            mContainImage = getIntent().getExtras().getBoolean("mContainImage");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // inflate menu
        getMenuInflater().inflate(R.menu.menu_add_review, menu);

        // show/hide upload image option
        menu.findItem(R.id.menu_upload_image).setVisible(mContainImage);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_upload_image:
                openCamera();
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

    /**
     * Open camera to take a dish image.
     */
    private void openCamera() {
        // open camera
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    /**
     * Add user review
     * @param view The view clicked
     */
    public void onAddReview(View view) {
        // check if fields are empty
        if (!mEtDesc.getText().toString().equals("")) {
            //
            ParseUser user = ParseUser.getCurrentUser();
            // link review to dish
            ParseObject dishPointerID = ParseObject.createWithoutData("RestaurantsMenus", mDishID);
            ParseObject userPointerID = ParseObject.createWithoutData("_User", user.getObjectId());

            // create and upload review to parse
            ParseObject parseObjectReview = new ParseObject("DishesReviews");
            parseObjectReview.put("username", ParseUser.getCurrentUser().getString("name"));
            parseObjectReview.put("dishName", mDishName);
            parseObjectReview.put("review", mEtDesc.getText().toString());
            parseObjectReview.put("rate", Integer.parseInt(mNpRate.getSelectedItem().toString()));
            parseObjectReview.put("flavor", mNpFlavors.getSelectedItem().toString().toLowerCase());
            parseObjectReview.put("dishPointer", dishPointerID);
            parseObjectReview.put("userPointer", userPointerID);

            // save object
            parseObjectReview.saveInBackground();

            // update user total post count
            user.increment("postTotal");
            user.saveInBackground();

            // update dish review counter
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("RestaurantsMenus");
            parseQuery.getInBackground(mDishID, new GetCallback<ParseObject>() {
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        parseObject.increment("dishReviewsCount");

                        // upload dish image url
                        if (mParseFileImage != null) {
                            parseObject.put("dishImage", mParseFileImage.getUrl());
                        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // get captured image
        try {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ImageView mIvImagePreview = (ImageView) findViewById(R.id.dish_image_preview);
            mIvImagePreview.setImageBitmap(bitmap);

            // convert to image to byte[]
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            // save image to parse
            mParseFileImage = new ParseFile("dish_image", stream.toByteArray());
            mParseFileImage.saveInBackground();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
