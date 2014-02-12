package com.ristaurants.ristaurants.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Class that extends TextView, to change font to Bender - Solid
 */
public class BenderSolidTextView extends TextView {

    public BenderSolidTextView(Context mContext, AttributeSet mAttrs){
        super(mContext, mAttrs);

        if (!isInEditMode()){
            // set text view font
            Typeface mFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/Bender-Solid.otf");
            this.setTypeface(mFont);
        }
    }
}
