package com.shamlatech.activity.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.activity.AnnouncementListActivity;
import com.shamlatech.activity.AnnouncementViewActivity;
import com.shamlatech.activity.ResourceActivity;
import com.shamlatech.activity.SearchActivity;
import com.shamlatech.adapter.AnnouncementAdapter;
import com.shamlatech.api_response.Res_Announcement;
import com.shamlatech.api_response.Result_Announcement;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.BaseFragment;
import com.shamlatech.school_management.HomeActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.school_management.fill_marks;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.CanReloadAPI;
import static com.shamlatech.utils.Vars.Refresh_Name_Announcement;
import static com.shamlatech.utils.Vars.staUserInfo;

public class TeachersDashboard extends BaseFragment implements View.OnClickListener {


    final int SEARCH_RESULT = 123;
    TextView txtNoNewAnnouncement;
    LinearLayout linearMyStudent, linearClassTools,
            linearEducationalProgress, linearBehaviouralProgress,
            linearHealthReport, linearAttendance,
            linearCalendar, linearMedia, linearForums,linearMarks,linearAbsentNote,linearResources; //linearSurvey
    ImageView imgMore;
    RecyclerView recyclerAnnouncement;
    ArrayList<Res_Announcement> listAnnouncement = new ArrayList<>();
    Activity activity;
    String url ="http://apps.classteacher.school/index.php/admin/marks_manage_app";
    private AnnouncementAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_teachers_dashboard, container, false);
        init(view);
        listAnnouncement = new ArrayList<>();


        mAdapter = new AnnouncementAdapter(activity, listAnnouncement, TeachersDashboard.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerAnnouncement.setLayoutManager(mLayoutManager);
        recyclerAnnouncement.setItemAnimator(new DefaultItemAnimator());
        recyclerAnnouncement.setAdapter(mAdapter);


        return view;

    }

    public void init(View view) {
        txtNoNewAnnouncement = view.findViewById(R.id.txtNoNewAnnouncement);

        linearMyStudent = view.findViewById(R.id.linearMyStudent);
        linearClassTools = view.findViewById(R.id.linearClassTools);
        linearResources =  view.findViewById(R.id.linearResources);

        linearEducationalProgress = view.findViewById(R.id.linearEducationalProgress);
        linearBehaviouralProgress = view.findViewById(R.id.linearBehaviouralProgress);
        linearHealthReport = view.findViewById(R.id.linearHealthReport);
        linearAttendance = view.findViewById(R.id.linearAttendance);
        //linearSurvey = view.findViewById(R.id.linearSurvey);
        linearCalendar = view.findViewById(R.id.linearCalendar);
        linearMedia = view.findViewById(R.id.linearMedia);
        linearForums = view.findViewById(R.id.linearForums);
        linearAbsentNote = view.findViewById(R.id.linearAbsentNote);
        linearMarks = view.findViewById(R.id.linearMarks);
        imgMore = view.findViewById(R.id.imgMore);

        recyclerAnnouncement = view.findViewById(R.id.recyclerAnnouncement);

        linearResources.setOnClickListener(this);
       // linearSurvey.setOnClickListener(this);
        linearMyStudent.setOnClickListener(this);
        linearClassTools.setOnClickListener(this);
        linearEducationalProgress.setOnClickListener(this);
        linearBehaviouralProgress.setOnClickListener(this);
        linearHealthReport.setOnClickListener(this);
        linearAttendance.setOnClickListener(this);
        linearCalendar.setOnClickListener(this);
        linearMedia.setOnClickListener(this);
        linearForums.setOnClickListener(this);
        linearAbsentNote.setOnClickListener(this);
        imgMore.setOnClickListener(this);
        linearMarks.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) activity).declareAppBar1("My Dashboard", false);
        ((BaseActivity) activity).UpdateAppBarColor(R.color.colorTabPrimary);
        if (CanReloadAPI(Refresh_Name_Announcement)) {
            getRetro_AccessAnnouncement(staUserInfo.getId(), staUserInfo.getRole(), "1", "-1", "", "");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearResources:
                startActivity(new Intent(activity, ResourceActivity.class)
                        .putExtra("id", ""));
                break;
            case R.id.linearMyStudent:
                ((HomeActivity) activity).UpdateTeacherActionFragment(new MyStudentFolderActivity(), "");
                /*startActivity(new Intent(activity, fill_marks.class)
                        .putExtra("id", staUserInfo.getId())
                        .putExtra("role", 2)
                        .putExtra("url", url)
                );*/
                break;
            case R.id.linearClassTools:
                ((HomeActivity) activity).UpdateTeacherActionFragment(new ClassToolsActivity(), "");
                break;
            case R.id.linearEducationalProgress:
                activity.startActivityForResult(new Intent(activity, SearchActivity.class).putExtra("tab", "EDUCATION"), SEARCH_RESULT);
                break;
            case R.id.linearBehaviouralProgress:
                activity.startActivityForResult(new Intent(activity, SearchActivity.class).putExtra("tab", "BEHAVIOUR"), SEARCH_RESULT);
                break;
            case R.id.linearHealthReport:
                activity.startActivityForResult(new Intent(activity, SearchActivity.class).putExtra("tab", "HEALTH"), SEARCH_RESULT);
                break;
            case R.id.linearAttendance:
                activity.startActivityForResult(new Intent(activity, SearchActivity.class).putExtra("tab", "ATTENDANCE"), SEARCH_RESULT);
                break;
            case R.id.linearCalendar:
                ((HomeActivity) activity).UpdateStudentFragment(new TeachersAdditionalActivity(), "CALENDAR", "", "");
                break;
            case R.id.linearMedia:
                ((HomeActivity) activity).UpdateStudentFragment(new TeachersAdditionalActivity(), "MEDIA", "", "");
                break;
            case R.id.linearForums:
                ((HomeActivity) activity).UpdateStudentFragment(new TeachersAdditionalActivity(), "FORUMS", "", "");
                break;

            case R.id.linearAbsentNote:
               ((HomeActivity) activity).UpdateStudentFragment(new TeachersAdditionalActivity(), "ABSENT_NOTE", "", "");
                break;

            case R.id.linearMarks:
                startActivity(new Intent(activity, fill_marks.class)
                        .putExtra("id", staUserInfo.getId())
                        .putExtra("role", 2)
                        .putExtra("url", url)
                );
                break;
/*            case R.id.linearSurvey:

                String role2 =staUserInfo.getRole();
                String url2 ="http://apps.classteacher.school/index.php/admin/app_survey";
                startActivity(new Intent(activity, fill_marks.class)
                        .putExtra("id", staUserInfo.getId())
                        .putExtra("url", url2)
                        .putExtra("role", role2)
                );

                break;*/
            case R.id.imgMore:

                startActivity(new Intent(activity, AnnouncementListActivity.class)
                        .putExtra("tab", ""));
                break;
        }
    }


    public void onAnnouncementClick(int position) {
        startActivity(new Intent(activity, AnnouncementViewActivity.class).putExtra("announcement_info", listAnnouncement.get(position)));
    }

    public void getRetro_AccessAnnouncement(String user_id, String role_id, String status, String last_id, String date_from, String date_to) {
        getRetro_Call(activity, service.getAccessAnnouncement(
                user_id, role_id, status, last_id, date_from, date_to),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Announcement mPojo_Announcement = onPojoBuilder(objectResponse, Result_Announcement.class);
                        if (mPojo_Announcement != null) {
                            if (mPojo_Announcement.getStatus().equals(API_Code.Success)) {
                                listAnnouncement = mPojo_Announcement.getAnnouncement();
                                if (listAnnouncement.size() == 0) {
                                    txtNoNewAnnouncement.setVisibility(View.VISIBLE);
                                } else {
                                    txtNoNewAnnouncement.setVisibility(View.GONE);
                                    mAdapter = new AnnouncementAdapter(activity, listAnnouncement, TeachersDashboard.this);
                                    recyclerAnnouncement.setAdapter(mAdapter);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

}
