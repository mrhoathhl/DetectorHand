package com.monster.handscan.protecthealth.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.activity.MainActivity;
import com.monster.handscan.protecthealth.adapters.ListHistoryAdapter;
import com.monster.handscan.protecthealth.model.ScanHistoryModel;

import java.util.List;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    ListView listView;
    ImageButton backBtn, resetBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        List<ScanHistoryModel> image_details = MainActivity.self().db.getAllScanHistories();
        listView = (ListView) view.findViewById(R.id.history_list);
        if (image_details != null && image_details.size() > 0) {
            listView.setAdapter(new ListHistoryAdapter(getContext(), image_details));
        }
        backBtn = (ImageButton) view.findViewById(R.id.backBtn);
        resetBtn = (ImageButton) view.findViewById(R.id.resetBtn);

        backBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        return view;
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
            case R.id.resetBtn:
                showDialog();
                break;
        }
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
}