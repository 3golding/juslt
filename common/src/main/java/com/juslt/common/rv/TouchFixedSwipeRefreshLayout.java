package com.juslt.common.rv;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by User on 2016/12/14.
 */

public class TouchFixedSwipeRefreshLayout extends SwipeRefreshLayout {

    private int mTouchSloop;
    private float mPrevX;

    public TouchFixedSwipeRefreshLayout(Context context) {
        super(context);
        mTouchSloop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public TouchFixedSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSloop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);
                if (xDiff > mTouchSloop) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);
    }
}
