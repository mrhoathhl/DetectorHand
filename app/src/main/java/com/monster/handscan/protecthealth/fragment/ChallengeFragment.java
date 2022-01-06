package com.monster.handscan.protecthealth.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.activity.DetectorActivity;
import com.monster.handscan.protecthealth.activity.MainActivity;
import com.monster.handscan.protecthealth.adapters.ListViewAdapter;
import com.monster.handscan.protecthealth.adapters.SharedPrefsManager;
import com.monster.handscan.protecthealth.model.ScanHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class ChallengeFragment extends Fragment implements View.OnClickListener {

    ImageButton backBtn, agreeChallengeBtn, scanBtn, confirmBtn;
    CardView confirmCard, agreeCard, progressChallengeCard;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_challenge, container, false);

        List<ScanHistoryModel> image_details = getListData();
        listView = (ListView) view.findViewById(R.id.dayList);
        listView.setAdapter(new ListViewAdapter(getContext(), image_details));

        agreeCard = (CardView) view.findViewById(R.id.agreePopup);
        confirmCard = (CardView) view.findViewById(R.id.noticeChallenge);
        progressChallengeCard = (CardView) view.findViewById(R.id.progressChallenge);

        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        agreeChallengeBtn = (ImageButton) view.findViewById(R.id.agreeChallenge);
        confirmBtn = (ImageButton) view.findViewById(R.id.confirmChallenge);
        scanBtn = (ImageButton) view.findViewById(R.id.scanBtn);

        if (!SharedPrefsManager.getInstance().getBoolean("confirmCard")) {
            confirmCard.setVisibility(View.VISIBLE);
        }
        if (SharedPrefsManager.getInstance().getBoolean("confirmCard") && !SharedPrefsManager.getInstance().getBoolean("agreeCard")) {
            agreeCard.setVisibility(View.VISIBLE);
        }
        if (SharedPrefsManager.getInstance().getBoolean("confirmCard") && SharedPrefsManager.getInstance().getBoolean("agreeCard")) {
            scanBtn.setVisibility(View.VISIBLE);
            progressChallengeCard.setVisibility(View.VISIBLE);
        }

        backBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        agreeChallengeBtn.setOnClickListener(this);
        scanBtn.setOnClickListener(this);

        return view;
    }

    private List<ScanHistoryModel> getListData() {
        List<ScanHistoryModel> list_db = MainActivity.self().db.getAllHistoriesChallenge();
        List<ScanHistoryModel> list = new ArrayList<>();
        list.add(new ScanHistoryModel(R.drawable.day1, false, false));
        list.add(new ScanHistoryModel(R.drawable.day2, false, false));
        list.add(new ScanHistoryModel(R.drawable.day3, false, false));
        list.add(new ScanHistoryModel(R.drawable.day4, false, false));
        list.add(new ScanHistoryModel(R.drawable.day5, false, false));
        list.add(new ScanHistoryModel(R.drawable.day6, false, false));
        list.add(new ScanHistoryModel(R.drawable.day7, false, false));
        if (list_db.size() >= 7) {
            showDialog();
            MainActivity.self().db.deleteAllChallengeHistories();
        } else {
            if (list_db != null && list_db.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (i < list_db.size()) {
                        list.get(i).setDay(list_db.get(i).isDay());
                        list.get(i).setNight(list_db.get(i).isNight());
                    }
                }
            }
        }
        return list;
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_clear_history_custom);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView mDialogNo = dialog.findViewById(R.id.dismiss);
        mDialogNo.setOnClickListener(v -> dialog.dismiss());

        TextView mDialogOk = dialog.findViewById(R.id.confirm);
        mDialogOk.setOnClickListener(v -> {
            MainActivity.self().db.deleteAllScanHistories();
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.agreeChallenge:
                agreeCard.setVisibility(View.INVISIBLE);
                scanBtn.setVisibility(View.VISIBLE);
                progressChallengeCard.setVisibility(View.VISIBLE);
//                SharedPrefsManager.getInstance().putBoolean("agreeCard", true);
                break;
            case R.id.confirmChallenge:
                confirmCard.setVisibility(View.INVISIBLE);
                agreeCard.setVisibility(View.VISIBLE);
                agreeChallengeBtn.setVisibility(View.VISIBLE);
//                SharedPrefsManager.getInstance().putBoolean("confirmCard", true);
                break;
            case R.id.scanBtn:
                Intent intent = new Intent(getActivity(), DetectorActivity.class);
                intent.putExtra("type", "challenge");
                requireActivity().startActivity(intent);

                break;
        }

    }
}