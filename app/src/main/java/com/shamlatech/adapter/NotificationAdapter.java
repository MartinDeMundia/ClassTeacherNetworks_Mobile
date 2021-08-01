package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.activity.NotificationActivity;
import com.shamlatech.api_response.Res_Notification;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private ArrayList<Res_Notification> listNotification;
    Activity act;
    Context mContext;
    String source;
    NotificationActivity notificationActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearNotification;
        public RelativeLayout viewBackground, viewForeground;
        TextView txtType, txtDate, txtBody , txtDelete;
        ImageView delimg;

        public MyViewHolder(View view) {
            super(view);
            linearNotification = view.findViewById(R.id.linearNotification);
            txtType = view.findViewById(R.id.txtType);
            txtDate = view.findViewById(R.id.txtDate);
            txtBody = view.findViewById(R.id.txtBody);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);

            txtDelete = view.findViewById(R.id.txtdel);
            delimg = view.findViewById(R.id.delete_icon);
        }
    }


    public NotificationAdapter(Activity act, ArrayList<Res_Notification> listNotification, NotificationActivity notificationActivity) {
        this.act = act;
        this.listNotification = listNotification;
        this.notificationActivity = notificationActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_notifications, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Res_Notification list = listNotification.get(position);

        holder.txtType.setText(list.getTitle());
        holder.txtDate.setText(Html.fromHtml(Support.FormatDateTimeForShow(list.getDatetime(), Vars.DatePattern3, "")));
        holder.txtBody.setText(list.getContent());
        holder.linearNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationActivity.onNotificationClicked(position);
            }
        });


        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationActivity.deleteNotification(list.getId());
            }
        });
        holder.delimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationActivity.deleteNotification(list.getId());
            }
        });

        if (list.getIs_read().equals("1")) {
            holder.viewForeground.setBackgroundColor(act.getResources().getColor(R.color.colorLightGray));
        } else {
            holder.viewForeground.setBackgroundColor(act.getResources().getColor(R.color.colorWhite));
        }
    }

    @Override
    public int getItemCount() {
        return listNotification.size();
    }

    public void removeItem(int position) {
        listNotification.remove(position);
        notifyItemRemoved(position);
    }
}









