package com.shamlatech.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shamlatech.adapter.MessageListAdapter;
import com.shamlatech.api_response.Result_Message_List;
import com.shamlatech.pojo.PojoMessageList;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.BaseFragment;
import com.shamlatech.school_management.HomeActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.SimpleDividerItemDecoration;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.staUserInfo;

public class MessageListActivity extends BaseFragment {

    RecyclerView recyclerMessage;
    ArrayList<PojoMessageList> listMessages = new ArrayList<>();
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    SharedPreferences sharedpreferences;
    Activity activity;
    private MessageListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_message_list, container, false);
        sharedpreferences = activity.getSharedPreferences(Vars.MyPref, Context.MODE_PRIVATE);
        listMessages = new ArrayList<>();

        declareDb(activity);

        recyclerMessage = view.findViewById(R.id.recyclerMessage);

        listMessages = db.AccessMessageList();

        mAdapter = new MessageListAdapter(activity, listMessages, MessageListActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerMessage.setLayoutManager(mLayoutManager);
        recyclerMessage.setItemAnimator(new DefaultItemAnimator());
        recyclerMessage.addItemDecoration(new SimpleDividerItemDecoration(activity, R.drawable.line_divider_1));
        recyclerMessage.setAdapter(mAdapter);

        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(Vars.Pref_RefreshNewChat)) {
                    RefreshList();
                    getRetro_AccessMessageList(staUserInfo.getId(), staUserInfo.getRole());
                }
            }
        };

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) activity).declareAppBar1("Messages", false);
        ((BaseActivity) activity).UpdateAppBarColor(R.color.colorTabPrimary);
        getRetro_AccessMessageList(staUserInfo.getId(), staUserInfo.getRole());
        sharedpreferences.registerOnSharedPreferenceChangeListener(listener);
        RefreshList();
    }

    @Override
    public void onDestroy() {
        sharedpreferences.unregisterOnSharedPreferenceChangeListener(listener);
        super.onDestroy();
    }


    public void RefreshList() {
        listMessages = db.AccessMessageList();
        mAdapter = new MessageListAdapter(activity, listMessages, MessageListActivity.this);
        recyclerMessage.setAdapter(mAdapter);
        ((HomeActivity) activity).updateBottomBar();
    }


    public void onMessageClick(int position) {
        startActivity(new Intent(activity, MessageActivity.class)
                .putExtra("id", listMessages.get(position).getFriend_id())
                .putExtra("role", listMessages.get(position).getRole_id())
                .putExtra("name", listMessages.get(position).getFriend_name())
                .putExtra("image", listMessages.get(position).getProfile_pic()));
    }


    public void getRetro_AccessMessageList(String user_id, String role_id) {
        getRetro_Call(activity, service.getAccessMessageList(user_id, role_id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Message_List mPojo_Message = onPojoBuilder(objectResponse, Result_Message_List.class);
                        if (mPojo_Message != null) {
                            db.InsertMessageList(mPojo_Message.getMessage_list());
                            RefreshList();
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

}
