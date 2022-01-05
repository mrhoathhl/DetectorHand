package com.monster.handscan.protecthealth.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.database.SQLiteDBHelper;
import com.monster.handscan.protecthealth.fragment.HomeFragment;
import com.monster.handscan.protecthealth.fragment.SettingFragment;

public class SplashFragment extends Fragment {

    SQLiteDBHelper db;
    ProgressBar simpleProgressBar;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        simpleProgressBar = view.findViewById(R.id.progressBar); // initiate the progress bar

        db = new SQLiteDBHelper(requireContext());
        MobileAds.initialize(requireContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        doStartProgressBar();
        return view;
    }

    private void doStartProgressBar() {
        final int MAX = 101;
        this.simpleProgressBar.setMax(MAX);

        Thread thread = new Thread(() -> {
            for (int i = 0; i < MAX; i++) {
                final int progress = i + 1;
                // Do something (Download, Upload, Update database,..)
                SystemClock.sleep(20); // Sleep 20 milliseconds.

                // Update interface.
                handler.post(() -> {
                    simpleProgressBar.setProgress(progress);

                    if (progress == MAX) {
                        ((MainActivity) requireActivity()).changeFragment(new HomeFragment());
                    }
                });
            }
        });
        thread.start();
    }

}