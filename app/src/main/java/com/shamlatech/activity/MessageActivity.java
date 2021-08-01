package com.shamlatech.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.shamlatech.adapter.ChatMessageAdapter;
import com.shamlatech.pojo.PojoChatMessageDisplay;
import com.shamlatech.pojo.PojoChatTyping;
import com.shamlatech.pojo.PojoMessageList;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.MyApplication;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.FirebaseDB;
import static com.shamlatech.utils.Vars.staUserInfo;

public class MessageActivity extends BaseActivity {

    static Timer timer = new Timer();
    ChildEventListener FirebaseMessageListener;
    String RoomPath;
    ListView listMessages;
    ArrayList<PojoChatMessageDisplay> listChat = new ArrayList<>();
    EditText edtMessage;
    ImageView imgSend;
    String strFriendChatId = "", strFriendId = "", strMyChatId = "", strMyId = "";
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    SharedPreferences sharedpreferences;
    RelativeLayout relativeDate;
    TextView txtChatDate;
    boolean TypeListener = false;
    PojoMessageList userinfo;
    private ChatMessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        sharedpreferences = getSharedPreferences(Vars.MyPref, Context.MODE_PRIVATE);

        txtChatDate = findViewById(R.id.txtChatDate);
        relativeDate = findViewById(R.id.relativeDate);
        edtMessage = findViewById(R.id.edtMessage);
        imgSend = findViewById(R.id.imgSend);
        listMessages = findViewById(R.id.listMessages);

        listChat = new ArrayList<>();
        declareAppBar6("Name", "http://shamlatech.net/android_portal/school/image/student.jpg", "MessageActivity");
        declareDb();
        PrepareBundleData();
        getMessageList();

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(Vars.Pref_RefreshChat)) {
                    getMessageList();
                }
            }
        };

        listMessages.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (listChat.size() > 0 && firstVisibleItem != 0) {
                    String Send_Date = listChat.get(firstVisibleItem - 1).getSent_on();
                    if (!Send_Date.equals("")) {
                        relativeDate.setVisibility(View.VISIBLE);
                        txtChatDate.setText(Support.Show_Format_Date_For_Chat(Send_Date));
                    } else {
                        relativeDate.setVisibility(View.GONE);
                        txtChatDate.setText("");
                    }
                } else {
                    relativeDate.setVisibility(View.GONE);
                    txtChatDate.setText("");
                }
            }
        });

        edtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SendTypingToFriend();
            }
        });

        imgSend.setOnClickListener(this);
    }

    private void PrepareBundleData() {
        strMyId = staUserInfo.getId();
        strMyChatId = staUserInfo.getId() + "_" + staUserInfo.getRole();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("id")) {
                strFriendId = bundle.getString("id");
                String Role = bundle.getString("role");
                String Name = bundle.getString("name");
                String Profile_Pic = bundle.getString("image");
                declareAppBar6(Name, Profile_Pic, "MessageActivity");
                strFriendChatId = strFriendId + "_" + Role;
            }
        }
    }

    private void SendTypingToFriend() {
        String typingPath = FirebaseDB + "/Chats/" + strFriendChatId + "/Typing/" + strMyChatId;
        PojoChatTyping type = new PojoChatTyping();
        type.setDatetime(Support.GetCurrentUTCTimeFormat());
        MyApplication.getFirebaseRef().child(typingPath).setValue(type);
    }


    @Override
    protected void onResume() {
        RefreshRoom();
        sharedpreferences.registerOnSharedPreferenceChangeListener(listener);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        sharedpreferences.unregisterOnSharedPreferenceChangeListener(listener);
        super.onDestroy();
    }

    public void getMessageList() {
        listChat = db.AccessMessage(strFriendChatId);
        mAdapter = new ChatMessageAdapter(MessageActivity.this, listChat);
        listMessages.setAdapter(mAdapter);
    }

    public void RefreshRoom() {
        if (!TypeListener) {
            TypeListener = true;
            StartTypeListener();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSend:
                String Message = edtMessage.getText().toString();
                edtMessage.setText("");
                if (!Message.trim().toString().equals("")) {

                    String Message_Id = Support.GetCurrentTimeMillis() + "_" + strFriendChatId;
                    String roomMyPath = FirebaseDB + "/Chats/" + strMyChatId + "/Messages/" + Message_Id;
                    String roomOtherPath = FirebaseDB + "/Chats/" + strFriendChatId + "/Messages/" + Message_Id;

                    PojoChatMessageDisplay msg = new PojoChatMessageDisplay();
                    msg.setMessage_id(Message_Id);
                    msg.setRead_on("");
                    msg.setReceive_on("");
                    msg.setMessage(Message);
                    msg.setSender_id(strMyChatId);
                    msg.setType("1");
                    msg.setReceiver_id(strFriendChatId);
                    msg.setSent_on(Support.GetUTCCurrentTimeMillis());
                    MyApplication.getFirebaseRef().child(roomMyPath).setValue(msg);
                    MyApplication.getFirebaseRef().child(roomOtherPath).setValue(msg);
                    getRetro_SendMessages(staUserInfo.getId(), staUserInfo.getRole(), strFriendId, strMyChatId, strFriendChatId, Message);
                }
                break;
            default:
                super.onClick(v);
        }

    }

    private void StartTypeListener() {
        RoomPath = FirebaseDB + "/Chats/" + strMyChatId + "/Typing/" + strFriendChatId;
        FirebaseMessageListener = MyApplication.getFirebaseRef().child(RoomPath).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                try {
                    txtTyping.setText("Typing...");
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateOffTyping();
                                }
                            });
                        }
                    }, 2000);
                } catch (Exception e) {
                }
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

    public void updateOffTyping() {
        //String Status = Support.MinToDays(str_Online_Status);
        txtTyping.setText("");
    }


    public void getRetro_SendMessages(String user_id, String role_id, String to, String sender, String receiver, String message) {
        getRetro_Call(MessageActivity.this, service.getSendMessage(user_id, role_id, to, sender, receiver, message),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }
}
