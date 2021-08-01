package com.shamlatech.activity.teacher;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.shamlatech.adapter.AssignmentDetailListAdapter;
import com.shamlatech.adapter.ExamEditListAdapter;
import com.shamlatech.api_response.Res_Assignment_List;
import com.shamlatech.api_response.Res_Look_ExamList;
import com.shamlatech.api_response.Res_Teacher_TeachingSubjects;
import com.shamlatech.api_response.Result_Assignment_List;
import com.shamlatech.api_response.Result_Common;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.FormatDateForShow;
import static com.shamlatech.utils.Support.FormatDateFromDate;
import static com.shamlatech.utils.Support.FormatDateFromString;
import static com.shamlatech.utils.Support.GetCurrentDateInFormat;
import static com.shamlatech.utils.Vars.Assignment_Status_Cancel;
import static com.shamlatech.utils.Vars.Assignment_Status_Not_Submitted;
import static com.shamlatech.utils.Vars.Assignment_Status_Pending;
import static com.shamlatech.utils.Vars.Assignment_Status_Submitted;
import static com.shamlatech.utils.Vars.staUserInfo;

public class EducationEditActivity extends BaseActivity {

    TextView txtSubjects, txtInformationHint;

    RecyclerView recyclerEditExam, recyclerAssignmentEdit;

    ArrayList<Res_Look_ExamList> listExams = new ArrayList<>();
    ArrayList<Res_Assignment_List> listAssignment = new ArrayList<>();
    ExamEditListAdapter mExamAdapter;
    TextView txtAdd;
    AssignmentDetailListAdapter mAssignmentAdapter;
    String StudId = "";
    ArrayList<Res_Teacher_TeachingSubjects> listTeachingSubject = new ArrayList<>();
    String Subjects = "";
    Dialog pick_Dialog, pick_Dialog1;
    Calendar instance;
    TextView txtSubmittedDateEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_edit);

        instance = Calendar.getInstance();


        declareAppBar2("Edit Mode", "EducationEditActivity");
        declareBottomBar();
        UpdateAppBarColor(R.color.colorTabGreen);
        declareDb();

        txtSubjects = findViewById(R.id.txtSubjects);
        recyclerEditExam = findViewById(R.id.recyclerEditExam);
        recyclerAssignmentEdit = findViewById(R.id.recyclerAssignmentEdit);
        txtAdd = findViewById(R.id.txtAdd);

        listExams = new ArrayList<>();
        listAssignment = new ArrayList<>();

        listExams = db.AccessExamList();

        mExamAdapter = new ExamEditListAdapter(this, listExams);
        RecyclerView.LayoutManager mExamLayoutManager = new LinearLayoutManager(this);
        recyclerEditExam.setLayoutManager(mExamLayoutManager);
        recyclerEditExam.setItemAnimator(new DefaultItemAnimator());
        recyclerEditExam.setAdapter(mExamAdapter);

        mAssignmentAdapter = new AssignmentDetailListAdapter(this, listAssignment, "edit");
        RecyclerView.LayoutManager mAssignmentLayoutManager = new LinearLayoutManager(this);
        recyclerAssignmentEdit.setLayoutManager(mAssignmentLayoutManager);
        recyclerAssignmentEdit.setItemAnimator(new DefaultItemAnimator());
        recyclerAssignmentEdit.setAdapter(mAssignmentAdapter);

        txtAdd.setOnClickListener(this);
        PreparePage();

    }

    private void PreparePage() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("stud_id")) {
                StudId = bundle.getString("stud_id");
                getRetro_AccessStudentAssignmentList(staUserInfo.getId(), staUserInfo.getRole(), StudId);
                listTeachingSubject = db.AccessTeachingSubjectBasedStudent(StudId);
                for (int i = 0; i < listTeachingSubject.size(); i++) {
                    Subjects += listTeachingSubject.get(i).getSubject_name();
                    if (listTeachingSubject.size() != (i + 1)) {
                        Subjects += ", ";
                    }
                }
                txtSubjects.setText(Subjects);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRetro_AccessStudentAssignmentList(staUserInfo.getId(), staUserInfo.getRole(), StudId);
    }

    public void onClickEditExam(int position) {
        startActivity(new Intent(EducationEditActivity.this, UpdateScoreActivity.class)
                .putExtra("exam_id", listExams.get(position).getId())
                .putExtra("exam_name", listExams.get(position).getExam_name())
                .putExtra("stud_id", StudId));
    }

    public void onClickAssignment(int position) {
        showShowAssignment(listAssignment.get(position));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtAdd:
                startActivity(new Intent(EducationEditActivity.this, UpdateAssignmentActivity.class)
                        .putExtra("stud_id", StudId)
                        .putExtra("assignment_id", "0"));
                break;
            default:
                super.onClick(v);
        }

    }


    private void showShowAssignment(final Res_Assignment_List assignment) {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(this);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_assignment_view);

            TextView txtSubjectName, txtAssignmentName, txtAssignmentDetails, txtGivenDate, txtDueDate, txtSubmittedDateView, txtStatus;
            Button btnNotSubmit, btnCancelled, btnSubmitted;
            LinearLayout linearAction;
            RelativeLayout relativeSubmit, relativeStatus;

            txtStatus = pick_Dialog.findViewById(R.id.txtStatus);
            txtSubjectName = pick_Dialog.findViewById(R.id.txtSubjectName);
            txtAssignmentName = pick_Dialog.findViewById(R.id.txtAssignmentName);
            txtAssignmentDetails = pick_Dialog.findViewById(R.id.txtAssignmentDetails);
            txtGivenDate = pick_Dialog.findViewById(R.id.txtGivenDate);
            txtDueDate = pick_Dialog.findViewById(R.id.txtDueDate);
            txtSubmittedDateEdit = pick_Dialog.findViewById(R.id.txtSubmittedDateEdit);
            txtSubmittedDateView = pick_Dialog.findViewById(R.id.txtSubmittedDateView);
            relativeSubmit = pick_Dialog.findViewById(R.id.relativeSubmit);
            relativeStatus = pick_Dialog.findViewById(R.id.relativeStatus);

            btnNotSubmit = pick_Dialog.findViewById(R.id.btnNotSubmit);
            btnCancelled = pick_Dialog.findViewById(R.id.btnCancelled);
            btnSubmitted = pick_Dialog.findViewById(R.id.btnSubmitted);

            linearAction = pick_Dialog.findViewById(R.id.linearAction);

            txtSubjectName.setText(assignment.getSubject_name());
            txtAssignmentName.setText(assignment.getName());
            txtAssignmentDetails.setText(assignment.getDetails());
            txtGivenDate.setText(FormatDateForShow(assignment.getGiven_date(), Vars.DatePattern8, ""));
            txtDueDate.setText(FormatDateForShow(assignment.getDue_date(), Vars.DatePattern8, ""));
            txtSubmittedDateView.setText(FormatDateForShow(assignment.getSubmit_on(), Vars.DatePattern8, ""));
            txtSubmittedDateEdit.setText(GetCurrentDateInFormat(Vars.DatePattern6));

            if (assignment.getStatus().equalsIgnoreCase(Assignment_Status_Pending)) {
                txtSubmittedDateEdit.setVisibility(View.VISIBLE);
                txtSubmittedDateView.setVisibility(View.GONE);
                linearAction.setVisibility(View.VISIBLE);
                relativeStatus.setVisibility(View.GONE);
            } else {

                txtSubmittedDateEdit.setVisibility(View.GONE);
                relativeSubmit.setVisibility(View.GONE);
                linearAction.setVisibility(View.GONE);
                relativeStatus.setVisibility(View.VISIBLE);
                if (assignment.getStatus().equalsIgnoreCase(Assignment_Status_Submitted)) {
                    relativeSubmit.setVisibility(View.VISIBLE);
                    txtStatus.setText("Submitted");
                } else if (assignment.getStatus().equalsIgnoreCase(Assignment_Status_Not_Submitted)) {
                    txtStatus.setText("Not Submitted");
                } else if (assignment.getStatus().equalsIgnoreCase(Assignment_Status_Cancel)) {
                    txtStatus.setText("Cancelled");
                }
            }

            btnNotSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pick_Dialog.dismiss();
                    String strDate = FormatDateFromDate(FormatDateFromString(txtSubmittedDateEdit.getText().toString(), Vars.DatePattern6), Vars.DatePattern9);
                    getRetro_AddSubmitAssignment(staUserInfo.getId(), staUserInfo.getRole(), StudId, assignment.getId(), strDate, Assignment_Status_Not_Submitted);
                }
            });

            btnCancelled.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pick_Dialog.dismiss();
                    String strDate = FormatDateFromDate(FormatDateFromString(txtSubmittedDateEdit.getText().toString(), Vars.DatePattern6), Vars.DatePattern9);
                    getRetro_AddSubmitAssignment(staUserInfo.getId(), staUserInfo.getRole(), StudId, assignment.getId(), strDate, Assignment_Status_Cancel);
                }
            });

            btnSubmitted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pick_Dialog.dismiss();
                    String strDate = FormatDateFromDate(FormatDateFromString(txtSubmittedDateEdit.getText().toString(), Vars.DatePattern6), Vars.DatePattern9);
                    getRetro_AddSubmitAssignment(staUserInfo.getId(), staUserInfo.getRole(), StudId, assignment.getId(), strDate, Assignment_Status_Submitted);
                }
            });

            txtSubmittedDateEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCalenderPicker("Assignment Submit Date", instance.getTime());

                }
            });

            pick_Dialog.show();
        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showShowAssignment(assignment);
        } else {
            pick_Dialog = null;
            showShowAssignment(assignment);
        }
    }


    private void showCalenderPicker(String type, Date selectedDate) {
        if (pick_Dialog1 == null) {
            pick_Dialog1 = new Dialog(this);
            pick_Dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog1.setCancelable(true);
            pick_Dialog1.setCanceledOnTouchOutside(true);
            pick_Dialog1.setContentView(R.layout.pop_custom_date_picker);

            RelativeLayout relativeCalenderHeader;
            final MaterialCalendarView calendarDate;
            TextView txtCalendarName;
            ImageView imgCalendarDateLeft, imgCalendarDateRight;

            relativeCalenderHeader = pick_Dialog1.findViewById(R.id.relativeCalenderHeader);
            txtCalendarName = pick_Dialog1.findViewById(R.id.txtCalendarName);
            calendarDate = pick_Dialog1.findViewById(R.id.calendarDate);
            imgCalendarDateLeft = pick_Dialog1.findViewById(R.id.imgCalendarDateLeft);
            imgCalendarDateRight = pick_Dialog1.findViewById(R.id.imgCalendarDateRight);

            relativeCalenderHeader.setBackground(getResources().getDrawable(R.drawable.card_header_green));

            calendarDate.setSelectedDate(selectedDate);

            txtCalendarName.setText(type);

            imgCalendarDateLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendarDate.goToPrevious();
                }
            });

            imgCalendarDateRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    calendarDate.goToNext();
                }
            });

            calendarDate.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                    pick_Dialog1.dismiss();
                    txtSubmittedDateEdit.setText(FormatDateFromDate(date.getDate(), Vars.DatePattern6));
                }
            });

            pick_Dialog1.show();
        } else if (pick_Dialog1.isShowing()) {
            pick_Dialog1.dismiss();
            showCalenderPicker(type, selectedDate);
        } else {
            pick_Dialog1 = null;
            showCalenderPicker(type, selectedDate);
        }
    }

    public void getRetro_AccessStudentAssignmentList(String user_id, String role_id, String stud_id) {
        getRetro_Call(EducationEditActivity.this, service.getAccessStudentAssignmentList(user_id, role_id, stud_id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Assignment_List mPojo_Assignment = onPojoBuilder(objectResponse, Result_Assignment_List.class);
                        if (mPojo_Assignment != null) {
                            if (mPojo_Assignment.getStatus().equals(API_Code.Success)) {
                                listAssignment = mPojo_Assignment.getAssignment();
                                mAssignmentAdapter = new AssignmentDetailListAdapter(EducationEditActivity.this, listAssignment, "edit");
                                recyclerAssignmentEdit.setAdapter(mAssignmentAdapter);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }


    public void getRetro_AddSubmitAssignment(String user_id, String role_id, String stud_id, String assignment_id, String date, String status) {
        getRetro_Call(EducationEditActivity.this, service.getSubmitAssignment(user_id, role_id, stud_id, assignment_id, date, status),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Common mPojo_Assignment = onPojoBuilder(objectResponse, Result_Common.class);
                        if (mPojo_Assignment != null) {
                            if (mPojo_Assignment.getStatus().equals(API_Code.Success)) {
                                Toast.makeText(getApplicationContext(), "Assignment Updated", Toast.LENGTH_SHORT).show();
                                getRetro_AccessStudentAssignmentList(staUserInfo.getId(), staUserInfo.getRole(), StudId);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }
}
