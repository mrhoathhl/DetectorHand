package com.monster.handscan.protecthealth.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.activity.DetectorActivity;
import com.monster.handscan.protecthealth.adapters.ListViewAdapter;
import com.monster.handscan.protecthealth.adapters.SharedPrefsManager;
import com.monster.handscan.protecthealth.model.DayChallengeModel;

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

        List<DayChallengeModel> image_details = getListData();
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

    private List<DayChallengeModel> getListData() {
        List<DayChallengeModel> list = new ArrayList<>();
        String day_data = SharedPrefsManager.getInstance().getString("day");
        ObjectMapper mapper = new ObjectMapper();
        try {
            list = mapper.readValue(day_data, new TypeReference<List<DayChallengeModel>>() {
            });
        } catch (JsonProcessingException e) {
            list.add(new DayChallengeModel(R.drawable.day1, false, false));
            list.add(new DayChallengeModel(R.drawable.day2, false, false));
            list.add(new DayChallengeModel(R.drawable.day3, false, false));
            list.add(new DayChallengeModel(R.drawable.day4, false, false));
            list.add(new DayChallengeModel(R.drawable.day5, false, false));
            list.add(new DayChallengeModel(R.drawable.day6, false, false));
            list.add(new DayChallengeModel(R.drawable.day7, false, false));
        }

        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                getActivity().onBackPressed();
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
                requireActivity().startActivity(intent);
                break;
        }

    }
}