package com.shamlatech.fragment.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.activity.teacher.ClassLayoutAddEditActivity;
import com.shamlatech.adapter.ClassPositionAdapter;
import com.shamlatech.adapter.MyClassListAdapter;
import com.shamlatech.adapter.StudentListAdapter;
import com.shamlatech.api_response.Res_Teacher_Class;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.pojo.PojoClassPosition;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;

import static com.shamlatech.utils.Support.ConvertToInteger;
import static com.shamlatech.utils.Support.PrepareClassLayout;
import static com.shamlatech.utils.Support.downloadFile;

/**
 * Created by Martin Mundia Mugambi on 13-11-2019.
 */

public class MyClassesFragment extends Fragment implements View.OnClickListener {


    LinearLayout linearShare, linearEmail, linearDownload;
    View view;
    Res_Teacher_Class MyClass = new Res_Teacher_Class();
    ArrayList<PojoClassPosition> listPosition = new ArrayList<>();
    StudentListAdapter mAdapter;
    ClassPositionAdapter mClassAdapter;
    RecyclerView recyclerStudent, recyclerClassLayout;
    ImageView imgMore;
    TextView txtClassName, txtTotalStudent, txtNoResult;
    DBAdapter db;
    RelativeLayout relativeMyClassLayout;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_my_classes, container, false);

        try {
            db = new DBAdapter(activity, activity.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }

        linearShare = view.findViewById(R.id.linearShare);
        linearEmail = view.findViewById(R.id.linearEmail);
        linearDownload = view.findViewById(R.id.linearDownload);

        txtNoResult = view.findViewById(R.id.txtNoResult);
        relativeMyClassLayout = view.findViewById(R.id.relativeMyClassLayout);
        txtClassName = view.findViewById(R.id.txtClassName);
        txtTotalStudent = view.findViewById(R.id.txtTotalStudent);
        recyclerStudent = view.findViewById(R.id.recyclerStudent);
        recyclerClassLayout = view.findViewById(R.id.recyclerClassLayout);
        imgMore = view.findViewById(R.id.imgMore);


        linearShare.setOnClickListener(this);
        linearEmail.setOnClickListener(this);
        linearDownload.setOnClickListener(this);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshLayout();
    }

    public void refreshLayout() {
        MyClass = db.AccessMyClass();

        if (MyClass != null) {
            txtNoResult.setVisibility(View.GONE);
            relativeMyClassLayout.setVisibility(View.VISIBLE);
            listPosition = PrepareClassLayout(MyClass);

            mAdapter = new StudentListAdapter(activity, MyClass.getStudent());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyclerStudent.setLayoutManager(mLayoutManager);
            recyclerStudent.setItemAnimator(new DefaultItemAnimator());
            recyclerStudent.addItemDecoration(new SimpleDividerItemDecoration(activity, R.drawable.line_divider_1));
            recyclerStudent.setAdapter(mAdapter);

            mClassAdapter = new ClassPositionAdapter(activity, listPosition,
                    "view", ConvertToInteger(MyClass.getDivides()), ConvertToInteger(MyClass.getColumn()),
                    MyClassesFragment.this);
            Integer spanCount;
            if(ConvertToInteger(MyClass.getColumn()) <= 0){
                spanCount = 4;
            }else{
                spanCount = ConvertToInteger(MyClass.getColumn());
            }

            final GridLayoutManager mLayoutManagerCategory = new GridLayoutManager(activity,spanCount);
            recyclerClassLayout.setLayoutManager(mLayoutManagerCategory);
            recyclerClassLayout.setItemAnimator(new DefaultItemAnimator());
            recyclerClassLayout.setAdapter(mClassAdapter);
            imgMore.setOnClickListener(this);
            preparePageData();
        } else {
            relativeMyClassLayout.setVisibility(View.GONE);
            txtNoResult.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public void preparePageData() {
        txtClassName.setText(MyClass.getClass_name() + MyClass.getSection_name());
        txtTotalStudent.setText(MyClass.getStudent().size() + " Student");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearShare:
                Toast.makeText(activity, "Share Clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.linearEmail:
                Toast.makeText(activity, "Email Clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.linearDownload:
                String clsdlink = "http://apps.classteacher.school/admin/layout/"+MyClass.getClass_id()+"/"+MyClass.getSection_id();
                if (clsdlink != null && !clsdlink.isEmpty())
                    downloadFile(activity,clsdlink);
                else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imgMore:
                startActivity(new Intent(activity, ClassLayoutAddEditActivity.class));
                break;
        }
    }

    public void onPlaceClicked(int position) {
        Toast.makeText(activity, listPosition.get(position).getName(), Toast.LENGTH_LONG).show();
    }
}