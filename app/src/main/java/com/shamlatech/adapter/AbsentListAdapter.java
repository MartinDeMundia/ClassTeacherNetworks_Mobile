package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Stud_Attendance_Details;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class AbsentListAdapter extends RecyclerView.Adapter<AbsentListAdapter.MyViewHolder> {

    private ArrayList<Res_Stud_Attendance_Details> listAbsent;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtSubject, txtReasons;

        public MyViewHolder(View view) {
            super(view);
            txtSubject = view.findViewById(R.id.txtSubject);
            txtReasons = view.findViewById(R.id.txtReasons);
        }
    }

    public AbsentListAdapter(Activity act, ArrayList<Res_Stud_Attendance_Details> listAbsent) {
        this.act = act;
        this.listAbsent = listAbsent;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_attendance_absent, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Attendance_Details list = listAbsent.get(position);

        holder.txtSubject.setText(list.getSubject_name());
        holder.txtReasons.setText(list.getReason());

    }

    @Override
    public int getItemCount() {
        return listAbsent.size();
    }

}









