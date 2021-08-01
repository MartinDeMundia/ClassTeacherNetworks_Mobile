package com.shamlatech.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.shamlatech.api_response.Result_Common;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

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

public class LastHealthAddActivity extends BaseActivity {

    Calendar instance;
    Dialog pick_Dialog;
    TextView txtDate;
    LinearLayout linearAttendanceDate;
    EditText edtOccurrence, edtActionTaken, edtFurtherAction;
    RelativeLayout relativeDone;
    String StudId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_health_add);

        declareAppBar2("Add Health Occurrence", "UpdateAttendanceActivity");
        UpdateAppBarColor(R.color.colorTabRed);
        declareDb();


        instance = Calendar.getInstance();

        txtDate = findViewById(R.id.txtDate);
        edtOccurrence = findViewById(R.id.edtOccurrence);
        edtActionTaken = findViewById(R.id.edtActionTaken);
        edtFurtherAction = findViewById(R.id.edtFurtherAction);
        relativeDone = findViewById(R.id.relativeDone);

        linearAttendanceDate = findViewById(R.id.linearAttendanceDate);
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
                txtDate.setText(GetCurrentDateInFormat(Vars.DatePattern6));
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
                String strDate = FormatDateFromDate(FormatDateFromString(txtDate.getText().toString(), Vars.DatePattern6), Vars.DatePattern9);
                String strOccurrence = edtOccurrence.getText().toString().trim();
                String strActionTaken = edtActionTaken.getText().toString().trim();
                String strFurtherAction = edtFurtherAction.getText().toString().trim();

                boolean Error = false;
                if (strOccurrence.equals("")) {
                    edtOccurrence.setError("Please fill this");
                    Error = true;
                }
                if (strActionTaken.equals("")) {
                    edtActionTaken.setError("Please fill this");
                    Error = true;
                }
                if (!Error) {
                    getRetro_UpdateHealth(staUserInfo.getId(), staUserInfo.getRole(), StudId, strDate, strOccurrence,
                            strActionTaken, strFurtherAction);
                }
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

            relativeCalenderHeader.setBackground(getResources().getDrawable(R.drawable.card_header_red));

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
                    txtDate.setText(FormatDateFromDate(date.getDate(), Vars.DatePattern6));
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


    public void getRetro_UpdateHealth(String user_id, String role_id, String stud_id, String date, String occurrence,
                                      String action, String further_action) {
        getRetro_Call(LastHealthAddActivity.this, service.getUpdateLastHealthOccurrence(user_id, role_id, stud_id, date, occurrence,
                action, further_action),
                true, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Common mPojo_Assignment = onPojoBuilder(objectResponse, Result_Common.class);
                        if (mPojo_Assignment != null) {
                            if (mPojo_Assignment.getStatus().equals(API_Code.Success)) {
                                Vars.Refresh_Stud_Health = "1";
                                Toast.makeText(getApplicationContext(), "Health Occurrence Updated", Toast.LENGTH_SHORT).show();
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
