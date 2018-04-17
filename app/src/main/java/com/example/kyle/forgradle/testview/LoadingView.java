package com.example.kyle.forgradle.testview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.kyle.forgradle.R;

public class LoadingView extends RelativeLayout {
    private static final String TAG = "LoadingView";
    public static final int DURATION = 300;

    private Context mContext;
    private ImageView[] mCircles;
    private AnimatorSet[] mAnimSets;
    private static final int TRANSLATE_X_DP = 56 - 12;

    public LoadingView(Context context) {
        super(context);

        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context) {
        mContext = context;
        View view = View.inflate(context, R.layout.loading_view_layout, this);
        mCircles = new ImageView[4];
        int[] circleIds = new int[]{R.id.loading_circle1, R.id.loading_circle2, R.id
                .loading_circle3, R.id.loading_circle4};
        for (int i = 0; i < 4; i++) {
            mCircles[i] = (ImageView) view.findViewById(circleIds[i]);
        }

        mAnimSets = new AnimatorSet[4];
        for (int i = 0; i < 4; i++) {
            mAnimSets[i] = new AnimatorSet();
            initAnim(mCircles[i], mAnimSets[i]);
        }
    }

    public void start() {
        for (int i = 0; i < 4; i++) {
            final int j = i;
            mAnimSets[i].addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAnimSets[j].start();
                }
            });
        }

        mCircles[0].setVisibility(VISIBLE);
        mAnimSets[0].start();
        postDelayed(mR1, DURATION);
        postDelayed(mR2, DURATION * 2);
        postDelayed(mR3, DURATION * 3);
    }

    private Runnable mR1 = new Runnable() {
        @Override
        public void run() {
            mCircles[1].setVisibility(VISIBLE);
            mAnimSets[1].start();
        }
    };

    private Runnable mR2 = new Runnable() {
        @Override
        public void run() {
            mCircles[2].setVisibility(VISIBLE);
            mAnimSets[2].start();
        }
    };

    private Runnable mR3 = new Runnable() {
        @Override
        public void run() {
            mCircles[3].setVisibility(VISIBLE);
            mAnimSets[3].start();
        }
    };

    public void stop() {
        removeCallbacks(mR1);
        removeCallbacks(mR2);
        removeCallbacks(mR3);
        for (int i = 0; i < 4; i++) {
            mAnimSets[i].removeAllListeners();
            mCircles[i].clearAnimation();
            mAnimSets[i].cancel();
            mCircles[i].setVisibility(GONE);
        }
    }

    private void initAnim(final View view, final AnimatorSet animatorSet) {
        float TRANSLATE_X_LENGTH = dip2px(mContext, TRANSLATE_X_DP);

        Log.d(TAG, "initAnim,  TRANSLATE_X_LENGTH: " + TRANSLATE_X_LENGTH);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        scaleX.setDuration(DURATION);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        scaleY.setDuration(DURATION);
        ObjectAnimator transLeft = ObjectAnimator.ofFloat(view, "translationX", 0f,
                TRANSLATE_X_LENGTH);
        transLeft.setInterpolator(new AccelerateDecelerateInterpolator());
        transLeft.setDuration(DURATION * 2);
        ObjectAnimator scaleEndX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        scaleEndX.setDuration(DURATION);
        ObjectAnimator scaleEndY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);
        scaleEndY.setDuration(DURATION);
        ObjectAnimator transRight = ObjectAnimator.ofFloat(view, "translationX",
                TRANSLATE_X_LENGTH, 0f);
        transRight.setDuration(0);

        animatorSet.play(scaleX).with(scaleY).before(transLeft);
        animatorSet.play(transLeft).before(scaleEndX);
        animatorSet.play(scaleEndX).with(scaleEndY).before(transRight);
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
