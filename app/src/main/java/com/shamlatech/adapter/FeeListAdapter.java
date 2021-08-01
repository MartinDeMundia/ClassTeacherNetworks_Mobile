package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Stud_Fees_Details;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class FeeListAdapter extends RecyclerView.Adapter<FeeListAdapter.MyViewHolder> {

    private ArrayList<Res_Stud_Fees_Details> listFees;
    Activity act;
    Context mContext;
    FeeDetailsAdapter mAdapter;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtFeeHeader, txtFeeTotal;
        RecyclerView recyclerFees;

        public MyViewHolder(View view) {
            super(view);
            txtFeeHeader = view.findViewById(R.id.txtFeeHeader);
            txtFeeTotal = view.findViewById(R.id.txtFeeTotal);
            recyclerFees = view.findViewById(R.id.recyclerFees);
        }
    }

    public FeeListAdapter(Activity act, ArrayList<Res_Stud_Fees_Details> listFees) {
        this.act = act;
        this.listFees = listFees;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_fees, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Fees_Details list = listFees.get(position);

        holder.txtFeeHeader.setText(list.getTitle());
        holder.txtFeeTotal.setText(Html.fromHtml(Support.FormatFee(list.getAmount(), true)));

        mAdapter = new FeeDetailsAdapter(act, list.getContent());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
        holder.recyclerFees.setLayoutManager(mLayoutManager);
        holder.recyclerFees.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerFees.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return listFees.size();
    }

}









