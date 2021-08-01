package com.shamlatech.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shamlatech.school_management.R;

import java.util.List;

/**
 * Created by SUN on 30-03-2017.
 */

public class Intro_Adapter extends PagerAdapter {
    List<String> introContent;
    List<Integer> introBack;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public Intro_Adapter(Context context, List<String> introContent, List<Integer> introBack) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.introContent = introContent;
        this.introBack = introBack;
    }

    @Override
    public int getCount() {
        return introContent.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.listpager_intro, container, false);
        final TextView txtIntroContent = itemView.findViewById(R.id.txtIntroContent);
        final RelativeLayout relativeIntro = itemView.findViewById(R.id.relativeIntro);
        txtIntroContent.setText(introContent.get(position));
        relativeIntro.setBackgroundResource(introBack.get(position));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
}