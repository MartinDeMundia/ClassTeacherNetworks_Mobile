package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.shamlatech.api_response.Res_Subject_Report;
import com.shamlatech.school_management.R;
import java.util.ArrayList;
/**
 * Created by Martin Mundia Mugambi on 12-11-2019.
 */
public class SubjectReportAdapter extends RecyclerView.Adapter<SubjectReportAdapter.MyViewHolder> {
    private ArrayList<Res_Subject_Report> listSubjectReport;
    Activity act;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubject, txtItem, txtComment ,txtTeacher ;
        public MyViewHolder(View view) {
            super(view);
            txtSubject = view.findViewById(R.id.txtSubject);
            txtItem = view.findViewById(R.id.txtItem);
            txtComment = view.findViewById(R.id.txtComment);
            txtTeacher = view.findViewById(R.id.txtTeacher);
        }
    }
    public SubjectReportAdapter(Activity act, ArrayList<Res_Subject_Report> listSubjectReport) {
        this.act = act;
        this.listSubjectReport = listSubjectReport;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_subjectreport, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Subject_Report list = listSubjectReport.get(position);
        holder.txtSubject.setText(list.getSubject());
        holder.txtItem.setText(list.getItem());
        holder.txtComment.setText(list.getComment());
        holder.txtTeacher.setText(list.getTeacher());

        holder.txtSubject.setTextSize(12f);
        holder.txtItem.setTextSize(11f);
        holder.txtComment.setTextSize(11f);
        holder.txtTeacher.setTextSize(11f);

        DisplayMetrics metrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float scaleFactor = metrics.density;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);
        if (smallestWidth > 600) {
            holder.txtSubject.setTextSize(17f);
            holder.txtItem.setTextSize(17f);
            holder.txtComment.setTextSize(20f);
            holder.txtTeacher.setTextSize(17f);
        }
    }

    @Override
    public int getItemCount() {
        return listSubjectReport.size();
    }

}










