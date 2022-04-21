package com.monster.handscan.protecthealth.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.activity.MainActivity;
import com.monster.handscan.protecthealth.utils.StringUtil;

public class SettingFragment extends Fragment implements View.OnClickListener {

    private ImageButton rateBtn, policyBtn, backBtn;
    private MaxAd nativeAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

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
        rateBtn = (ImageButton) view.findViewById(R.id.rateBtn);
        policyBtn = (ImageButton) view.findViewById(R.id.policyBtn);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);

        rateBtn.setOnClickListener(this);
        policyBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rateBtn:
                openRatingDialog();
                break;
            case R.id.policyBtn:
                break;
            case R.id.backBtn:
                ((MainActivity) requireActivity()).showInterstitial(new MainActivity.OnInterstitialListener() {
                    @Override
                    public void onGameInterstitialClosed() {
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onGameInterstitialShowFailed() {
                        getActivity().onBackPressed();
                    }
                });
                break;
        }
    }

    public void openRatingDialog() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_rating_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RatingBar ratingBar = dialog.findViewById(R.id.rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                final String appPackageName = "com.monster.handscan.protecthealth"; // getPackageName() from Context or Activity object
                try {
                    requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    try {
                        requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    } catch (Exception e) {
                        Log.e("error", e.getLocalizedMessage());
                    } finally {
                        dialog.dismiss();
                    }
                } catch (Exception ignored) {
                    Log.e("error", ignored.getLocalizedMessage());
                } finally {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
}