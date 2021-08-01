package com.shamlatech.activity.teacher;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.shamlatech.api_response.Res_Look_AbsentReason;
import com.shamlatech.api_response.Res_Teacher_TeachingSubjects;
import com.shamlatech.api_response.Result_Common;
import com.shamlatech.pojo.PojoAttendanceKeyPair;
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

public class UpdateAttendanceActivity extends BaseActivity {

    Spinner spinnerSubject, spinnerAttendance, spinnerReason, spinnerTime;
    TextView txtAttendanceDate;
    LinearLayout linearAttendanceDate;
    RelativeLayout relativeDone;
    RelativeLayout relativeAttendance;
    Calendar instance;
    String[] arrayAttendance = {"Present", "Absent"};
    Dialog pick_Dialog;
    String StudId;
    ArrayList<Res_Teacher_TeachingSubjects> listTeachingSubject;
    ArrayList<Res_Look_AbsentReason> listReason;
    ArrayList<PojoAttendanceKeyPair> listTime;
    ArrayAdapter adapterSubject;
    ArrayAdapter adapterReason;
    ArrayAdapter adapterTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_attendance);

        declareAppBar2("Edit Mode: Attendance", "UpdateAttendanceActivity");
        declareBottomBar();
        UpdateAppBarColor(R.color.colorTabOrange);
        declareDb();

        instance = Calendar.getInstance();

        spinnerSubject = findViewById(R.id.spinnerSubject);
        spinnerAttendance = findViewById(R.id.spinnerAttendance);
        spinnerReason = findViewById(R.id.spinnerReason);
        spinnerTime = findViewById(R.id.spinnerTime);

        txtAttendanceDate = findViewById(R.id.txtAttendanceDate);

        linearAttendanceDate = findViewById(R.id.linearAttendanceDate);
        relativeAttendance = findViewById(R.id.relativeAttendance);

        relativeDone = findViewById(R.id.relativeDone);

        relativeAttendance.setVisibility(View.GONE);

        ArrayAdapter adapterAttendance = new ArrayAdapter(this, R.layout.simple_spinner_item, arrayAttendance);
        adapterAttendance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAttendance.setAdapter(adapterAttendance);

        spinnerAttendance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    relativeAttendance.setVisibility(View.VISIBLE);
                } else {
                    relativeAttendance.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        linearAttendanceDate.setOnClickListener(this);
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
                listTeachingSubject = db.AccessTeachingSubjectBasedStudent(StudId);
                adapterSubject = new ArrayAdapter<Res_Teacher_TeachingSubjects>(this, R.layout.simple_spinner_item, listTeachingSubject);
                adapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSubject.setAdapter(adapterSubject);

                listReason = db.AccessAbsentReason();
                adapterReason = new ArrayAdapter<Res_Look_AbsentReason>(this, R.layout.simple_spinner_item, listReason);
                adapterReason.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerReason.setAdapter(adapterReason);


                listTime = db.AccessTimeSlot();
                adapterTime = new ArrayAdapter<PojoAttendanceKeyPair>(this, R.layout.simple_spinner_item, listTime);
                adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerTime.setAdapter(adapterTime);

                txtAttendanceDate.setText(GetCurrentDateInFormat(Vars.DatePattern6));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearAttendanceDate:
                showCalenderPicker("Select Date", instance.getTime());
                break;
            case R.id.relativeDone:
                String strReason = "";
                String strAttendance = (spinnerAttendance.getSelectedItemPosition()+1) + "";
                String strDate = FormatDateFromDate(FormatDateFromString(txtAttendanceDate.getText().toString(), Vars.DatePattern6), Vars.DatePattern9);
                String strTime = listTime.get(spinnerTime.getSelectedItemPosition()).getTime();
                String strSubject = listTeachingSubject.get(spinnerSubject.getSelectedItemPosition()).getId();
                if (strAttendance.equals("2")) {
                    strReason = listReason.get(spinnerReason.getSelectedItemPosition()).getReason();
                }
                getRetro_UpdateAttendance(staUserInfo.getId(), staUserInfo.getRole(), StudId, strDate, strTime,
                        strSubject, strAttendance, strReason);
                break;
            default:
                super.onClick(v);
        }
    }


    private void showCalenderPicker(String strHeader, Date selectedDate) {
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

            relativeCalenderHeader.setBackground(getResources().getDrawable(R.drawable.card_header_orange));

            calendarDate.setSelectedDate(selectedDate);

            txtCalendarName.setText(strHeader);

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
                    txtAttendanceDate.setText(FormatDateFromDate(date.getDate(), Vars.DatePattern6));
                }
            });

            pick_Dialog.show();
        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showCalenderPicker(strHeader, selectedDate);
        } else {
            pick_Dialog = null;
            showCalenderPicker(strHeader, selectedDate);
        }
    }


    public void getRetro_UpdateAttendance(String user_id, String role_id, String stud_id, String date, String period,
                                          String subject_id, String attendance, String reason) {
        getRetro_Call(UpdateAttendanceActivity.this, service.getUpdateAttendance(user_id, role_id, stud_id, date, period,
                subject_id, attendance, reason),
                true, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Common mPojo_Assignment = onPojoBuilder(objectResponse, Result_Common.class);
                        if (mPojo_Assignment != null) {
                            if (mPojo_Assignment.getStatus().equals(API_Code.Success)) {
                                Vars.Refresh_Stud_Attendance = "1";
                                Toast.makeText(getApplicationContext(), "Attendance Updated", Toast.LENGTH_SHORT).show();
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
