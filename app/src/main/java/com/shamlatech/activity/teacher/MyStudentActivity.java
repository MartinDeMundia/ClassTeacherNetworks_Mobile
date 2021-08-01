package com.shamlatech.activity.teacher;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.shamlatech.fragment.teacher.FilterByClassFragment;
import com.shamlatech.fragment.teacher.FilterBySubjectFragment;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.BaseFragment;
import com.shamlatech.school_management.R;

import java.util.ArrayList;
import java.util.List;

import static com.shamlatech.utils.Vars.staUserInfo;


@SuppressLint("ValidFragment")
public class MyStudentActivity extends BaseFragment {

    private final int mPosition;
    TextView txtAppBarHeader;
    Activity activity;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private  String user ;

    public MyStudentActivity(int position) {
        super();
        mPosition = position;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_student, container, false);


        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        txtAppBarHeader = view.findViewById(R.id.txtAppBarHeader);

        user =staUserInfo.getId();

        //tabLayout.addTab(tabLayout.newTab().setText("Fill In Marks"));
        tabLayout.addTab(tabLayout.newTab().setText("Fill In Marks"));

        //Add fragments
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager());
        //adapter.addFragment(new FilterByClassFragment(mPosition));
        adapter.addFragment(new FilterBySubjectFragment(mPosition));

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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) activity).declareAppBar1("My Student", true);
        ((BaseActivity) activity).UpdateAppBarColor(R.color.colorTabPrimary);
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
