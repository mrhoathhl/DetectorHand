package com.monster.handscan.protecthealth.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.applovin.sdk.AppLovinSdkUtils;
import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.adapters.SharedPrefsManager;
import com.monster.handscan.protecthealth.database.SQLiteDBHelper;
import com.monster.handscan.protecthealth.fragment.HomeFragment;
import com.monster.handscan.protecthealth.utils.StringUtil;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    private static MainActivity mSelf;
    public SQLiteDBHelper db;
    private MaxAdView adView;
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;
    private boolean mBannerInitialized;
    public OnInterstitialListener interstitialListener;

    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSelf = this;

        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, configuration -> {
        });

        getWindow().getDecorView().setSystemUiVisibility(flags);

        // Code below is to handle presses of Volume up or Volume down.
        // Without this, after pressing volume buttons, the navigation bar will
        // show up and won't hide
        final View decorView = getWindow().getDecorView();
        decorView
                .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            decorView.setSystemUiVisibility(flags);
                        }
                    }
                });
        loadAd();
        db = new SQLiteDBHelper(this);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, new HomeFragment(), null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPrefsManager.getInstance().putLong("lastShow", 0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public static MainActivity self() {
        return mSelf;
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        fragmentTransaction1.replace(R.id.mainContainer, fragment);
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commitAllowingStateLoss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    public void showInterstitial(OnInterstitialListener listener) {
        interstitialListener = listener;
        // Show the ad if it's ready. Otherwise toast and restart the game.
        long lastShow = SharedPrefsManager.getInstance().getLong("lastShow");
        if (Math.abs(System.currentTimeMillis() - lastShow) >= 30000 && interstitialAd != null) {
            interstitialAd.showAd();
            SharedPrefsManager.getInstance().putLong("lastShow", System.currentTimeMillis());
        } else {
            if (interstitialListener != null) {
                interstitialListener.onGameInterstitialShowFailed();
            }
//            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showInterstitial(null);
    }

    public void loadAd() {

        interstitialAd = new MaxInterstitialAd(StringUtil.INTER_ID, this);
        interstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(MaxAd ad) {
                retryAttempt = 0;
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
//                if (interstitialListener != null) {
//                    interstitialListener.onGameInterstitialClosed();
//                }
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                if (interstitialListener != null) {
                    interstitialListener.onGameInterstitialClosed();
                }
                interstitialAd.loadAd();
            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                retryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        interstitialAd.loadAd();
                    }
                }, delayMillis);
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                if (interstitialListener != null) {
                    interstitialListener.onGameInterstitialShowFailed();
                }
                interstitialAd.loadAd();
            }
        });
        interstitialAd.loadAd();
    }

    void loadBannerAd() {
        adView = new MaxAdView(StringUtil.BANNER_ID, this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT, Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        addContentView(adView, params);
        adView.setGravity(Gravity.BOTTOM);
        adView.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        adView.loadAd();
    }

    public void showBanner() {
        try {
            if (!mBannerInitialized) {
                mBannerInitialized = true;
                loadBannerAd();
            } else {
                Log.e("Banner", "show");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> adView.setVisibility(View.VISIBLE));
            }
        } catch (NullPointerException e) {
            Log.e("Null Banner", e.getMessage());
        }
    }

    public void hideBanner() {
        if (adView != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adView.setVisibility(View.GONE);
                }
            });
        }
    }

    public interface OnInterstitialListener {
        void onGameInterstitialClosed();

        void onGameInterstitialShowFailed();
    }

}