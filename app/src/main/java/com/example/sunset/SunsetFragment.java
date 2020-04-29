package com.example.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;
    private AnimatorSet mSunriseAnimatorSet;
    private AnimatorSet mSunsetAnimatorSet;
    private boolean mSunBack = false;


    public static SunsetFragment newInstance(){
        return new SunsetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);
        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSunBack) {
                    startAnimation();
                    if (mSunriseAnimatorSet != null) {
                        mSunriseAnimatorSet.end();
                        mSunriseAnimatorSet = null;
                    }
                } else {
                    reverseAnimation();
                    if (mSunsetAnimatorSet != null) {
                        mSunsetAnimatorSet.end();
                        mSunsetAnimatorSet = null;
                    }
                }
                mSunBack = !mSunBack;
                startSunHeatAnimation();
            }
        });
        return view;
    }

    private void startAnimation(){
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

//        heightAnimator.start();
//        sunsetSkyAnimator.start();

        mSunsetAnimatorSet = new AnimatorSet();
        mSunsetAnimatorSet
                .play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);

        mSunsetAnimatorSet.start();
    }

    private void reverseAnimation(){
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYEnd, sunYStart)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mNightSkyColor, mSunsetSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        mSunriseAnimatorSet = new AnimatorSet();
        mSunriseAnimatorSet
                .play(sunsetSkyAnimator)
                .with(heightAnimator)
                .after(nightSkyAnimator);

        mSunriseAnimatorSet.start();
    }

    private void startSunHeatAnimation() {
        ObjectAnimator heatAnimator = ObjectAnimator
                .ofPropertyValuesHolder(mSunView,
                        PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.1f))
                .setDuration(3000);
        heatAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        heatAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        heatAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        heatAnimator.start();
    }

}
