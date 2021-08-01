package com.shamlatech.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class PaymentsPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragements;

    public PaymentsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragements=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragements.get(position);
    }

    @Override
    public int getCount() {
        return fragements.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "New Payments";
            case 1: return "Payment History";
            default: return "Unknown";
        }
    }
}
