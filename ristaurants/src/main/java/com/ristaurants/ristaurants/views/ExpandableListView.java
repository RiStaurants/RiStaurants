package com.ristaurants.ristaurants.views;

import android.content.*;
import android.widget.*;
import android.util.*;
import android.view.*;

public class ExpandableListView extends ListView {
	
	
	public ExpandableListView(Context context){
		super(context);
		init();
	}
	
	public ExpandableListView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}
	
	public ExpandableListView(Context context, AttributeSet attrs, int desStyle){
		super(context, attrs, desStyle);
		init();
	}
	
	private void init(){
		setOnItemClickListener(mItemClickListener);
	}
	
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// expand or collapse clicked view
			
		}
	};
	
	private int[] getTopAndBottomTranslation(int top, int bottom, int yDelta, boolean isExpanding){
		// variables
		int yTranslationTop = 0;
		int yTranslationBottom = yDelta;
		int height = (bottom - top);
		
		// if it is expanding
		if (isExpanding){
			//
			boolean isOverTop = top < 0;
			boolean isBelowBottom = (top + height + yDelta) > getHeight();
			
			//
			if (isOverTop){
				yTranslationTop = top;
				yTranslationBottom = (yDelta - yTranslationTop);
			} else if (isBelowBottom){
				int deltaBelow = (top + height - getHeight());
				yTranslationTop = (top - deltaBelow) < 0 ? top : deltaBelow;
				yTranslationBottom = (yDelta - yTranslationTop);
			}
		} else {
			//
			int offset = computeVerticalScrollOffset();
			int range = computeVerticalScrollRange();
			int extent = computeVerticalScrollExtent();
			int leftOverExtent = (range - offset - extent);
			
			boolean isCollapsingBelowBottom = (yTranslationBottom > leftOverExtent);
			boolean isCellCompletelyDisappearing = (bottom - yTranslationBottom) < 0;
			
			//
			if (isCollapsingBelowBottom){
				yTranslationTop = (yTranslationBottom - leftOverExtent);
				yTranslationBottom = (yDelta - yTranslationTop);
			} else if (isCellCompletelyDisappearing){
				yTranslationBottom = bottom;
				yTranslationTop = (yDelta - yTranslationBottom);
			}
		}
		
		return new int[] {yTranslationTop, yTranslationBottom};
	}
}
