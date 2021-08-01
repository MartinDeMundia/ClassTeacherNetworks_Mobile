package com.shamlatech.activity.teacher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loicteillard.easytabs.EasyTabs;
import com.shamlatech.fragment.SAbsentFrag;
import com.shamlatech.fragment.SCalendarFrag;
import com.shamlatech.fragment.SForumsFrag;
import com.shamlatech.fragment.SMediaFrag;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.BaseFragment;
import com.shamlatech.school_management.R;

import static com.shamlatech.utils.Vars.TeacherAppBarAdditionalList;

public class TeachersAdditionalActivity extends BaseFragment {

    EasyTabs easyTabs;
    TextView txtAppBarHeader;
    MyPagerAdapter adapterViewPager;
    String[] strHeaders = {"CALENDAR", "MEDIA", "FORUMS","ABSENT NOTE"};
    SCalendarFrag SCFrag;
    SMediaFrag SMFrag;
    SForumsFrag SFFrag;
    SAbsentFrag SAbsFrag;
    String innerTab = "";
    private ViewPager viewPager;
    Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_teachers_additional, container, false);

        ((BaseActivity) activity).declareAppBar1("", true);

        viewPager = view.findViewById(R.id.viewPager);
        easyTabs = (EasyTabs) view.findViewById(R.id.easytabs);

        txtAppBarHeader = view.findViewById(R.id.txtAppBarHeader);

        // Attach page Adapter
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        easyTabs.setViewPager(viewPager, 0);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((BaseActivity) activity).UpdateAppBarColor(TeacherAppBarAdditionalList[position]);
                ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ((BaseActivity) activity).UpdateAppBarColor(TeacherAppBarAdditionalList[0]);
        ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[0]);
        PreparePage();

        return view;
    }


    private void PreparePage() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("tab")) {
                String tab = bundle.getString("tab");
                if (tab.equals("CALENDAR")) {
                    viewPager.setCurrentItem(0);
                    ((BaseActivity) activity).UpdateAppBarColor(TeacherAppBarAdditionalList[0]);
                    ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[0]);
                } else if (tab.equals("MEDIA")) {
                    viewPager.setCurrentItem(1);
                    ((BaseActivity) activity).UpdateAppBarColor(TeacherAppBarAdditionalList[1]);
                    ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[1]);
                } else if (tab.equals("FORUMS")) {
                    viewPager.setCurrentItem(2);
                    ((BaseActivity) activity).UpdateAppBarColor(TeacherAppBarAdditionalList[2]);
                    ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[2]);
                }else if (tab.equals("ABSENT_NOTE")) {
                    viewPager.setCurrentItem(3);
                    ((BaseActivity) activity).UpdateAppBarColor(TeacherAppBarAdditionalList[3]);
                    ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[3]);
                }
            }
            if (bundle.containsKey("innertab")) {
                innerTab = bundle.getString("innertab");

//                if (innertab.equals("DOCUMENT") || innertab.equals("PHOTO") || innertab.equals("VIDEO")) {
//                    if (SMFrag != null) {
//                        SMFrag.loadTab(innertab);
//                    }
//                } else if (innertab.equals("TEACHER") || innertab.equals("PARENT")) {
//                    if (SFFrag != null) {
//                        SFFrag.loadTab(innertab);
//                    }
//                }
            }
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    SCFrag = new SCalendarFrag();
                    return SCFrag;
                case 1:
                    SMFrag = new SMediaFrag();
                    Bundle args = new Bundle();
                    args.putString("innertab", innerTab);
                    SMFrag.setArguments(args);
                    return SMFrag;
                case 2:
                    SFFrag = new SForumsFrag();
                    Bundle Fargs = new Bundle();
                    Fargs.putString("innertab", innerTab);
                    SFFrag.setArguments(Fargs);
                    return SFFrag;
                case 3:
                    SAbsFrag = new SAbsentFrag();
                    Bundle Absargs = new Bundle();
                    Absargs.putString("innertab", innerTab);
                    SAbsFrag.setArguments(Absargs);
                    return SAbsFrag;
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }

}
