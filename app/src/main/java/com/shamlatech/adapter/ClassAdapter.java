package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Class_info;
import com.shamlatech.api_response.Res_Subjects;
import com.shamlatech.school_management.R;
import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyViewHolder> {

    private ArrayList<Res_Class_info> listStudentClasses;
    Activity act;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtStream,txtClass;
        public MyViewHolder(View view) {
            super(view);
            txtStream = view.findViewById(R.id.tv);
            txtClass = view.findViewById(R.id.txtClass);
        }
    }
    public ClassAdapter(Activity act, ArrayList<Res_Class_info> listStudentClasses) {
        this.act = act;
        this.listStudentClasses = listStudentClasses;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_student_class, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Class_info list = listStudentClasses.get(position);
        holder.txtStream.setText(list.getStream_name());
        holder.txtClass.setText(list.getClass_name());
    }
    @Override
    public int getItemCount() {
        return listStudentClasses.size();
    }

}










