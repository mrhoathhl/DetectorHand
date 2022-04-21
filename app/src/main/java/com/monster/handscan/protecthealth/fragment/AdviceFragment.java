package com.monster.handscan.protecthealth.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.activity.MainActivity;
import com.monster.handscan.protecthealth.adapters.ViewPagerAdapter;
import com.monster.handscan.protecthealth.model.AdviceModel;
import com.monster.handscan.protecthealth.utils.StringUtil;

import java.util.ArrayList;

public class AdviceFragment extends Fragment {

    ViewPager viewPager;
    ArrayList<AdviceModel> arrayList;
    LinearLayout layout_dot;
    TextView[] dot;
    ImageButton backBtn;
    private MaxAd nativeAd;

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

        arrayList = new ArrayList<>();

        arrayList.add(new AdviceModel("Before going back home", R.drawable.ad1));
        arrayList.add(new AdviceModel("Before and after going to bed", R.drawable.ad2));
        arrayList.add(new AdviceModel("Before and after eating", R.drawable.ad3));
        arrayList.add(new AdviceModel("After shaking hand", R.drawable.ad4));
        arrayList.add(new AdviceModel("After working out", R.drawable.ad5));

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

        for (int i = 0; i < dot.length; i++) {
            ;
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