package com.monster.handscan.protecthealth.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.NativeAd;
import com.monster.handscan.protecthealth.activity.MainActivity;

import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.activity.DetectorActivity;
import com.monster.handscan.protecthealth.utils.StringUtil;

public class HomeFragment extends Fragment implements View.OnClickListener {

    ImageButton settingBtn, noticeBtn, historyBtn, adviceBtn, scanBtn, objectBtn;
    FragmentManager fragmentManager;

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
        settingBtn = view.findViewById(R.id.settingBtn);
        noticeBtn = view.findViewById(R.id.challenge);
        historyBtn = view.findViewById(R.id.historyBtn);
        adviceBtn = view.findViewById(R.id.adviceBtn);
        scanBtn = view.findViewById(R.id.scanBtn);
        objectBtn = view.findViewById(R.id.objectBtn);

        AdLoader adLoader = new AdLoader.Builder(getContext(), StringUtil.NATIVE_ID)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(10000)).build();
                        TemplateView template = view.findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

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
        MainActivity.self().appOpenManager.showAdIfAvailable();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scanBtn) {
            Intent intent = new Intent(getContext(), DetectorActivity.class);
            intent.putExtra("type", "normal");
            requireActivity().startActivity(intent);
        } else {
            ((MainActivity) requireActivity()).showInterstitial(new MainActivity.OnInterstitialListener() {
                @Override
                public void onGameInterstitialClosed() {
                    switch (v.getId()) {
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
        }
    }
}