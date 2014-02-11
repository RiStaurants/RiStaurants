package com.ristaurants.ristaurants.misc;
import android.app.*;
import android.graphics.drawable.*;
import com.ristaurants.ristaurants.app.*;

public class HelperClass {
	
	/**
	 * Set action bar bacground
	 *
	 * @params activity The activity to set ActionBar bacground to.
	 * @params resColorId The Drawable color to set the ActionBar to.
	 *
	 */
	public static void setActionBarBackground(Activity activity, int resColorId){
		// change action bar color
		activity.getActionBar().setBackgroundDrawable(new ColorDrawable(activity.getResources().getInteger(resColorId)));
		activity.getActionBar().setDisplayShowTitleEnabled(false);
		activity.getActionBar().setDisplayShowTitleEnabled(true);
	}
}
