package com.example.kyle.forgradle.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kyle.forgradle.R;
import com.example.kyle.forgradle.testeventbus.MessageEvent;
import com.example.kyle.forgradle.testeventbus.SomeOtherEvent;
import com.example.kyle.forgradle.testview.SoundView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class SoundBtnActivity extends AppCompatActivity {
    private static final String TAG = "SoundBtnActivity";

    private ImageView[] mImgs;
    private AnimatorSet mSilenceSet;
    private AnimatorSet mSoundSet;

    private SoundView mSoundView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundbtn);

        int[] res = new int[]{R.id.img_start, R.id.img_stop, R.id.img_circle1, R.id.img_circle2,
                R.id.img_circle3};
        int len = res.length;
        mImgs = new ImageView[len];
        for (int i = 0; i < len; i++) {
            mImgs[i] = (ImageView) findViewById(res[i]);
        }

        mSilenceSet = new AnimatorSet();
        mSoundSet = new AnimatorSet();

        initAnimation();

        mSoundView = (SoundView) findViewById(R.id.soundbtn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.i(TAG, "onMessageEvent, thread name: " + Thread.currentThread().getName());
        Toast.makeText(this, "message event: " + event.message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleSomethingElse(SomeOtherEvent event) {
        Log.i(TAG, "handleSomethingElse, thread name: " + Thread.currentThread().getName());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.test_event_bus1:
                EventBus.getDefault().post(new MessageEvent("test 1"));
                break;
            case R.id.test_event_bus2:
                EventBus.getDefault().post(new SomeOtherEvent("test 2", "data blablabla.."));
                break;
        }
    }

    public void onSnackbarClick(View view) {
        Snackbar.make(findViewById(R.id.llayout), "hahaha", Snackbar.LENGTH_SHORT).setAction
                ("press", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSoundView.onStart();
            }
        }).show();
    }

    public void onStartClick1(View view) {
        mSoundView.onStart();
    }

    public void onSoundClick1(View view) {
        mSoundView.onSoundReceive();
    }

    public void onSilenceClick1(View view) {
        mSoundView.onSilence();
    }

    public void onStopClick1(View view) {
        mSoundView.onStop();
    }

    public void onDisable(View view) {
        mSoundView.setFuncDisable();
    }

    public void onStartClick(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mImgs[0], "alpha", 1f, 0f);
        objectAnimator.setDuration(2000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mImgs[1], "alpha", 0f, 1f);
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mImgs[2], "alpha", 0f, 1f);
                objectAnimator1.setDuration(2000);
                objectAnimator2.setDuration(2000);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(objectAnimator1).with(objectAnimator2);
                animatorSet.start();
            }
        });

        objectAnimator.start();
    }

    public void onSoundClick(View view) {
        mSilenceSet.cancel();

        if (!mSoundSet.isRunning()) {
            mSoundSet.start();
        }
    }

    public void onSilenceClick(View view) {
        mSoundSet.cancel();

        mImgs[3].animate().alpha(0f).setDuration(1000).start();
        mImgs[3].animate().scaleX(0.5f).setDuration(1000).start();
        mImgs[3].animate().scaleY(0.5f).setDuration(1000).start();
        mImgs[4].animate().alpha(0f).setDuration(1000).start();
        mImgs[4].animate().scaleX(0.5f).setDuration(1000).start();
        mImgs[4].animate().scaleY(0.5f).setDuration(1000).start();

        if (!mSilenceSet.isRunning()) {
            mSilenceSet.start();
        }
    }

    public void onStopClick(View view) {
        mSilenceSet.cancel();
        mSoundSet.cancel();

        mImgs[2].setAlpha(0f);
        mImgs[3].setAlpha(0f);
        mImgs[4].setAlpha(0f);

        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(mImgs[1], "alpha", 1f, 0f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(mImgs[2], "alpha", 1f, 0f);
        objectAnimator1.setDuration(2000);
        objectAnimator2.setDuration(2000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mImgs[0], "alpha", 0f, 1f);
                objectAnimator.setDuration(2000);
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
        ObjectAnimator circle2Alpha = ObjectAnimator.ofFloat(mImgs[3], "alpha", 0.8f, 1f);
        ObjectAnimator circle2ScaleX = ObjectAnimator.ofFloat(mImgs[3], "scaleX", 0.5f, 1f);
        ObjectAnimator circle2ScaleY = ObjectAnimator.ofFloat(mImgs[3], "scaleY", 0.5f, 1f);
        ObjectAnimator circle3Alpha = ObjectAnimator.ofFloat(mImgs[4], "alpha", 0.8f, 1f);
        ObjectAnimator circle3ScaleX = ObjectAnimator.ofFloat(mImgs[4], "scaleX", 0.5f, 1f);
        ObjectAnimator circle3ScaleY = ObjectAnimator.ofFloat(mImgs[4], "scaleY", 0.5f, 1f);

        circle1Alpha.setDuration(2000);
        circle1ScaleX.setDuration(2000);
        circle1ScaleY.setDuration(2000);

        circle2ScaleX.setDuration(2000);
        circle2ScaleY.setDuration(2000);
        circle2Alpha.setDuration(2000);

        circle3ScaleX.setDuration(2000);
        circle3ScaleY.setDuration(2000);
        circle3Alpha.setDuration(2000);

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
        ObjectAnimator silenceCircleAlpha = ObjectAnimator.ofFloat(mImgs[2], "alpha", 0.7f, 1f);
        silenceCircleScaleX.setDuration(2000);
        silenceCircleScaleY.setDuration(2000);
        silenceCircleAlpha.setDuration(2000);
        silenceCircleScaleX.setRepeatMode(ValueAnimator.REVERSE);
        silenceCircleScaleY.setRepeatMode(ValueAnimator.REVERSE);
        silenceCircleAlpha.setRepeatMode(ValueAnimator.REVERSE);
        silenceCircleScaleX.setRepeatCount(ValueAnimator.INFINITE);
        silenceCircleScaleY.setRepeatCount(ValueAnimator.INFINITE);
        silenceCircleAlpha.setRepeatCount(ValueAnimator.INFINITE);
        mSilenceSet.play(silenceCircleScaleX).with(silenceCircleScaleY).with(silenceCircleAlpha);
    }

    //    public void onShowClick(View view) {
    //        mShowTv.setVisibility(View.VISIBLE);
    //
    //        ValueAnimator animator = ValueAnimator.ofInt(0, mShowTvHeight);
    //        animator.setInterpolator(new LinearInterpolator());
    //        animator.setDuration(2000);
    //        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    //            @Override
    //            public void onAnimationUpdate(ValueAnimator animation) {
    //                ViewGroup.LayoutParams layoutParams = mShowTv.getLayoutParams();
    //                layoutParams.height = (int) animation.getAnimatedValue();
    //                mShowTv.setLayoutParams(layoutParams);
    //            }
    //        });
    //        animator.start();
    //    }
    //
    //    public void onHideClick(View view) {
    //        ValueAnimator animator = ValueAnimator.ofInt(mShowTvHeight, 0);
    //        // default is AccelerateInterpolator
    //        animator.setInterpolator(new LinearInterpolator());
    //        animator.setDuration(2000);
    //        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    //            @Override
    //            public void onAnimationUpdate(ValueAnimator animation) {
    //                ViewGroup.LayoutParams layoutParams = mShowTv.getLayoutParams();
    //                layoutParams.height = (int) animation.getAnimatedValue();
    //                mShowTv.setLayoutParams(layoutParams);
    //            }
    //        });
    //        animator.addListener(new AnimatorListenerAdapter() {
    //            @Override
    //            public void onAnimationEnd(Animator animation) {
    //                mShowTv.setVisibility(View.GONE);
    //            }
    //        });
    //        animator.start();
    //    }
    //
    //    public void onTvReset(View view) {
    //        mCountDownTv.setText("5s");
    //    }
    //
    //    public void onTvCountDown(View view) {
    //        ValueAnimator animator = ValueAnimator.ofInt(5, 0);
    //        animator.setInterpolator(new LinearInterpolator());
    //        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    //            @Override
    //            public void onAnimationUpdate(ValueAnimator animation) {
    //                int sec = (int) animation.getAnimatedValue();
    //                mCountDownTv.setText(sec + "s");
    //                Log.i(TAG, "onAnimationUpdate: " + sec);
    //            }
    //        });
    //        animator.setDuration(5000);
    //        animator.start();
    //    }

    //    public void onBatteryUp(View view) {
    //        mBatteryImg.setImageResource(R.drawable.battery_up);
    //        AnimationDrawable batteryUp = (AnimationDrawable) mBatteryImg.getDrawable();
    //        batteryUp.start();
    //    }
    //
    //    public void onBatteryDown(View view) {
    //        mBatteryImg.setImageResource(R.drawable.battery_down);
    //        AnimationDrawable batteryDown = (AnimationDrawable) mBatteryImg.getDrawable();
    //        batteryDown.start();
    //    }
    //
    //    public void onObjAnimClick(View view) {
    //        ObjectAnimator alpha = ObjectAnimator.ofFloat(mImg, "alpha", 1f, 0f, 1f);
    //        alpha.setInterpolator(new AccelerateDecelerateInterpolator());
    //
    //        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mImg, "scaleX", 0.3f, 1f);
    //        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mImg, "scaleY", 0.3f, 1f);
    //        scaleY.setRepeatMode(ObjectAnimator.REVERSE);
    //        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
    //
    //        AnimatorSet set = new AnimatorSet();
    //        set.setDuration(2000);
    //        set.play(scaleX).with(scaleY).after(alpha);
    //        set.start();
    //
    //        set.addListener(new Animator.AnimatorListener() {
    //            @Override
    //            public void onAnimationStart(Animator animation) {
    //
    //            }
    //
    //            @Override
    //            public void onAnimationEnd(Animator animation) {
    //
    //            }
    //
    //            @Override
    //            public void onAnimationCancel(Animator animation) {
    //
    //            }
    //
    //            @Override
    //            public void onAnimationRepeat(Animator animation) {
    //
    //            }
    //        });
    //    }
    //
    //    public void onPvhClick(View view) {
    //        PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("translationX", 100f);
    //        PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("translationY", 100f);
    //        PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat("alpha", 1, 0.2f, 1);
    //        ObjectAnimator.ofPropertyValuesHolder(mImg, pvh1, pvh2, pvh3).setDuration(3000)
    // .start();
    //    }
    //
    //    public void onValueClick(View view) {
    //        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
    //        valueAnimator.setTarget(mImg);
    //        valueAnimator.setDuration(2000).start();
    //        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    //            private FloatEvaluator evaluator = new FloatEvaluator();
    //
    //            @Override
    //            public void onAnimationUpdate(ValueAnimator animation) {
    //                float value = (float) animation.getAnimatedValue();
    //                float fraction = animation.getAnimatedFraction();
    //                float alphaValue = value * fraction;
    //
    //                float calValue = evaluator.evaluate(fraction, 0, 1);
    //                Log.i(TAG, "onAnimationUpdate, alphaValue: " + alphaValue + ", calValue: " +
    //                        calValue);
    //
    //                mImg.setAlpha(calValue);
    //
    //            }
    //        });
    //    }
    //
    //    public void onFuncClick(View view) {
    //        mImg.animate().alpha(0).setDuration(2000).withStartAction(new Runnable() {
    //            @Override
    //            public void run() {
    //                Log.i(TAG, "start action");
    //            }
    //        }).withEndAction(new Runnable() {
    //            @Override
    //            public void run() {
    //                Log.i(TAG, "end action");
    //            }
    //        }).start();
    //    }
}
