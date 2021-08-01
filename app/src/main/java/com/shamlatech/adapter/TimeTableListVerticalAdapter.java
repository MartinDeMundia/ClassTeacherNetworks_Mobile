package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Teacher_TimeTable;
import com.shamlatech.school_management.R;

import java.time.Period;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Martin Mundia Mugambi on 04-12-2019.
 */

public class TimeTableListVerticalAdapter extends RecyclerView.Adapter<TimeTableListVerticalAdapter.MyViewHolder> {

    private ArrayList<Res_Teacher_TimeTable> listTimetable;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearHeader, linearInner, linearBreak;
        TextView txtInnerTime, txtBreakTime, txtBreakName;
        TextView txtInnerMon, txtInnerTue, txtInnerWed, txtInnerThu, txtInnerFri;
        LinearLayout linearInnerMon, linearInnerTue, linearInnerWed, linearInnerThu, linearInnerFri;

        public MyViewHolder(View view) {
            super(view);
            linearHeader = view.findViewById(R.id.linearHeader);
            linearInner = view.findViewById(R.id.linearInner);
            linearBreak = view.findViewById(R.id.linearBreak);

            txtInnerTime = view.findViewById(R.id.txtInnerTime);
            txtBreakTime = view.findViewById(R.id.txtBreakTime);
            txtBreakName = view.findViewById(R.id.txtBreakName);

            txtInnerMon = view.findViewById(R.id.txtInnerMon);
            txtInnerTue = view.findViewById(R.id.txtInnerTue);
            txtInnerWed = view.findViewById(R.id.txtInnerWed);
            txtInnerThu = view.findViewById(R.id.txtInnerThu);
            txtInnerFri = view.findViewById(R.id.txtInnerFri);

            linearInnerMon = view.findViewById(R.id.linearInnerMon);
            linearInnerTue = view.findViewById(R.id.linearInnerTue);
            linearInnerWed = view.findViewById(R.id.linearInnerWed);
            linearInnerThu = view.findViewById(R.id.linearInnerThu);
            linearInnerFri = view.findViewById(R.id.linearInnerFri);
        }
    }

    public TimeTableListVerticalAdapter(Activity act, ArrayList<Res_Teacher_TimeTable> listTimetable) {
        this.act = act;
        this.listTimetable = listTimetable;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_timetable_vertical, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Teacher_TimeTable list = listTimetable.get(position);
        if (list.getType().equals("header")) {
            holder.linearHeader.setVisibility(View.VISIBLE);
            holder.linearInner.setVisibility(View.GONE);
            holder.linearBreak.setVisibility(View.GONE);
        } else if (list.getType().equals("class")) {
            holder.linearHeader.setVisibility(View.GONE);
            holder.linearInner.setVisibility(View.VISIBLE);
            holder.linearBreak.setVisibility(View.GONE);


            holder.txtInnerMon.setText(splitSubject(list.getMon()));
            holder.txtInnerTue.setText(splitSubject(list.getTue()));
            holder.txtInnerWed.setText(splitSubject(list.getWed()));
            holder.txtInnerThu.setText(splitSubject(list.getThu()));
            holder.txtInnerFri.setText(splitSubject(list.getFri()));
            holder.txtInnerTime.setText(list.getTime());

            SetBackground(holder.linearInnerMon, list.getMon());
            SetBackground(holder.linearInnerTue, list.getTue());
            SetBackground(holder.linearInnerWed, list.getWed());
            SetBackground(holder.linearInnerThu, list.getThu());
            SetBackground(holder.linearInnerFri, list.getFri());

        } else if (list.getType().equals("break")) {
            holder.linearHeader.setVisibility(View.GONE);
            holder.linearInner.setVisibility(View.GONE);
            holder.linearBreak.setVisibility(View.VISIBLE);
            holder.txtBreakName.setText(list.getBreak_name());
            holder.txtBreakTime.setText(list.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return listTimetable.size();
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public void SetBackground(LinearLayout linear, String Period) {
        if (Period.equals("N/A")) {
            linear.setBackground(act.getResources().getDrawable(R.drawable.back_timetable_empty));
        } else {
            String[] strSplitData = Period.split("_");
            if (strSplitData.length > 1) {
                if (strSplitData[1].equals("M")) {
                    linear.setBackground(act.getResources().getDrawable(R.drawable.back_combain_timetable_fill));
                } else {
                    //linear.setBackground(act.getResources().getDrawable(R.drawable.bgo));
                    String lessonColor = getRandomNumberString() ;
                    linear.setBackgroundColor(Color.parseColor("#"+lessonColor));
                }
            } else {
               // linear.setBackground(act.getResources().getDrawable(R.drawable.bgo));
                linear.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

        }
    }

    public String splitSubject(String period) {
        String result = "";
        String[] strSplitData = period.split("_");
        if (strSplitData.length > 1) {
            result = strSplitData[0];
        }
        return result;
    }
}









