package org.tensorflow.lite.examples.detection.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import org.tensorflow.lite.examples.detection.R;
import org.tensorflow.lite.examples.detection.activity.DetectorActivity;
import org.tensorflow.lite.examples.detection.activity.MainActivity;

import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    ImageButton settingBtn, noticeBtn, historyBtn, adviceBtn, scanBtn;
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
        noticeBtn = view.findViewById(R.id.noticeBtn);
        historyBtn = view.findViewById(R.id.historyBtn);
        adviceBtn = view.findViewById(R.id.adviceBtn);
        scanBtn = view.findViewById(R.id.scanBtn);

        settingBtn.setOnClickListener(this);
        noticeBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        adviceBtn.setOnClickListener(this);
        scanBtn.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingBtn:
                ((MainActivity) requireActivity()).changeFragment(new SettingFragment());
                break;
            case R.id.noticeBtn:
                ((MainActivity) requireActivity()).changeFragment(new NoticeFragment());
                break;
            case R.id.historyBtn:
                ((MainActivity) requireActivity()).changeFragment(new HistoryFragment());
                break;
            case R.id.adviceBtn:
                ((MainActivity) requireActivity()).changeFragment(new AdviceFragment());
                break;
            case R.id.scanBtn:
                Intent intent = new Intent(getContext(), DetectorActivity.class);
                requireActivity().startActivity(intent);
                break;
        }
    }
}