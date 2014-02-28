package com.ristaurants.ristaurants.misc;
import android.app.*;
import android.graphics.drawable.*;
import android.text.Spannable;
import android.text.SpannableString;

import com.parse.ParseObject;
import com.ristaurants.ristaurants.app.*;
import com.ristaurants.ristaurants.views.ActionBarFont;
import android.content.*;

import org.json.JSONArray;

import java.util.List;
import android.graphics.*;
import android.widget.*;
import android.text.*;

public class HelperClass {
	
	/**
	 * Set action bar bacground
	 *
	 * @param activity The activity to set ActionBar background to.
	 * @param resColorId The Drawable color to set the ActionBar to.
	 *
	 */
	public static void setActionBarBackground(Activity activity, int resColorId){
		// change action bar color
		activity.getActionBar().setBackgroundDrawable(new ColorDrawable(activity.getResources().getInteger(resColorId)));
		activity.getActionBar().setDisplayShowTitleEnabled(false);
		activity.getActionBar().setDisplayShowTitleEnabled(true);
	}

    /**
     * Set action bar to custom font
     *
     * @param activity The activity to set ActionBar title to.
     * @param title The title to display.
     */
    public static SpannableString setActionbarTitle(Activity activity, String title) {
        // create custom font for action bar
        SpannableString customActionBarFont = new SpannableString(title);
        customActionBarFont.setSpan(new ActionBarFont(activity, "Bender-Solid.otf"), 0, customActionBarFont.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return customActionBarFont;
    }
	
	/**
	 * Get the url of the rate image.
	 *
	 * @param context The context.
	 * @param rateValue Which rate image url to return.
	 */
	public static String getRateImage(Context context, int rateValue){
		// array containing all the rate images urls
		String[] rateImageUrl = context.getResources().getStringArray(R.array.rate_image_urls);
		
		// return rate image url
		return rateImageUrl[rateValue];
	}
	
	/**
     * Convert image view to gray scale.
     *
     * @param imageView The ImageView to convert to GrayScale.
     */
    public static void toGrayScale(ImageView imageView){
        float[] colorMatrix = {
			0.33f, 0.33f, 0.33f, 0, 0, //red
			0.33f, 0.33f, 0.33f, 0, 0, //green
			0.33f, 0.33f, 0.33f, 0, 0, //blue
			0, 0, 0, 1, 0    //alpha
        };

        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(colorFilter);
    }
}
