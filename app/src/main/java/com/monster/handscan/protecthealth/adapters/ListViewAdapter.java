package com.monster.handscan.protecthealth.adapters;

import android.annotation.SuppressLint;
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

public class ListViewAdapter extends BaseAdapter {
    private List<ScanHistoryModel> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public ListViewAdapter(Context aContext,  List<ScanHistoryModel> listData) {
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

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.challenge_item, null);
            holder = new ViewHolder();
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayLabel);
            holder.dayHand = (ImageView) convertView.findViewById(R.id.dayHand);
            holder.nightHand = (ImageView) convertView.findViewById(R.id.nightHand);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ScanHistoryModel country = this.listData.get(position);

        holder.dayLabel.setText(country.getDayLabel());
        if (country.isDay()) {
            holder.dayHand.setImageResource(R.drawable.handon);
        } else {
            holder.dayHand.setImageResource(R.drawable.handoff);
        }

        if (country.isNight()) {
            holder.nightHand.setImageResource(R.drawable.handon);
        } else {
            holder.nightHand.setImageResource(R.drawable.handoff);
        }



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
        TextView dayLabel;
        ImageView dayHand;
        ImageView nightHand;
    }
}
