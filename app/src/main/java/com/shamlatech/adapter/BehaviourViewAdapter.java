package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;
import com.shamlatech.api_response.Res_Stud_Behaviour_Content;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Martin Mundia on 23-10-2019.
 */

public class BehaviourViewAdapter extends RecyclerView.Adapter<BehaviourViewAdapter.MyViewHolder> {

    private ArrayList<Res_Stud_Behaviour_Content> listBehaviour;
    Activity act;
    Context mContext;
    private static final String[] deviantValues = new String[] {"21","22","25","26","27","28","24"};
    String behaviour_selected;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearBehaviours;
        TextView txtBehaviour, txtAction, txtYesNo;


        public MyViewHolder(View view) {
            super(view);
            linearBehaviours = view.findViewById(R.id.linearBehaviours);
            txtBehaviour = view.findViewById(R.id.txtBehaviour);
            txtAction = view.findViewById(R.id.txtAction);
            txtYesNo = view.findViewById(R.id.txtYesNo);
        }
    }

    public BehaviourViewAdapter(Activity act, ArrayList<Res_Stud_Behaviour_Content> listBehaviour, String id) {
        this.act = act;
        this.listBehaviour = listBehaviour;
        behaviour_selected = id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_behaviour_view_list, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Behaviour_Content list = listBehaviour.get(position);

        holder.txtBehaviour.setText(list.getContent_name());
        holder.txtAction.setText(list.getAction());
        holder.txtYesNo.setText(list.getReport());

        if ( ArrayUtils.contains( deviantValues  , behaviour_selected) ) {

            if (list.getReport().equalsIgnoreCase("No")) {
                holder.linearBehaviours.setBackground(null);
                holder.txtBehaviour.setTextColor(act.getResources().getColor(R.color.colorDarkGray));
                holder.txtYesNo.setTextColor(act.getResources().getColor(R.color.colorLightBlack));
                holder.txtAction.setVisibility(View.GONE);
            } else {
                holder.linearBehaviours.setBackgroundColor(act.getResources().getColor(R.color.colorTransRed));
                holder.txtBehaviour.setTextColor(act.getResources().getColor(R.color.colorRed));
                holder.txtYesNo.setTextColor(act.getResources().getColor(R.color.colorRed));
                holder.txtAction.setTextColor(act.getResources().getColor(R.color.colorRed));
                holder.txtAction.setVisibility(View.VISIBLE);
            }


        }else{


            if (list.getReport().equalsIgnoreCase("Yes")) {
                holder.linearBehaviours.setBackground(null);
                holder.txtBehaviour.setTextColor(act.getResources().getColor(R.color.colorDarkGray));
                holder.txtYesNo.setTextColor(act.getResources().getColor(R.color.colorLightBlack));
                holder.txtAction.setVisibility(View.GONE);
            } else {
                holder.linearBehaviours.setBackgroundColor(act.getResources().getColor(R.color.colorTransRed));
                holder.txtBehaviour.setTextColor(act.getResources().getColor(R.color.colorRed));
                holder.txtYesNo.setTextColor(act.getResources().getColor(R.color.colorRed));
                holder.txtAction.setTextColor(act.getResources().getColor(R.color.colorRed));
                holder.txtAction.setVisibility(View.VISIBLE);
            }


        }





    }

    @Override
    public int getItemCount() {
        return listBehaviour.size();
    }

}









