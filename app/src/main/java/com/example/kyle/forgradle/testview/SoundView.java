package com.example.kyle.forgradle.testview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.kyle.forgradle.R;

public class SoundView extends RelativeLayout {
    private static final String TAG = "SoundView";
    private static final int START_STOP_TIME = 500;
    private static final int NORMAL_TIME = 1000;

    private ImageView[] mImgs;
    private AnimatorSet mSilenceSet;
    private AnimatorSet mSoundSet;

    public SoundView(Context context) {
        super(context);

        init(context);
    }

    public SoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context) {
        View view = View.inflate(context, R.layout.soundview_layout, this);

        int[] res = new int[]{R.id.img_start, R.id.img_stop, R.id.img_circle1, R.id.img_circle2,
                R.id.img_circle3};
        int len = res.length;
        mImgs = new ImageView[len];
        for (int i = 0; i < len; i++) {
            mImgs[i] = (ImageView) view.findViewById(res[i]);
        }

        mSilenceSet = new AnimatorSet();
        mSoundSet = new AnimatorSet();

        initAnimation();
    }

    public void setFuncDisable() {
        mImgs[0].setAlpha(0.4f);
    }

    public void onStart() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mImgs[0], "alpha", 1f, 0f);
        objectAnimator.setDuration(START_STOP_TIME);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mImgs[1], "alpha", 0f, 1f);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mImgs[2], "alpha", 0f, 1f);
                objectAnimator1.setDuration(START_STOP_TIME);
                objectAnimator2.setDuration(START_STOP_TIME);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(objectAnimator1).with(objectAnimator2);
                animatorSet.start();
            }
        });

        objectAnimator.start();
    }

    public void onSoundReceive() {
        mSilenceSet.cancel();

        if (!mSoundSet.isRunning()) {
            mSoundSet.start();
        }
    }

    public void onSilence() {
        mSoundSet.cancel();

        mImgs[3].animate().alpha(0f).setDuration(START_STOP_TIME).start();
        mImgs[3].animate().scaleX(0.5f).setDuration(START_STOP_TIME).start();
        mImgs[3].animate().scaleY(0.5f).setDuration(START_STOP_TIME).start();
        mImgs[4].animate().alpha(0f).setDuration(START_STOP_TIME).start();
        mImgs[4].animate().scaleX(0.5f).setDuration(START_STOP_TIME).start();
        mImgs[4].animate().scaleY(0.5f).setDuration(START_STOP_TIME).start();

        if (!mSilenceSet.isRunning()) {
            mSilenceSet.start();
        }
    }

    public void onStop() {
        mSilenceSet.cancel();
        mSoundSet.cancel();

        mImgs[2].setAlpha(0f);
        mImgs[3].setAlpha(0f);
        mImgs[4].setAlpha(0f);

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mImgs[1], "alpha", 1f, 0f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mImgs[2], "alpha", 1f, 0f);
        objectAnimator1.setDuration(NORMAL_TIME);
        objectAnimator2.setDuration(NORMAL_TIME);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mImgs[0], "alpha", 0f, 1f);
                objectAnimator.setDuration(NORMAL_TIME);
                objectAnimator.start();
            }
        });
        animatorSet.play(objectAnimator1).with(objectAnimator2);
        animatorSet.start();
    }

    private void initAnimation() {
        // sound
        ObjectAnimator circle1Alpha = ObjectAnimator.ofFloat(mImgs[2], "alpha", 0.8f, 1f);
        ObjectAnimator circle1ScaleX = ObjectAnimator.ofFloat(mImgs[2], "scaleX", 0.8f, 1f);
        ObjectAnimator circle1ScaleY = ObjectAnimator.ofFloat(mImgs[2], "scaleY", 0.8f, 1f);
        ObjectAnimator circle2Alpha = ObjectAnimator.ofFloat(mImgs[3], "alpha", 0.7f, 0.9f);
        ObjectAnimator circle2ScaleX = ObjectAnimator.ofFloat(mImgs[3], "scaleX", 0.5f, 1f);
        ObjectAnimator circle2ScaleY = ObjectAnimator.ofFloat(mImgs[3], "scaleY", 0.5f, 1f);
        ObjectAnimator circle3Alpha = ObjectAnimator.ofFloat(mImgs[4], "alpha", 0.6f, 0.8f);
        ObjectAnimator circle3ScaleX = ObjectAnimator.ofFloat(mImgs[4], "scaleX", 0.4f, 1f);
        ObjectAnimator circle3ScaleY = ObjectAnimator.ofFloat(mImgs[4], "scaleY", 0.4f, 1f);

        circle1Alpha.setDuration(NORMAL_TIME);
        circle1ScaleX.setDuration(NORMAL_TIME);
        circle1ScaleY.setDuration(NORMAL_TIME);

        circle2ScaleX.setDuration(NORMAL_TIME);
        circle2ScaleY.setDuration(NORMAL_TIME);
        circle2Alpha.setDuration(NORMAL_TIME);

        circle3ScaleX.setDuration(NORMAL_TIME);
        circle3ScaleY.setDuration(NORMAL_TIME);
        circle3Alpha.setDuration(NORMAL_TIME);

        circle1Alpha.setRepeatMode(ValueAnimator.REVERSE);
        circle1ScaleX.setRepeatMode(ValueAnimator.REVERSE);
        circle1ScaleY.setRepeatMode(ValueAnimator.REVERSE);

        circle2ScaleX.setRepeatMode(ValueAnimator.REVERSE);
        circle2ScaleY.setRepeatMode(ValueAnimator.REVERSE);
        circle2Alpha.setRepeatMode(ValueAnimator.REVERSE);

        circle3ScaleX.setRepeatMode(ValueAnimator.REVERSE);
        circle3ScaleY.setRepeatMode(ValueAnimator.REVERSE);
        circle3Alpha.setRepeatMode(ValueAnimator.REVERSE);

        circle1Alpha.setRepeatCount(ValueAnimator.INFINITE);
        circle2ScaleX.setRepeatCount(ValueAnimator.INFINITE);
        circle2ScaleY.setRepeatCount(ValueAnimator.INFINITE);

        circle2ScaleX.setRepeatCount(ValueAnimator.INFINITE);
        circle2ScaleY.setRepeatCount(ValueAnimator.INFINITE);
        circle2Alpha.setRepeatCount(ValueAnimator.INFINITE);


        circle3ScaleX.setRepeatCount(ValueAnimator.INFINITE);
        circle3ScaleY.setRepeatCount(ValueAnimator.INFINITE);
        circle3Alpha.setRepeatCount(ValueAnimator.INFINITE);

        mSoundSet.play(circle1Alpha).with(circle1ScaleX).with(circle1ScaleY).with(circle2ScaleX)
                .with(circle2ScaleY).with(circle2Alpha).with(circle3ScaleX).with(circle3ScaleY)
                .with(circle3Alpha);


        // silence
        ObjectAnimator silenceCircleScaleX = ObjectAnimator.ofFloat(mImgs[2], "scaleX", 0.9f, 1.1f);
        ObjectAnimator silenceCircleScaleY = ObjectAnimator.ofFloat(mImgs[2], "scaleY", 0.9f, 1.1f);
        ObjectAnimator silenceCircleAlpha = ObjectAnimator.ofFloat(mImgs[2], "alpha", 0.5f, 1f);
        silenceCircleScaleX.setDuration(NORMAL_TIME);
        silenceCircleScaleY.setDuration(NORMAL_TIME);
        silenceCircleAlpha.setDuration(NORMAL_TIME);
        silenceCircleScaleX.setRepeatMode(ValueAnimator.REVERSE);
        silenceCircleScaleY.setRepeatMode(ValueAnimator.REVERSE);
        silenceCircleAlpha.setRepeatMode(ValueAnimator.REVERSE);
        silenceCircleScaleX.setRepeatCount(ValueAnimator.INFINITE);
        silenceCircleScaleY.setRepeatCount(ValueAnimator.INFINITE);
        silenceCircleAlpha.setRepeatCount(ValueAnimator.INFINITE);
        mSilenceSet.play(silenceCircleScaleX).with(silenceCircleScaleY).with(silenceCircleAlpha);
    }

}
