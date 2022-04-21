package com.monster.handscan.protecthealth.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.activity.MainActivity;
import com.monster.handscan.protecthealth.adapters.ListHistoryAdapter;
import com.monster.handscan.protecthealth.model.ScanHistoryModel;
import com.monster.handscan.protecthealth.utils.StringUtil;

import java.util.List;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    ListView listView;
    ImageButton backBtn, resetBtn;
    private MaxAd nativeAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        List<ScanHistoryModel> image_details = MainActivity.self().db.getAllScanHistories();
        listView = (ListView) view.findViewById(R.id.history_list);
        if (image_details != null && image_details.size() > 0) {
            listView.setAdapter(new ListHistoryAdapter(getContext(), image_details));
        }

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
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        resetBtn = (ImageButton) view.findViewById(R.id.resetBtn);

        backBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.resetBtn:
                showDialog();
                break;
        }
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_clear_history_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView mDialogNo = dialog.findViewById(R.id.dismiss);
        mDialogNo.setOnClickListener(v -> dialog.dismiss());

        ImageView mDialogOk = dialog.findViewById(R.id.confirm);
        mDialogOk.setOnClickListener(v -> {
            MainActivity.self().db.deleteAllScanHistories();
            dialog.dismiss();
        });

        dialog.show();
    }
}