package com.shamlatech.fragment.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shamlatech.activity.teacher.GroupMakerAddEditActivity;
import com.shamlatech.adapter.GroupMakerAdapter;
import com.shamlatech.api_response.Res_Group;
import com.shamlatech.api_response.Result_Media;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Dharmalingam Sekar on 08-05-2018.
 */

public class GroupMakerFragment extends Fragment implements View.OnClickListener {

    View view;
    GroupMakerAdapter mAdapter;
    RecyclerView recyclerGroupMaker;
    ArrayList<Res_Group> listGroupMaker = new ArrayList<>();
    FloatingActionButton fabAddGroup;
    TextView txtNoResult;
    Activity activity;
    DBAdapter db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_group_maker, container, false);

        try {
            db = new DBAdapter(activity, activity.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }

        txtNoResult = view.findViewById(R.id.txtNoResult);
        recyclerGroupMaker = view.findViewById(R.id.recyclerGroupMaker);
        fabAddGroup = view.findViewById(R.id.fabAddGroup);
        listGroupMaker = new ArrayList<>();

        listGroupMaker = db.AccessGroup();

        mAdapter = new GroupMakerAdapter(activity, listGroupMaker, GroupMakerFragment.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerGroupMaker.setLayoutManager(mLayoutManager);
        recyclerGroupMaker.setItemAnimator(new DefaultItemAnimator());
        recyclerGroupMaker.setAdapter(mAdapter);

        fabAddGroup.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        listGroupMaker = db.AccessGroup();
        mAdapter = new GroupMakerAdapter(activity, listGroupMaker, GroupMakerFragment.this);
        recyclerGroupMaker.setAdapter(mAdapter);
        validateGroupMakerList();
    }

    public void validateGroupMakerList() {
        if (listGroupMaker.size() == 0) {
            txtNoResult.setVisibility(View.VISIBLE);
        } else {
            txtNoResult.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddGroup:
                startActivity(new Intent(activity, GroupMakerAddEditActivity.class));
                break;
        }
    }

    public void onGroupMakerListClick(int position) {
        startActivity(new Intent(activity, GroupMakerAddEditActivity.class));
    }

    public void onGroupMakerViewClick(int position) {
    }

    public void onGroupMakerEditClick(int position) {
    }

    public void onGroupMakerDeleteClick(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("Are you sure, want to delete this?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                        GetRetro_RemoveDocument(listGroupMaker.get(position).getId(), position);
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void GetRetro_RemoveDocument(final String id, final int position) {
        getRetro_Call(activity, service.getRemoveGroup(staUserInfo.getId(), staUserInfo.getRole(), id), false, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Media mPojo = onPojoBuilder(objectResponse, Result_Media.class);
                if (mPojo.getStatus().equals("1")) {
                    db.DeleteGroup(id);
                    listGroupMaker.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

}
