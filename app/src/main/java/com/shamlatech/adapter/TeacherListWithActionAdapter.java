package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Stud_Teacher_Info;
import com.shamlatech.fragment.STeacherFrag;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class TeacherListWithActionAdapter extends RecyclerView.Adapter<TeacherListWithActionAdapter.MyViewHolder> {

    private ArrayList<Res_Stud_Teacher_Info> listTeachers;
    Activity act;
    Context mContext;
    STeacherFrag sTeacherFrag;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtTeacherName, txtSubjectName;
        ImageView imgCallToTeacher, imgMessageToTeacher, imgMailToTeacher;
        RelativeLayout relativeTeachers;


        public MyViewHolder(View view) {
            super(view);
            txtTeacherName = view.findViewById(R.id.txtTeacherName);
            txtSubjectName = view.findViewById(R.id.txtSubjectName);
            imgCallToTeacher = view.findViewById(R.id.imgCallToTeacher);
            imgMessageToTeacher = view.findViewById(R.id.imgMessageToTeacher);
            imgMailToTeacher = view.findViewById(R.id.imgMailToTeacher);
            relativeTeachers = view.findViewById(R.id.relativeTeachers);
        }
    }

    public TeacherListWithActionAdapter(Activity act, ArrayList<Res_Stud_Teacher_Info> listTeachers, STeacherFrag sTeacherFrag) {
        this.act = act;
        this.listTeachers = listTeachers;
        this.sTeacherFrag = sTeacherFrag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_contact_techers, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Teacher_Info list = listTeachers.get(position);
        holder.txtTeacherName.setText(list.getFirst_name() + " " + list.getMiddle_name() + " " + list.getLast_name());
        holder.txtSubjectName.setText(list.getSubject());

        holder.txtTeacherName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTeacherFrag.onClickTeacherName(position);
            }
        });

        holder.txtSubjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTeacherFrag.onClickSubjectName(position);
            }
        });

        holder.imgCallToTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTeacherFrag.onClickCall(position);
            }
        });

        holder.imgMessageToTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTeacherFrag.onClickMessage(position);
            }
        });

        holder.imgMailToTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sTeacherFrag.onClickMail(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTeachers.size();
    }

}









