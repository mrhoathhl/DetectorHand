package com.monster.handscan.protecthealth.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.model.DayChallengeModel;
import com.monster.handscan.protecthealth.model.ScanHistoryModel;

import java.util.List;

public class ListHistoryAdapter extends BaseAdapter {
    private List<ScanHistoryModel> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListHistoryAdapter(Context aContext,  List<ScanHistoryModel> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ListHistoryAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.history_item, null);
            holder = new ListHistoryAdapter.ViewHolder();
            holder.percent = (TextView) convertView.findViewById(R.id.percentTxt);
            holder.time = (TextView) convertView.findViewById(R.id.timeTxt);
            convertView.setTag(holder);
        } else {
            holder = (ListHistoryAdapter.ViewHolder) convertView.getTag();
        }

        ScanHistoryModel country = this.listData.get(position);

        holder.percent.setText(country.getPercent());
        holder.time.setText(country.getTime());


        return convertView;
    }

    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        // Return 0 if not found.
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    static class ViewHolder {
        TextView percent;
        TextView time;
    }
}