package com.ristaurants.ristaurants.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * Custom scroll view for restaurant desc
 */
public class QuickReturnScrollview extends ScrollView {
    // instances
    private Callbacks mCallbacks;
    private Context mContext;

    // constructor
    public QuickReturnScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    /**
     * @param l    Current horizontal scroll origin.
     * @param t    Current vertical scroll origin.
     * @param oldl Previous horizontal scroll origin.
     * @param oldt Previous vertical scroll origin.
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        //
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(t);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //
        if (mCallbacks != null) {
            switch (ev.getActionMasked()) {
                // ACTION_DOWN = A pressed gesture has started, the motion contains the initial starting location.
                case MotionEvent.ACTION_DOWN:
                    mCallbacks.onDownMotionEvent();
                    break;

                // ACTION_UP = A pressed gesture has finished, the motion contains the final release location
                // as well as any intermediate points since the last down or move event.
                case MotionEvent.ACTION_UP:

                // ACTION_CANCEL = The current gesture has been aborted.
                case MotionEvent.ACTION_CANCEL:
                    mCallbacks.onUpOrCancelMotionEvent();
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * The scroll range of a scroll view is the overall height of all of its children.
     *
     * @return the total vertical range represented by the vertical scrollbar.
     * <p/>
     * The default range is the drawing height of this view.
     */
    @Override
    protected int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    /**
     * Set the call backs
     *
     * @param callbacks The callback to be set.
     */
    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    // call back interface
    public static interface Callbacks {
        public void onScrollChanged(int scrollY);

        public void onDownMotionEvent();

        public void onUpOrCancelMotionEvent();
    }
}
