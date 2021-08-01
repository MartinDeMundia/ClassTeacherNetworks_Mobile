package com.shamlatech.activity.teacher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import com.shamlatech.adapter.MyClassListAdapter;
import com.shamlatech.adapter.MyClassListFolderAdapter;
import com.shamlatech.api_response.Res_Teacher_Class;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.school_management.BaseFragment;
import com.shamlatech.school_management.HomeActivity;
import com.shamlatech.school_management.R;

import java.util.ArrayList;

public class MyStudentFolderActivity extends BaseFragment {
    Activity activity;
    RecyclerView recyclerClasses;
    ArrayList<Res_Teacher_Class> listClasses = new ArrayList<>();
    MyClassListFolderAdapter mAdapter;
    private  String schoolname ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_student_folders, container, false);

        try {
            db = new DBAdapter(this.getActivity(), this.getActivity().getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }

        recyclerClasses = view.findViewById(R.id.recyclerClasses);
        listClasses = new ArrayList<>();
        prepareDateForPage();

        mAdapter = new MyClassListFolderAdapter(this.getActivity(), listClasses, this);
       // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
       // RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getActivity(),2);



        RecyclerView.LayoutManager mLayoutManager= new GridLayoutManager(this.getActivity(),2,LinearLayoutManager.VERTICAL,false);
        int spanCount = 2; // 3 columns
        int spacing = 50; // 50px
        boolean includeEdge = false;
        recyclerClasses.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        recyclerClasses.setLayoutManager(mLayoutManager);
        recyclerClasses.setItemAnimator(new DefaultItemAnimator());
        recyclerClasses.setAdapter(mAdapter);

        return view;
    }
    private void prepareDateForPage() {
        listClasses = db.AccessTeachersClass();
        //schoolname = listClasses.getStu
    }
    public  void onClickShowClass(int position) {
        ((HomeActivity) this.getActivity()).UpdateTeacherActionFragment(new MyStudentActivity(position), "");

    }

}
