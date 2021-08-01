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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.loicteillard.easytabs.EasyTabs;
import com.shamlatech.api_response.Res_Stud_Attendance;
import com.shamlatech.api_response.Res_Stud_Behaviour;
import com.shamlatech.api_response.Res_Stud_Education;
import com.shamlatech.api_response.Res_Stud_Education_Terms;
import com.shamlatech.api_response.Res_Stud_Health;
import com.shamlatech.api_response.Res_Stud_Journal;
import com.shamlatech.api_response.Res_Stud_Teacher;
import com.shamlatech.api_response.Result_Stud_Attendance;
import com.shamlatech.api_response.Result_Stud_Behaviour;
import com.shamlatech.api_response.Result_Stud_Education;
import com.shamlatech.api_response.Result_Stud_Health;
import com.shamlatech.api_response.Result_Stud_Journal;
import com.shamlatech.api_response.Result_Stud_Teacher;
import com.shamlatech.api_response.Result_Student_Info;
import com.shamlatech.fragment.SAttendanceFrag;
import com.shamlatech.fragment.SBehaviourFrag;
import com.shamlatech.fragment.SEducationFrag;
import com.shamlatech.fragment.SHealthFrag;
import com.shamlatech.fragment.SJournalFrag;
import com.shamlatech.fragment.STeacherFrag;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.BaseFragment;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.StudentAppBarList;
import static com.shamlatech.utils.Vars.staStudentInfo;
import static com.shamlatech.utils.Vars.staUserInfo;

public class ParentStudentActivity extends BaseFragment {

    public static String StudId;
    EasyTabs easyTabs;
    TextView txtAppBarHeader;
    MyPagerAdapter adapterViewPager;
    Activity activity;
    private ViewPager viewPager;
    TextView txtChooseAcademic;
    Spinner spinnerYear, spinnerTerm;
    boolean isEducationSelected = false;
    String selectedYear = "0", selectedTerm = "Term 1";
    RelativeLayout relativeAcademicYear;
    ArrayList<String> termsList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_parent_student, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        spinnerYear = view.findViewById(R.id.spinnerYear);
        spinnerTerm = view.findViewById(R.id.spinnerTerm);
        txtChooseAcademic = view.findViewById(R.id.txtChooseAcademic);
        easyTabs = view.findViewById(R.id.easytabs);
        relativeAcademicYear = view.findViewById(R.id.relativeAcademicYear);
        relativeAcademicYear.setVisibility(View.GONE);
        spinnerTerm.setVisibility(View.GONE);
        txtAppBarHeader = view.findViewById(R.id.txtAppBarHeader);

        ((BaseActivity) activity).declareAppBar3("Student Name", "http://shamlatech.net/android_portal/school/image/student.jpg", "ParentStudentActivity", "");

        // Attach page Adapter
        adapterViewPager = new MyPagerAdapter(ParentStudentActivity.this, getChildFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setOffscreenPageLimit(5);
        easyTabs.setViewPager(viewPager, 0);

        Vars.Refresh_Stud_Health = "1";

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((BaseActivity) activity).UpdateAppBarColor(StudentAppBarList[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ((BaseActivity) activity).UpdateAppBarColor(StudentAppBarList[0]);
        PreparePage();
        getRetro_AccessStudentInfo(StudId);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
//        getRetro_AccessStudentInfo(StudId);
        if (Vars.Refresh_Stud_Health.equals("1")) {
            getRetro_AccessStudentHealth(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
        }
    }


    private void PreparePage() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("stud_id")) {
                StudId = bundle.getString("stud_id");
            }
            if (bundle.containsKey("tab")) {
                String tab = bundle.getString("tab");
                if (tab.equals("TEACHER")) {
                    viewPager.setCurrentItem(0);
                    ((BaseActivity) activity).UpdateAppBarColor(StudentAppBarList[0]);
                } else if (tab.equals("EDUCATION")) {
                    viewPager.setCurrentItem(1);
                    ((BaseActivity) activity).UpdateAppBarColor(StudentAppBarList[1]);
                } else if (tab.equals("BEHAVIOUR")) {
                    viewPager.setCurrentItem(2);
                    ((BaseActivity) activity).UpdateAppBarColor(StudentAppBarList[2]);
                } else if (tab.equals("HEALTH")) {
                    viewPager.setCurrentItem(3);
                    ((BaseActivity) activity).UpdateAppBarColor(StudentAppBarList[3]);
                } else if (tab.equals("ATTENDANCE")) {
                    viewPager.setCurrentItem(4);
                    ((BaseActivity) activity).UpdateAppBarColor(StudentAppBarList[4]);
                }
                else if (tab.equals("JOURNAL")) {
                    viewPager.setCurrentItem(5);
                    ((BaseActivity) activity).UpdateAppBarColor(StudentAppBarList[5]);
                }
            }
        }
    }

    public void getRetro_AccessStudentInfo(final String stud_id) {
        getRetro_Call(activity, service.getAccessStudentInfo(stud_id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Student_Info mPojo_Student = onPojoBuilder(objectResponse, Result_Student_Info.class);
                        if (mPojo_Student != null) {
                            if (mPojo_Student.getStatus().equals(API_Code.Success)) {
                                Vars.staStudentInfo = mPojo_Student.getStudent();
                                ((BaseActivity) activity).declareAppBar3(staStudentInfo.getFirst_name(), staStudentInfo.getDOB(), "TeachersStudentActivity", stud_id);
//                                prepareYear();
                                getRetro_AccessStudentTeacher(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
                                getRetro_AccessStudentEducation(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
                                getRetro_AccessStudentBehaviour(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
                                getRetro_AccessStudentHealth(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
                                getRetro_AccessStudentAttendance(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
                                getRetro_Journal(staUserInfo.getId(), staUserInfo.getRole(), StudId);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    private void prepareYear() {
        relativeAcademicYear.setVisibility(View.VISIBLE);
        if (spinnerYear.getAdapter() == null) {
            selectedYear = Vars.staStudentInfo.getCurrent_year();
            final List<String> items = Arrays.asList(Vars.staStudentInfo.getStudied_years().split("\\s*,\\s*"));
            ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(activity, R.layout.simple_spinner_item, items);
            adapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerYear.setAdapter(adapterSubject);

            spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedYear = items.get(position);
                    getRetro_AccessStudentTeacher(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
                    getRetro_AccessStudentEducation(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
                    getRetro_AccessStudentBehaviour(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
                    getRetro_AccessStudentHealth(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
                    getRetro_AccessStudentAttendance(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).equalsIgnoreCase(selectedYear))
                    spinnerYear.setSelection(i);
            }
        } else {
            getRetro_AccessStudentTeacher(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
            getRetro_AccessStudentEducation(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
            getRetro_AccessStudentBehaviour(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
            getRetro_AccessStudentHealth(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
            getRetro_AccessStudentAttendance(staUserInfo.getId(), staUserInfo.getRole(), StudId, selectedYear);
        }
    }

    public void getRetro_AccessStudentTeacher(String user_id, String role_id, String stud_id, String year) {
        adapterViewPager.initTeacher();
        getRetro_Call(activity, service.getAccessStudentTeacher(user_id, role_id, stud_id, year),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Stud_Teacher mPojo_Teacher = onPojoBuilder(objectResponse, Result_Stud_Teacher.class);
                        if (mPojo_Teacher != null) {
                            if (mPojo_Teacher.getStatus().equals(API_Code.Success)) {
                                adapterViewPager.loadTeacher(mPojo_Teacher.getTeachers());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public void getRetro_AccessStudentEducation(String user_id, String role_id, String stud_id, String year) {
        adapterViewPager.initEducation();
        getRetro_Call(activity, service.getAccessStudentEducation(user_id, role_id, stud_id, year),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Stud_Education mPojo_Education = onPojoBuilder(objectResponse, Result_Stud_Education.class);
                        if (mPojo_Education != null) {
                            if (mPojo_Education.getStatus().equals(API_Code.Success)) {
//                                prepareTerms(mPojo_Education.getEducation());
                                adapterViewPager.loadEducation(mPojo_Education.getEducation());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public void getRetro_AccessStudentBehaviour(String user_id, String role_id, String stud_id, String year) {
        adapterViewPager.initBehaviour();
        getRetro_Call(activity, service.getAccessStudentBehaviour(user_id, role_id, stud_id, year),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Stud_Behaviour mPojo_Behaviour = onPojoBuilder(objectResponse, Result_Stud_Behaviour.class);
                        if (mPojo_Behaviour != null) {
                            if (mPojo_Behaviour.getStatus().equals(API_Code.Success)) {
                                adapterViewPager.loadBehaviour(mPojo_Behaviour.getBehaviour());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public void getRetro_AccessStudentHealth(String user_id, String role_id, String stud_id, String year) {
        adapterViewPager.initHealth();
        getRetro_Call(activity, service.getAccessStudentHealth(user_id, role_id, stud_id, year),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Stud_Health mPojo_Health = onPojoBuilder(objectResponse, Result_Stud_Health.class);
                        if (mPojo_Health != null) {
                            if (mPojo_Health.getStatus().equals(API_Code.Success)) {
                                adapterViewPager.loadHealth(mPojo_Health.getHealth());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public void getRetro_AccessStudentAttendance(String user_id, String role_id, String stud_id, String year) {
        adapterViewPager.initAttendance();
        getRetro_Call(activity, service.getAccessStudentAttendance(user_id, role_id, stud_id, year),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Stud_Attendance mPojo_Attendance = onPojoBuilder(objectResponse, Result_Stud_Attendance.class);
                        if (mPojo_Attendance != null) {
                            if (mPojo_Attendance.getStatus().equals(API_Code.Success)) {
                                adapterViewPager.loadAttendance(mPojo_Attendance.getAttendance());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }


    public void getRetro_Journal(String user_id, String role_id, String stud_id) {
        adapterViewPager.initJournals();
        getRetro_Call(activity, service.getJournalList(user_id, role_id, stud_id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Stud_Journal mPojo_Journals = onPojoBuilder(objectResponse, Result_Stud_Journal.class);
                        if (mPojo_Journals != null) {
                            if (mPojo_Journals.getStatus().equals(API_Code.Success)) {
                                adapterViewPager.loadJournals(mPojo_Journals.getJournals());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    private void prepareTerms(final Res_Stud_Education education) {
        termsList = new ArrayList<>();

        for (int i = 0; i < education.getTerms().size(); i++) {
            Res_Stud_Education_Terms terms = education.getTerms().get(i);
            termsList.add(terms.getTerm_name());
        }

        ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(activity, R.layout.simple_spinner_item, termsList);
        adapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTerm.setAdapter(adapterSubject);

        spinnerTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTerm = termsList.get(position);
                for (int i = 0; i < termsList.size(); i++) {
                    if (selectedTerm.equalsIgnoreCase(termsList.get(i))) {
//                        adapterViewPager.loadEducation(education.getTerms().get(i));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTerm.setSelection(termsList.size() - 1);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 6;
        STeacherFrag STFrag;
        SEducationFrag SEFrag;
        SBehaviourFrag SBFrag;
        SHealthFrag SHFrag;
        SAttendanceFrag SAFrag;
        SJournalFrag SJFrag;
        ParentStudentActivity parentStudentActivity;

        public MyPagerAdapter(ParentStudentActivity parentStudentActivity, FragmentManager fragmentManager) {
            super(fragmentManager);
            this.parentStudentActivity = parentStudentActivity;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        public void initTeacher() {
            if (STFrag != null) {
                STFrag.showProgress();
            }
        }

        public void initEducation() {
            if (SEFrag != null) {
                SEFrag.showProgress();
            }
        }

        public void initBehaviour() {
            if (SBFrag != null) {
                SBFrag.showProgress();
            }
        }

        public void initHealth() {
            if (SHFrag != null) {
                SHFrag.showProgress();
            }
        }

        public void initAttendance() {
            if (SAFrag != null) {
                SAFrag.showProgress();
            }
        }

        public void initJournals() {
            if (SJFrag != null) {
                SJFrag.showProgress();
            }
        }

        public void loadTeacher(Res_Stud_Teacher teachers) {
            if (STFrag != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("teacher_info", teachers);
                bundle.putSerializable("stud_id", StudId);
                STFrag.loadData(bundle);
            }
        }

        public void loadEducation(Res_Stud_Education_Terms education) {
            if (SEFrag != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("education_info", education);
                bundle.putSerializable("stud_id", StudId);
                SEFrag.loadData(bundle);
            }
        }

        public void loadBehaviour(Res_Stud_Behaviour behaviour) {
            if (SBFrag != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("behaviour_info", behaviour);
                bundle.putSerializable("stud_id", StudId);
                SBFrag.loadData(bundle);
            }
        }

        public void loadHealth(Res_Stud_Health health) {
            if (SHFrag != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("health_info", health);
                bundle.putSerializable("stud_id", StudId);
                SHFrag.loadData(bundle);
            }
        }

        public void loadAttendance(Res_Stud_Attendance attendance) {
            if (SAFrag != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("attendance_info", attendance);
                bundle.putSerializable("stud_id", StudId);
                SAFrag.loadData(bundle);
            }
        }

        public void loadJournals(ArrayList<Res_Stud_Journal> journals) {
            if (SJFrag != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("journal_info", journals);
                bundle.putSerializable("stud_id", StudId);
                SJFrag.loadData(bundle);
            }
        }


        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    STFrag = new STeacherFrag();
                    return STFrag;
                case 1:
                    SEFrag = new SEducationFrag(parentStudentActivity);
                    return SEFrag;
                case 2:
                    SBFrag = new SBehaviourFrag();
                    return SBFrag;
                case 3:
                    SHFrag = new SHealthFrag();
                    //SHFrag.setArguments(bundle);
                    return SHFrag;
                case 4:
                    SAFrag = new SAttendanceFrag();
                    //SAFrag.setArguments(bundle);
                    return SAFrag;
                case 5:
                    SJFrag = new SJournalFrag();
                    return SJFrag;
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

    public void showHideTerm(boolean isVisible) {
        if (isVisible) {
            spinnerTerm.setVisibility(View.VISIBLE);
            txtChooseAcademic.setText("Choose the Academic Period");
        } else {
            spinnerTerm.setVisibility(View.GONE);
            txtChooseAcademic.setText("Choose the Academic Year");
        }
    }
}