package com.shamlatech.activity.teacher;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shamlatech.adapter.ClassPositionAdapter;
import com.shamlatech.adapter.StudentListPlaceAdapter;
import com.shamlatech.api_response.Res_Teacher_Class;
import com.shamlatech.api_response.Res_Teacher_ClassStudent;
import com.shamlatech.api_response.Result_MyClass;
import com.shamlatech.pojo.PojoClassPosition;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.SimpleDividerItemDecoration;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.ConvertToInteger;
import static com.shamlatech.utils.Support.PrepareClassLayout;
import static com.shamlatech.utils.Vars.staUserInfo;

public class ClassLayoutAddEditActivity extends BaseActivity {

    RecyclerView recyclerClassLayout;
    ClassPositionAdapter mClassAdapter;
    ArrayList<PojoClassPosition> listPosition = new ArrayList<>();
    int column = 6;
    Dialog pick_Dialog;
    ArrayList<Res_Teacher_ClassStudent> listAllStudent = new ArrayList<>();
    ArrayList<Res_Teacher_ClassStudent> listStudent = new ArrayList<>();
    StudentListPlaceAdapter mAdapter;
    int selected_position = 0;
    boolean isAlreadySelected = false;
    Res_Teacher_Class MyClass = new Res_Teacher_Class();
    TextView txtClassName;
    RelativeLayout relativeDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_layout_add_edit);

        declareAppBar2("Class Layout", "ClassLayoutAddEditActivity");
        declareBottomBar();
        declareDb();

        listAllStudent = new ArrayList<>();
        listStudent = new ArrayList<>();

        MyClass = db.AccessMyClass();
        listPosition = PrepareClassLayout(MyClass);
        listAllStudent = MyClass.getStudent();


        relativeDone = findViewById(R.id.relativeDone);

        txtClassName = findViewById(R.id.txtClassName);
        recyclerClassLayout = findViewById(R.id.recyclerClassLayout);

        txtClassName.setText(MyClass.getClass_name() + MyClass.getSection_name());

        mClassAdapter = new ClassPositionAdapter(this, listPosition, "update",
                ConvertToInteger(MyClass.getDivides()), ConvertToInteger(MyClass.getColumn()));

        Integer spanCount;
        if(ConvertToInteger(MyClass.getColumn()) <= 0){
            spanCount = 4;
        }else{
            spanCount = ConvertToInteger(MyClass.getColumn());
        }
        final GridLayoutManager mLayoutManagerCategory = new GridLayoutManager(this, spanCount);
        recyclerClassLayout.setLayoutManager(mLayoutManagerCategory);
        recyclerClassLayout.setItemAnimator(new DefaultItemAnimator());
        recyclerClassLayout.setAdapter(mClassAdapter);

        relativeDone.setOnClickListener(this);
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

    public void prepareStudentListBeforeChoose() {
        listStudent.clear();
        for (int i = 0; i < listAllStudent.size(); i++) {
            boolean Exist = false;
            for (int j = 0; j < listPosition.size(); j++) {
                if (listPosition.get(j).getId().equals(listAllStudent.get(i).getId())) {
                    Exist = true;
                }
            }
            if (!Exist) {
                listStudent.add(listAllStudent.get(i));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.relativeDone:
                getRetro_UpdateClassLayout(staUserInfo.getId(), staUserInfo.getRole(), MyClass.getClass_id(), MyClass.getSection_id(), new Gson().toJson(listPosition));
                break;
            default:
                super.onClick(v);
        }
    }

    public void onPlaceClicked(int position) {
        if (!isAlreadySelected) {
            isAlreadySelected = true;
            selected_position = position;
            listPosition.get(position).setSelection(true);
            if (listPosition.get(position).getStatus().equals("placed")) {

            } else {
                showStudentList();
            }

        } else {

            String old_position = listPosition.get(position).getPosition();
            String new_position = listPosition.get(selected_position).getPosition();
            listPosition.get(selected_position).setPosition(old_position);
            listPosition.get(position).setPosition(new_position);
            Collections.swap(listPosition, selected_position, position);
            resetPlaceList();
        }

        mClassAdapter.notifyDataSetChanged();
    }

    public void showStudentList() {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(this);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_student_list);

            final RecyclerView recyclerStudent;

            prepareStudentListBeforeChoose();

            recyclerStudent = pick_Dialog.findViewById(R.id.recyclerStudent);

            mAdapter = new StudentListPlaceAdapter(this, listStudent);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerStudent.setLayoutManager(mLayoutManager);
            recyclerStudent.setItemAnimator(new DefaultItemAnimator());
            recyclerStudent.addItemDecoration(new SimpleDividerItemDecoration(this, R.drawable.line_divider_1));
            recyclerStudent.setAdapter(mAdapter);

            pick_Dialog.show();

            if (listStudent.size() == 0) {

                pick_Dialog.dismiss();
                resetPlaceList();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("No Student, All are placed");
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }


        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showStudentList();
        } else {
            pick_Dialog = null;
            showStudentList();
        }
    }

    public void onClickStudent(Res_Teacher_ClassStudent list) {
        listPosition.get(selected_position).setName(list.getFirst_name());
        listPosition.get(selected_position).setId(list.getId());
        listPosition.get(selected_position).setStatus("placed");
        pick_Dialog.dismiss();
        resetPlaceList();
    }

    public void resetPlaceList() {
        for (int i = 0; i < listPosition.size(); i++)
            listPosition.get(i).setSelection(false);
        isAlreadySelected = false;
        mClassAdapter.notifyDataSetChanged();
    }

    public void getRetro_UpdateClassLayout(String user_id, String role_id, String class_id, String section_id, String place) {
        getRetro_Call(ClassLayoutAddEditActivity.this, service.getUpdateMyClassLayout(user_id, role_id, class_id, section_id, place),
                true, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_MyClass mPojo_MyClass = onPojoBuilder(objectResponse, Result_MyClass.class);
                        if (mPojo_MyClass != null) {
                            if (mPojo_MyClass.getStatus().equals(API_Code.Success)) {
                                db.InsertTeachersClass(mPojo_MyClass.getMy_class());
                                Toast.makeText(getApplicationContext(), "Class Layout Update", Toast.LENGTH_LONG).show();
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
