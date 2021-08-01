package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Stud_Education_Parts_Marks;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class DetailPartMarkListAdapter extends RecyclerView.Adapter<DetailPartMarkListAdapter.MyViewHolder> {

    Activity act;
    Context mContext;
    private ArrayList<Res_Stud_Education_Parts_Marks> listPartMarks;


    public DetailPartMarkListAdapter(Activity act, ArrayList<Res_Stud_Education_Parts_Marks> listPartMarks) {
        this.act = act;
        this.listPartMarks = listPartMarks;
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
        final Res_Stud_Education_Parts_Marks list = listPartMarks.get(position);
        holder.txtName.setText(list.getName());
        holder.txtMark.setText(list.getStudent_mark() + "/" + list.getTotal_mark());

        holder.txtName.setTextSize(12f);
        holder.txtMark.setTextSize(12f);
        holder.txtGrade.setTextSize(13f);


        DisplayMetrics metrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float scaleFactor = metrics.density;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);
        if (smallestWidth > 600) {
            holder.txtName.setTextSize(16f);
            holder.txtMark.setTextSize(16f);
            holder.txtGrade.setTextSize(19f);
        }

        holder.txtGrade.setVisibility(View.GONE);
        holder.txtParts.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return listPartMarks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtMark, txtGrade, txtParts;

        public MyViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtSubject);
            txtMark = view.findViewById(R.id.txtMark);
            txtGrade = view.findViewById(R.id.txtGrade);
            txtParts = view.findViewById(R.id.txtParts);
        }
    }

}









