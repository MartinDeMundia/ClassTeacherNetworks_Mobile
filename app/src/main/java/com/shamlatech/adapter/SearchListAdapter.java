package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shamlatech.activity.SearchActivity;
import com.shamlatech.pojo.PojoStudentWithClassList;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> {

    private ArrayList<PojoStudentWithClassList> listStudent;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeSearch;
        TextView txtStudentName, txtClassName;

        public MyViewHolder(View view) {
            super(view);
            relativeSearch = view.findViewById(R.id.relativeSearch);
            txtStudentName = view.findViewById(R.id.txtStudentName);
            txtClassName = view.findViewById(R.id.txtClassName);
        }
    }

    public SearchListAdapter(Activity act, ArrayList<PojoStudentWithClassList> listStudent) {
        this.act = act;
        this.listStudent = listStudent;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_search_student, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PojoStudentWithClassList list = listStudent.get(position);

        holder.txtStudentName.setText(list.getName());
        holder.txtClassName.setText(list.getClass_name() + list.getSection_name());

        holder.relativeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchActivity) act).onSearchClick(list.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listStudent.size();
    }

}









