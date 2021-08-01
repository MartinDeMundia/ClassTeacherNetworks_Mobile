package com.shamlatech.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shamlatech.activity.ViewVideoActivity;
import com.shamlatech.activity.teacher.AddDocumentActivity;
import com.shamlatech.adapter.VideoListAdapter;
import com.shamlatech.api_response.Res_Media;
import com.shamlatech.api_response.Result_Media;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.Refresh_Video;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Dharmalingam Sekar on 09-05-2018.
 */

public class SMediaVideoFrag extends Fragment implements View.OnClickListener {

    View view;
    TextView txtNoDocument;
    RecyclerView recyclerAttachment;
    VideoListAdapter mAdapter;
    ArrayList<Res_Media> listVideos = new ArrayList<>();
    FloatingActionButton fabAddDocument;
    RelativeLayout progress_LoadMore;
    SwipeRefreshLayout swipeRefreshLayout;

    int pastVisibleItems, visibleItemCount, totalItemCount;
    Activity activity;
    private boolean ListLoading = false;
    private String last_id = "1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_attachment, container, false);


        listVideos = new ArrayList<>();

        txtNoDocument = view.findViewById(R.id.txtNoDocument);
        recyclerAttachment = view.findViewById(R.id.recyclerAttachment);
        fabAddDocument = view.findViewById(R.id.fabAddDocument);
        progress_LoadMore = view.findViewById(R.id.progress_LoadMore);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        fabAddDocument.setOnClickListener(this);

        txtNoDocument.setText("No Videos");

        mAdapter = new VideoListAdapter(activity, listVideos, "Teacher", this);
        final GridLayoutManager mLinearLayout = new GridLayoutManager(activity, 3);
        recyclerAttachment.setLayoutManager(mLinearLayout);
        recyclerAttachment.setItemAnimator(new DefaultItemAnimator());
        recyclerAttachment.setAdapter(mAdapter);

        preparePage();

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

        recyclerAttachment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLinearLayout.getChildCount();
                    totalItemCount = mLinearLayout.getItemCount();
                    pastVisibleItems = mLinearLayout.findFirstVisibleItemPosition();

                    if (ListLoading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            try {
                                last_id = listVideos.get(listVideos.size() - 1).getId();
                                ListLoading = false;
                                progress_LoadMore.setVisibility(View.VISIBLE);
                                loadPage(last_id);
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            }
        });

        loadPage("-1");

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Refresh_Video.equals("1")) {
            loadPage("-1");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && activity != null) {
            if (Refresh_Video.equals("1")) {
                loadPage("-1");
            }
        }
    }

    public void loadPage(String last_Id) {
        if (last_Id == "-1") {
            listVideos = new ArrayList<>();
        }
        GetRetro_AccessVideoList(last_Id);
    }

    private void preparePage() {
        if (staUserInfo.getRole().equals(Vars.Role_Teacher) || staUserInfo.getRole().equals(Vars.Role_Principal)) {
            fabAddDocument.setVisibility(View.VISIBLE);
        } else {
            fabAddDocument.setVisibility(View.GONE);
        }
    }

    public void onVideoClicked(int position) {
        Intent intent = new Intent(activity, ViewVideoActivity.class);
        intent.putExtra("video_document", listVideos.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddDocument:
                startActivity(new Intent(activity, AddDocumentActivity.class));
                break;
        }
    }


    public void updateList(ArrayList<Res_Media> videos) {
        boolean NewList = true;
        if (listVideos.size() > 0) {
            NewList = false;
        }
        progress_LoadMore.setVisibility(View.GONE);
        if (videos.size() >= 10)
            ListLoading = true;
        else {
            ListLoading = false;
        }
        ArrayList<Res_Media> TempNearBy = new ArrayList();
        TempNearBy.addAll(listVideos);
        listVideos.clear();
        for (int i = 0; i < videos.size(); i++) {
            boolean Exist = false;
            for (int j = 0; j < TempNearBy.size(); j++) {
                if (videos.get(i).getId().equals(TempNearBy.get(j).getId())) {
                    TempNearBy.set(j, videos.get(i));
                    Exist = true;
                }
            }
            if (!Exist) {
                TempNearBy.add(videos.get(i));
            }
        }
        listVideos.addAll(TempNearBy);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (NewList) {
            mAdapter = new VideoListAdapter(activity, listVideos, "Teacher", this);
            recyclerAttachment.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (listVideos.size() > 0) {
            txtNoDocument.setVisibility(View.GONE);
        } else {
            txtNoDocument.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }


    public void GetRetro_AccessVideoList(String last_Id) {
        getRetro_Call(activity, service.getAccessVideos(staUserInfo.getId(), staUserInfo.getRole(), last_Id), false, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Media mPojo = onPojoBuilder(objectResponse, Result_Media.class);
                if (mPojo.getStatus().equals("1")) {
                    updateList(mPojo.getVideo());
                    Refresh_Video = "0";
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}