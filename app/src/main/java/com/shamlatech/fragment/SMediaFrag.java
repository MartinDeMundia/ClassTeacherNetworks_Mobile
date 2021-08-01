package com.shamlatech.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shamlatech.school_management.R;

import java.util.ArrayList;
import java.util.List;

import static com.shamlatech.utils.Vars.TeacherAppBarAdditionalList;

/**
 * Created by Dharmalingam Sekar on 09-05-2018.
 */

public class SMediaFrag extends Fragment {

    View view;
    Activity activity;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_stud_media, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);


        tabLayout.addTab(tabLayout.newTab().setText("Document"));
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));
        tabLayout.addTab(tabLayout.newTab().setText("Videos"));

        //Add fragments
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(new SMediaDocumentFrag());
        adapter.addFragment(new SMediaPhotoFrag());
        adapter.addFragment(new SMediaVideoFrag());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        PreparePage();
        return view;
    }


    private void PreparePage() {
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("innertab")) {
                String Tab = bundle.getString("innertab");
                if (Tab.equals("DOCUMENT"))
                    viewPager.setCurrentItem(0);
                if (Tab.equals("PHOTO"))
                    viewPager.setCurrentItem(1);
                if (Tab.equals("VIDEO"))
                    viewPager.setCurrentItem(2);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

}