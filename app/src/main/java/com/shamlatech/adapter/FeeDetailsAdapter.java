package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Stud_Fees_Details_Content;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class FeeDetailsAdapter extends RecyclerView.Adapter<FeeDetailsAdapter.MyViewHolder> {

    private ArrayList<Res_Stud_Fees_Details_Content> listFee;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtFeeName, txtFeeAmount;

        public MyViewHolder(View view) {
            super(view);
            txtFeeName = view.findViewById(R.id.txtFeeName);
            txtFeeAmount = view.findViewById(R.id.txtFeeAmount);
        }
    }

    public FeeDetailsAdapter(Activity act, ArrayList<Res_Stud_Fees_Details_Content> listFee) {
        this.act = act;
        this.listFee = listFee;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_fees_details, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Fees_Details_Content list = listFee.get(position);
        holder.txtFeeName.setText(list.getName());
        holder.txtFeeAmount.setText(Html.fromHtml(Support.FormatFee(list.getAmount(), false)));
    }

    @Override
    public int getItemCount() {
        return listFee.size();
    }

}









