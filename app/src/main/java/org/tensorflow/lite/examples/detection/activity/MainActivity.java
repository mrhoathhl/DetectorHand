package org.tensorflow.lite.examples.detection.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    Button settingBtn, noticeBtn, historyBtn, adviceBtn, scanBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mainContainer, new HomeFragment(), null);
        fragmentTransaction.commit();
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        fragmentTransaction1.replace(R.id.mainContainer, fragment);
        fragmentTransaction1.addToBackStack(null);
        fragmentTransaction1.commit();
    }
}