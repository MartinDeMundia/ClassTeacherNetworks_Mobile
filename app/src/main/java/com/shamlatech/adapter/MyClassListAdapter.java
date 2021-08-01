package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Martin Mundia Mugambi on 24-02-2020.
 */

public class MyClassListAdapter extends RecyclerView.Adapter<MyClassListAdapter.MyViewHolder> {

    private ArrayList<Res_Teacher_Class> listClasses;
    Activity act;
    Context mContext;
    FilterByClassFragment filterByClassFragment;
    FilterBySubjectFragment filterBySubjectFragment;
    StudentListAdapter mAdapter;
    MyStudentFolderActivity mFolderActivity;

    public MyClassListAdapter(Activity act, ArrayList<Res_Teacher_Class> listClasses, FilterByClassFragment filterByClassFragment) {
        this.act = act;
        this.listClasses = listClasses;
        this.filterByClassFragment = filterByClassFragment;
    }

    public MyClassListAdapter(Activity act, ArrayList<Res_Teacher_Class> listClasses, FilterBySubjectFragment filterBySubjectFragment) {
        this.act = act;
        this.listClasses = listClasses;
        this.filterBySubjectFragment = filterBySubjectFragment;
    }
    public void showStudentInfo(String id) {
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtClassName;
        LinearLayout linearYourClass, linearClass;
        RecyclerView recyclerStudent;
        RelativeLayout relativeListBack;

        public MyViewHolder(View view) {
            super(view);
            linearClass = view.findViewById(R.id.linearClass);
            txtClassName = view.findViewById(R.id.txtClassName);
            linearYourClass = view.findViewById(R.id.linearYourClass);
            recyclerStudent = view.findViewById(R.id.recyclerStudent);
            relativeListBack = view.findViewById(R.id.relativeListBack);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_class_with_student, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Teacher_Class list = listClasses.get(position);

        holder.txtClassName.setText(list.getClass_name() + list.getSection_name());
        if (list.getIs_my_class().equals("1")) {
            holder.linearYourClass.setVisibility(View.VISIBLE);
            holder.linearClass.setBackground(act.getResources().getDrawable(R.drawable.card_look_1));
            holder.relativeListBack.setBackground(null);
        } else {
            holder.linearYourClass.setVisibility(View.GONE);
            holder.linearClass.setBackground(null);
            holder.relativeListBack.setBackground(act.getResources().getDrawable(R.drawable.card_look));
        }

        mAdapter = new StudentListAdapter(act, list.getStudent());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
        holder.recyclerStudent.setLayoutManager(mLayoutManager);
        holder.recyclerStudent.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerStudent.addItemDecoration(new SimpleDividerItemDecoration(act, R.drawable.line_divider_1));
        holder.recyclerStudent.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return listClasses.size();
    }

}









