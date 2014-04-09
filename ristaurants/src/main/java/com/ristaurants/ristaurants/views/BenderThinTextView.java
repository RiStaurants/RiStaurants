package com.ristaurants.ristaurants.views;

import android.widget.*;
import android.util.*;
import android.content.*;
import android.database.*;
import android.graphics.*;

/**
 * Class that extends TextView, to change font to Roboto - Thin
 */
public class BenderThinTextView extends TextView {

    public BenderThinTextView(Context mContext, AttributeSet mAttrs){
        super(mContext, mAttrs);

        if (!isInEditMode()){
            // set text view font
            Typeface mFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/Bender-Thin.otf");
            this.setTypeface(mFont);
        }
    }
}
