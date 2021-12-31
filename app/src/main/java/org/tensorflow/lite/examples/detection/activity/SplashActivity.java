package org.tensorflow.lite.examples.detection.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.detection.R;

public class SplashActivity extends AppCompatActivity {

    ProgressBar simpleProgressBar;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        simpleProgressBar = findViewById(R.id.progressBar); // initiate the progress bar
        doStartProgressBar();
    }

    private void doStartProgressBar() {
        final int MAX = 110;
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