package com.shamlatech.activity.parent;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.shamlatech.adapter.AllergiesUpdateListAdapter;
import com.shamlatech.api_response.Res_Stud_Health_Known_Allergies;
import com.shamlatech.api_response.Result_Allergies;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.Refresh_Stud_Health;
import static com.shamlatech.utils.Vars.staUserInfo;

public class AddHealthReport extends BaseActivity {

    EditText edtAllergy, edtDetails;
    RelativeLayout relativeAdd;
    RecyclerView recyclerAllergies;
    AllergiesUpdateListAdapter mAdapter;

    ArrayList<Res_Stud_Health_Known_Allergies> listAllergies;

    String stud_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_health_report);

        listAllergies = new ArrayList<>();

        declareAppBar2("Health Report", "UpdateScoreActivity");
        UpdateAppBarColor(R.color.colorTabRed);


        edtAllergy = findViewById(R.id.edtAllergy);
        edtDetails = findViewById(R.id.edtDetails);

        relativeAdd = findViewById(R.id.relativeAdd);
        recyclerAllergies = findViewById(R.id.recyclerAllergies);

        mAdapter = new AllergiesUpdateListAdapter(this, listAllergies);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerAllergies.setLayoutManager(mLayoutManager);
        recyclerAllergies.setItemAnimator(new DefaultItemAnimator());
        recyclerAllergies.setAdapter(mAdapter);

        relativeAdd.setOnClickListener(this);

        prepareBundle();
    }


    public void prepareBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("stud_id")) {
                stud_Id = bundle.getString("stud_id");
                getRetro_AccessAllergies(staUserInfo.getId(), staUserInfo.getRole(), stud_Id);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeAdd:
                String strAllergyName = edtAllergy.getText().toString().trim();
                String strDetails = edtDetails.getText().toString().trim();
                if (strAllergyName.equals("")) {
                    edtAllergy.setError("Enter allergy name");
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(500);
                } else {
                    getRetro_AddAllergies(staUserInfo.getId(), staUserInfo.getRole(), stud_Id, strAllergyName, strDetails);
                }
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    public void onClickRemove(int position) {
        getRetro_RemoveAllergies(staUserInfo.getId(), staUserInfo.getRole(), stud_Id, listAllergies.get(position).getId());
        listAllergies.remove(position);
    }

    public void getRetro_AccessAllergies(String user_id, String role_id, String stud_id) {
        getRetro_Call(this, service.getAccessAllergyList(user_id, role_id, stud_id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Allergies mPojo_Allergy = onPojoBuilder(objectResponse, Result_Allergies.class);
                        if (mPojo_Allergy != null) {
                            listAllergies = mPojo_Allergy.getAllergies();
                            mAdapter = new AllergiesUpdateListAdapter(AddHealthReport.this, listAllergies);
                            recyclerAllergies.setAdapter(mAdapter);
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public void getRetro_AddAllergies(String user_id, String role_id, String stud_id, String name, String details) {
        getRetro_Call(this, service.getAddAllergyList(user_id, role_id, stud_id, name, details),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Allergies mPojo_Allergy = onPojoBuilder(objectResponse, Result_Allergies.class);
                        if (mPojo_Allergy != null) {
                            listAllergies = mPojo_Allergy.getAllergies();
                            mAdapter = new AllergiesUpdateListAdapter(AddHealthReport.this, listAllergies);
                            recyclerAllergies.setAdapter(mAdapter);
                            edtAllergy.setText("");
                            edtDetails.setText("");
                            Refresh_Stud_Health = "1";
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public void getRetro_RemoveAllergies(String user_id, String role_id, String stud_id, String id) {
        getRetro_Call(this, service.getRemoveAllergyList(user_id, role_id, stud_id, id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Allergies mPojo_Allergy = onPojoBuilder(objectResponse, Result_Allergies.class);
                        if (mPojo_Allergy != null) {
                            listAllergies = mPojo_Allergy.getAllergies();
                            mAdapter = new AllergiesUpdateListAdapter(AddHealthReport.this, listAllergies);
                            recyclerAllergies.setAdapter(mAdapter);
                            Refresh_Stud_Health = "1";
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }


}
