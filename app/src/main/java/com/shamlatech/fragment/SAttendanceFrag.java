package com.shamlatech.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.shamlatech.activity.teacher.UpdateAttendanceActivity;
import com.shamlatech.adapter.AbsentListAdapter;
import com.shamlatech.adapter.ClassPositionAdapter;
import com.shamlatech.api_response.Res_Stud_Attendance;
import com.shamlatech.api_response.Res_Stud_Attendance_Details;
import com.shamlatech.pojo.PojoClassPosition;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.vo.DateData;

import static com.shamlatech.utils.Support.ConvertToFloat;
import static com.shamlatech.utils.Support.ConvertToInteger;
import static com.shamlatech.utils.Support.FormatDateForShow;
import static com.shamlatech.utils.Support.FormatDateFromDate;
import static com.shamlatech.utils.Support.PrepareSeatForStudent;
import static com.shamlatech.utils.Support.downloadFile;
import static com.shamlatech.utils.Support.sendEmail;
import static com.shamlatech.utils.Vars.staStudentInfo;

/**
 * Created by Martin Mundia Mugambi on 15-11-2019.
 */

public class SAttendanceFrag extends Fragment implements View.OnClickListener {

    View view;

    LinearLayout linearDownload, linearShare, linearEmail;
    TextView txtLastMissedDate, txtLastMissedTime, txtSubjectMissed, txtReason, txtOverAllAttendance, txtDate, txtWeek;
    SimpleRatingBar rattingOverAllAttendance;
    ProgressBar progressAbsent;

    MaterialCalendarView calendarAttendance;

    FloatingActionButton fabEditAttendance;

    RecyclerView recyclerAbsent, recyclerClassLayout;
    AbsentListAdapter mAbsentAdapter;
    ArrayList<Res_Stud_Attendance_Details> listAbsent = new ArrayList<>();
    ArrayList<PojoClassPosition> listPosition = new ArrayList<>();
    ClassPositionAdapter mClassAdapter;
    Res_Stud_Attendance studAttendanceInfo;
    TextView txtNoLeaveLabel;
    String StudId = "";
    TextView txtClassAbsentLabel;
    Activity activity;
    Calendar instance;

    NestedScrollView scrollContent;
    View InnerProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_stud_attendance, container, false);

        listAbsent = new ArrayList<>();
        listPosition = new ArrayList<>();
        InitializeProgress(view);

        linearDownload = view.findViewById(R.id.linearDownload);
        linearShare = view.findViewById(R.id.linearShare);
        linearEmail = view.findViewById(R.id.linearEmail);
        txtLastMissedDate = view.findViewById(R.id.txtLastMissedDate);
        txtLastMissedTime = view.findViewById(R.id.txtLastMissedTime);
        txtSubjectMissed = view.findViewById(R.id.txtSubjectMissed);
        txtReason = view.findViewById(R.id.txtReason);
        txtNoLeaveLabel = view.findViewById(R.id.txtNoLeaveLabel);
        txtClassAbsentLabel = view.findViewById(R.id.txtClassAbsentLabel);

        rattingOverAllAttendance = view.findViewById(R.id.rattingOverAllAttendance);

        txtDate = view.findViewById(R.id.txtDate);
        txtWeek = view.findViewById(R.id.txtWeek);
        txtOverAllAttendance = view.findViewById(R.id.txtOverAllAttendance);
        progressAbsent = view.findViewById(R.id.progressAbsent);

        recyclerClassLayout = view.findViewById(R.id.recyclerClassLayout);
        recyclerAbsent = view.findViewById(R.id.recyclerAbsent);


        fabEditAttendance = view.findViewById(R.id.fabEditAttendance);

        calendarAttendance = view.findViewById(R.id.calendarAttendance);

        instance = Calendar.getInstance();


        calendarAttendance.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                showAbsentList(date.getDate());
            }
        });

        fabEditAttendance.setOnClickListener(this);
        linearDownload.setOnClickListener(this);
        linearShare.setOnClickListener(this);
        linearEmail.setOnClickListener(this);
        preparePage();

        return view;
    }

    public void showAbsentList(Date date) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);

        txtDate.setText(FormatDateFromDate(date, Vars.DatePattern6));
        txtWeek.setText("WEEK " + now.get(Calendar.WEEK_OF_MONTH));
        RedirectSearchAbsent(FormatDateFromDate(date, Vars.DatePattern9));
    }

    public void InitializeProgress(View view) {
        scrollContent = view.findViewById(R.id.scrollContent);
        InnerProgress = view.findViewById(R.id.ProgressInner);
        InnerProgress.setVisibility(View.VISIBLE);
        scrollContent.setVisibility(View.INVISIBLE);
        String indicator = activity.getIntent().getStringExtra("indicator");
        AVLoadingIndicatorView avi = view.findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public void loadData(Bundle bundle) {
        studAttendanceInfo = new Res_Stud_Attendance();
        if (bundle != null) {
            if (bundle.containsKey("attendance_info")) {
                StudId = bundle.getString("stud_id");
                studAttendanceInfo = (Res_Stud_Attendance) bundle.getSerializable("attendance_info");
                if (studAttendanceInfo != null) {
                    PrepareLoadData();
                    calendarAttendance.setSelectedDate(instance.getTime());
                    showAbsentList(instance.getTime());
                }
            }
        }
    }

    public void showProgress() {
        InitializeProgress(view);
    }

    private void PrepareLoadData() {
        InnerProgress.setVisibility(View.GONE);
        scrollContent.setVisibility(View.VISIBLE);
        txtLastMissedDate.setText(FormatDateForShow(studAttendanceInfo.getLast_missed_date(), Vars.DatePattern7, "-"));
        txtLastMissedTime.setText(studAttendanceInfo.getLast_missed_time().equals("") ? "-" : studAttendanceInfo.getLast_missed_time());
        txtSubjectMissed.setText(studAttendanceInfo.getSubject_missed().equals("") ? "-" : studAttendanceInfo.getSubject_missed());
        txtReason.setText(studAttendanceInfo.getReason().equals("") ? "-" : studAttendanceInfo.getReason());

        txtOverAllAttendance.setText(studAttendanceInfo.getOver_all_attendance_report());
        rattingOverAllAttendance.setRating(ConvertToFloat(studAttendanceInfo.getOver_all_attendance_ratting()));

        listPosition = PrepareSeatForStudent(staStudentInfo);

        mClassAdapter = new ClassPositionAdapter(activity, listPosition,
                "attendance_view", ConvertToInteger(staStudentInfo.getClass_details().getDivides()),
                ConvertToInteger(staStudentInfo.getClass_details().getColumn()), SAttendanceFrag.this);
        final GridLayoutManager mLayoutManagerCategory = new GridLayoutManager(activity,4);//ConvertToInteger(staStudentInfo.getClass_details().getColumn())
        recyclerClassLayout.setLayoutManager(mLayoutManagerCategory);
        recyclerClassLayout.setItemAnimator(new DefaultItemAnimator());
        recyclerClassLayout.setAdapter(mClassAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
//        if (Refresh_Stud_Attendance.equals("1")) {
//            InnerProgress.setVisibility(View.VISIBLE);
//        } else {
//            InnerProgress.setVisibility(View.GONE);
//        }
    }

    private void preparePage() {
        if (Vars.staUserInfo.getRole().equals(Vars.Role_Teacher) || Vars.staUserInfo.getRole().equals(Vars.Role_Principal)) {
            fabEditAttendance.setVisibility(View.VISIBLE);
        } else {
            fabEditAttendance.setVisibility(View.GONE);
        }
    }

    private HashSet<CalendarDay> getCalendarDaysSet(Calendar cal1, Calendar cal2) {
        HashSet<CalendarDay> setDays = new HashSet<>();
/*
        while (cal1.getTime().before(cal2.getTime())) {
            CalendarDay calDay = CalendarDay.from(cal1);
            setDays.add(calDay);
            cal1.add(Calendar.DATE, 1);
        }
*/
        CalendarDay calDay = CalendarDay.from(cal1);
        cal1.add(Calendar.DATE, 1);
        setDays.add(calDay);

        return setDays;
    }


    private HashSet<CalendarDay> stringToCalDay(String sDays){

        if(sDays == null || sDays.compareTo("") == 0){
            sDays = "1$1$2017-";
        }
        String [] days = sDays.split("-");
        HashSet<CalendarDay> setDays = new HashSet<>();

        for(String day:days){

            if(day == null || day.compareTo("") == 0){
                day = "1$1$2017";
            }
            String [] dayTemp = day.split("\\$");
            CalendarDay calDay = CalendarDay.from(Integer.parseInt(dayTemp[2]),Integer.parseInt(dayTemp[1])-1,Integer.parseInt(dayTemp[0]));

            setDays.add(calDay);
        }
        return setDays;
    }

    public void RedirectSearchAbsent(final String date) {
        txtNoLeaveLabel.setVisibility(View.GONE);
        txtClassAbsentLabel.setVisibility(View.GONE);
        final ArrayList<Res_Stud_Attendance_Details> DAttendance = studAttendanceInfo.getDetailed_attendance();
        listAbsent.clear();
        HashSet<CalendarDay> setDays = new HashSet<>();
        for (int i = 0; i < DAttendance.size(); i++) {
            if (DAttendance.get(i).getDate().equalsIgnoreCase(date)) {
                listAbsent.add(DAttendance.get(i));
            }
            String dbdate = DAttendance.get(i).getDate();
            String [] dateParts = dbdate.split("-");
            String day = dateParts[2];
            String month = dateParts[1];
            String year = dateParts[0];
            setDays.add(CalendarDay.from(Integer.parseInt(year),Integer.parseInt(month)-1, Integer.parseInt(day)));
            calendarAttendance.addDecorators(new EventDecorator(Color.BLUE, setDays ,activity ));
        }
        mAbsentAdapter = new AbsentListAdapter(activity, listAbsent);
        RecyclerView.LayoutManager mEmergencyLayoutManager = new LinearLayoutManager(activity);
        recyclerAbsent.setLayoutManager(mEmergencyLayoutManager);
        recyclerAbsent.setItemAnimator(new DefaultItemAnimator());
        recyclerAbsent.setAdapter(mAbsentAdapter);
        if (listAbsent.size() == 0) {
            txtNoLeaveLabel.setVisibility(View.VISIBLE);
        } else {
            txtClassAbsentLabel.setVisibility(View.VISIBLE);
            txtNoLeaveLabel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabEditAttendance:
                startActivity(new Intent(activity, UpdateAttendanceActivity.class)
                        .putExtra("stud_id", StudId));
                break;
            case R.id.linearDownload:
                if (studAttendanceInfo.getReport_download_link() != null && !studAttendanceInfo.getReport_download_link().isEmpty())
                    downloadFile(activity, studAttendanceInfo.getReport_download_link());
                else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearShare:
                if (studAttendanceInfo.getReport_download_link() != null && !studAttendanceInfo.getReport_download_link().isEmpty()) {
                    Support.shareLink(activity, studAttendanceInfo.getReport_download_link());
                } else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearEmail:
                sendEmail(activity, studAttendanceInfo.getReport_download_link());
                break;
        }
    }

    public void onPlaceClicked(int position) {

    }



    private class EventDecorator implements DayViewDecorator {
        private final int color;
        private final HashSet<CalendarDay> dates;
        Activity activity;

        public EventDecorator(int color, Collection<CalendarDay> dates, Activity activity) {
            this.color = color;
            this.dates = new HashSet<>(dates);
            this.activity = activity;
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }
        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
            view.addSpan(new BackgroundColorSpan(Color.YELLOW));
            //view.setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.selector));
        }
    }

}

