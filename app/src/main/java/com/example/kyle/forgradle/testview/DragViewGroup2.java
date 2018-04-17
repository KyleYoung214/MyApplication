package com.example.kyle.forgradle.testview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;


public class DragViewGroup2 extends FrameLayout {
    private static final String TAG = "DragViewGroup2";
    private ViewDragHelper mDragHelper;

    private float mOriginalX;
    private float mOriginalY;


    public DragViewGroup2(@NonNull Context context) {
        super(context);
        init();
    }

    public DragViewGroup2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public DragViewGroup2(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                mOriginalX = capturedChild.getX();
                mOriginalY = capturedChild.getY();
                Log.i(TAG, "onViewCaptured: " + mOriginalX + ", " + mOriginalY);
            }

            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                Log.i(TAG, "onEdgeTouched: " + edgeFlags);
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
                Log.i(TAG, "onEdgeDragStarted: " + edgeFlags);
                mDragHelper.captureChildView(getChildAt(getChildCount() - 1), pointerId);
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                View child = getChildAt(0);
                if (child != null && child == releasedChild) {
                    mDragHelper.flingCapturedView(getPaddingLeft(), getPaddingTop(), getWidth() -
                            getPaddingRight() - child.getWidth(), getHeight() - getPaddingBottom
                            () - child.getHeight());
                } else {

                    mDragHelper.settleCapturedViewAt((int) mOriginalX, (int) mOriginalY);
                }
                invalidate();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 1;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return 1;
            }
        });

        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper != null && mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }


    public void testSmoothSlide(boolean isReverse) {
        if (mDragHelper != null) {
            View child = getChildAt(1);
            if (child != null) {
                if (isReverse) {
                    mDragHelper.smoothSlideViewTo(child, getLeft(), getTop());
                } else {
                    mDragHelper.smoothSlideViewTo(child, getRight() - child.getWidth(), getBottom
                            () - child.getHeight());
                }
                invalidate();
            }
        }
    }
}
