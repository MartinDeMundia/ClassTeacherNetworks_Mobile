package com.shamlatech.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.pojo.PojoChatMessage;
import com.shamlatech.school_management.MyApplication;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import java.util.HashMap;

import static com.shamlatech.utils.Vars.FirebaseDB;


public class MessageService extends IntentService {

    DBAdapter db;
    String MyUserId = "";
    String MyUserChatId = "";
    ChildEventListener FirebaseMessageListener;
    String RoomPath;

    public MessageService() {
        super("MessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            db = new DBAdapter(this, this.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }
        StartMessageListener();
        return Service.START_NOT_STICKY;
    }

    private void StartMessageListener() {
        MyUserId = Support.GetPrefDefault(getApplicationContext(), Vars.Pref_User_ID, "");
        MyUserChatId = Support.GetPrefDefault(getApplicationContext(), Vars.Pref_Chat_User_ID, "");
        if (!MyUserId.equals("")) {
            RoomPath = FirebaseDB + "/Chats/" + MyUserChatId + "/Messages/";
            FirebaseMessageListener = MyApplication.getFirebaseRef().child(RoomPath).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    PojoChatMessage model = dataSnapshot.getValue(PojoChatMessage.class);
                    String MessageID = dataSnapshot.getKey();
                    db.InsertChat(MessageID, model);
                    updatePref(model.getSender_id());
                    updateReceive(model);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    PojoChatMessage model = dataSnapshot.getValue(PojoChatMessage.class);
                    String MessageID = dataSnapshot.getKey();
                    db.InsertChat(MessageID, model);
                    updatePref(model.getSender_id());
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getFirebaseRef().child(RoomPath).removeEventListener(FirebaseMessageListener);
    }

    private void updateReceive(PojoChatMessage model) {
        if (model != null) {
            if (model.getRead_on() == null) {
                final String MyRoomName = FirebaseDB + "/Chats/" + MyUserChatId + "/Messages/" + model.getMessage_id();
                MyApplication.getFirebaseRef().child(MyRoomName).removeValue();
            }
            if (model.getRead_on() != null && !model.getRead_on().equals("")) {
                final String MyRoomName = FirebaseDB + "/Chats/" + MyUserChatId + "/Messages/" + model.getMessage_id();
                final String MyAchieveRoom = FirebaseDB + "/Chats/" + MyUserChatId + "/Achieve/" + model.getMessage_id();
                MyApplication.getFirebaseRef().child(MyAchieveRoom).setValue(model);
                MyApplication.getFirebaseRef().child(MyRoomName).removeValue();
            } else if (model.getReceive_on() != null && model.getSender_id() != null && model.getReceive_on().equals("") && !model.getSender_id().equals(MyUserChatId)) {
                final String MyRoomName = FirebaseDB + "/Chats/" + MyUserChatId + "/Messages/" + model.getMessage_id();
                HashMap<String, Object> result = new HashMap<>();
                result.put("receive_on", Support.GetCurrentUTCTimeFormat());
                MyApplication.getFirebaseRef().child(MyRoomName).updateChildren(result);
                UpdateReadOnFriend(model.getSender_id(), model.getMessage_id());
            }
        }
    }

    public void UpdateReadOnFriend(String Friend_Id, final String messageID) {
        final String FriendRoomName = FirebaseDB + "/Chats/" + Friend_Id + "/Messages/" + messageID;
        MyApplication.getFirebaseRef().child(FriendRoomName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.hasChild("message_id")) {
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("receive_on", Support.GetUTCCurrentTimeMillis());
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

    public void updatePref(String Sender) {
        Support.SetPref(getApplicationContext(), Vars.Pref_RefreshChat, Support.GetCurrentTimeMillis());
        Support.SetPref(getApplicationContext(), Vars.Pref_RefreshNewChat, Support.GetCurrentTimeMillis());
    }
}
