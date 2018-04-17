package com.example.kyle.forgradle.testview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;


public class DragViewGroup extends FrameLayout {
    enum State {
        IDLE, DRAGGING;
    }

    private float mLastX;
    private float mLastY;

    private View mDragView;

    private State mCurState;

    private int mSlop;

    public DragViewGroup(@NonNull Context context) {
        super(context);
        init();
    }

    public DragViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public DragViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();

        setMeasuredDimension(measureDimension(200, widthMeasureSpec), measureDimension(200,
                heightMeasureSpec));
    }

    private int measureDimension(int defaultSize, int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result = 0;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isPointOnViews(event)) {
                    mLastX = event.getX();
                    mLastY = event.getY();
                    mCurState = State.DRAGGING;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (event.getX() - mLastX);
                int deltaY = (int) (event.getY() - mLastY);
                if (mCurState == State.DRAGGING && mDragView != null && Math.abs(deltaX) > mSlop
                        && Math.abs(deltaY) > mSlop) {
                    ViewCompat.offsetLeftAndRight(mDragView, deltaX);
                    ViewCompat.offsetTopAndBottom(mDragView, deltaY);
                    mLastX = event.getX();
                    mLastY = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mCurState == State.DRAGGING) {
                    mCurState = State.IDLE;
                    mDragView = null;
                }
                break;
        }
        return true;
    }

    private void init() {
        mSlop = ViewConfiguration.getWindowTouchSlop();
    }

    private boolean isPointOnViews(MotionEvent event) {
        boolean result = false;
        Rect rect = new Rect();

        int len = getChildCount();
        for (int i = len - 1; i >= 0; i--) {
            View view = getChildAt(i);

            rect.set((int) view.getX(), (int) view.getY(), (int) (view.getX() + view.getWidth()),
                    (int) (view.getY() + view.getHeight()));

            if (rect.contains((int) event.getX(), (int) event.getY())) {
                mDragView = view;

                result = true;
                break;
            }
        }

        return result && mCurState != State.DRAGGING;
    }
}
