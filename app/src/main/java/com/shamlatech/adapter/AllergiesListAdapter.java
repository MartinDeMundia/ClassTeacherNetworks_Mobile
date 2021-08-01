package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Stud_Health_Known_Allergies;
import com.shamlatech.fragment.SHealthFrag;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class AllergiesListAdapter extends RecyclerView.Adapter<AllergiesListAdapter.MyViewHolder> {

    private ArrayList<Res_Stud_Health_Known_Allergies> listAllergies;
    Activity act;
    Context mContext;
    SHealthFrag frag;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtList;

        public MyViewHolder(View view) {
            super(view);
            txtList = view.findViewById(R.id.txtComment);
        }
    }

    public AllergiesListAdapter(Activity act, ArrayList<Res_Stud_Health_Known_Allergies> listAllergies, SHealthFrag frag) {
        this.act = act;
        this.listAllergies = listAllergies;
        this.frag = frag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_singleline_show, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Health_Known_Allergies list = listAllergies.get(position);

        holder.txtList.setText(list.getName());

        holder.txtList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag.showAllergyDetails(list);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listAllergies.size();
    }

}









