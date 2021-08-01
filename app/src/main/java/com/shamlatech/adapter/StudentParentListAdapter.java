package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shamlatech.activity.teacher.StudentInfoActivity;
import com.shamlatech.api_response.Res_SI_Parent_Details;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class StudentParentListAdapter extends RecyclerView.Adapter<StudentParentListAdapter.MyViewHolder> {

    private ArrayList<Res_SI_Parent_Details> listParents;
    Activity act;
    Context mContext;
    StudentInfoActivity studentInfoActivity;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtParentName, txtOccupation;
        ImageView imgImage, imgCallToParent, imgMessageToParent, imgMailToParent;


        public MyViewHolder(View view) {
            super(view);
            imgImage = view.findViewById(R.id.imgImage);
            txtParentName = view.findViewById(R.id.txtParentName);
            txtOccupation = view.findViewById(R.id.txtOccupation);
            imgCallToParent = view.findViewById(R.id.imgCallToParent);
            imgMessageToParent = view.findViewById(R.id.imgMessageToParent);
            imgMailToParent = view.findViewById(R.id.imgMailToParent);
        }
    }

    public StudentParentListAdapter(Activity act, ArrayList<Res_SI_Parent_Details> listParents, StudentInfoActivity studentInfoActivity) {
        this.act = act;
        this.listParents = listParents;
        this.studentInfoActivity = studentInfoActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_student_parents, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Integer pst =  holder.getLayoutPosition();

        final Res_SI_Parent_Details list = listParents.get(position);
        holder.txtParentName.setText(list.getFirst_name() + " " + list.getMiddle_name() + " " + list.getLast_name());
        holder.txtOccupation.setText(list.getOccupation());


        holder.imgCallToParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentInfoActivity.onClickCall(position);
            }
        });

        holder.imgMessageToParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentInfoActivity.onClickMessage(position);
            }
        });

        holder.imgMailToParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studentInfoActivity.onClickMail(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listParents.size();
    }

}









