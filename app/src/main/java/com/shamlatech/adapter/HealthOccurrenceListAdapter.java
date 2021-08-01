package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Stud_Health_Last_Health_Occurrence;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

import static com.shamlatech.utils.Support.FormatDateTimeForShow;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class HealthOccurrenceListAdapter extends RecyclerView.Adapter<HealthOccurrenceListAdapter.MyViewHolder> {

    private ArrayList<Res_Stud_Health_Last_Health_Occurrence> listHealthOccurrence;
    Activity act;
    Context mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout linearHealthOccurrence;
        TextView txtName, txtDate, txtAction;

        public MyViewHolder(View view) {
            super(view);
            linearHealthOccurrence = view.findViewById(R.id.relativeBehaviours);
            txtName = view.findViewById(R.id.txtSubject);
            txtDate = view.findViewById(R.id.txtDate);
            txtAction = view.findViewById(R.id.txtAction);
        }
    }

    public HealthOccurrenceListAdapter(Activity act, ArrayList<Res_Stud_Health_Last_Health_Occurrence> listHealthOccurrence) {
        this.act = act;
        this.listHealthOccurrence = listHealthOccurrence;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_health_occurance, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Stud_Health_Last_Health_Occurrence list = listHealthOccurrence.get(position);

        holder.txtName.setText(list.getName());
        holder.txtDate.setText(FormatDateTimeForShow(list.getUpdated_date(), Vars.DatePattern7, ""));
        holder.txtAction.setText(list.getAction());

    }

    @Override
    public int getItemCount() {
        return listHealthOccurrence.size();
    }

}









