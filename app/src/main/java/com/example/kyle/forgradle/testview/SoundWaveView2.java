package com.example.kyle.forgradle.testview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.kyle.forgradle.R;


public class SoundWaveView2 extends View {
    private static final String TAG = "SoundWaveView";
    private static int NORMAL_BASE_SIZE;
    private static int SMALL_BASE_SIZE;

    private Paint mSmallCirclePaint;
    private Paint mNormalCirclePaint;
    private Paint mLargeCirclePaint;
    private int mWidth;
    private int mHeight;

    private int mSmallCircleSize;
    private int mNormalCircleSize;
    private int mLargeCircleSize;
    private RectF mRectF;

    private Runnable mResetRunnable;
    private int mLastAmplitude;

    private Bitmap mCrossImg;


    public SoundWaveView2(Context context) {
        super(context);

        init();
    }

    public SoundWaveView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
        initAttrs(context, attrs);
    }

    public SoundWaveView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
        initAttrs(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mRectF.set(0, 0, mWidth, mWidth);

        mLargeCircleSize = mWidth / 2;
        NORMAL_BASE_SIZE = mLargeCircleSize * 2 / 3;
        SMALL_BASE_SIZE = mLargeCircleSize / 3;

        mNormalCircleSize = NORMAL_BASE_SIZE;
        mSmallCircleSize = SMALL_BASE_SIZE;
        Log.i(TAG, "onMeasure, size: " + mLargeCircleSize + ", " + mNormalCircleSize + ", " +
                mSmallCircleSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mWidth / 2, mWidth / 2, mLargeCircleSize, mLargeCirclePaint);
        canvas.drawCircle(mWidth / 2, mWidth / 2, mNormalCircleSize, mNormalCirclePaint);
        canvas.drawCircle(mWidth / 2, mWidth / 2, mSmallCircleSize, mSmallCirclePaint);


        int bitmapSize = mCrossImg.getWidth();
        float scale = SMALL_BASE_SIZE * 1.0f / bitmapSize;
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        Bitmap dstbmp = Bitmap.createBitmap(mCrossImg, 0, 0, bitmapSize, bitmapSize, matrix, true);

        int top = mLargeCircleSize - SMALL_BASE_SIZE / 2;
        int left = mLargeCircleSize - SMALL_BASE_SIZE / 2;

        canvas.drawBitmap(dstbmp, new Rect(0, 0, SMALL_BASE_SIZE, SMALL_BASE_SIZE), new Rect
                (left, top, SMALL_BASE_SIZE + left, SMALL_BASE_SIZE + top), mSmallCirclePaint);
    }

    private void decrease() {
        mNormalCircleSize -= mLastAmplitude;
        mSmallCircleSize -= mLastAmplitude * 2;
        if (mNormalCircleSize < NORMAL_BASE_SIZE) {
            mNormalCircleSize = NORMAL_BASE_SIZE;
        }

        if (mSmallCircleSize < SMALL_BASE_SIZE) {
            mSmallCircleSize = SMALL_BASE_SIZE;
        }

        invalidate();
    }

    public void reset() {
        mNormalCircleSize = NORMAL_BASE_SIZE;
        mSmallCircleSize = SMALL_BASE_SIZE;
        invalidate();
    }

    public void updateAmplitude(int amplitude) {
        removeCallbacks(mResetRunnable);

        mLastAmplitude = amplitude;

        mNormalCircleSize = amplitude + NORMAL_BASE_SIZE;
        if (mNormalCircleSize >= 420) {
            mNormalCircleSize = 420;
        }

        mSmallCircleSize = (int) (amplitude * 2.0f) + SMALL_BASE_SIZE;
        if (mSmallCircleSize >= 400) {
            mSmallCircleSize = 400;
        }

        Log.i(TAG, "updateAmplitude, size: " + mLargeCircleSize + ", " + mNormalCircleSize + ", "
                + mSmallCircleSize);
        invalidate();

        postDelayed(mResetRunnable, 500);
    }

    private void init() {
        mSmallCirclePaint = new Paint();
        mSmallCirclePaint.setAntiAlias(true);
        mSmallCirclePaint.setColor(Color.RED);
        mSmallCirclePaint.setStyle(Paint.Style.FILL);
        mSmallCirclePaint.setDither(true);

        mNormalCirclePaint = new Paint();
        mNormalCirclePaint.setAntiAlias(true);
        mNormalCirclePaint.setStyle(Paint.Style.FILL);
        mNormalCirclePaint.setColor(Color.BLUE);
        mNormalCirclePaint.setDither(true);
        //        CornerPathEffect cornerPathEffect = new CornerPathEffect(20);
        //        mNormalCirclePaint.setPathEffect(cornerPathEffect);


        mLargeCirclePaint = new Paint();
        mLargeCirclePaint.setAntiAlias(true);
        mLargeCirclePaint.setDither(true);
        mLargeCirclePaint.setColor(Color.YELLOW);
        mLargeCirclePaint.setStyle(Paint.Style.FILL);
        mLargeCirclePaint.setDither(true);

        mRectF = new RectF();

        mResetRunnable = new Runnable() {
            @Override
            public void run() {
                //decrease();
                reset();
            }
        };
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyView);

        int circleColor = typedArray.getColor(R.styleable.MyView_circle_color, Color.BLUE);
        mSmallCirclePaint.setColor(circleColor);

        int progressColor = typedArray.getColor(R.styleable.MyView_progress_color, Color.RED);
        mNormalCirclePaint.setColor(progressColor);

        typedArray.recycle();

        mCrossImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.error);

    }

}
