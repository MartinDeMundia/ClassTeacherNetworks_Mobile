package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shamlatech.activity.teacher.ClassLayoutAddEditActivity;
import com.shamlatech.fragment.SAttendanceFrag;
import com.shamlatech.fragment.teacher.MyClassesFragment;
import com.shamlatech.pojo.PojoClassPosition;
import com.shamlatech.school_management.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class ClassPositionAdapter extends RecyclerView.Adapter<ClassPositionAdapter.MyViewHolder> {

    private ArrayList<PojoClassPosition> listPosition;
    Activity act;
    String type;
    int divider;
    int column;
    Context mContext;
    MyClassesFragment myClassesFragment;
    SAttendanceFrag sAttendanceFrag;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLeft, relativeRight;
        LinearLayout linearPosition;
        TextView txtStudentName;
        ImageView imgClassPosition;
        AVLoadingIndicatorView aviSelected;

        public MyViewHolder(View view) {
            super(view);
            relativeLeft = view.findViewById(R.id.relativeLeft);
            relativeRight = view.findViewById(R.id.relativeRight);
            linearPosition = view.findViewById(R.id.linearPosition);
            txtStudentName = view.findViewById(R.id.txtStudentName);
            imgClassPosition = view.findViewById(R.id.imgClassPosition);
            aviSelected = view.findViewById(R.id.aviSelected);
        }
    }

    public ClassPositionAdapter(Activity act, ArrayList<PojoClassPosition> listPosition, String type, int divider, int column) {
        this.act = act;
        this.listPosition = listPosition;
        this.type = type;
        this.divider = divider;
        this.column = column;
    }

    public ClassPositionAdapter(Activity act, ArrayList<PojoClassPosition> listPosition, String type, int divider, int column, MyClassesFragment myClassesFragment) {
        this.act = act;
        this.listPosition = listPosition;
        this.type = type;
        this.divider = divider;
        this.column = column;
        this.myClassesFragment = myClassesFragment;
    }

    public ClassPositionAdapter(Activity act, ArrayList<PojoClassPosition> listPosition, String type, int divider, int column, SAttendanceFrag sAttendanceFrag) {
        this.act = act;
        this.listPosition = listPosition;
        this.type = type;
        this.divider = divider;
        this.column = column;
        this.sAttendanceFrag = sAttendanceFrag;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_class_layout, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PojoClassPosition list = listPosition.get(position);

        holder.relativeLeft.setVisibility(View.GONE);
        holder.relativeRight.setVisibility(View.GONE);
        holder.aviSelected.setVisibility(View.GONE);
        if(column <= 0) {
            column = 4;
            divider = 2;
        }
        int place = position % column;
        if ((place + 1) % divider == 1) {
            holder.relativeLeft.setVisibility(View.VISIBLE);
        } else if ((place + 1) % divider == 0) {
            holder.relativeRight.setVisibility(View.VISIBLE);
        }

        if (type.equals("view")) {
            if (list.getStatus().equals("placed")) {
                holder.txtStudentName.setText(list.getName());
                holder.imgClassPosition.setBackground(act.getResources().getDrawable(R.drawable.class_layout_view_placed));
                holder.imgClassPosition.setImageResource(android.R.color.transparent);
                holder.linearPosition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myClassesFragment.onPlaceClicked(position);
                    }
                });
            } else {
                holder.txtStudentName.setText("");
                holder.imgClassPosition.setBackground(act.getResources().getDrawable(R.drawable.class_layout_view_empty));
                holder.imgClassPosition.setImageResource(android.R.color.transparent);
            }
        } else if (type.equals("attendance_view")) {
            if (list.getStatus().equals("placed")) {
                holder.txtStudentName.setText(list.getName());
                holder.txtStudentName.setTextColor(act.getResources().getColor(R.color.colorCommonRed));
                holder.imgClassPosition.setBackground(act.getResources().getDrawable(R.drawable.class_layout_view_placed_red));
                holder.imgClassPosition.setImageResource(android.R.color.transparent);
                holder.linearPosition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sAttendanceFrag.onPlaceClicked(position);
                    }
                });
            } else {
                holder.txtStudentName.setText("");
                holder.imgClassPosition.setBackground(act.getResources().getDrawable(R.drawable.class_layout_view_empty));
                holder.imgClassPosition.setImageResource(android.R.color.transparent);
            }
        } else {
            if (list.getStatus().equals("placed")) {
                holder.txtStudentName.setText(list.getName());
                holder.imgClassPosition.setBackground(act.getResources().getDrawable(R.drawable.class_layout_update_placed));
                holder.imgClassPosition.setImageResource(android.R.color.transparent);
                holder.linearPosition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ClassLayoutAddEditActivity) mContext).onPlaceClicked(position);
                    }
                });
                if (list.isSelection()) {
                    holder.aviSelected.setVisibility(View.VISIBLE);
                } else {
                    holder.aviSelected.setVisibility(View.GONE);
                }
            } else {
                holder.txtStudentName.setText("");
                holder.imgClassPosition.setBackground(act.getResources().getDrawable(R.drawable.class_layout_update_empty));
                holder.imgClassPosition.setImageResource(R.drawable.ic_add_16);
                holder.linearPosition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ClassLayoutAddEditActivity) mContext).onPlaceClicked(position);
                    }
                });
                if (list.isSelection()) {
                    holder.aviSelected.setVisibility(View.VISIBLE);
                } else {
                    holder.aviSelected.setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return listPosition.size();
    }


}









