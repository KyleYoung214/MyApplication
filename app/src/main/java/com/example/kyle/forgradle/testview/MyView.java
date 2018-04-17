package com.example.kyle.forgradle.testview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.kyle.forgradle.R;


public class MyView extends View {
    private static final String TAG = "MyView";

    private Paint mCirclePaint;
    private Paint mProgressPaint;
    private Paint mTextPaint;
    private int mWidth;
    private int mHeight;

    private int mPadding;

    private int mCircleSize = 250;
    private RectF mRectF;

    private int mProgress;


    public MyView(Context context) {
        super(context);

        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
        initAttrs(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
        initAttrs(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mPadding = Math.max(getPaddingLeft(), getPaddingRight());
        mPadding = Math.max(getPaddingBottom(), mPadding);
        mPadding = Math.max(getPaddingTop(), mPadding);

        mRectF.set(mPadding, mPadding, mWidth - mPadding, mWidth - mPadding);

        mCircleSize = Math.min(mWidth, mHeight) / 2 - mPadding;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(5);
        mCirclePaint.setDither(true);

        canvas.drawCircle(mWidth / 2, mWidth / 2, mCircleSize, mCirclePaint);

        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        float baseline = (mRectF.bottom + mRectF.top - fontMetrics.bottom - fontMetrics.top) / 2;

        canvas.drawText(mProgress + "%", mWidth / 2, baseline, mTextPaint);

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(Color.RED);
        mCirclePaint.setStrokeWidth(15);
        mCirclePaint.setDither(true);

        canvas.drawArc(mRectF, -90, mProgress * 360f / 100, false, mCirclePaint/*mProgressPaint*/);
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            mProgress = 0;
        } else if (progress > 100) {
            mProgress = 100;
        } else {
            mProgress = progress;
        }
        invalidate();
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(5);
        mCirclePaint.setDither(true);

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setColor(Color.RED);
        mProgressPaint.setStrokeWidth(15);
        mProgressPaint.setDither(true);
        //        CornerPathEffect cornerPathEffect = new CornerPathEffect(20);
        //        mProgressPaint.setPathEffect(cornerPathEffect);


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(50);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mRectF = new RectF();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyView);

        int circleColor = typedArray.getColor(R.styleable.MyView_circle_color, Color.BLUE);
        mCirclePaint.setColor(circleColor);

        int progressColor = typedArray.getColor(R.styleable.MyView_progress_color, Color.RED);
        mProgressPaint.setColor(progressColor);

        mProgress = typedArray.getInteger(R.styleable.MyView_progress, 50);

        typedArray.recycle();
    }

}
