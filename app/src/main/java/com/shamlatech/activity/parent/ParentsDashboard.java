package com.shamlatech.activity.parent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.activity.AnnouncementListActivity;
import com.shamlatech.activity.AnnouncementViewActivity;
import com.shamlatech.activity.MessageActivity;
import com.shamlatech.activity.ResourceActivity;
import com.shamlatech.activity.teacher.PaymentsActivity;
import com.shamlatech.adapter.AnnouncementAdapter;
import com.shamlatech.adapter.MyChildrenAdapter;
import com.shamlatech.api_response.Res_Announcement;
import com.shamlatech.api_response.Res_Student_Info;
import com.shamlatech.api_response.Result_Announcement;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.BaseFragment;
import com.shamlatech.school_management.HomeActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.CanReloadAPI;
import static com.shamlatech.utils.Vars.REQUEST_PHONE_CALL;
import static com.shamlatech.utils.Vars.Refresh_Name_Announcement;
import static com.shamlatech.utils.Vars.staUserInfo;

public class ParentsDashboard extends BaseFragment implements View.OnClickListener {

    Activity activity;
    MyChildrenAdapter myChildrenAdapter;
    ViewPager pagerChildren;

    TextView txtNoNewAnnouncement;
    LinearLayout linearEducationalProgress, linearBehaviouralProgress,
            linearHealthReport, linearAttendance,
            linearFees, linearCalendar, linearMedia, linearForums, linearPayments ,linearAbsentNote ,linearJournalReport,linearResources;
    ImageView imgMore;
    RelativeLayout relativeIntro;
    RecyclerView recyclerAnnouncement;
    ArrayList<Res_Announcement> listAnnouncement = new ArrayList<>();
    ArrayList<Res_Student_Info> listChildren = new ArrayList<>();
    String stud_id = "";
    private AnnouncementAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_parents_dashboard, container, false);

        listAnnouncement = new ArrayList<>();
        listChildren = new ArrayList<>();
        declareDb(activity);


        listChildren = db.AccessParentStudent();

        init(view);

        mAdapter = new AnnouncementAdapter(activity, listAnnouncement, ParentsDashboard.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerAnnouncement.setLayoutManager(mLayoutManager);
        recyclerAnnouncement.setItemAnimator(new DefaultItemAnimator());
        recyclerAnnouncement.setAdapter(mAdapter);


        myChildrenAdapter = new MyChildrenAdapter(activity, listChildren, ParentsDashboard.this);
        pagerChildren.setAdapter(myChildrenAdapter);
        if (listChildren.size() > 0) {
            stud_id = listChildren.get(0).getId();
            relativeIntro.setVisibility(View.VISIBLE);
        } else {
            relativeIntro.setVisibility(View.GONE);
        }
        pagerChildren.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                stud_id = listChildren.get(position).getId();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;

    }

    public void init(View view) {

        relativeIntro = view.findViewById(R.id.relativeIntro);

        txtNoNewAnnouncement = view.findViewById(R.id.txtNoNewAnnouncement);
        linearResources =  view.findViewById(R.id.linearResources);
        linearEducationalProgress = view.findViewById(R.id.linearEducationalProgress);
        linearBehaviouralProgress = view.findViewById(R.id.linearBehaviouralProgress);
        linearHealthReport = view.findViewById(R.id.linearHealthReport);
        linearAttendance = view.findViewById(R.id.linearAttendance);

        linearFees = view.findViewById(R.id.linearFees);
        linearCalendar = view.findViewById(R.id.linearCalendar);
        linearMedia = view.findViewById(R.id.linearMedia);
        linearForums = view.findViewById(R.id.linearForums);
        linearPayments = view.findViewById(R.id.linearPayments);
        linearAbsentNote = view.findViewById(R.id.linearAbsentNote);
        linearJournalReport = view.findViewById(R.id.linearJournalReport);

        imgMore = view.findViewById(R.id.imgMore);

        recyclerAnnouncement = view.findViewById(R.id.recyclerAnnouncement);
        pagerChildren = view.findViewById(R.id.pagerChildren);

        linearResources.setOnClickListener(this);
        linearEducationalProgress.setOnClickListener(this);
        linearBehaviouralProgress.setOnClickListener(this);
        linearHealthReport.setOnClickListener(this);
        linearAttendance.setOnClickListener(this);
        linearFees.setOnClickListener(this);
        linearCalendar.setOnClickListener(this);
        linearMedia.setOnClickListener(this);
        linearForums.setOnClickListener(this);
        linearPayments.setOnClickListener(this);
        imgMore.setOnClickListener(this);
        linearAbsentNote.setOnClickListener(this);
        linearJournalReport.setOnClickListener(this);
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
            case R.id.linearEducationalProgress:
                if (!stud_id.isEmpty())
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentStudentActivity(), "EDUCATION", stud_id, "");
                break;
            case R.id.linearBehaviouralProgress:
                if (!stud_id.isEmpty())
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentStudentActivity(), "BEHAVIOUR", stud_id, "");
                break;
            case R.id.linearHealthReport:
                if (!stud_id.isEmpty())
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentStudentActivity(), "HEALTH", stud_id, "");
                break;
            case R.id.linearAttendance:
                if (!stud_id.isEmpty())
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentStudentActivity(), "ATTENDANCE", stud_id, "");
                break;
            case R.id.linearFees:
                if (!stud_id.isEmpty())
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentsAdditionalActivity(), "FEES", stud_id, "");
                break;
            case R.id.linearCalendar:
                if (!stud_id.isEmpty())
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentsAdditionalActivity(), "CALENDAR", stud_id, "");
                break;
            case R.id.linearMedia:
                if (!stud_id.isEmpty())
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentsAdditionalActivity(), "MEDIA", stud_id, "");
                break;
            case R.id.linearForums:
                if (!stud_id.isEmpty())
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentsAdditionalActivity(), "FORUMS", stud_id, "");
                break;
            case R.id.linearPayments:
                activity.startActivity(new Intent(activity, PaymentsActivity.class));
                break;
            case R.id.imgMore:
                startActivity(new Intent(activity, AnnouncementListActivity.class));
                break;
            case R.id.linearAbsentNote:
                ((HomeActivity) activity).UpdateStudentFragment(new ParentsAdditionalActivity(), "ABSENT_NOTE", "", "");
                break;
            case R.id.linearJournalReport:
                if (!stud_id.isEmpty())
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentStudentActivity(), "JOURNAL", stud_id, "");
                break;
        }
    }

    public void onAnnouncementClick(int position) {
        startActivity(new Intent(activity, AnnouncementViewActivity.class).putExtra("announcement_info", listAnnouncement.get(position)));
    }

    public void onNextClicked(int position) {
        pagerChildren.setCurrentItem(position + 1, true);
    }

    public void onPrevClicked(int position) {
        pagerChildren.setCurrentItem(position - 1, true);
    }

    public void onClickCallTeacher(int position) {
        sendCall(listChildren.get(position).getClass_teacher().getPhone_number());
    }

    public void onClickMessageTeacher(int position) {
        startActivity(new Intent(activity, MessageActivity.class)
                .putExtra("id", listChildren.get(position).getClass_teacher().getId())
                .putExtra("role", Vars.Role_Teacher)
                .putExtra("name", listChildren.get(position).getClass_teacher().getFirst_name())
                .putExtra("image", listChildren.get(position).getClass_teacher().getImage()));

    }

    public void onClickMailTeacher(int position) {
        sendEmail(listChildren.get(position).getClass_teacher().getEmail());
    }


    protected void sendCall(String phoneNumber) {
        Intent in = new Intent(Intent.ACTION_CALL);
        in.setData(Uri.parse("tel:" + phoneNumber));
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            startActivity(in);
        }
    }

    protected void sendEmail(String Email) {
        String[] TO = {Email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
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
                                    mAdapter = new AnnouncementAdapter(activity, listAnnouncement, ParentsDashboard.this);
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
