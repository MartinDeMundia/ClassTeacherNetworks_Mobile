package com.shamlatech.activity.teacher;

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
import android.widget.TextView;

import com.shamlatech.fragment.teacher.GroupMakerFragment;
import com.shamlatech.fragment.teacher.MyClassesFragment;
import com.shamlatech.fragment.teacher.MyTimeTableFragment;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.BaseFragment;
import com.shamlatech.school_management.R;

import java.util.ArrayList;
import java.util.List;

import static com.shamlatech.utils.Design.wrapTabIndicatorToTitle;
import static com.shamlatech.utils.Vars.StudentAppBarList;

public class ClassToolsActivity extends BaseFragment {

    TextView txtAppBarHeader;
    Activity activity;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_class_tools, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        txtAppBarHeader = view.findViewById(R.id.txtAppBarHeader);


        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ct_group_maker).setText("Group Maker"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ct_timetable).setText("My Timetable"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ct_my_class).setText("Class Layout"));

        wrapTabIndicatorToTitle(tabLayout, 25, 25);

        //Add fragments
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(new GroupMakerFragment());
        adapter.addFragment(new MyTimeTableFragment());
        adapter.addFragment(new MyClassesFragment());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                ((BaseActivity) activity).declareAppBar1(tab.getText().toString(), true);
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

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) activity).declareAppBar1("Group Maker", true);
        ((BaseActivity) activity).UpdateAppBarColor(R.color.colorTabPrimary);
    }

    private void PreparePage() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("tab")) {
                String tab = bundle.getString("tab");
                if (tab.equals("GROUP")) {
                    viewPager.setCurrentItem(0);
                } else if (tab.equals("TIMETABLE")) {
                    viewPager.setCurrentItem(1);
                } else if (tab.equals("CLASSLAYOUT")) {
                    viewPager.setCurrentItem(2);
                }
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
