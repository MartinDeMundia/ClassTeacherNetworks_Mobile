package com.shamlatech.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;

import com.shamlatech.api_response.Result_Lookups;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.NetworkUtil;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.staUserInfo;


public class BGLookupService extends IntentService {

    DBAdapter db;

    public BGLookupService() {
        super("BGLookupService");
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

        getRetro_AccessLookups();
        return Service.START_NOT_STICKY;
    }


    public void getRetro_AccessLookups() {
        if (NetworkUtil.isConnected(getApplicationContext())) {
            getRetro_Call(getApplicationContext(), service.getAccessLookups(staUserInfo.getId(), staUserInfo.getRole()), false, new API_Calls.OnApiResult() {
                @Override
                public void onSuccess(Response<Object> objectResponse) {
                    Result_Lookups mPojoLooks = onPojoBuilder(objectResponse, Result_Lookups.class);
                    if (mPojoLooks != null) {
                        if (mPojoLooks.getStatus().equals(API_Code.Success)) {
                            db.InsertAbsentReasons(mPojoLooks.getAbsent_reason());
                            db.InsertExamList(mPojoLooks.getExam_list());
                            db.InsertBehaviourList(mPojoLooks.getBehaviour());
                        }
                    }
                }

                @Override
                public void onFailure(String message) {

                }
            });
        }
    }
}
