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

import static com.shamlatech.utils.Vars.ForumTypeT_P;
import static com.shamlatech.utils.Vars.ForumTypeT_T;
import static com.shamlatech.utils.Vars.Role_Parent;
import static com.shamlatech.utils.Vars.Role_Principal;
import static com.shamlatech.utils.Vars.Role_Teacher;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Dharmalingam Sekar on 09-05-2018.
 */

public class SForumsFrag extends Fragment {

    View view;
    String From = "", To = "";
    String StudId = "0";
    Activity activity;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_teacher_fourms, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        prepareBundle();
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());

        if (staUserInfo.getRole().equals(Role_Teacher) || staUserInfo.getRole().equals(Role_Principal)) {
            tabLayout.addTab(tabLayout.newTab().setText("Teacher-Teacher"));
            tabLayout.addTab(tabLayout.newTab().setText("Teacher-Parent"));
            adapter.addFragment(new ForumFragList(Role_Teacher, Role_Teacher, ForumTypeT_T, StudId));
            adapter.addFragment(new ForumFragList(Role_Teacher, Role_Parent, ForumTypeT_P, StudId));
        } else {
            tabLayout.addTab(tabLayout.newTab().setText("Parent-Teacher"));
            adapter.addFragment(new ForumFragList(Role_Parent, Role_Parent, ForumTypeT_P, StudId));
        }

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
                if (Tab.equals("TEACHER"))
                    viewPager.setCurrentItem(0);
                if (Tab.equals("PARENT"))
                    viewPager.setCurrentItem(1);
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public void prepareBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("stud_id")) {
                StudId = bundle.getString("stud_id");
            }
        }
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