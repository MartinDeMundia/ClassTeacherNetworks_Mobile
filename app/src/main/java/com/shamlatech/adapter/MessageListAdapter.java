package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shamlatech.activity.MessageListActivity;
import com.shamlatech.pojo.PojoMessageList;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {

    private ArrayList<PojoMessageList> listMessage;
    Activity act;
    Context mContext;
    MessageListActivity messageListActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeMessage;
        ImageView imgMessageUser, imgMyMessageReadStatus;
        TextView txtName, txtDate, txtMessage, txtUnreadCount;

        public MyViewHolder(View view) {
            super(view);
            relativeMessage = view.findViewById(R.id.relativeMessage);
            imgMessageUser = view.findViewById(R.id.imgMessageUser);
            txtName = view.findViewById(R.id.txtSubject);
            txtDate = view.findViewById(R.id.txtDate);
            txtMessage = view.findViewById(R.id.txtMessage);
            imgMyMessageReadStatus = view.findViewById(R.id.imgMyMessageReadStatus);
            txtUnreadCount = view.findViewById(R.id.txtUnreadCount);
        }
    }

    public MessageListAdapter(Activity act, ArrayList<PojoMessageList> listMessage, MessageListActivity messageListActivity) {
        this.act = act;
        this.listMessage = listMessage;
        this.messageListActivity = messageListActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_message_list, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PojoMessageList list = listMessage.get(position);

        holder.txtName.setText(list.getFriend_name());
        holder.txtDate.setText(Html.fromHtml(Support.Show_Format_Date_For_Message_List(list.getLast_message_on())));
        holder.txtMessage.setText(list.getLast_message());
        holder.txtUnreadCount.setText(list.unread_count);
        ShowImage(holder.imgMessageUser, list.getProfile_pic());

        if (list.unread_count.equals("0")) {
            holder.txtUnreadCount.setVisibility(View.GONE);
        } else {
            holder.txtUnreadCount.setVisibility(View.VISIBLE);
        }

        if (list.getLast_message_status().equals(Vars.Message_Status_Mine)) {
            holder.imgMyMessageReadStatus.setVisibility(View.GONE);
        } else {
            if (list.getLast_message_status().equals(Vars.Message_Status_Sent)) {
                holder.imgMyMessageReadStatus.setImageResource(R.drawable.ic_tick_sent_24);
            }
            if (list.getLast_message_status().equals(Vars.Message_Status_Received)) {
                holder.imgMyMessageReadStatus.setImageResource(R.drawable.ic_tick_received_24);
            }
            if (list.getLast_message_status().equals(Vars.Message_Status_Read)) {
                holder.imgMyMessageReadStatus.setImageResource(R.drawable.ic_tick_read_24);
            }
        }

        holder.relativeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageListActivity.onMessageClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMessage.size();
    }

    private void ShowImage(ImageView img_View, String img_URL) {
        Log.e("Image", img_URL);
        if (!img_URL.equals(""))
            Picasso.with(act).load(img_URL).fit().into(img_View);
    }
}









