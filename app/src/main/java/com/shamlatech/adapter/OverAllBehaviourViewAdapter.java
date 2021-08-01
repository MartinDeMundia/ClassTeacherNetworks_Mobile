package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Stud_Behaviour_Overall_Behaviour;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class OverAllBehaviourViewAdapter extends RecyclerView.Adapter<OverAllBehaviourViewAdapter.MyViewHolder> {

    private ArrayList<Res_Stud_Behaviour_Overall_Behaviour> listBehaviour;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtBehaviourTitle, txtReport;

        public MyViewHolder(View view) {
            super(view);
            txtBehaviourTitle = view.findViewById(R.id.txtBehaviourTitle);
            txtReport = view.findViewById(R.id.txtReport);
        }
    }

    public OverAllBehaviourViewAdapter(Activity act, ArrayList<Res_Stud_Behaviour_Overall_Behaviour> listBehaviour) {
        this.act = act;
        this.listBehaviour = listBehaviour;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_overall_behaviour_view_list, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Behaviour_Overall_Behaviour list = listBehaviour.get(position);

        holder.txtBehaviourTitle.setText(list.getBehaviour_title());
        holder.txtReport.setText(list.getReport());
    }

    @Override
    public int getItemCount() {
        return listBehaviour.size();
    }

}









