package com.monster.handscan.protecthealth.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.database.SQLiteDBHelper;
import com.monster.handscan.protecthealth.fragment.HomeFragment;
import com.monster.handscan.protecthealth.fragment.SettingFragment;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start home activity
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        // close splash activity
        finish();
    }
}