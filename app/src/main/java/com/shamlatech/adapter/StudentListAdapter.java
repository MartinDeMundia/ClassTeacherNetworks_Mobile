package com.shamlatech.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.shamlatech.activity.teacher.StudentInfoActivity;
import com.shamlatech.api_response.Res_Teacher_ClassStudent;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.school_management.HomeActivity;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Martin Mundia M on 24-OCT-2019.
 */

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder> {

    Activity act;
    Context mContext;
    private ArrayList<Res_Teacher_ClassStudent> listStudent;
    DBAdapter db;


    public StudentListAdapter(Activity act, ArrayList<Res_Teacher_ClassStudent> listStudent) {
        this.act = act;
        this.listStudent = listStudent;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_singleline_show_admno, parent, false);
        mContext = parent.getContext();
        try {
            db = new DBAdapter(act, act.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Res_Teacher_ClassStudent list = listStudent.get(position);
        holder.txtList.setText(list.getFirst_name() + " " + list.getMiddle_name() + " " + list.getLast_name());
        holder.txtNo.setText(String.valueOf( position + 1));
        holder.txtNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) act).UpdateStudentFragment(new StudentInfoActivity(), "", list.getId(), "");
            }
        });
        holder.txtAdmNo.setText(list.getId());
        holder.txtAdmNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) act).UpdateStudentFragment(new StudentInfoActivity(), "", list.getId(), "");
            }
        });

        holder.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ((HomeActivity) act).UpdateStudentFragment(new StudentInfoActivity(), "", list.getId(), "");
            }
        });
        holder.txtMarks.setText(list.getMarks());



        holder.txtMarks.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                Spinner spinnerSubject , spinnerTerm , spinnerExam ,spinnerSubsubjects;
                spinnerTerm =  act.findViewById( R.id.spinnerTerm);
                spinnerExam =  act.findViewById( R.id.spinnerExam);
                spinnerSubject =  act.findViewById( R.id.spinnerSubject);

                spinnerSubsubjects =  act.findViewById( R.id.spinnerSubsubjects);

                String textexam = spinnerExam.getSelectedItem().toString();
                String textsubject = spinnerSubject.getSelectedItem().toString();
                String texterm = spinnerTerm.getSelectedItem().toString();

                String selectedSubsubj = "";
                if(spinnerSubsubjects.getSelectedItem() != null) {
                    selectedSubsubj = spinnerSubsubjects.getSelectedItem().toString();
                }

                String names =  holder.txtNames .getText().toString();
                String admno =  holder.txtAdmNo .getText().toString();
                String marks =  holder.txtMarks .getText().toString();

                String  uid = staUserInfo.getId();
                String  urole = staUserInfo.getRole();
                if(selectedSubsubj != ""){
                    db.saveStudentSubMarks(names,textexam,textsubject,texterm,admno,marks,uid,urole,selectedSubsubj);
                }else{
                    db.saveStudentMarks(names,textexam,textsubject,texterm,admno,marks,uid,urole);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listStudent.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView  txtNo , txtAdmNo , txtMarks , txtNames;
        EditText txtList;
        ImageView imageView3;

        public MyViewHolder(View view) {
            super(view);
            txtList = view.findViewById(R.id.txtComment);
            txtNo = view.findViewById(R.id.txtSubject);
            txtAdmNo = view.findViewById(R.id.txtItem);
            txtMarks = view.findViewById(R.id.txtTeacher);
            txtNames = view.findViewById(R.id.txtComment);
            imageView3 = view.findViewById(R.id.imageView3);
        }
    }

}









