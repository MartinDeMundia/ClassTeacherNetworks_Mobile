package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Stud_Education_Detailed_Marks;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class ExamListAdapter extends RecyclerView.Adapter<ExamListAdapter.MyViewHolder> {

    private ArrayList<Res_Stud_Education_Detailed_Marks> listExams;
    Activity act;
    Context mContext;
    DetailMarkListAdapter mAdapter;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtExamName, txtGrade;
        RecyclerView recyclerMarks;

        public MyViewHolder(View view) {
            super(view);
            txtExamName = view.findViewById(R.id.txtExamName);
            txtGrade = view.findViewById(R.id.txtGrade);
            recyclerMarks = view.findViewById(R.id.recyclerMarks);
        }
    }

    public ExamListAdapter(Activity act, ArrayList<Res_Stud_Education_Detailed_Marks> listExams) {
        this.act = act;
        this.listExams = listExams;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_exams, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Education_Detailed_Marks list = listExams.get(position);

        holder.txtExamName.setText(list.getExam_name());
        holder.txtGrade.setText(list.getGrade());

        mAdapter = new DetailMarkListAdapter(act, list.getMarks());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
        holder.recyclerMarks.setLayoutManager(mLayoutManager);
        holder.recyclerMarks.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerMarks.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return listExams.size();
    }

}









