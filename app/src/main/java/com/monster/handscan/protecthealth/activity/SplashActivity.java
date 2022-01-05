package com.monster.handscan.protecthealth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.database.SQLiteDBHelper;

public class SplashActivity extends AppCompatActivity {

    SQLiteDBHelper db;
    ProgressBar simpleProgressBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        simpleProgressBar = findViewById(R.id.progressBar); // initiate the progress bar

        db = new SQLiteDBHelper(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        doStartProgressBar();
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
                        Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
                        SplashActivity.this.startActivity(myIntent);
                    }
                });
            }
        });
        thread.start();
    }

}