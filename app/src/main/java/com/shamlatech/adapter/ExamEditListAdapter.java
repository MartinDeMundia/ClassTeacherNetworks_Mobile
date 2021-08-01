package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamlatech.activity.teacher.EducationEditActivity;
import com.shamlatech.api_response.Res_Look_ExamList;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class ExamEditListAdapter extends RecyclerView.Adapter<ExamEditListAdapter.MyViewHolder> {

    private ArrayList<Res_Look_ExamList> listExams;
    Activity act;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtExamName, txtEdit;

        public MyViewHolder(View view) {
            super(view);
            txtExamName = view.findViewById(R.id.txtExamName);
            txtEdit = view.findViewById(R.id.txtEdit);
        }
    }

    public ExamEditListAdapter(Activity act, ArrayList<Res_Look_ExamList> listExams) {
        this.act = act;
        this.listExams = listExams;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_exams_edit, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Look_ExamList list = listExams.get(position);

        holder.txtExamName.setText(list.getExam_name());
        holder.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EducationEditActivity) act).onClickEditExam(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listExams.size();
    }

}









