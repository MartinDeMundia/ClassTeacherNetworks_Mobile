package com.shamlatech.fragment.teacher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shamlatech.adapter.MyClassListAdapter;
import com.shamlatech.api_response.Res_Teacher_Class;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.school_management.R;

import java.util.ArrayList;
import java.util.Arrays;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Martin Mundia Mugambi on 23-10-2019.
 */

@SuppressLint("ValidFragment")
public class FilterByClassFragment extends Fragment {

    View view;
    RecyclerView recyclerClasses;
    ArrayList<Res_Teacher_Class> listClasses = new ArrayList<>();
    MyClassListAdapter mAdapter;
    DBAdapter db;
    Activity activity;
    private final int mPosition;

    @SuppressLint("ValidFragment")
    public FilterByClassFragment(int position) {
        mPosition = position;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_filter_by_class, container, false);

        try {
            db = new DBAdapter(activity, activity.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }

        recyclerClasses = view.findViewById(R.id.recyclerClasses);
        listClasses = new ArrayList<>();
        prepareDateForPage();

        mAdapter = new MyClassListAdapter(activity, listClasses, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerClasses.setLayoutManager(mLayoutManager);
        recyclerClasses.setItemAnimator(new DefaultItemAnimator());
        recyclerClasses.setAdapter(mAdapter);
   /*     Toast.makeText(
                this.getActivity(),
                "Position : "+mPosition,
                Toast.LENGTH_SHORT
        ).show();*/
        PerformFilter(listClasses.get(mPosition).getClass_id());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    private void prepareDateForPage() {
        listClasses = db.AccessTeachersClass();

    }

    public void PerformFilter(String class_id) {
        ArrayList<Res_Teacher_Class> tempList = db.AccessTeachersClass();
        listClasses = new ArrayList<>();

        for (int i = 0; i < tempList.size(); i++) {
            if (class_id.equals("0")) {
                listClasses.add(tempList.get(i));
            } else {
                String[] teachingClass = tempList.get(i).getClass_id().split(",");
                if (Arrays.asList(teachingClass).contains(class_id)) {
                    listClasses.add(tempList.get(i));
                }
            }
        }
        mAdapter = new MyClassListAdapter(activity, listClasses, this);
        recyclerClasses.setAdapter(mAdapter);
    }

}