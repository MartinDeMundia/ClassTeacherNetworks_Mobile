package com.shamlatech.activity.parent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loicteillard.easytabs.EasyTabs;
import com.shamlatech.api_response.Res_Stud_Fees;
import com.shamlatech.api_response.Result_Stud_Fees;
import com.shamlatech.fragment.SAbsentFrag;
import com.shamlatech.fragment.SCalendarFrag;
import com.shamlatech.fragment.SForumsFrag;
import com.shamlatech.fragment.SMediaFrag;
import com.shamlatech.fragment.parent.SFeesFrag;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.BaseFragment;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.ParentAppBarAdditionalList;
import static com.shamlatech.utils.Vars.staUserInfo;

public class ParentsAdditionalActivity extends BaseFragment {

    public static String StudId;
    EasyTabs easyTabs;
    TextView txtAppBarHeader;
    MyPagerAdapter adapterViewPager;
    String[] strHeaders = {"FEES", "CALENDAR", "MEDIA", "FORUMS" ,"ABSENT NOTE"};
    SFeesFrag SFFrag;
    SCalendarFrag SCFrag;
    SMediaFrag SMFrag;
    SForumsFrag SFOFrag;
    SAbsentFrag SAbsFrag;
    String innerTab;
    Activity activity;
    private ViewPager viewPager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_parents_additional, container, false);

        ((BaseActivity) activity).declareAppBar1("", true);

        viewPager = view.findViewById(R.id.viewPager);
        easyTabs = (EasyTabs) view.findViewById(R.id.easytabs);

        txtAppBarHeader = view.findViewById(R.id.txtAppBarHeader);

        // Attach page Adapter
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setOffscreenPageLimit(4);
        easyTabs.setViewPager(viewPager, 0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((BaseActivity) activity).UpdateAppBarColor(ParentAppBarAdditionalList[position]);
                ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ((BaseActivity) activity).UpdateAppBarColor(ParentAppBarAdditionalList[0]);
        ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[0]);
        PreparePage();
        getRetro_AccessStudentFees(staUserInfo.getId(), staUserInfo.getRole(), StudId);

        return view;
    }


    private void PreparePage() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("stud_id")) {
                StudId = bundle.getString("stud_id");
            }
            if (bundle.containsKey("tab")) {
                String tab = bundle.getString("tab");
                if (tab.equals("FEES")) {
                    viewPager.setCurrentItem(0);
                    ((BaseActivity) activity).UpdateAppBarColor(ParentAppBarAdditionalList[0]);
                    ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[0]);
                } else if (tab.equals("CALENDAR")) {
                    viewPager.setCurrentItem(1);
                    ((BaseActivity) activity).UpdateAppBarColor(ParentAppBarAdditionalList[1]);
                    ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[1]);
                } else if (tab.equals("MEDIA")) {
                    viewPager.setCurrentItem(2);
                    ((BaseActivity) activity).UpdateAppBarColor(ParentAppBarAdditionalList[2]);
                    ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[2]);
                } else if (tab.equals("FORUMS")) {
                    viewPager.setCurrentItem(3);
                    ((BaseActivity) activity).UpdateAppBarColor(ParentAppBarAdditionalList[3]);
                    ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[3]);
                }else if (tab.equals("ABSENT_NOTE")) {
                    viewPager.setCurrentItem(4);
                    ((BaseActivity) activity).UpdateAppBarColor(ParentAppBarAdditionalList[4]);
                    ((BaseActivity) activity).UpdateAppBarHeader(strHeaders[4]);
                }

            }
            if (bundle.containsKey("innertab")) {
                innerTab = bundle.getString("innertab");
            }
        }
    }

    private void getRetro_AccessStudentFees(String user_id, String role_id, String stud_id) {
        getRetro_Call(activity, service.getAccessStudentFees(user_id, role_id, stud_id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Stud_Fees mPojo_Fees = onPojoBuilder(objectResponse, Result_Stud_Fees.class);
                        if (mPojo_Fees != null) {
                            if (mPojo_Fees.getStatus().equals(API_Code.Success)) {
                                adapterViewPager.loadFees(mPojo_Fees.getFees());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 5;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        public void loadFees(Res_Stud_Fees fees) {
            if (SFFrag != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("fees_info", fees);
                bundle.putSerializable("stud_id", StudId);
                SFFrag.loadData(bundle);
            }
        }


        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    SFFrag = new SFeesFrag();
                    return SFFrag;
                case 1:
                    SCFrag = new SCalendarFrag();
                    return SCFrag;
                case 2:
                    SMFrag = new SMediaFrag();
                    Bundle args = new Bundle();
                    args.putString("innertab", innerTab);
                    SMFrag.setArguments(args);
                    return SMFrag;
                case 3:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("stud_id", StudId);
                    SFOFrag = new SForumsFrag();
                    SFOFrag.setArguments(bundle);
                    return SFOFrag;
                case 4:
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
