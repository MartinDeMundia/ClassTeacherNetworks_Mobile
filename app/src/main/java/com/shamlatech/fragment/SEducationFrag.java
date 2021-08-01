package com.shamlatech.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.activity.parent.AssignmentViewActivity;
import com.shamlatech.activity.parent.ParentStudentActivity;
import com.shamlatech.activity.teacher.EducationEditActivity;
import com.shamlatech.activity.teacher.TeachersStudentActivity;
import com.shamlatech.adapter.AssignmentListAdapter;
import com.shamlatech.adapter.ExamListAdapter;
import com.shamlatech.adapter.OverAllMarkListAdapter;
import com.shamlatech.adapter.SubjectReportAdapter;
import com.shamlatech.api_response.Res_Stud_Education_Assignment;
import com.shamlatech.api_response.Res_Stud_Education_Detailed_Marks;
import com.shamlatech.api_response.Res_Stud_Education_Overall_Marks;
import com.shamlatech.api_response.Res_Stud_Education_Terms;
import com.shamlatech.api_response.Res_Subject_Report;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import static com.shamlatech.utils.Support.downloadFile;
import static com.shamlatech.utils.Support.sendEmail;
import static com.shamlatech.utils.Vars.Assignment_Status_Pending;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Martin Mundia Mugambi on 12-11-2019.
 */

@SuppressLint("ValidFragment")
public class SEducationFrag extends Fragment implements View.OnClickListener {

    Activity activity;
    View view;
    ArrayList<Res_Stud_Education_Detailed_Marks> listDetailedMarks = new ArrayList<>();
    ArrayList<Res_Stud_Education_Overall_Marks> listOverallMarks = new ArrayList<>();
    ArrayList<Res_Subject_Report> listSubjectReport = new ArrayList<>();
    ArrayList<Res_Stud_Education_Assignment> listAssignment = new ArrayList<>();
    OverAllMarkListAdapter markAdapter;
    SubjectReportAdapter subjAdapter;
    ExamListAdapter examAdapter;
    AssignmentListAdapter assignmentAdapter;
    TextView txtOverAllPerformance;
    LinearLayout linearDownload, linearShare, linearEmail ,linearDownloadSubjectReport;

    RecyclerView recyclerOverallResult, recyclerAssignment, recyclerExamResult , recyclerSubjectReport;
    FloatingActionButton fabEditEducation;
    Res_Stud_Education_Terms studEducationInfo;
    String StudId = "";

    RelativeLayout relativeOverAllPerformance;
    NestedScrollView scrollContent;
    View InnerProgress;

    TeachersStudentActivity teachersStudentActivity;
    ParentStudentActivity parentStudentActivity;
    Spinner spinnerTerm;
    Res_Stud_Education_Terms res_stud_education;
    ArrayList<String> termsList;
    String selectedTerm = "";
    RelativeLayout relativeNoData;
    TextView txtViewAssignment;

    @SuppressLint("ValidFragment")
    public SEducationFrag(TeachersStudentActivity teachersStudentActivity) {
        this.teachersStudentActivity = teachersStudentActivity;
    }

    @SuppressLint("ValidFragment")
    public SEducationFrag(ParentStudentActivity parentStudentActivity) {
        this.parentStudentActivity = parentStudentActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_stud_education, container, false);

        listDetailedMarks = new ArrayList<>();
        listOverallMarks = new ArrayList<>();
        listAssignment = new ArrayList<>();
        termsList = new ArrayList<>();
        InitializeProgress(view);

        txtViewAssignment = view.findViewById(R.id.txtViewAssignment);
        relativeNoData = view.findViewById(R.id.relativeNoData);
        spinnerTerm = view.findViewById(R.id.spinnerTerm);
        linearDownload = view.findViewById(R.id.linearDownload);
        linearDownloadSubjectReport = view.findViewById(R.id.linearDownloadSubjectReport);
        linearShare = view.findViewById(R.id.linearShare);
        linearEmail = view.findViewById(R.id.linearEmail);
        txtOverAllPerformance = view.findViewById(R.id.txtOverAllPerformance);
        recyclerOverallResult = view.findViewById(R.id.recyclerOverallResult);
        recyclerAssignment = view.findViewById(R.id.recyclerAssignment);
        recyclerExamResult = view.findViewById(R.id.recyclerExamResult);

        fabEditEducation = view.findViewById(R.id.fabEditEducation);
        relativeOverAllPerformance = view.findViewById(R.id.relativeOverAllPerformance);

        markAdapter = new OverAllMarkListAdapter(activity, listOverallMarks);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerOverallResult.setLayoutManager(mLayoutManager);
        recyclerOverallResult.setItemAnimator(new DefaultItemAnimator());
        recyclerOverallResult.setAdapter(markAdapter);

        examAdapter = new ExamListAdapter(activity, listDetailedMarks);
        RecyclerView.LayoutManager mexamLayoutManager = new LinearLayoutManager(activity);
        recyclerExamResult.setLayoutManager(mexamLayoutManager);
        recyclerExamResult.setItemAnimator(new DefaultItemAnimator());
        recyclerExamResult.setAdapter(examAdapter);

        subjAdapter = new SubjectReportAdapter(activity, listSubjectReport);
        recyclerSubjectReport =  view.findViewById(R.id.recyclerSubjectReport);
        RecyclerView.LayoutManager subjRepLayoutManager = new LinearLayoutManager(activity);
        recyclerSubjectReport .setLayoutManager(subjRepLayoutManager);
        recyclerSubjectReport .setItemAnimator(new DefaultItemAnimator());
        recyclerSubjectReport .setAdapter(subjAdapter);

        assignmentAdapter = new AssignmentListAdapter(activity, listAssignment);
        RecyclerView.LayoutManager massignLayoutManager = new LinearLayoutManager(activity);
        recyclerAssignment.setLayoutManager(massignLayoutManager);
        recyclerAssignment.setItemAnimator(new DefaultItemAnimator());
        recyclerAssignment.setAdapter(assignmentAdapter);

        linearDownload.setOnClickListener(this);
        linearShare.setOnClickListener(this);
        linearEmail.setOnClickListener(this);
        fabEditEducation.setOnClickListener(this);
        txtViewAssignment.setOnClickListener(this);
        linearDownloadSubjectReport.setOnClickListener(this);
        preparePage();

        return view;
    }

    public void InitializeProgress(View view) {
        scrollContent = view.findViewById(R.id.scrollContent);
        InnerProgress = (View) view.findViewById(R.id.ProgressInner);
        InnerProgress.setVisibility(View.VISIBLE);
        scrollContent.setVisibility(View.INVISIBLE);
        String indicator = activity.getIntent().getStringExtra("indicator");
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.show();
    }


    public void loadData(Bundle bundle) {
        studEducationInfo = new Res_Stud_Education_Terms();
        if (bundle != null) {
            if (bundle.containsKey("education_info")) {
                termsList = new ArrayList<>();
                studEducationInfo = (Res_Stud_Education_Terms) bundle.getSerializable("education_info");

                for (int i = 0; i < studEducationInfo.getOverall_marks().size(); i++) {
                    Res_Stud_Education_Overall_Marks terms = studEducationInfo.getOverall_marks().get(i);
                    if (terms.getExam_name() != null)
                        termsList.add(terms.getExam_name());
                }

                ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(activity, R.layout.simple_spinner_item_green, termsList);
                adapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTerm.setAdapter(adapterSubject);

                spinnerTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedTerm = termsList.get(position);
                        PrepareLoadData(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                if (!termsList.isEmpty())
                    spinnerTerm.setSelection(termsList.size() - 1);
                else {
                    InnerProgress.setVisibility(View.GONE);
                    scrollContent.setVisibility(View.GONE);
                    relativeNoData.setVisibility(View.VISIBLE);
                }

                StudId = bundle.getString("stud_id");

            }
        }
    }

    public void showProgress() {
        InitializeProgress(view);
    }

    private void PrepareLoadData(int position) {
        InnerProgress.setVisibility(View.GONE);
        scrollContent.setVisibility(View.VISIBLE);
        relativeNoData.setVisibility(View.GONE);

        txtOverAllPerformance.setText(studEducationInfo.getOverall_performance_grade());
        if (studEducationInfo.getOverall_performance_grade().equals("")) {
            relativeOverAllPerformance.setVisibility(View.GONE);
            txtOverAllPerformance.setVisibility(View.GONE);
        } else {
            relativeOverAllPerformance.setVisibility(View.VISIBLE);
            txtOverAllPerformance.setVisibility(View.VISIBLE);
        }

        listOverallMarks = studEducationInfo.getOverall_marks();
        listDetailedMarks = new ArrayList<>();
        listDetailedMarks.add(studEducationInfo.getDetailed_marks().get(position));
        listAssignment = studEducationInfo.getAssignments();

        markAdapter = new OverAllMarkListAdapter(activity, listOverallMarks);
        recyclerOverallResult.setAdapter(markAdapter);

        examAdapter = new ExamListAdapter(activity, listDetailedMarks);
        recyclerExamResult.setAdapter(examAdapter);

        if (listAssignment.size() == 0) {
            txtViewAssignment.setText("No Assignments");
            txtViewAssignment.setEnabled(false);
        }

        ArrayList<Res_Stud_Education_Assignment> templistAssignment = new ArrayList<>();
        templistAssignment.addAll(listAssignment);
        listAssignment = new ArrayList<>();
        for (int i = 0; i < templistAssignment.size(); i++) {
            if (templistAssignment.get(i).getStatus().equals(Assignment_Status_Pending)) {
                listAssignment.add(templistAssignment.get(i));
            }
        }

        assignmentAdapter = new AssignmentListAdapter(activity, listAssignment);
        recyclerAssignment.setAdapter(assignmentAdapter);
        if (listAssignment.isEmpty())
            recyclerAssignment.setVisibility(View.GONE);
        else
            recyclerAssignment.setVisibility(View.VISIBLE);

        //subject reports
        listSubjectReport = studEducationInfo.getSubjectReports();
        ArrayList<Res_Subject_Report> templistSubjectReports = new ArrayList<>();
        templistSubjectReports.addAll(listSubjectReport);
        listSubjectReport = new ArrayList<>();
        for (int i = 0; i < templistSubjectReports.size(); i++) {
                listSubjectReport.add(templistSubjectReports.get(i));
        }

        subjAdapter = new SubjectReportAdapter(activity, listSubjectReport);
        recyclerSubjectReport.setAdapter(subjAdapter);
        if (listSubjectReport.isEmpty())
            recyclerSubjectReport.setVisibility(View.GONE);
        else
            recyclerSubjectReport.setVisibility(View.VISIBLE);



    }

    private void preparePage() {
        if (staUserInfo.getRole().equals(Vars.Role_Teacher) || staUserInfo.getRole().equals(Vars.Role_Principal)) {
            fabEditEducation.setVisibility(View.VISIBLE);
        } else {
            fabEditEducation.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtViewAssignment:
                startActivity(new Intent(activity, AssignmentViewActivity.class).putExtra("stud_id", StudId));
                break;
            case R.id.fabEditEducation:
                startActivity(new Intent(activity, EducationEditActivity.class).putExtra("stud_id", StudId));
                break;
            case R.id.linearDownload:
                if (studEducationInfo.getReport_download_link() != null && !studEducationInfo.getReport_download_link().isEmpty())
                    downloadFile(activity, studEducationInfo.getReport_download_link());
                else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.linearDownloadSubjectReport:
                if (studEducationInfo.getSubjectReport_download_link() != null && !studEducationInfo.getSubjectReport_download_link().isEmpty())
                    downloadFile(activity, studEducationInfo.getSubjectReport_download_link());
                else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.linearShare:
                if (studEducationInfo.getReport_download_link() != null && !studEducationInfo.getReport_download_link().isEmpty()) {
                    Support.shareLink(activity, studEducationInfo.getReport_download_link());
                } else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearEmail:
                sendEmail(activity, studEducationInfo.getReport_download_link());
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}