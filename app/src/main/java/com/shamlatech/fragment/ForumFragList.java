package com.shamlatech.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.shamlatech.activity.ForumViewActivity;
import com.shamlatech.adapter.ForumListAdapter;
import com.shamlatech.api_response.Res_Forum_List;
import com.shamlatech.api_response.Result_Forum_List;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.pojo.PojoParentWithClassList;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.ShowError;
import static com.shamlatech.utils.Vars.ForumTypeT_P;
import static com.shamlatech.utils.Vars.ForumTypeT_T;
import static com.shamlatech.utils.Vars.Refresh_Forum_T_P;
import static com.shamlatech.utils.Vars.Refresh_Forum_T_T;
import static com.shamlatech.utils.Vars.Role_Parent;
import static com.shamlatech.utils.Vars.Role_Principal;
import static com.shamlatech.utils.Vars.Role_Teacher;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Dharmalingam Sekar on 09-05-2018.
 */

@SuppressLint("ValidFragment")
public class ForumFragList extends Fragment implements View.OnClickListener {

    View view;

    Spinner spinnerFilter;
    RecyclerView recyclerForum;
    FloatingActionButton fabCreateTopic;
    ForumListAdapter mAdapter;
    ArrayList<Res_Forum_List> listForums = new ArrayList<>();
    String statusFilter = "1";
    Dialog pick_Dialog;

    String[] strFilter = {"All Forums", "Open Forums"};

    RelativeLayout relative_List, progress_LoadMore, relative_No_List;
    SwipeRefreshLayout swipeRefreshLayout;

    int pastVisibleItems, visibleItemCount, totalItemCount;
    String StudId = "0";
    String FFrom = "", FTo = "", FType = "";
    DBAdapter db;
    ArrayAdapter<PojoParentWithClassList> adapterViewers;
    ArrayList<PojoParentWithClassList> listClass;
    Activity activity;
    private boolean ListLoading = false;
    private String last_ride_id = "1";

    public ForumFragList(String from, String to, String type, String studId) {
        FFrom = from;
        FTo = to;
        FType = type;
        StudId = studId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_forums_list, container, false);

        listForums = new ArrayList<>();

        try {
            db = new DBAdapter(activity, activity.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }

        recyclerForum = view.findViewById(R.id.recyclerForum);
        relative_List = view.findViewById(R.id.relative_List);
        progress_LoadMore = view.findViewById(R.id.progress_LoadMore);
        relative_No_List = view.findViewById(R.id.relative_No_List);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        spinnerFilter = view.findViewById(R.id.spinnerFilter);
        fabCreateTopic = view.findViewById(R.id.fabCreateTopic);

        ArrayAdapter adapterFilter = new ArrayAdapter(activity, R.layout.simple_spinner_item, strFilter);
        adapterFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapterFilter);


        mAdapter = new ForumListAdapter(activity, listForums, this);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerForum.setLayoutManager(mLayoutManager);
        recyclerForum.setItemAnimator(new DefaultItemAnimator());
        recyclerForum.setAdapter(mAdapter);

        fabCreateTopic.setOnClickListener(this);

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusFilter = position + "";
                loadPage("-1");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPage("-1");
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerForum.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (ListLoading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            last_ride_id = listForums.get(listForums.size() - 1).getId();
                            ListLoading = false;
                            progress_LoadMore.setVisibility(View.VISIBLE);
                            loadPage(last_ride_id);
                        }
                    }
                }
            }
        });

        if (staUserInfo.getRole().equals(Role_Teacher) || staUserInfo.getRole().equals(Role_Principal)) {
            fabCreateTopic.setVisibility(View.VISIBLE);
        } else
            fabCreateTopic.setVisibility(View.GONE);

        loadPage("-1");
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (FType.equals(ForumTypeT_T)) {
            if (Refresh_Forum_T_T.equals("1")) {
                loadPage("-1");
            }
        }
        if (FType.equals(ForumTypeT_P)) {
            if (Refresh_Forum_T_P.equals("1")) {
                loadPage("-1");
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public void loadPage(String last_Id) {
        if (last_Id == "-1") {
            listForums = new ArrayList<>();
        }
        getRetro_AccessForums(staUserInfo.getId(), staUserInfo.getRole(), FType, StudId, statusFilter, last_Id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabCreateTopic:
                showCreateForum();
                break;
        }
    }

    private void showCreateForum() {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(activity);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_create_forum);

            final EditText edtTopic;
            RelativeLayout relativeSend;
            final Spinner spinnerViewers;
            RelativeLayout relativeTo;

            edtTopic = pick_Dialog.findViewById(R.id.edtTopic);
            relativeSend = pick_Dialog.findViewById(R.id.relativeSend);
            spinnerViewers = pick_Dialog.findViewById(R.id.spinnerViewers);
            relativeTo = pick_Dialog.findViewById(R.id.relativeTo);

            if ((FFrom.equals(Role_Teacher) || FFrom.equals(Role_Principal)) && FTo.equals(Role_Parent)) {
                relativeTo.setVisibility(View.VISIBLE);
                listClass = db.AccessTeacherClassList();
                adapterViewers = new ArrayAdapter<PojoParentWithClassList>(activity, R.layout.simple_spinner_item, listClass);
                adapterViewers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerViewers.setAdapter(adapterViewers);
            } else {
                relativeTo.setVisibility(View.GONE);
            }

            relativeSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strTopic = edtTopic.getText().toString().trim();
                    String class_id = "0", section_id = "0";
                    if ((FFrom.equals(Role_Teacher) || FFrom.equals(Role_Principal)) && FTo.equals(Role_Parent)) {
                        if (listClass.size() > 0) {
                            class_id = listClass.get(spinnerViewers.getSelectedItemPosition()).getClass_id();
                            section_id = listClass.get(spinnerViewers.getSelectedItemPosition()).getSection_id();
                        }
                    }

                    if ((FFrom.equals(Role_Teacher) || FFrom.equals(Role_Principal)) && FTo.equals(Role_Parent) && listClass.size() == 0) {
                        ShowError(activity, "Sorry", "You could not post forum to any parent at this time");
                        pick_Dialog.dismiss();
                    } else if (strTopic.equals("")) {
                        Toast.makeText(activity, "Please enter the valid topic", Toast.LENGTH_LONG).show();
                    } else {
                        pick_Dialog.dismiss();
                        getRetro_CreateForums(staUserInfo.getId(), staUserInfo.getRole(), strTopic, FFrom, FTo, FType, class_id, section_id, StudId);
                    }
                }
            });

            pick_Dialog.show();
        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showCreateForum();
        } else {
            pick_Dialog = null;
            showCreateForum();
        }
    }

    public void onClickForum(Res_Forum_List forum) {
        startActivity(new Intent(activity, ForumViewActivity.class).putExtra("forum_info", forum));
    }

    public void updateList(ArrayList<Res_Forum_List> forum) {
        boolean NewList = true;
        if (listForums.size() > 0) {
            NewList = false;
        }
        progress_LoadMore.setVisibility(View.GONE);
        if (forum.size() >= 20)
            ListLoading = true;
        else {
            ListLoading = false;
        }
        ArrayList<Res_Forum_List> TempNearBy = new ArrayList();
        TempNearBy.addAll(listForums);
        listForums.clear();
        for (int i = 0; i < forum.size(); i++) {
            boolean Exist = false;
            for (int j = 0; j < TempNearBy.size(); j++) {
                if (forum.get(i).getId().equals(TempNearBy.get(j).getId())) {
                    TempNearBy.set(j, forum.get(i));
                    Exist = true;
                }
            }
            if (!Exist) {
                TempNearBy.add(forum.get(i));
            }
        }
        listForums.addAll(TempNearBy);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (NewList) {
            mAdapter = new ForumListAdapter(activity, listForums, this);
            recyclerForum.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (listForums.size() > 0) {
            relative_No_List.setVisibility(View.GONE);
        } else {
            relative_No_List.setVisibility(View.VISIBLE);
        }
    }


    public void getRetro_AccessForums(String user_id, String role_id, String type, String studId, String status, String last_id) {
        getRetro_Call(activity, service.getAccessForumsList(user_id, role_id, type, studId, status, last_id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Forum_List mPojo_Forum = onPojoBuilder(objectResponse, Result_Forum_List.class);
                        if (mPojo_Forum != null) {
                            updateList(mPojo_Forum.getForums_list());
                            if (FType.equals(ForumTypeT_T)) {
                                Refresh_Forum_T_T = "0";
                            }
                            if (FType.equals(ForumTypeT_P)) {
                                Refresh_Forum_T_P = "0";
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public void getRetro_CreateForums(String user_id, String role_id, String topic, String from, String to, String type, String class_id, String section_id, String stud_id) {
        getRetro_Call(activity, service.getCreateForumsList(user_id, role_id, topic, from, to, type, class_id, section_id, stud_id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Forum_List mPojo_Forum = onPojoBuilder(objectResponse, Result_Forum_List.class);
                        if (mPojo_Forum != null) {
                            startActivity(new Intent(activity, ForumViewActivity.class).putExtra("forum_info", mPojo_Forum.getForums_list().get(0)));
                            Refresh_Forum_T_T = "1";
                            Refresh_Forum_T_P = "1";
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

}