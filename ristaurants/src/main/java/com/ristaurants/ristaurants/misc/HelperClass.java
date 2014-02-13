package com.ristaurants.ristaurants.misc;
import android.app.*;
import android.graphics.drawable.*;
import android.text.Spannable;
import android.text.SpannableString;

import com.ristaurants.ristaurants.app.*;
import com.ristaurants.ristaurants.views.ActionBarFont;

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
}
