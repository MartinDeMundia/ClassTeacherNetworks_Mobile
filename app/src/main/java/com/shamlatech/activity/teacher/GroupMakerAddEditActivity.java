package com.shamlatech.activity.teacher;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.adapter.StudentSelectionAdapter;
import com.shamlatech.api_response.Res_Teacher_TeachingSubjects;
import com.shamlatech.api_response.Result_Group;
import com.shamlatech.pojo.PojoClassWithSectionList;
import com.shamlatech.pojo.PojoStudentKeyPare;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.staUserInfo;

public class GroupMakerAddEditActivity extends BaseActivity {

    ArrayList<PojoStudentKeyPare> listStudents = new ArrayList<>();

    Spinner spinnerClass, spinnerSubject;
    EditText edtGroupName, edtStudents;
    StudentSelectionAdapter mAdapter;
    RecyclerView recyclerStudentSelection;
    ArrayList<PojoClassWithSectionList> listClass;
    ArrayList<Res_Teacher_TeachingSubjects> listSubject;

    TextView txtNoStudent;
    ArrayAdapter<PojoClassWithSectionList> adapterClass;
    ArrayAdapter<Res_Teacher_TeachingSubjects> adapterSubject;
    String strClassId = "", strSectionId = "", strGroupName = "", strSubjectId = "", strStudent = "";
    RelativeLayout relativeSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_maker_add_edit);

        declareAppBar2("Create", "GroupMakerAddEditActivity");
        declareBottomBar();

        listClass = db.AccessTeacherClassWithSectionList();

        txtNoStudent = findViewById(R.id.txtNoStudent);
        recyclerStudentSelection = findViewById(R.id.recyclerStudentSelection);
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        edtGroupName = findViewById(R.id.edtGroupName);
        edtStudents = findViewById(R.id.edtStudents);
        relativeSave = findViewById(R.id.relativeSave);

        mAdapter = new StudentSelectionAdapter(this, listStudents);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerStudentSelection.setLayoutManager(mLayoutManager);
        recyclerStudentSelection.setItemAnimator(new DefaultItemAnimator());
        recyclerStudentSelection.setAdapter(mAdapter);

        adapterClass = new ArrayAdapter<PojoClassWithSectionList>(this, R.layout.simple_spinner_item, listClass);
        adapterClass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(adapterClass);

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                strClassId = listClass.get(position).getClass_id();
                strSectionId = listClass.get(position).getSection_id();
                listSubject = db.AccessTeachingSubject(false, listClass.get(position).getTeaching_subject());
                loadSubjectSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                strSubjectId = listSubject.get(position).getId();
                loadStudentList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        relativeSave.setOnClickListener(this);
    }

    public void loadSubjectSpinner() {
        adapterSubject = new ArrayAdapter<Res_Teacher_TeachingSubjects>(GroupMakerAddEditActivity.this, R.layout.simple_spinner_item, listSubject);
        adapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(adapterSubject);
    }

    public void loadStudentList() {
        listStudents = db.AccessStudentListForGroup(strClassId, strSectionId, listSubject.get(spinnerSubject.getSelectedItemPosition()).getId());

        if (listStudents.size() > 0) {
            mAdapter = new StudentSelectionAdapter(this, listStudents);
            recyclerStudentSelection.setAdapter(mAdapter);
            recyclerStudentSelection.setVisibility(View.VISIBLE);
            txtNoStudent.setVisibility(View.GONE);
        } else {
            txtNoStudent.setVisibility(View.VISIBLE);
            recyclerStudentSelection.setVisibility(View.GONE);
        }
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

    public void getSelectedStudentNames() {
        if (listStudents.size() > 0) {
            List<String> listSelectedName = new ArrayList<>();
            List<String> listSelectedId = new ArrayList<>();
            for (int i = 0; i < listStudents.size(); i++) {
                if (listStudents.get(i).isSelected()) {
                    listSelectedName.add(listStudents.get(i).getName().trim());
                    listSelectedId.add(listStudents.get(i).getId().trim());
                }
            }
            String selectedStudent = android.text.TextUtils.join(", ", listSelectedName);
            strStudent = android.text.TextUtils.join(",", listSelectedId);
            edtStudents.setText(selectedStudent);
        }
    }

    public void onStudentSelection(int position, boolean isChecked) {
        listStudents.get(position).setSelected(isChecked);
        mAdapter.notifyDataSetChanged();
        getSelectedStudentNames();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeSave:
                if (listStudents.size() == 0) {
                    Support.ShowAlertWithOutTitle(this, "All student are depend on group");
                } else {
                    strGroupName = edtGroupName.getText().toString().trim();
                    if (strGroupName.equals("")) {
                        edtGroupName.setError("Please enter the group name");
                        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(500);
                    } else if (strStudent.equals("")) {
                        Support.ShowAlertWithOutTitle(this, "Please select the student for the group");
                    } else {
                        getRetro_CreateGroup(staUserInfo.getId(), staUserInfo.getRole(), strClassId, strSectionId, strSubjectId, strGroupName, strStudent);
                    }
                }
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    public void getRetro_CreateGroup(String user_id, String role_id, String classId, String sectionId, String subjectId, String group_name, String students) {
        getRetro_Call(this, service.getCreateGroup(user_id, role_id, classId, sectionId, subjectId, group_name, students),
                true, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Group mPojo_Group = onPojoBuilder(objectResponse, Result_Group.class);
                        if (mPojo_Group != null) {
                            db.InsertGroup(mPojo_Group.getGroup());
                            Toast.makeText(getApplicationContext(), "Group Created", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }
}
