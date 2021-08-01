package com.shamlatech.activity.teacher;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shamlatech.adapter.BehaviourEditAdapter;
import com.shamlatech.api_request.Req_Update_Behaviour;
import com.shamlatech.api_response.Res_Look_Behaviour;
import com.shamlatech.api_response.Res_Look_BehaviourContent;
import com.shamlatech.api_response.Res_Stud_Behaviour;
import com.shamlatech.api_response.Result_Stud_Behaviour;
import com.shamlatech.pojo.PojoBehaviourReport;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.staUserInfo;

public class UpdateBehaviourActivity extends BaseActivity {

    TextView txtLevel;
    Spinner spinnerReport;

    RecyclerView recyclerBehaviour;
    RelativeLayout relativeDone;
    BehaviourEditAdapter mAdapter;
    ArrayList<Req_Update_Behaviour> listUpdate = new ArrayList<>();
    ArrayList<Res_Look_BehaviourContent> listBehaviourContent = new ArrayList<>();
    ArrayList<Res_Look_Behaviour> listBehaviourLooks = new ArrayList<>();
    ArrayAdapter<Res_Look_Behaviour> adapterBehaviourTitle;
    String StudId = "";
    Res_Stud_Behaviour studBehaviourInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_behaviour);

        declareAppBar2("Edit Mode", "UpdateBehaviourActivity");
        declareBottomBar();
        UpdateAppBarColor(R.color.colorTabBlue);
        PrepareBehaviour();

        txtLevel = findViewById(R.id.txtLevel);
        spinnerReport = findViewById(R.id.spinnerReport);
        recyclerBehaviour = findViewById(R.id.recyclerBehaviour);

        relativeDone = findViewById(R.id.relativeDone);

        mAdapter = new BehaviourEditAdapter(this, listBehaviourContent, "0");
        RecyclerView.LayoutManager mExamLayoutManager = new LinearLayoutManager(this);
        recyclerBehaviour.setLayoutManager(mExamLayoutManager);
        recyclerBehaviour.setItemAnimator(new DefaultItemAnimator());
        recyclerBehaviour.setAdapter(mAdapter);


        relativeDone.setOnClickListener(this);
        PreparePage();
    }


    @Override
    protected void onResume() {
        super.onResume();
        InitializeProgress();
    }

    public void InitializeProgress() {
        Vars.Custom_Progress = (View) findViewById(R.id.Custom_Progress);
        Vars.Custom_Progress.setVisibility(View.GONE);
        String indicator = getIntent().getStringExtra("indicator");
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.show();
    }

    private void PreparePage() {
        studBehaviourInfo = new Res_Stud_Behaviour();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("stud_id") && bundle.containsKey("behaviour_info")) {
                studBehaviourInfo = (Res_Stud_Behaviour) bundle.getSerializable("behaviour_info");
                StudId = bundle.getString("stud_id");
                listBehaviourLooks = db.AccessBehaviourLookup();
                PrepareBehaviour();
            }
        }
        adapterBehaviourTitle = new ArrayAdapter<Res_Look_Behaviour>(this, R.layout.simple_spinner_item, listBehaviourLooks);
        adapterBehaviourTitle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReport.setAdapter(adapterBehaviourTitle);

        spinnerReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                listBehaviourContent = listBehaviourLooks.get(i).getContent();
                mAdapter = new BehaviourEditAdapter(UpdateBehaviourActivity.this, listBehaviourContent,listBehaviourLooks.get(i).getId());
                recyclerBehaviour.setAdapter(mAdapter);
                prepareFreshUpdate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void PrepareBehaviour() {
        for (int i = 0; i < listBehaviourLooks.size(); i++) {
            for (int j = 0; j < listBehaviourLooks.get(i).getContent().size(); j++) {
                PojoBehaviourReport report = accessReport(listBehaviourLooks.get(i).getContent().get(j).getId());
                listBehaviourLooks.get(i).getContent().get(j).setAction_taken(report.getAction());
                listBehaviourLooks.get(i).getContent().get(j).setReport(report.getReport());
            }
        }
    }

    private PojoBehaviourReport accessReport(String id) {
        PojoBehaviourReport report = new PojoBehaviourReport();
        report.setReport("Yes");
        report.setAction("");
        for (int i = 0; i < studBehaviourInfo.getDetailed_behaviour().size(); i++) {
            for (int j = 0; j < studBehaviourInfo.getDetailed_behaviour().get(i).getContent().size(); j++) {
                if (studBehaviourInfo.getDetailed_behaviour().get(i).getContent().get(j).getId().equalsIgnoreCase(id)) {
                    report.setAction(studBehaviourInfo.getDetailed_behaviour().get(i).getContent().get(j).getAction());
                    report.setReport(studBehaviourInfo.getDetailed_behaviour().get(i).getContent().get(j).getReport());
                }
            }
        }
        return report;
    }

    public void prepareFreshUpdate() {
        listUpdate = new ArrayList<>();
        for (int i = 0; i < listBehaviourContent.size(); i++) {
            Req_Update_Behaviour update = new Req_Update_Behaviour();
            update.setId(listBehaviourContent.get(i).getId());
            update.setReport(listBehaviourContent.get(i).getReport());
            update.setAction(listBehaviourContent.get(i).getAction_taken());
            listUpdate.add(update);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeDone:
                getRetro_UpdateBehaviour(staUserInfo.getId(), staUserInfo.getRole(), StudId, new Gson().toJson(listUpdate).toString());
                break;
            default:
                super.onClick(v);
        }
    }

    public void onUpdateEditList(String id, String report, String action) {
        for (int i = 0; i < listUpdate.size(); i++) {
            if (listUpdate.get(i).getId().equalsIgnoreCase(id)) {
                listUpdate.get(i).setReport(report);
                listUpdate.get(i).setAction(action);
            }
        }
    }


    public void getRetro_UpdateBehaviour(String user_id, String role_id, String stud_id, String behaviour) {
        getRetro_Call(UpdateBehaviourActivity.this, service.getUpdateBehaviour(user_id, role_id, stud_id, behaviour),
                true, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Stud_Behaviour mPojo_Behaviour = onPojoBuilder(objectResponse, Result_Stud_Behaviour.class);
                        if (mPojo_Behaviour != null) {
                            if (mPojo_Behaviour.getStatus().equals(API_Code.Success)) {
                                Vars.Refresh_Stud_Behaviour = "1";
                                Toast.makeText(getApplicationContext(), "Behaviour Updated", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }
}
