package com.shamlatech.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.pojo.PojoChatMessage;
import com.shamlatech.school_management.MyApplication;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import org.json.JSONObject;

import static com.shamlatech.utils.Vars.FirebaseDB;


public class AchieveMessageService extends IntentService {

    DBAdapter db;
    String MyUserId = "";
    String MyUserChatId = "";
    ChildEventListener FirebaseMessageListener;
    String RoomPath;

    public AchieveMessageService() {
        super("AchieveMessageService");
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
        MyUserId = Support.GetPrefDefault(getApplicationContext(), Vars.Pref_Chat_User_ID, "");
        MyUserChatId = Support.GetPrefDefault(getApplicationContext(), Vars.Pref_Chat_User_ID, "");
        RoomPath = FirebaseDB + "/Chats/" + MyUserChatId + "/Achieve/";
        MyApplication.getFirebaseRef().child(RoomPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(dataSnapshot.getValue()));
                    for (int i = 0; i < jsonObject.names().length(); i++) {
                        String Name = jsonObject.names().get(i).toString();
                        JSONObject Value = new JSONObject(jsonObject.get(Name).toString());
                        PojoChatMessage info = new PojoChatMessage();
                        info.setMessage_id(Value.getString("message_id"));
                        info.setSender_id(Value.getString("sender_id"));
                        info.setReceiver_id(Value.getString("receiver_id"));
                        info.setSent_on(Value.getString("sent_on"));
                        info.setType(Value.getString("type"));
                        info.setMessage(Value.getString("message"));
                        info.setReceive_on(Value.getString("receive_on"));
                        info.setRead_on(Value.getString("read_on"));
                        db.InsertChat(Name, info);
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getFirebaseRef().child(RoomPath).removeEventListener(FirebaseMessageListener);
    }

}
