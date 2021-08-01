package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.shamlatech.activity.teacher.GroupMakerAddEditActivity;
import com.shamlatech.pojo.PojoStudentKeyPare;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class StudentSelectionAdapter extends RecyclerView.Adapter<StudentSelectionAdapter.MyViewHolder> {

    private ArrayList<PojoStudentKeyPare> listStudent;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox cbkList;

        public MyViewHolder(View view) {
            super(view);
            cbkList = view.findViewById(R.id.cbkList);
        }
    }

    public StudentSelectionAdapter(Activity act, ArrayList<PojoStudentKeyPare> listStudent) {
        this.act = act;
        this.listStudent = listStudent;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_checkbox_selection, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PojoStudentKeyPare list = listStudent.get(position);

        holder.cbkList.setText(list.getName());
        holder.cbkList.setChecked(list.isSelected());
        holder.cbkList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = !list.isSelected();
                ((GroupMakerAddEditActivity) act).onStudentSelection(position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listStudent.size();
    }

}









