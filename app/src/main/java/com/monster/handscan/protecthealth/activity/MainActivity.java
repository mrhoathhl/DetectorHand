package com.monster.handscan.protecthealth.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    private static MainActivity mSelf;
    private AdView mAdView;
    Button settingBtn, noticeBtn, historyBtn, adviceBtn, scanBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSelf = this;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, new HomeFragment(), null);
        fragmentTransaction.commit();
    }

    public static MainActivity self() {
        return mSelf;
    }
    public void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        fragmentTransaction1.replace(R.id.mainContainer, fragment);
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
    }
}