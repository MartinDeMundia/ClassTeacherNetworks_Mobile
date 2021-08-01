package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.shamlatech.activity.AnnouncementListActivity;
import com.shamlatech.activity.parent.ParentsDashboard;
import com.shamlatech.activity.teacher.TeachersDashboard;
import com.shamlatech.api_response.Res_Announcement;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.MyViewHolder> {

    Activity act;
    Context mContext;
    String source = "";
    ParentsDashboard parentsDashboard = null;
    TeachersDashboard teachersDashboard = null;

    private ArrayList<Res_Announcement> listAnnouncement;

    public AnnouncementAdapter(Activity act, ArrayList<Res_Announcement> listAnnouncement, ParentsDashboard parentsDashboard) {
        this.act = act;
        this.listAnnouncement = listAnnouncement;
        this.parentsDashboard = parentsDashboard;
    }

    public AnnouncementAdapter(Activity act, ArrayList<Res_Announcement> listAnnouncement, String source) {
        this.act = act;
        this.listAnnouncement = listAnnouncement;
        this.source = source;
    }

    public AnnouncementAdapter(Activity act, ArrayList<Res_Announcement> listAnnouncement, TeachersDashboard teachersDashboard) {
        this.act = act;
        this.listAnnouncement = listAnnouncement;
        this.teachersDashboard = teachersDashboard;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_announcements, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Announcement list = listAnnouncement.get(position);

        LayoutInflater layoutInflater = act.getLayoutInflater();
        holder.txtType.setText(list.getType());
        holder.txtDate.setText(Html.fromHtml(Support.FormatDateTimeForShow(list.getDate(), Vars.DatePattern7, "")));
        holder.txtHeader.setText(list.getTitle());
        holder.txtBody.setText(list.getBody());
        holder.linearAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (source.equals("Announcement"))
                    ((AnnouncementListActivity) act).onAnnouncementClick(position);
                if (parentsDashboard != null)
                    parentsDashboard.onAnnouncementClick(position);
                if (teachersDashboard != null)
                    teachersDashboard.onAnnouncementClick(position);
            }
        });
        View view;
        String[] tags = list.getTags().split(",");
        holder.flexboxTags.removeAllViews();
        for (int i = 0; i < tags.length; i++) {
            if (!tags[i].trim().equals("")) {
                view = layoutInflater.inflate(R.layout.announcement_tag, holder.flexboxTags, false);

                RelativeLayout relativeTag = view.findViewById(R.id.relativeTag);
                TextView textView = (TextView) view.findViewById(R.id.txtSubject);
                textView.setText(tags[i].trim());
                holder.flexboxTags.addView(relativeTag);
            }
        }

    }

    @Override
    public int getItemCount() {
        return listAnnouncement.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearAnnouncement;
        TextView txtType, txtDate, txtHeader, txtBody;
        FlexboxLayout flexboxTags;

        public MyViewHolder(View view) {
            super(view);
            linearAnnouncement = view.findViewById(R.id.linearAnnouncement);
            txtType = view.findViewById(R.id.txtType);
            txtDate = view.findViewById(R.id.txtDate);
            txtHeader = view.findViewById(R.id.txtHeader);
            txtBody = view.findViewById(R.id.txtBody);
            flexboxTags = view.findViewById(R.id.flexboxTags);
        }
    }

}









