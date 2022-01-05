package com.monster.handscan.protecthealth.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.monster.handscan.protecthealth.activity.MainActivity;

import java.util.Date;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks {

    private static final String LOG_TAG = "AppOpenManager";
    private AppOpenAd appOpenAd = null;
    private static boolean isShowingAd = false;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private long loadTime = 0;

    private MainActivity myApplication;

    /**
     * Constructor
     */
    public AppOpenManager(MainActivity myApplication) {
        this.myApplication = myApplication;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.myApplication.registerActivityLifecycleCallbacks(this);
        }
        fetchAd();
    }

    /**
     * Request an ad
     */
    public void fetchAd() {
        if (isAdAvailable()) {
            Log.w(LOG_TAG, "Available Fetch");
            return;
        }
        Log.w(LOG_TAG, "Start Fetch");

        loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    public void onAppOpenAdLoaded(AppOpenAd ad) {
                        AppOpenManager.this.appOpenAd = ad;
                        AppOpenManager.this.loadTime = (new Date()).getTime();
                        Log.w(LOG_TAG, "Success");
                    }

                    public void onAppOpenAdFailedToLoad(LoadAdError var1) {
                        Log.w(LOG_TAG, "Fail");
                    }

                };
        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, StringUtil.APP_OPEN_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    /**
     * Utility method that checks if ad exists and can be shown.
     */
    public boolean isAdAvailable() {
        Log.w(LOG_TAG, "Available");
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    public void showAdIfAvailable() {
        Log.w(LOG_TAG, "Available Show");
        // Only show ad if there is not already an app open ad currently showing
        // and an ad is available.
        Log.d(LOG_TAG, "show ad.");
        if (!isShowingAd && isAdAvailable()) {
            Log.d(LOG_TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            AppOpenManager.this.appOpenAd = null;
                            isShowingAd = false;
                            fetchAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };
            myApplication.runOnUiThread(() -> {
                appOpenAd.show(myApplication);
            });

        } else {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd();
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        myApplication = (MainActivity) activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        myApplication = (MainActivity) activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
