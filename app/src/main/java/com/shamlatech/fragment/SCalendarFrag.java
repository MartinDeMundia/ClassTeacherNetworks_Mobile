package com.shamlatech.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.shamlatech.adapter.EventCalendarListAdapter;
import com.shamlatech.api_response.Res_School_Events;
import com.shamlatech.api_response.Result_School_Events;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Dharmalingam Sekar on 09-05-2018.
 */

public class SCalendarFrag extends Fragment {

    View view;

    CompactCalendarView calendarEvents;
    RecyclerView recyclerTeacherCalendar;
    Dialog pick_Dialog;
    EventCalendarListAdapter mAdapter;
    ArrayList<Res_School_Events> listEventData = new ArrayList<>();
    DBAdapter db;
    TextView txtMonth;
    Date selected_Date;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_stud_calendar, container, false);

        try {
            db = new DBAdapter(activity, activity.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }

        listEventData = new ArrayList<>();

        recyclerTeacherCalendar = view.findViewById(R.id.recyclerTeacherCalendar);
        txtMonth = view.findViewById(R.id.txtMonth);
        calendarEvents = view.findViewById(R.id.calendarEvents);
        calendarEvents.setUseThreeLetterAbbreviation(true);
        calendarEvents.setFirstDayOfWeek(Calendar.SUNDAY);

        selected_Date = Calendar.getInstance().getTime();

        mAdapter = new EventCalendarListAdapter(activity, listEventData, SCalendarFrag.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerTeacherCalendar.setLayoutManager(mLayoutManager);
        recyclerTeacherCalendar.setItemAnimator(new DefaultItemAnimator());
        recyclerTeacherCalendar.setAdapter(mAdapter);

        calendarEvents.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                selected_Date = dateClicked;
                PrepareEventList();

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                selected_Date = firstDayOfNewMonth;
                PrepareEventList();
            }
        });

        addEvents();
        getRetro_AccessTeacherDetails(staUserInfo.getId(), staUserInfo.getRole());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public void PrepareEventList() {
        txtMonth.setText(android.text.format.DateFormat.format("MMMM-yyyy", selected_Date));
        String date = Support.FormatDateFromDate(selected_Date, Vars.DatePattern9);
        ArrayList<Res_School_Events> listEvents = db.AccessEventList();
        listEventData.clear();
        for (int i = 0; i < listEvents.size(); i++) {
            if (listEvents.get(i).getDate().equals(date)) {
                listEventData.add(listEvents.get(i));
            }
        }
        mAdapter = new EventCalendarListAdapter(activity, listEventData, SCalendarFrag.this);
        recyclerTeacherCalendar.setAdapter(mAdapter);
    }

    public void addEvents() {
        calendarEvents.removeAllEvents();
        ArrayList<Res_School_Events> listEvents = db.AccessEventList();
        for (int i = 0; i < listEvents.size(); i++) {
            Event ev = new Event(Color.RED, Support.FormatDateFromString(listEvents.get(i).getDate(), Vars.DatePattern9).getTime());
            calendarEvents.addEvent(ev);
        }
        PrepareEventList();
    }

    public void showEventDetails(Res_School_Events list) {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(activity);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_event_details);

            TextView txtEventTitle = pick_Dialog.findViewById(R.id.txtEventTitle);
            TextView txtEventDetails = pick_Dialog.findViewById(R.id.txtEventDetails);

            txtEventTitle.setText(list.getTitle());
            txtEventDetails.setText(list.getDetails());
            pick_Dialog.show();

        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showEventDetails(list);
        } else {
            pick_Dialog = null;
            showEventDetails(list);
        }
    }

    public void getRetro_AccessTeacherDetails(String user_id, String role_id) {
        getRetro_Call(activity, service.getAccessSchoolEvents(user_id, role_id), false, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_School_Events mPojo_Event = onPojoBuilder(objectResponse, Result_School_Events.class);
                if (mPojo_Event != null) {
                    if (mPojo_Event.getStatus().equals(API_Code.Success)) {
                        db.InsertSchoolEvents(mPojo_Event.getEvents());
                        addEvents();
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

}