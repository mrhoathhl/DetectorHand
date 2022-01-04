package com.monster.handscan.protecthealth.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;


import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.model.AdviceModel;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<AdviceModel> pager;

    public ViewPagerAdapter(Context context, ArrayList<AdviceModel> pager) {
        this.context = context;
        this.pager = pager;
    }

    @Override
    public int getCount() {
        return pager.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public  Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.page_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageResource(pager.get(position).getImageId());

        TextView textView = (TextView) view.findViewById(R.id.adviceTxt);
        textView.setText(pager.get(position).getAdviceText());
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
