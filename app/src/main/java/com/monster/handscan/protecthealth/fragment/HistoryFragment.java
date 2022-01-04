package com.monster.handscan.protecthealth.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.adapters.ListHistoryAdapter;
import com.monster.handscan.protecthealth.adapters.ListViewAdapter;
import com.monster.handscan.protecthealth.adapters.SharedPrefsManager;
import com.monster.handscan.protecthealth.model.ScanHistoryModel;
import com.monster.handscan.protecthealth.model.ScanHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        List<ScanHistoryModel> image_details = getListData();
        listView = (ListView) view.findViewById(R.id.history_list);
        listView.setAdapter(new ListHistoryAdapter(getContext(), image_details));
        return view;
    }

    private List<ScanHistoryModel> getListData() {
        List<ScanHistoryModel> list = new ArrayList<>();
        String day_data = SharedPrefsManager.getInstance().getString("history");
        ObjectMapper mapper = new ObjectMapper();
        try {
            list = mapper.readValue(day_data, new TypeReference<List<ScanHistoryModel>>() {
            });
        } catch (JsonProcessingException e) {
            list.add(new ScanHistoryModel("85%", "asdasdasdasd"));
            list.add(new ScanHistoryModel("85%", "asdasdasdasd"));
            list.add(new ScanHistoryModel("85%", "asdasdasdasd"));
            list.add(new ScanHistoryModel("85%", "asdasdasdasd"));
            list.add(new ScanHistoryModel("85%", "asdasdasdasd"));
            list.add(new ScanHistoryModel("85%", "asdasdasdasd"));
            list.add(new ScanHistoryModel("85%", "asdasdasdasd"));
        }

        return list;
    }

}