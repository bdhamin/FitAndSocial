package com.FitAndSocial.app.fragment.helper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by mint on 31-8-14.
 */
public class NonSwipeableViewPager extends ViewPager{

    private boolean swipeable = true;

    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(swipeable){
            return super.onInterceptTouchEvent(event);
        }
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(swipeable){
            return super.onTouchEvent(event);
        }
        // Never allow swiping to switch between pages
        return false;
    }

    public void setIsSwipeable(boolean isSwipeable){
        this.swipeable = isSwipeable;
    }



}
