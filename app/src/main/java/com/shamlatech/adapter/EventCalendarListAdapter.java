package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shamlatech.api_response.Res_School_Events;
import com.shamlatech.fragment.SCalendarFrag;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class EventCalendarListAdapter extends RecyclerView.Adapter<EventCalendarListAdapter.MyViewHolder> {

    private ArrayList<Res_School_Events> listEvent;
    Activity act;
    Context mContext;
    SCalendarFrag frag;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearEvents;
        TextView txtEventName;
        View viewLine;

        public MyViewHolder(View view) {
            super(view);
            linearEvents = view.findViewById(R.id.linearEvents);
            txtEventName = view.findViewById(R.id.txtEventName);
            viewLine = view.findViewById(R.id.viewLine);
        }
    }

    public EventCalendarListAdapter(Activity act, ArrayList<Res_School_Events> listEvent, SCalendarFrag frag) {
        this.act = act;
        this.listEvent = listEvent;
        this.frag = frag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_event_calendar, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_School_Events list = listEvent.get(position);

        holder.txtEventName.setText(list.getTitle());
        holder.linearEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag.showEventDetails(list);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listEvent.size();
    }

}









