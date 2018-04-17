package com.example.kyle.forgradle.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kyle.forgradle.R;


public class AnimActivity extends AppCompatActivity {
    private static final String TAG = "AnimActivity";

    private ImageView mImg;
    private TranslateAnimation translateAnimation;
    private ScaleAnimation scaleAnimation;
    private AlphaAnimation alphaAnimation;
    private RotateAnimation rotateAnimation;
    private AnimationSet animationSet;

    private ImageView mBatteryImg;
    private TextView mCountDownTv;
    private TextView mShowTv;
    private int mShowTvHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        mImg = (ImageView) findViewById(R.id.img_miao);
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnimActivity.this, "img clicked", Toast.LENGTH_SHORT).show();
            }
        });

        mBatteryImg = (ImageView) findViewById(R.id.img_battery);

        mCountDownTv = (TextView) findViewById(R.id.tv_countdown);

        mShowTv = (TextView) findViewById(R.id.tv_show);

        mShowTvHeight = (int) (getResources().getDisplayMetrics().density * 60 + 0.5);
        Log.i(TAG, "onCreate: mShowTvHeight: " + mShowTvHeight);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onShowClick(View view) {
        mShowTv.setVisibility(View.VISIBLE);

        ValueAnimator animator = ValueAnimator.ofInt(0, mShowTvHeight);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams layoutParams = mShowTv.getLayoutParams();
                layoutParams.height = (int) animation.getAnimatedValue();
                mShowTv.setLayoutParams(layoutParams);
            }
        });
        animator.start();
    }

    public void onHideClick(View view) {
        ValueAnimator animator = ValueAnimator.ofInt(mShowTvHeight, 0);
        // default is AccelerateInterpolator
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams layoutParams = mShowTv.getLayoutParams();
                layoutParams.height = (int) animation.getAnimatedValue();
                mShowTv.setLayoutParams(layoutParams);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mShowTv.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    public void onTvReset(View view) {
        mCountDownTv.setText("5s");
    }

    public void onTvCountDown(View view) {
        ValueAnimator animator = ValueAnimator.ofInt(5, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int sec = (int) animation.getAnimatedValue();
                mCountDownTv.setText(sec + "s");
                Log.i(TAG, "onAnimationUpdate: " + sec);
            }
        });
        animator.setDuration(5000);
        animator.start();
    }

    public void onClearAnim(View view) {
        mImg.clearAnimation();
    }

    public void onStartAnim(View view) {
        final int TIME = 2 * 1000;
        translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 300, 0, 400);
        translateAnimation.setDuration(TIME);
        translateAnimation.setFillAfter(true);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setRepeatCount(1);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "onAnimationStart: translateAnimation");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG, "onAnimationEnd: translateAnimation");
                //mImg.startAnimation(scaleAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i(TAG, "onAnimationRepeat: translateAnimation");
            }
        });

        scaleAnimation = new ScaleAnimation(1, 0.5f, 1, 0.5f, mImg.getWidth() / 2, mImg.getHeight
                () / 2);
        scaleAnimation.setDuration(TIME);
        scaleAnimation.setFillBefore(false);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "onAnimationStart: scaleAnimation");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG, "onAnimationEnd: scaleAnimation");
                mImg.startAnimation(alphaAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i(TAG, "onAnimationRepeat: scaleAnimation");
            }
        });

        alphaAnimation = new AlphaAnimation(1, 0.6f);
        alphaAnimation.setDuration(TIME);
        alphaAnimation.setFillBefore(false);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "onAnimationStart: alphaAnimation");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG, "onAnimationEnd: alphaAnimation");
                mImg.startAnimation(rotateAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i(TAG, "onAnimationRepeat: alphaAnimation");
            }
        });

        rotateAnimation = new RotateAnimation(0, 180, mImg.getWidth() / 2, mImg.getHeight() / 2);
        rotateAnimation.setDuration(TIME);
        rotateAnimation.setFillBefore(false);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "onAnimationStart: rotateAnimation");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG, "onAnimationEnd: rotateAnimation");
                mImg.startAnimation(animationSet);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i(TAG, "onAnimationRepeat: rotateAnimation");
            }
        });

        animationSet = new AnimationSet(false);
        animationSet.setFillBefore(false);
        animationSet.setFillAfter(true);
        //        animationSet.setin

        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i(TAG, "onAnimationStart: animationSet");
                rotateAnimation.setAnimationListener(null);
                alphaAnimation.setAnimationListener(null);
                translateAnimation.setAnimationListener(null);
                scaleAnimation.setAnimationListener(null);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG, "onAnimationEnd: animationSet");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i(TAG, "onAnimationRepeat: animationSet");
            }
        });

        mImg.startAnimation(translateAnimation);
    }

    public void onBatteryUp(View view) {
        mBatteryImg.setImageResource(R.drawable.battery_up);
        AnimationDrawable batteryUp = (AnimationDrawable) mBatteryImg.getDrawable();
        batteryUp.start();
    }

    public void onBatteryDown(View view) {
        mBatteryImg.setImageResource(R.drawable.battery_down);
        AnimationDrawable batteryDown = (AnimationDrawable) mBatteryImg.getDrawable();
        batteryDown.start();
    }

    public void onObjAnimClick(View view) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mImg, "alpha", 1f, 0f, 1f);
        alpha.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mImg, "scaleX", 0.3f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mImg, "scaleY", 0.3f, 1f);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(2000);
        set.play(scaleX).with(scaleY).after(alpha);
        set.start();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void onPvhClick(View view) {
        PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("translationX", 100f);
        PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("translationY", 100f);
        PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat("alpha", 1, 0.2f, 1);
        ObjectAnimator.ofPropertyValuesHolder(mImg, pvh1, pvh2, pvh3).setDuration(3000).start();
    }

    public void onValueClick(View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setTarget(mImg);
        valueAnimator.setDuration(2000).start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private FloatEvaluator evaluator = new FloatEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float fraction = animation.getAnimatedFraction();
                float alphaValue = value * fraction;

                float calValue = evaluator.evaluate(fraction, 0, 1);
                Log.i(TAG, "onAnimationUpdate, alphaValue: " + alphaValue + ", calValue: " +
                        calValue);

                mImg.setAlpha(calValue);

            }
        });
    }

    public void onFuncClick(View view) {
        mImg.animate().alpha(0).setDuration(2000).withStartAction(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "start action");
            }
        }).withEndAction(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "end action");
            }
        }).start();
    }
}
