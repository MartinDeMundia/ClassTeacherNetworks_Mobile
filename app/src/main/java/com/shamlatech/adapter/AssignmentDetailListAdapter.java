package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shamlatech.activity.parent.AssignmentViewActivity;
import com.shamlatech.activity.teacher.EducationEditActivity;
import com.shamlatech.api_response.Res_Assignment_List;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

import static com.shamlatech.utils.Support.FormatDateForShow;
import static com.shamlatech.utils.Support.FormatDateTimeForShow;
import static com.shamlatech.utils.Vars.Assignment_Status_Cancel;
import static com.shamlatech.utils.Vars.Assignment_Status_Not_Submitted;
import static com.shamlatech.utils.Vars.Assignment_Status_Pending;
import static com.shamlatech.utils.Vars.Assignment_Status_Submitted;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class AssignmentDetailListAdapter extends RecyclerView.Adapter<AssignmentDetailListAdapter.MyViewHolder> {

    private ArrayList<Res_Assignment_List> listAssignment;
    Activity act;
    Context mContext;
    String location;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtAssignment, txtSubject, txtDate, txtStatus;
        LinearLayout linearAssignment;

        public MyViewHolder(View view) {
            super(view);
            linearAssignment = view.findViewById(R.id.linearAssignment);
            txtStatus = view.findViewById(R.id.txtStatus);
            txtAssignment = view.findViewById(R.id.txtAssignment);
            txtSubject = view.findViewById(R.id.txtSubject);
            txtDate = view.findViewById(R.id.txtDate);
        }
    }

    public AssignmentDetailListAdapter(Activity act, ArrayList<Res_Assignment_List> listAssignment, String location) {
        this.act = act;
        this.listAssignment = listAssignment;
        this.location = location;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_exam_assignment_edit_list, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Assignment_List list = listAssignment.get(position);

        holder.txtAssignment.setText(list.getName());
        holder.txtSubject.setText(list.getSubject_name());
        holder.txtDate.setText(list.getName());

        if (list.getStatus().equalsIgnoreCase(Assignment_Status_Pending)) {
            holder.txtDate.setText("Due on: " + FormatDateForShow(list.getDue_date(), Vars.DatePattern8, ""));
            holder.txtStatus.setText("Pending");
        } else if (list.getStatus().equalsIgnoreCase(Assignment_Status_Submitted)) {
            holder.txtDate.setText("Submitted on : " + FormatDateForShow(list.getSubmit_on(), Vars.DatePattern8, ""));
            holder.txtStatus.setText("Submitted");
        } else if (list.getStatus().equalsIgnoreCase(Assignment_Status_Not_Submitted)) {
            holder.txtDate.setText("Due on: " + FormatDateForShow(list.getDue_date(), Vars.DatePattern8, ""));
            holder.txtStatus.setText("Not Submitted");
        } else if (list.getStatus().equalsIgnoreCase(Assignment_Status_Cancel)) {
            holder.txtDate.setText("Due on: " + FormatDateForShow(list.getDue_date(), Vars.DatePattern8, ""));
            holder.txtStatus.setText("Cancelled");
        }

        holder.linearAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location.equals("view")) {
                    ((AssignmentViewActivity) mContext).onClickAssignment(position);
                } else {
                    ((EducationEditActivity) mContext).onClickAssignment(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listAssignment.size();
    }


}









