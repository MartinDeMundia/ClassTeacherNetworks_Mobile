package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shamlatech.pojo.PojoChatMessageDisplay;
import com.shamlatech.school_management.MyApplication;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;
import java.util.HashMap;

import static com.shamlatech.utils.Vars.FirebaseDB;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Dharmalingam Sekar on 07-09-2017.
 */

public class ChatMessageAdapter extends BaseAdapter {

    ArrayList<PojoChatMessageDisplay> chatMessage;
    Activity act;
    Context mContext;
    String MyUserId;
    String MyChatUserId;

    public ChatMessageAdapter(Activity act, ArrayList<PojoChatMessageDisplay> chatMessage) {
        super();
        this.chatMessage = chatMessage;
        this.act = act;
    }

    @Override
    public int getCount() {
        return chatMessage.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflator = act.getLayoutInflater();

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflator.inflate(R.layout.list_chat_message, null);

            holder.linearDate = convertView.findViewById(R.id.linearDate);
            holder.linearOtherMessage = convertView.findViewById(R.id.linearOtherMessage);
            holder.linearMyMessage = convertView.findViewById(R.id.linearMyMessage);
            holder.imgMyMessageReadStatus = convertView.findViewById(R.id.imgMyMessageReadStatus);
            holder.txtOtherMessage = convertView.findViewById(R.id.txtOtherMessage);
            holder.txtOtherMessageDate = convertView.findViewById(R.id.txtOtherMessageDate);
            holder.txtMyMessage = convertView.findViewById(R.id.txtMyMessage);
            holder.txtMyMessageDate = convertView.findViewById(R.id.txtMyMessageDate);
            holder.txtChatDate = convertView.findViewById(R.id.txtChatDate);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyChatUserId = staUserInfo.getId() + "_" + staUserInfo.getRole();
        MyUserId = Support.GetPrefDefault(act, Vars.Pref_User_ID, "");
        final PojoChatMessageDisplay chats = chatMessage.get(position);
        final String message_id = chats.getMessage_id();
        final String sender_id = chats.getSender_id();
        final String receiver_id = chats.getReceiver_id();
        final String message = chats.getMessage();
        final String sent_on = chats.getSent_on();
        final String receive_on = chats.getReceive_on();
        final String read_on = chats.getRead_on();
        final String type = chats.getType();
        String status = Vars.Message_Status_Sent;

        if (type.equals("3")) {
            holder.txtChatDate.setText(message);
            holder.linearDate.setVisibility(View.VISIBLE);
            holder.linearOtherMessage.setVisibility(View.GONE);
            holder.linearMyMessage.setVisibility(View.GONE);

        } else {
            holder.linearDate.setVisibility(View.GONE);
            if (!read_on.equals("")) {
                status = Vars.Message_Status_Read;
            } else if (!receive_on.equals("")) {
                status = Vars.Message_Status_Received;
            } else {
                status = Vars.Message_Status_Sent;
            }

            if (MyChatUserId.equals(sender_id)) {
                holder.linearMyMessage.setVisibility(View.VISIBLE);
                holder.linearOtherMessage.setVisibility(View.GONE);
                holder.txtMyMessage.setText(message);
                holder.txtMyMessageDate.setText(Support.Show_Format_Date_Chat_With_TimeOnly(sent_on));
                if (status.equals(Vars.Message_Status_Sent)) {
                    holder.imgMyMessageReadStatus.setImageResource(R.drawable.ic_tick_sent_24);
                }
                if (status.equals(Vars.Message_Status_Received)) {
                    holder.imgMyMessageReadStatus.setImageResource(R.drawable.ic_tick_received_24);
                }
                if (status.equals(Vars.Message_Status_Read)) {
                    holder.imgMyMessageReadStatus.setImageResource(R.drawable.ic_tick_read_24);
                }
            } else {
                holder.linearMyMessage.setVisibility(View.GONE);
                holder.linearOtherMessage.setVisibility(View.VISIBLE);
                holder.txtOtherMessage.setText(message);
                holder.txtOtherMessageDate.setText(Support.Show_Format_Date_Chat_With_TimeOnly(sent_on));
            }

            updateRead(chats);
        }
        return convertView;
    }

    private void updateRead(PojoChatMessageDisplay model) {
        if (model.getRead_on().equals("") && !model.getSender_id().equals(MyChatUserId)) {
            UpdateReadOnFriend(MyChatUserId, model.getSender_id(), model.getMessage_id());
        }
    }

    public void UpdateReadOnFriend(String MyChatUserId, String FriendChatId, final String messageID) {
        final String MyRoomName = FirebaseDB + "/Chats/" + MyChatUserId + "/Messages/" + messageID;
        MyApplication.getFirebaseRef().child(MyRoomName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.hasChild("message_id")) {
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("read_on", Support.GetUTCCurrentTimeMillis());
                        MyApplication.getFirebaseRef().child(MyRoomName).updateChildren(result);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final String FriendRoomName = FirebaseDB + "/Chats/" + FriendChatId + "/Messages/" + messageID;
        MyApplication.getFirebaseRef().child(FriendRoomName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.hasChild("message_id")) {
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("read_on", Support.GetUTCCurrentTimeMillis());
                        MyApplication.getFirebaseRef().child(FriendRoomName).updateChildren(result);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class ViewHolder {
        LinearLayout linearDate, linearOtherMessage, linearMyMessage;
        ImageView imgMyMessageReadStatus;
        TextView txtOtherMessage, txtOtherMessageDate, txtMyMessage, txtMyMessageDate, txtChatDate;
    }
}









