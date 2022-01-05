package com.monster.handscan.protecthealth.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.NativeAd;
import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.activity.MainActivity;
import com.monster.handscan.protecthealth.adapters.ViewPagerAdapter;
import com.monster.handscan.protecthealth.model.AdviceModel;
import com.monster.handscan.protecthealth.utils.StringUtil;


import java.util.ArrayList;
import java.util.Objects;

public class AdviceFragment extends Fragment {

    ViewPager viewPager;
    ArrayList<AdviceModel> arrayList;
    LinearLayout layout_dot;
    TextView[] dot;
    ImageButton backBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_advice, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        layout_dot = (LinearLayout) view.findViewById(R.id.layout_dot);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(view1 ->
                ((MainActivity) requireActivity()).showInterstitial(new MainActivity.OnInterstitialListener() {
                    @Override
                    public void onGameInterstitialClosed() {
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onGameInterstitialShowFailed() {
                        getActivity().onBackPressed();
                    }
                }));

        AdLoader adLoader = new AdLoader.Builder(requireActivity(), StringUtil.APP_OPEN_ID)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(10000)).build();
                        TemplateView template =view.findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
        arrayList = new ArrayList<>();

        arrayList.add(new AdviceModel("Before Entering Home From Outside", R.drawable.ad1));
        arrayList.add(new AdviceModel("Before/After Going To Bed", R.drawable.ad2));
        arrayList.add(new AdviceModel("Before Eating", R.drawable.ad3));
        arrayList.add(new AdviceModel("After Hand Shaking", R.drawable.ad4));
        arrayList.add(new AdviceModel("After Completing Work Out", R.drawable.ad5));

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getContext(), arrayList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageMargin(20);
        addDot(0);

        // whenever the page changes
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            @Override
            public void onPageSelected(int i) {
                addDot(i);
            }
            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return view;
    }

    public void addDot(int page_position) {
        dot = new TextView[arrayList.size()];
        layout_dot.removeAllViews();

        for (int i = 0; i < dot.length; i++) {;
            dot[i] = new TextView(getContext());
            dot[i].setText(Html.fromHtml("&#9673;"));
            dot[i].setTextSize(15);
            dot[i].setTextColor(getResources().getColor(R.color.darker_gray));
            layout_dot.addView(dot[i]);
        }
        //active dot
        dot[page_position].setTextColor(getResources().getColor(R.color.red));
    }
}