package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamlatech.activity.teacher.ClassLayoutAddEditActivity;
import com.shamlatech.api_response.Res_Teacher_ClassStudent;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class StudentListPlaceAdapter extends RecyclerView.Adapter<StudentListPlaceAdapter.MyViewHolder> {

    private ArrayList<Res_Teacher_ClassStudent> listStudent;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtList, txtNo , txtAdmNo;

        public MyViewHolder(View view) {
            super(view);
            txtList = view.findViewById(R.id.txtComment);
            txtNo = view.findViewById(R.id.txtSubject);
            txtAdmNo = view.findViewById(R.id.txtItem);
        }
    }

    public StudentListPlaceAdapter(Activity act, ArrayList<Res_Teacher_ClassStudent> listStudent) {
        this.act = act;
        this.listStudent = listStudent;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_singleline_show_admno, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Teacher_ClassStudent list = listStudent.get(position);
        holder.txtList.setText(list.getFirst_name() + " " + list.getMiddle_name() + " " + list.getLast_name());
        holder.txtNo.setText(String.valueOf( position + 1));
        holder.txtNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClassLayoutAddEditActivity) act).onClickStudent(list);
            }
        });
        holder.txtAdmNo.setText(list.getId());
        holder.txtAdmNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClassLayoutAddEditActivity) act).onClickStudent(list);
            }
        });

        holder.txtList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClassLayoutAddEditActivity) act).onClickStudent(list);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listStudent.size();
    }

}









