package com.monster.handscan.protecthealth.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.adapters.SharedPrefsManager;
import com.monster.handscan.protecthealth.database.SQLiteDBHelper;
import com.monster.handscan.protecthealth.fragment.HomeFragment;
import com.monster.handscan.protecthealth.utils.AppOpenManager;
import com.monster.handscan.protecthealth.utils.StringUtil;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    private static MainActivity mSelf;
    public SQLiteDBHelper db;
    private AdView mAdView;
    public AppOpenManager appOpenManager;
    private InterstitialAd interstitialAd;
    Button settingBtn, noticeBtn, historyBtn, adviceBtn, scanBtn;

    public OnInterstitialListener interstitialListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSelf = this;
        mAdView = findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        appOpenManager = new AppOpenManager(this);
        appOpenManager.fetchAd();
        loadAd();
        db = new SQLiteDBHelper(this);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, new SplashFragment(), null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPrefsManager.getInstance().putLong("lastShow", 0);
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
            interstitialAd.show(this);
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
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                StringUtil.INTER_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        MainActivity.this.interstitialAd = interstitialAd;
//                        Toast.makeText(MainActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MainActivity.this.interstitialAd = null;
                                        if (interstitialListener != null) {
                                            interstitialListener.onGameInterstitialClosed();
                                        }
                                        loadAd();
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        if (interstitialListener != null) {
                                            interstitialListener.onGameInterstitialShowFailed();
                                        }
                                        MainActivity.this.interstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        loadAd();
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        interstitialAd = null;
                    }
                });
    }

    public interface OnInterstitialListener {
        void onGameInterstitialClosed();

        void onGameInterstitialShowFailed();
    }

}