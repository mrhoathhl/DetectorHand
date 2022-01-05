package com.monster.handscan.protecthealth.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.activity.MainActivity;

public class SettingFragment extends Fragment implements View.OnClickListener {

    private ImageButton rateBtn, policyBtn, backBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        rateBtn = (ImageButton) view.findViewById(R.id.rateBtn);
        policyBtn = (ImageButton) view.findViewById(R.id.policyBtn);
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);

        rateBtn.setOnClickListener(this);
        policyBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rateBtn:
                openRatingDialog();
                break;
            case R.id.policyBtn:
                break;
            case R.id.backBtn:
                ((MainActivity) requireActivity()).showInterstitial(new MainActivity.OnInterstitialListener() {
                    @Override
                    public void onGameInterstitialClosed() {
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onGameInterstitialShowFailed() {
                        getActivity().onBackPressed();
                    }
                });
                break;
        }
    }

    public void openRatingDialog() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_rating_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        RatingBar ratingBar = dialog.findViewById(R.id.rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                final String appPackageName = "com.monster.handscan.protecthealth"; // getPackageName() from Context or Activity object
                try {
                    requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    try {
                        requireActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    } catch (Exception e) {
                        Log.e("error", e.getLocalizedMessage());
                    } finally {
                        dialog.dismiss();
                    }
                } catch (Exception ignored) {
                    Log.e("error", ignored.getLocalizedMessage());
                } finally {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
}