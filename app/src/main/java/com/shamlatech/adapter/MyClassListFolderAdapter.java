package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shamlatech.activity.teacher.MyStudentFolderActivity;
import com.shamlatech.api_response.Res_Teacher_Class;
import com.shamlatech.fragment.teacher.FilterByClassFragment;
import com.shamlatech.fragment.teacher.FilterBySubjectFragment;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by Martin Mundia Mugambi on 03-05-2020.
 */

public class MyClassListFolderAdapter extends RecyclerView.Adapter<MyClassListFolderAdapter.MyViewHolder> {

    private ArrayList<Res_Teacher_Class> listClasses;
    Activity act;
    Context mContext;
    FilterByClassFragment filterByClassFragment;
    FilterBySubjectFragment filterBySubjectFragment;
    StudentListAdapter mAdapter;
    MyStudentFolderActivity mFolderActivity;

    public MyClassListFolderAdapter(Activity act, ArrayList<Res_Teacher_Class> listClasses, MyStudentFolderActivity mFolderActivity) {
        this.act = act;
        this.listClasses = listClasses;
        this.mFolderActivity = mFolderActivity;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtClassName;
        TextView txtStudentNo;
        TextView txtStreamName;
        ConstraintLayout linearYourClass, linearClass;
        ImageView imgFolder;
        RecyclerView recyclerStudent;
        RelativeLayout relativeListBack;

        public MyViewHolder(View view) {
            super(view);
            linearClass = view.findViewById(R.id.linearClass);
            txtClassName = view.findViewById(R.id.txtClassName);
            txtStudentNo = view.findViewById(R.id.txtStudentNo);
            linearYourClass = view.findViewById(R.id.linearYourClass);
            txtStreamName =  view.findViewById(R.id.txtStreamName);
            imgFolder = view.findViewById(R.id.imageView);
            //recyclerStudent = view.findViewById(R.id.recyclerStudent);
            //relativeListBack = view.findViewById(R.id.relativeListBack);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_class_with_student_folder, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Teacher_Class list = listClasses.get(position);
       /// holder.txtClassName.setText(list.getClass_name() + list.getSection_name());
        holder.txtClassName.setText(list.getSection_name());
        holder.txtStreamName.setText(list.getClass_name());
       ////// holder.linearClass.setBackground(act.getResources().getDrawable(R.drawable.card_look_1));
        if (list.getIs_my_class().equals("1")) {
           // holder.linearClass.setBackground(act.getResources().getDrawable(R.drawable.card_look_1));
            ////holder.relativeListBack.setBackground(null);
        } else {
            //holder.linearClass.setBackground(null);
            ////holder.relativeListBack.setBackground(act.getResources().getDrawable(R.drawable.card_look));
        }
        holder.txtStudentNo.setText("Students " + list.getStudent().size());
        holder.txtStudentNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFolderActivity.onClickShowClass(position);
            }
        });
        holder.txtClassName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFolderActivity.onClickShowClass(position);
            }
        });
        holder.imgFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFolderActivity.onClickShowClass(position);
            }
        });
        //mAdapter = new StudentListAdapter(act, list.getStudent());
       // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
        //holder.recyclerStudent.setLayoutManager(mLayoutManager);
        //holder.recyclerStudent.setItemAnimator(new DefaultItemAnimator());
        //holder.recyclerStudent.addItemDecoration(new SimpleDividerItemDecoration(act, R.drawable.line_divider_1));
        //holder.recyclerStudent.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return listClasses.size();
    }

}









