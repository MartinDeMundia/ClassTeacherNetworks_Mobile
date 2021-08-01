package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Stud_Education_Overall_Marks;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class OverAllMarkListAdapter extends RecyclerView.Adapter<OverAllMarkListAdapter.MyViewHolder> {

    private ArrayList<Res_Stud_Education_Overall_Marks> listMarks;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtMark, txtGrade;

        public MyViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtSubject);
            txtMark = view.findViewById(R.id.txtMark);
            txtGrade = view.findViewById(R.id.txtGrade);
        }
    }

    public OverAllMarkListAdapter(Activity act, ArrayList<Res_Stud_Education_Overall_Marks> listMarks) {
        this.act = act;
        this.listMarks = listMarks;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_mark, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Education_Overall_Marks list = listMarks.get(position);
        holder.txtName.setText(list.getExam_name());
        holder.txtMark.setText(list.getStudent_mark() + "/" + list.getTotal_mark());
        holder.txtGrade.setText(list.getGrade());

        holder.txtName.setTextSize(14f);
        holder.txtMark.setTextSize(14f);
        holder.txtGrade.setTextSize(15f);

        DisplayMetrics metrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float scaleFactor = metrics.density;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);
        if (smallestWidth > 600) {
            holder.txtName.setTextSize(17f);
            holder.txtMark.setTextSize(17f);
            holder.txtGrade.setTextSize(20f);
        }
    }

    @Override
    public int getItemCount() {
        return listMarks.size();
    }

}









