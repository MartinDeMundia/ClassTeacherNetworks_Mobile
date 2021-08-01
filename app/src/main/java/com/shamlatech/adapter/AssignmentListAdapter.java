package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Stud_Education_Assignment;
import com.shamlatech.pojo.PojoAssignment;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

import static com.shamlatech.utils.Vars.Assignment_Status_Cancel;
import static com.shamlatech.utils.Vars.Assignment_Status_Not_Submitted;
import static com.shamlatech.utils.Vars.Assignment_Status_Pending;
import static com.shamlatech.utils.Vars.Assignment_Status_Submitted;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class AssignmentListAdapter extends RecyclerView.Adapter<AssignmentListAdapter.MyViewHolder> {

    private ArrayList<Res_Stud_Education_Assignment> listAssignment;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtAssignmentName, txtAssignmentDate;

        public MyViewHolder(View view) {
            super(view);
            txtAssignmentName = view.findViewById(R.id.txtAssignmentName);
            txtAssignmentDate = view.findViewById(R.id.txtAssignmentDate);
        }
    }

    public AssignmentListAdapter(Activity act, ArrayList<Res_Stud_Education_Assignment> listAssignment) {
        this.act = act;
        this.listAssignment = listAssignment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_assignment_list, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Education_Assignment list = listAssignment.get(position);

        String status = "";
        if (list.getStatus().equalsIgnoreCase(Assignment_Status_Pending)) {
            status = "Pending";
        } else if (list.getStatus().equalsIgnoreCase(Assignment_Status_Submitted)) {
            status = "Submitted";
        } else if (list.getStatus().equalsIgnoreCase(Assignment_Status_Not_Submitted)) {
            status = "Not Submitted";
        } else if (list.getStatus().equalsIgnoreCase(Assignment_Status_Cancel)) {
            status = "Cancelled";
        }

        holder.txtAssignmentName.setText(list.getName() + " [" + status + "]");
        holder.txtAssignmentDate.setText(Html.fromHtml(Support.FormatDateForShow(list.getDue_date(), Vars.DatePattern8, "None")));
    }

    @Override
    public int getItemCount() {
        return listAssignment.size();
    }

}









