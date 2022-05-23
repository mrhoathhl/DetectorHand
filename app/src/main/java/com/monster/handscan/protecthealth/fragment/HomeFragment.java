package com.monster.handscan.protecthealth.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.activity.DetectorActivity;
import com.monster.handscan.protecthealth.activity.MainActivity;
import com.monster.handscan.protecthealth.utils.StringUtil;

public class HomeFragment extends Fragment implements View.OnClickListener {

    ImageButton settingBtn, noticeBtn, historyBtn, adviceBtn, scanBtn, objectBtn;
    FragmentManager fragmentManager;
    private MaxAd nativeAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("Change", "Change1");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        fragmentManager = requireActivity().getSupportFragmentManager();
        ((MainActivity) requireActivity()).showBanner();
        FrameLayout nativeAdContainer = view.findViewById(R.id.native_ad_layout);

        MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(StringUtil.NATIVE_ID, getContext());
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                // Clean up any pre-existing native ad to prevent memory leaks.
                if (nativeAd != null) {
                    nativeAdLoader.destroy(nativeAd);
                }
                // Save ad for cleanup.
                nativeAd = ad;
                // Add ad view to view.
                nativeAdContainer.removeAllViews();
                nativeAdContainer.addView(nativeAdView);
            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                nativeAdLoader.loadAd();
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {
                // Optional click callback
            }
        });

        nativeAdLoader.loadAd();
        settingBtn = view.findViewById(R.id.settingBtn);
        noticeBtn = view.findViewById(R.id.challenge);
        historyBtn = view.findViewById(R.id.historyBtn);
        adviceBtn = view.findViewById(R.id.adviceBtn);
        scanBtn = view.findViewById(R.id.scanBtn);
        objectBtn = view.findViewById(R.id.objectBtn);


        settingBtn.setOnClickListener(this);
        noticeBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        adviceBtn.setOnClickListener(this);
        scanBtn.setOnClickListener(this);
        objectBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.scanBtn) {
//            Intent intent = new Intent(getContext(), DetectorActivity.class);
//            intent.putExtra("type", "normal");
//            requireActivity().startActivity(intent);
//        } else {
        ((MainActivity) requireActivity()).showInterstitial(new MainActivity.OnInterstitialListener() {
            @Override
            public void onGameInterstitialClosed() {
                switch (v.getId()) {
                    case R.id.scanBtn:
                        Log.e("Show", "Successful1");
                        Intent intent = new Intent(getContext(), DetectorActivity.class);
                        intent.putExtra("type", "normal");
                        requireActivity().startActivity(intent);
                        break;
                    case R.id.settingBtn:
                        ((MainActivity) requireActivity()).changeFragment(new SettingFragment());
                        break;
                    case R.id.challenge:
                        ((MainActivity) requireActivity()).changeFragment(new ChallengeFragment());
                        break;
                    case R.id.historyBtn:
                        ((MainActivity) requireActivity()).changeFragment(new HistoryFragment());
                        break;
                    case R.id.adviceBtn:
                        ((MainActivity) requireActivity()).changeFragment(new AdviceFragment());
                        break;
                    case R.id.objectBtn:
                        ((MainActivity) requireActivity()).changeFragment(new ObjectFragment());
                        break;
                }
            }

            @Override
            public void onGameInterstitialShowFailed() {
                switch (v.getId()) {
                    case R.id.scanBtn:
                        Log.e("Show", "Successful2");
                        Intent intent = new Intent(getContext(), DetectorActivity.class);
                        intent.putExtra("type", "normal");
                        requireActivity().startActivity(intent);
                        break;
                    case R.id.settingBtn:
                        ((MainActivity) requireActivity()).changeFragment(new SettingFragment());
                        break;
                    case R.id.challenge:
                        ((MainActivity) requireActivity()).changeFragment(new ChallengeFragment());
                        break;
                    case R.id.historyBtn:
                        ((MainActivity) requireActivity()).changeFragment(new HistoryFragment());
                        break;
                    case R.id.adviceBtn:
                        ((MainActivity) requireActivity()).changeFragment(new AdviceFragment());
                        break;
                    case R.id.objectBtn:
                        ((MainActivity) requireActivity()).changeFragment(new ObjectFragment());
                        break;
                }
            }
        });
//        }
    }
}