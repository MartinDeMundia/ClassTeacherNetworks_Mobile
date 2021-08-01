package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shamlatech.activity.parent.AddHealthReport;
import com.shamlatech.api_response.Res_Stud_Education_Assignment;
import com.shamlatech.api_response.Res_Stud_Health_Known_Allergies;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class AllergiesUpdateListAdapter extends RecyclerView.Adapter<AllergiesUpdateListAdapter.MyViewHolder> {

    private ArrayList<Res_Stud_Health_Known_Allergies> listAllergies;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtAllergy, txtDetails;
        ImageView imgRemove;

        public MyViewHolder(View view) {
            super(view);
            txtAllergy = view.findViewById(R.id.txtAllergy);
            txtDetails = view.findViewById(R.id.txtDetails);
            imgRemove = view.findViewById(R.id.imgRemove);
        }
    }

    public AllergiesUpdateListAdapter(Activity act, ArrayList<Res_Stud_Health_Known_Allergies> listAllergies) {
        this.act = act;
        this.listAllergies = listAllergies;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_allergies, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Health_Known_Allergies list = listAllergies.get(position);

        holder.txtAllergy.setText(list.getName());
        holder.txtDetails.setText(list.getDetails());

        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AddHealthReport) mContext).onClickRemove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAllergies.size();
    }

}









