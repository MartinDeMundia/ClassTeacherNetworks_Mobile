package com.shamlatech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.shamlatech.adapter.AnnouncementAdapter;
import com.shamlatech.api_response.Res_Announcement;
import com.shamlatech.api_response.Result_Announcement;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.SimpleDividerItemDecoration;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.staUserInfo;

public class AnnouncementListActivity extends BaseActivity {

    RecyclerView recyclerAnnouncement;
    private AnnouncementAdapter mAdapter;
    ArrayList<Res_Announcement> listAnnouncement = new ArrayList<>();
    RelativeLayout relative_List, progress_LoadMore, relative_No_List;
    SwipeRefreshLayout swipeRefreshLayout;


    int pastVisibleItems, visibleItemCount, totalItemCount;
    private boolean ListLoading = false;
    private String last_ride_id = "1";
    String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_list);

        listAnnouncement = new ArrayList<>();
        declareAppBar5("Announcement", "AnnouncementListActivity");
        declareBottomBar();


        relative_List = findViewById(R.id.relative_List);
        progress_LoadMore = findViewById(R.id.progress_LoadMore);
        relative_No_List = findViewById(R.id.relative_No_List);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerAnnouncement = findViewById(R.id.recyclerAnnouncement);

        mAdapter = new AnnouncementAdapter(this, listAnnouncement, "Announcement");
        final LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);
        recyclerAnnouncement.setLayoutManager(mLinearLayout);
        recyclerAnnouncement.setItemAnimator(new DefaultItemAnimator());
        recyclerAnnouncement.addItemDecoration(new SimpleDividerItemDecoration(this, R.drawable.line_divider_1));
        recyclerAnnouncement.setAdapter(mAdapter);

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

        recyclerAnnouncement.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLinearLayout.getChildCount();
                    totalItemCount = mLinearLayout.getItemCount();
                    pastVisibleItems = mLinearLayout.findFirstVisibleItemPosition();

                    if (ListLoading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            last_ride_id = listAnnouncement.get(listAnnouncement.size() - 1).getId();
                            ListLoading = false;
                            progress_LoadMore.setVisibility(View.VISIBLE);
                            loadPage(last_ride_id);
                        }
                    }
                }
            }
        });

        loadPage("-1");


    }

    public void loadPage(String last_Id) {
        if (last_Id == "-1") {
            listAnnouncement = new ArrayList<>();
        }
        getRetro_AccessAnnouncement(staUserInfo.getId(), staUserInfo.getRole(), "0", last_Id, "", "");
    }


    public void onAnnouncementClick(int position) {
        startActivity(new Intent(AnnouncementListActivity.this, AnnouncementViewActivity.class).putExtra("announcement_info", listAnnouncement.get(position)));
    }

    public void getRetro_AccessAnnouncement(String user_id, String role_id, String status, String last_id, String date_from, String date_to) {
        getRetro_Call(AnnouncementListActivity.this, service.getAccessAnnouncement(
                user_id, role_id, status, last_id, date_from, date_to),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Announcement mPojo_Announcement = onPojoBuilder(objectResponse, Result_Announcement.class);
                        if (mPojo_Announcement != null) {
                            if (mPojo_Announcement.getStatus().equals(API_Code.Success)) {
                                updateList(mPojo_Announcement.getAnnouncement());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }


    public void updateList(ArrayList<Res_Announcement> announcement) {
        boolean NewList = true;
        if (listAnnouncement.size() > 0) {
            NewList = false;
        }
        progress_LoadMore.setVisibility(View.GONE);
        if (announcement.size() >= 10)
            ListLoading = true;
        else {
            ListLoading = false;
        }
        ArrayList<Res_Announcement> TempNearBy = new ArrayList();
        TempNearBy.addAll(listAnnouncement);
        listAnnouncement.clear();
        for (int i = 0; i < announcement.size(); i++) {
            boolean Exist = false;
            for (int j = 0; j < TempNearBy.size(); j++) {
                if (announcement.get(i).getId().equals(TempNearBy.get(j).getId())) {
                    TempNearBy.set(j, announcement.get(i));
                    Exist = true;
                }
            }
            if (!Exist) {
                TempNearBy.add(announcement.get(i));
            }
        }
        listAnnouncement.addAll(TempNearBy);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (NewList) {
            mAdapter = new AnnouncementAdapter(this, listAnnouncement, "Announcement");
            recyclerAnnouncement.setAdapter(mAdapter);
        } else {
            recyclerAnnouncement.setAdapter(mAdapter);
        }
        if (listAnnouncement.size() > 0) {
            relative_No_List.setVisibility(View.GONE);
        } else {
            relative_No_List.setVisibility(View.VISIBLE);
        }
    }

}


