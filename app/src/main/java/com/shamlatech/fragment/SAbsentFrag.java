package com.shamlatech.fragment;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.shamlatech.activity.ForumViewActivity;
import com.shamlatech.adapter.AbsentNoteAdapter;
import com.shamlatech.api_response.Res_Absnote_List;
import com.shamlatech.api_response.Result_AbsentNote_List;
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
 * Created by Martin Mundia on 27-AUG-2019.
 */
public class SAbsentFrag extends Fragment implements View.OnClickListener {

    View view;
    String StudId = "0";
    Activity activity;
    ArrayList<Res_Absnote_List> listAbsentNotes = new ArrayList<>();
    DBAdapter db;
    RecyclerView recyclerAbsentNote;
    AbsentNoteAdapter mAdapter;
    FloatingActionButton fabsentNote;
    Dialog pick_Dialog;
    String FFrom = "", FTo = "", FType = "";
    ArrayAdapter<PojoParentWithClassList> adapterViewers;
    ArrayList<PojoParentWithClassList> listClass;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout  progress_LoadMore;
    private boolean ListLoading = false;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    private String last_ride_id = "1";
    String statusFilter = "1";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.frag_absentnote_list, container, false);
        listAbsentNotes = new ArrayList<>();
        try {
            db = new DBAdapter(activity, activity.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }
        recyclerAbsentNote = view.findViewById(R.id.recyclerForum);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        progress_LoadMore = view.findViewById(R.id.progress_LoadMore);

        mAdapter = new AbsentNoteAdapter(activity, listAbsentNotes , this);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerAbsentNote.setLayoutManager(mLayoutManager);
        recyclerAbsentNote.setItemAnimator(new DefaultItemAnimator());
        recyclerAbsentNote.setAdapter(mAdapter);

        fabsentNote = view.findViewById(R.id.fabsentNote);
        fabsentNote.setOnClickListener(this);


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

        recyclerAbsentNote.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (ListLoading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            last_ride_id = listAbsentNotes.get(listAbsentNotes.size() - 1).getId();
                            ListLoading = false;
                            progress_LoadMore.setVisibility(View.VISIBLE);
                            loadPage(last_ride_id);
                        }
                    }
                }
            }
        });



        getRetro_AccessAbsentNotes(staUserInfo.getId(), staUserInfo.getRole(), FType, StudId, "true", "0");

        return view;
    }

    public void loadPage(String last_Id) {
        if (last_Id == "-1") {
            listAbsentNotes = new ArrayList<>();
        }
        getRetro_AccessAbsentNotes(staUserInfo.getId(), staUserInfo.getRole(), FType, StudId, statusFilter, last_Id);
    }

    public void getRetro_AccessAbsentNotes(String user_id, String role_id, String type, String studId, String status, String last_id) {
        getRetro_Call(activity, service.getAccessAbsentNoteList(user_id, role_id, type, studId, status, last_id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_AbsentNote_List mPojo_AbsentNotes = onPojoBuilder(objectResponse, Result_AbsentNote_List.class);
                        if (mPojo_AbsentNotes != null) {
                            updateList(mPojo_AbsentNotes.getAnote_list());
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


    public void updateList(ArrayList<Res_Absnote_List> forum) {
        boolean NewList = true;
        if (listAbsentNotes.size() > 0) {
            NewList = false;
        }
        progress_LoadMore.setVisibility(View.GONE);
        if (forum.size() >= 20)
            ListLoading = true;
        else {
            ListLoading = false;
        }
        ArrayList<Res_Absnote_List> TempNearBy = new ArrayList();
        TempNearBy.addAll(listAbsentNotes);
        listAbsentNotes.clear();
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
        listAbsentNotes.addAll(TempNearBy);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (NewList) {
            mAdapter = new AbsentNoteAdapter(activity, listAbsentNotes, this);
            recyclerAbsentNote.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public void prepareBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("stud_id")) {
                StudId = bundle.getString("stud_id");
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabsentNote:
                showCreateAbsentNote();
                break;
        }
    }
    private void showCreateAbsentNote() {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(activity);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_create_absentnote);

            final EditText edtTopic;
            final DatePicker edtAdatePick;
            RelativeLayout relativeSend;
            final Spinner spinnerViewers;
            RelativeLayout relativeTo;

            edtTopic = pick_Dialog.findViewById(R.id.edtTopic);
            edtAdatePick = pick_Dialog.findViewById(R.id.edtAdate);

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
                   // String edtAdate = edtAdatePick.getText().toString().trim();
                  //  String edtAdate =   edtAdatePick.getDayOfMonth()+"/"+ (edtAdatePick.getMonth() + 1)+"/"+edtAdatePick.getYear();
                    String edtAdate =   edtAdatePick.getYear()+ "-" + (edtAdatePick.getMonth() + 1) + "-"+ (edtAdatePick.getDayOfMonth());
                    String class_id = "0", section_id = "0";
                    if ((FFrom.equals(Role_Teacher) || FFrom.equals(Role_Principal)) && FTo.equals(Role_Parent)) {
                        if (listClass.size() > 0) {
                            class_id = listClass.get(spinnerViewers.getSelectedItemPosition()).getClass_id();
                            section_id = listClass.get(spinnerViewers.getSelectedItemPosition()).getSection_id();
                        }
                    }
                    if ((FFrom.equals(Role_Teacher) || FFrom.equals(Role_Principal)) && FTo.equals(Role_Parent) && listClass.size() == 0) {
                        pick_Dialog.dismiss();
                    } else if (strTopic.equals("")) {
                        Toast.makeText(activity, "Please enter the valid absent note", Toast.LENGTH_LONG).show();
                    } else {
                        pick_Dialog.dismiss();
                        getRetro_CreateAbsentNote(staUserInfo.getId(), staUserInfo.getRole(), strTopic, edtAdate, FTo, FType, class_id, section_id, StudId);
                    }
                }
            });

            pick_Dialog.show();
        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showCreateAbsentNote();
        } else {
            pick_Dialog = null;
            showCreateAbsentNote();
        }
    }

    public void getRetro_CreateAbsentNote(String user_id, String role_id, String topic, String adate, String to, String type, String class_id, String section_id, String stud_id) {
        getRetro_Call(activity, service.getCreateAbsentNoteList(user_id, role_id, topic, adate, to, type, class_id, section_id, stud_id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Toast.makeText(activity, "Created Note!", Toast.LENGTH_LONG).show();
                        Result_AbsentNote_List mPojo_AbsentNotes = onPojoBuilder(objectResponse, Result_AbsentNote_List.class);
                        if (mPojo_AbsentNotes != null) {
                            updateList(mPojo_AbsentNotes.getAnote_list());
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