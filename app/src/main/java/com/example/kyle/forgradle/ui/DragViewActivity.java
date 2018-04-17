package com.example.kyle.forgradle.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.kyle.forgradle.R;
import com.example.kyle.forgradle.testview.DragViewGroup2;
import com.example.kyle.forgradle.testview.LoadingView;


public class DragViewActivity extends AppCompatActivity {
    private static final String TAG = "DragViewActivity";

    private boolean mBln;

    private ImageView[] mCircles;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragview);

        final DragViewGroup2 dragViewGroup2 = (DragViewGroup2) findViewById(R.id.dragview2);


        mCircles = new ImageView[4];
        int[] circleIds = new int[]{R.id.loading_circle1, R.id.loading_circle2, R.id
                .loading_circle3, R.id.loading_circle4};
        for (int i = 0; i < 4; i++) {
            mCircles[i] = (ImageView) findViewById(circleIds[i]);
        }

        //        final AnimatorSet startAnim = new AnimatorSet();
        //        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mCircles[0], "scaleX", 0f, 1f);
        //        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mCircles[0], "scaleY", 0f, 1f);
        //        startAnim.setDuration(1000);
        //        startAnim.play(scaleX).with(scaleY);


        final AnimatorSet[] animatorSets = new AnimatorSet[4];
        for (int i = 0; i < 4; i++) {
            animatorSets[i] = new AnimatorSet();
            initAnim(mCircles[i], animatorSets[i]);
        }

        final LoadingView loadingView = (LoadingView) findViewById(R.id.load_view);
        Button btn = (Button) findViewById(R.id.btn_test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick");
                //                dragViewGroup2.testSmoothSlide(mBln);
                //                mBln = !mBln;
                if (mBln) {
                    loadingView.start();
                } else {
                    loadingView.stop();
                }

                mBln = !mBln;

            }
        });

        //        final AnimatorSet endAnim = new AnimatorSet();
        //        ObjectAnimator scaleEndX = ObjectAnimator.ofFloat(mCircles[0], "scaleX", 1f, 0f);
        //        ObjectAnimator scaleEndY = ObjectAnimator.ofFloat(mCircles[0], "scaleY", 1f, 0f);
        //        endAnim.setDuration(1000);
        //        endAnim.play(scaleEndX).with(scaleEndY);
        //
        //
        //        final ObjectAnimator transRight = ObjectAnimator.ofFloat(mCircles[0],
        // "translationX",
        //                300f, 0f);
        //        transRight.setDuration(2000);
        //        transRight.addListener(new AnimatorListenerAdapter() {
        //            @Override
        //            public void onAnimationEnd(Animator animation) {
        //                super.onAnimationEnd(animation);
        //                startAnim.start();
        //            }
        //        });
        //
        //
        //        endAnim.addListener(new AnimatorListenerAdapter() {
        //            @Override
        //            public void onAnimationEnd(Animator animation) {
        //                super.onAnimationEnd(animation);
        //                transRight.start();
        //            }
        //        });
        //
        //        final ObjectAnimator transLeft = ObjectAnimator.ofFloat(mCircles[0],
        // "translationX", 0f,
        //                300f);
        //        transLeft.setDuration(2000);
        //        AnimatorListenerAdapter transListener = new AnimatorListenerAdapter() {
        //            @Override
        //            public void onAnimationEnd(Animator animation) {
        //                super.onAnimationEnd(animation);
        //                endAnim.start();
        //            }
        //        };
        //
        //        transLeft.addListener(transListener);
        //
        //        AnimatorListenerAdapter startListener = new AnimatorListenerAdapter() {
        //            @Override
        //            public void onAnimationEnd(Animator animation) {
        //                super.onAnimationEnd(animation);
        //                transLeft.start();
        //            }
        //        };
        //        startAnim.addListener(startListener);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initAnim(final View view, final AnimatorSet animatorSet) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        animatorSet.setDuration(750);
        animatorSet.play(scaleX).with(scaleY);

        final AnimatorSet endAnim = new AnimatorSet();
        ObjectAnimator scaleEndX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        ObjectAnimator scaleEndY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);
        endAnim.setDuration(750);
        endAnim.play(scaleEndX).with(scaleEndY);


        final ObjectAnimator transRight = ObjectAnimator.ofFloat(view, "translationX", 500f, 0f);
        transRight.setDuration(0);
        transRight.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet.start();
            }
        });


        endAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                transRight.start();
            }
        });

        final ObjectAnimator transLeft = ObjectAnimator.ofFloat(view, "translationX", 0f, 500f);
        transLeft.setDuration(1500);
        AnimatorListenerAdapter transListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endAnim.start();
            }
        };

        transLeft.addListener(transListener);

        AnimatorListenerAdapter startListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                transLeft.start();
            }
        };
        animatorSet.addListener(startListener);
    }

    private static class WrapperView {
        private View mTarget;

        public WrapperView(View target) {
            mTarget = target;
        }

        public void setTranslateX(float x) {

        }

    }

}
