package com.shamlatech.fragment.teacher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.shamlatech.adapter.MyClassListAdapter;
import com.shamlatech.api_response.Res_Exams;
import com.shamlatech.api_response.Res_Student_Marks;
import com.shamlatech.api_response.Res_Subsubjects;
import com.shamlatech.api_response.Res_Teacher_Class;
import com.shamlatech.api_response.Res_Teacher_TeachingSubjects;
import com.shamlatech.api_response.Result_Exam_List;
import com.shamlatech.api_response.Result_Subsubjects;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Martin Mundia M on 24-OCT-2019.
 */

@SuppressLint("ValidFragment")
public class FilterBySubjectFragment extends Fragment {

    View view;
    Spinner spinnerSubject , spinnerTerm , spinnerExam , spinnerSubsubjects;
    RecyclerView recyclerClasses;
    ArrayList<Res_Teacher_TeachingSubjects> listSubjects = new ArrayList<>();
    ArrayList<Res_Teacher_Class> listClasses = new ArrayList<>();
    ArrayList<Res_Student_Marks> listSaveMarks = new ArrayList();

    ArrayList<String> listSubsubjects = new ArrayList<String>();

    ArrayList listExam = new ArrayList();
    ArrayList<String> listTerm = new ArrayList<String>() {{
       add("Term 1");
       add("Term 2");
       add("Term 3");
   }};
    MyClassListAdapter mAdapter;
    DBAdapter db;
    Activity activity;
    ArrayAdapter adapterExam , adapterSubsubj;
    private FloatingActionButton buttonSave;
    private final int mPosition;
    public String teachersclass ;

    @SuppressLint("ValidFragment")
    public FilterBySubjectFragment(int position) {
        mPosition = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_filter_by_subject, container, false);

        try {
            db = new DBAdapter(activity, activity.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }

        recyclerClasses = view.findViewById(R.id.recyclerClasses);
        spinnerSubject = view.findViewById(R.id.spinnerSubject);
        spinnerTerm = view.findViewById(R.id.spinnerTerm);
        spinnerExam = view.findViewById(R.id.spinnerExam);

        spinnerSubsubjects = view.findViewById(R.id.spinnerSubsubjects);
        adapterSubsubj = new ArrayAdapter(activity, R.layout.simple_spinner_item, listSubsubjects);
        adapterSubsubj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubsubjects.setAdapter(adapterSubsubj);

        listClasses = new ArrayList<>();
        prepareDateForPage();


        mAdapter = new MyClassListAdapter(activity, listClasses, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerClasses.setLayoutManager(mLayoutManager);
        recyclerClasses.setItemAnimator(new DefaultItemAnimator());
        recyclerClasses.setAdapter(mAdapter);

        ArrayAdapter<Res_Teacher_TeachingSubjects> adapterSubject = new ArrayAdapter<Res_Teacher_TeachingSubjects>(activity, R.layout.simple_spinner_item, listSubjects);
        adapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(adapterSubject);

        adapterExam = new ArrayAdapter(activity, R.layout.simple_spinner_item, listExam);
        adapterExam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExam.setAdapter(adapterExam);
        //spinnerExam.setSelection(1,true);

        ArrayAdapter adapterTerm = new ArrayAdapter(activity, R.layout.simple_spinner_item, listTerm);
        adapterTerm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTerm.setAdapter(adapterTerm);

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sSelected = listSubjects.get(spinnerSubject.getSelectedItemPosition()).getId();
                getRetroSubsubjects(sSelected);

                if(spinnerSubsubjects.getSelectedItem() != null) {
                    PerformFilterSubsubj( listSubjects.get(spinnerSubject.getSelectedItemPosition()).getId() ,teachersclass,spinnerSubsubjects.getSelectedItem().toString());
                }else{
                    PerformFilter(listSubjects.get(spinnerSubject.getSelectedItemPosition()).getId(),teachersclass);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerExam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerSubsubjects.getSelectedItem() != null) {
                    PerformFilterSubsubj( listSubjects.get(spinnerSubject.getSelectedItemPosition()).getId() ,teachersclass,spinnerSubsubjects.getSelectedItem().toString());
                }else{
                    PerformFilter(listSubjects.get(spinnerSubject.getSelectedItemPosition()).getId(),teachersclass);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerSubsubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerSubsubjects.getSelectedItem() != null) {
                    PerformFilterSubsubj(listSubjects.get(spinnerSubject.getSelectedItemPosition()).getId(), teachersclass, spinnerSubsubjects.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinnerSubsubjects.getSelectedItem() != null) {
                    PerformFilterSubsubj( listSubjects.get(spinnerSubject.getSelectedItemPosition()).getId() ,teachersclass,spinnerSubsubjects.getSelectedItem().toString());
                }else{
                    PerformFilter(listSubjects.get(spinnerSubject.getSelectedItemPosition()).getId(),teachersclass);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        PerformFilter("0",teachersclass);
        buttonSave = (FloatingActionButton) view.findViewById(R.id.flbsave);
        buttonSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Snackbar.make(view, "Saving student marks..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                String  uid = staUserInfo.getId();
                String  urole = staUserInfo.getRole();

                if(spinnerSubsubjects.getSelectedItem() != null) {
                    listSaveMarks = db.AccessSavedSubjectMarks(uid, urole);
                    getPostSubjectMarks(uid, urole,listSaveMarks);
                }else{
                    listSaveMarks = db.AccessSavedMarks(uid, urole);
                    getPostExam(uid, urole,listSaveMarks);
                }
                Snackbar.make(view, "Saving student marks..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    private void prepareDateForPage() {
        listSubjects = db.AccessSubjects(true, "");
        String  uid = staUserInfo.getId();
        String  urole = staUserInfo.getRole();
        getRetroExams(uid,urole);
        listClasses = db.AccessTeachersClass();
        teachersclass = listClasses.get(mPosition).getSection_id();
    }



    public void PerformFilterSubsubj(String subject_id, String class_id , String selSubj) {
        String textexam = "0";
        String textsubject = "0";
        String texterm = "0";
        String  uid = staUserInfo.getId();
        String  urole = staUserInfo.getRole();
        if(Integer.parseInt(subject_id) > 0){
            textexam = spinnerExam.getSelectedItem().toString();
            textsubject = spinnerSubject.getSelectedItem().toString();
            texterm = spinnerTerm.getSelectedItem().toString();
        }

        ArrayList<Res_Teacher_Class> tempList = db.AccessTeachersClassSubjectMarks(texterm,textexam,textsubject,uid,urole,class_id,selSubj);
        listClasses = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            if (subject_id.equals("0")) {
                listClasses.add(tempList.get(i));
            } else {
                String[] teachingSubject = tempList.get(i).getSubject_id().split(",");
                if (Arrays.asList(teachingSubject).contains(subject_id)){
                    listClasses.add(tempList.get(i));
                }
            }
        }
        mAdapter = new MyClassListAdapter(activity, listClasses, this);
        recyclerClasses.setAdapter(mAdapter);
    }



    public void PerformFilter(String subject_id, String class_id) {
        String textexam = "0";
        String textsubject = "0";
        String texterm = "0";
        String  uid = staUserInfo.getId();
        String  urole = staUserInfo.getRole();
        if(Integer.parseInt(subject_id) > 0){
             textexam = spinnerExam.getSelectedItem().toString();
             textsubject = spinnerSubject.getSelectedItem().toString();
             texterm = spinnerTerm.getSelectedItem().toString();
        }

        ArrayList<Res_Teacher_Class> tempList = db.AccessTeachersClassMarks(texterm,textexam,textsubject,uid,urole,class_id);
        listClasses = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            if (subject_id.equals("0")) {
               listClasses.add(tempList.get(i));
            } else {
                String[] teachingSubject = tempList.get(i).getSubject_id().split(",");
                if (Arrays.asList(teachingSubject).contains(subject_id)){
                    listClasses.add(tempList.get(i));
                }
            }
        }
        mAdapter = new MyClassListAdapter(activity, listClasses, this);
        recyclerClasses.setAdapter(mAdapter);
    }


    public void getRetroExams(String user_id, String role_id){
        getRetro_Call(activity, service.getExams(user_id, role_id ),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Exam_List mPojo__List = onPojoBuilder(objectResponse, Result_Exam_List.class);
                        ArrayList<Res_Exams> exmlist =  mPojo__List.getExams();
                        for (int i = 0; i < exmlist.size(); i++) {
                            listExam.add(exmlist.get(i).getExamname());
                        }
                        adapterExam.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
     }



    public void getRetroSubsubjects(String sId){
        listSubsubjects.clear();
        getRetro_Call(activity, service.getSubsubjects(sId),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Subsubjects mPojo__List = onPojoBuilder(objectResponse, Result_Subsubjects.class);
                        ArrayList<Res_Subsubjects> subsubj =  mPojo__List.getSubsubjects();
                        for (int i = 0; i < subsubj.size(); i++) {
                            listSubsubjects.add(subsubj.get(i).getSubject_name());
                        }
                        int fItem = new Double(subsubj.get(0).getId()).intValue();
                        if( fItem == 0){
                            spinnerSubsubjects.setVisibility(View.GONE);
                        }else{
                            spinnerSubsubjects.setVisibility(View.VISIBLE);
                            adapterSubsubj.notifyDataSetChanged();
                        }

                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
    }


    public void getPostExam(String user_id, String role_id , ArrayList<Res_Student_Marks> datasaved){
        String dsaved = "";
        for(int i = 0; i < datasaved.size(); i++){
            dsaved += datasaved.get(i).getStudentid() + "," + datasaved.get(i).getExamtype()+ "," + datasaved.get(i).getNames() + "," + datasaved.get(i).getSubject() + "," + datasaved.get(i).getTerm()+ "," + datasaved.get(i).getMarks() + "|";
        }
        getRetro_Call(activity, service.getPostExams(user_id, role_id ,dsaved ),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {

                        Snackbar.make(view, "Saved!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public void getPostSubjectMarks(String user_id, String role_id , ArrayList<Res_Student_Marks> datasaved){
        String dsaved = "";
        for(int i = 0; i < datasaved.size(); i++){
            dsaved += datasaved.get(i).getStudentid() + "," + datasaved.get(i).getExamtype()+ "," + datasaved.get(i).getSubjectpart() + "," + datasaved.get(i).getSubject() + "," + datasaved.get(i).getTerm()+ "," + datasaved.get(i).getMarks() + "|";
        }
        getRetro_Call(activity, service.getPostSubjectMarks(user_id, role_id ,dsaved ),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse){

                        Snackbar.make(view, "Saved!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
    }


}