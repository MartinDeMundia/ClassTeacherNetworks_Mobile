package com.shamlatech.activity.teacher;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.shamlatech.api_response.Res_Teacher_TeachingSubjects;
import com.shamlatech.api_response.Result_Common;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.FormatDateFromDate;
import static com.shamlatech.utils.Support.FormatDateFromString;
import static com.shamlatech.utils.Support.GetCurrentDateInFormat;
import static com.shamlatech.utils.Vars.staUserInfo;


public class UpdateAssignmentActivity extends BaseActivity {

    Dialog pick_Dialog;

    EditText edtName, edtDetails;
    LinearLayout linearGivenDate, linearDueDate;
    TextView txtGivenDate, txtDueDate;
    Spinner spinnerSubject;
    RelativeLayout relativeDone;
    Calendar instance;

    String StudId = "", AssignmentId = "0";
    ArrayList<Res_Teacher_TeachingSubjects> listTeachingSubject;
    ArrayAdapter<Res_Teacher_TeachingSubjects> adapterSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_assignment);


        declareAppBar2("Assignment", "UpdateAssignmentActivity");
        declareBottomBar();
        UpdateAppBarColor(R.color.colorTabGreen);

        instance = Calendar.getInstance();

        spinnerSubject = findViewById(R.id.spinnerSubject);

        edtName = findViewById(R.id.edtName);
        edtDetails = findViewById(R.id.edtDetails);

        linearGivenDate = findViewById(R.id.linearGivenDate);
        linearDueDate = findViewById(R.id.linearDueDate);

        txtGivenDate = findViewById(R.id.txtGivenDate);
        txtDueDate = findViewById(R.id.txtDueDate);

        relativeDone = findViewById(R.id.relativeDone);

        linearGivenDate.setOnClickListener(this);
        linearDueDate.setOnClickListener(this);
        relativeDone.setOnClickListener(this);

        PreparePage();
    }


    @Override
    protected void onResume() {
        super.onResume();
        InitializeProgress();
    }

    public void InitializeProgress() {
        Vars.Custom_Progress = (View) findViewById(R.id.Custom_Progress);
        Vars.Custom_Progress.setVisibility(View.GONE);
        String indicator = getIntent().getStringExtra("indicator");
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.show();
    }

    private void PreparePage() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("stud_id")) {
                StudId = bundle.getString("stud_id");
                AssignmentId = bundle.getString("assignment_id");
                listTeachingSubject = db.AccessTeachingSubjectBasedStudent(StudId);
                adapterSubject = new ArrayAdapter<Res_Teacher_TeachingSubjects>(this, R.layout.simple_spinner_item, listTeachingSubject);
                adapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSubject.setAdapter(adapterSubject);
                txtGivenDate.setText(GetCurrentDateInFormat(Vars.DatePattern6));
                txtDueDate.setText(GetCurrentDateInFormat(Vars.DatePattern6));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearGivenDate:
                showCalenderPicker("given_date", instance.getTime());
                break;
            case R.id.linearDueDate:
                showCalenderPicker("due_date", instance.getTime());
                break;
            case R.id.relativeDone:
                String AssignmentName = edtName.getText().toString().trim();
                String AssignmentDetails = edtDetails.getText().toString().trim();
                String GivenDate = FormatDateFromDate(FormatDateFromString(txtGivenDate.getText().toString(), Vars.DatePattern6), Vars.DatePattern9);
                String DueDate = FormatDateFromDate(FormatDateFromString(txtDueDate.getText().toString(), Vars.DatePattern6), Vars.DatePattern9);
                String subject_id = listTeachingSubject.get(spinnerSubject.getSelectedItemPosition()).getId();
                if (AssignmentName.equalsIgnoreCase("")) {
                    edtName.setError("Please enter the name of assignment");
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(500);
                } else {
                    getRetro_AddUpdateAssignment(staUserInfo.getId(), staUserInfo.getRole(), StudId, subject_id, AssignmentId, AssignmentName, AssignmentDetails, GivenDate, DueDate);
                }
                break;
            default:
                super.onClick(v);
        }
    }

    private void showCalenderPicker(final String type, Date selectedDate) {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(this);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_custom_date_picker);

            RelativeLayout relativeCalenderHeader;
            final MaterialCalendarView calendarDate;
            TextView txtCalendarName;
            ImageView imgCalendarDateLeft, imgCalendarDateRight;

            relativeCalenderHeader = pick_Dialog.findViewById(R.id.relativeCalenderHeader);
            txtCalendarName = pick_Dialog.findViewById(R.id.txtCalendarName);
            calendarDate = pick_Dialog.findViewById(R.id.calendarDate);
            imgCalendarDateLeft = pick_Dialog.findViewById(R.id.imgCalendarDateLeft);
            imgCalendarDateRight = pick_Dialog.findViewById(R.id.imgCalendarDateRight);

            relativeCalenderHeader.setBackground(getResources().getDrawable(R.drawable.card_header_green));

            calendarDate.setSelectedDate(selectedDate);

            if (type.equals("given_date")) {
                txtCalendarName.setText("Choose Given Date");
            } else {
                txtCalendarName.setText("Choose Due Date");
            }

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
                    pick_Dialog.dismiss();
                    if (type.equals("given_date")) {
                        txtGivenDate.setText(FormatDateFromDate(date.getDate(), Vars.DatePattern6));
                    } else if (type.equals("due_date")) {
                        txtDueDate.setText(FormatDateFromDate(date.getDate(), Vars.DatePattern6));
                    }
                }
            });

            pick_Dialog.show();
        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showCalenderPicker(type, selectedDate);
        } else {
            pick_Dialog = null;
            showCalenderPicker(type, selectedDate);
        }
    }


    public void getRetro_AddUpdateAssignment(String user_id, String role_id, String stud_id, String subject_id, String assignment_id,
                                             String assignment_name, String assignment_details, String given_date, String due_date) {
        getRetro_Call(UpdateAssignmentActivity.this, service.getAddUpdateAssignment(user_id, role_id, stud_id, subject_id, assignment_id,
                assignment_name, assignment_details, given_date, due_date),
                true, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Common mPojo_Assignment = onPojoBuilder(objectResponse, Result_Common.class);
                        if (mPojo_Assignment != null) {
                            if (mPojo_Assignment.getStatus().equals(API_Code.Success)) {
                                Toast.makeText(getApplicationContext(), "Assignment Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }
}
