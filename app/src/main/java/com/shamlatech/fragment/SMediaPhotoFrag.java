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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shamlatech.activity.ViewImageActivity;
import com.shamlatech.activity.teacher.AddDocumentActivity;
import com.shamlatech.adapter.PhotoListAdapter;
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
import static com.shamlatech.utils.Vars.Refresh_Photo;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Dharmalingam Sekar on 09-05-2018.
 */

public class SMediaPhotoFrag extends Fragment implements View.OnClickListener {

    View view;
    TextView txtNoDocument;
    RecyclerView recyclerAttachment;
    PhotoListAdapter mAdapter;
    ArrayList<Res_Media> listPhotos = new ArrayList<>();
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

        listPhotos = new ArrayList<>();

        txtNoDocument = view.findViewById(R.id.txtNoDocument);
        recyclerAttachment = view.findViewById(R.id.recyclerAttachment);
        fabAddDocument = view.findViewById(R.id.fabAddDocument);
        progress_LoadMore = view.findViewById(R.id.progress_LoadMore);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        fabAddDocument.setOnClickListener(this);

        txtNoDocument.setText("No Photos");

        mAdapter = new PhotoListAdapter(activity, listPhotos, "Teacher", this);
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
                                last_id = listPhotos.get(listPhotos.size() - 1).getId();
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
        if (activity != null) {
            if (Refresh_Photo.equals("1")) {
                loadPage("-1");
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && activity != null) {
            if (Refresh_Photo.equals("1")) {
                loadPage("-1");
            }
        }
    }

    public void loadPage(String last_Id) {
        if (last_Id == "-1") {
            listPhotos = new ArrayList<>();
        }
        GetRetro_AccessPhotoList(last_Id);
    }

    private void preparePage() {
        if (staUserInfo.getRole().equals(Vars.Role_Teacher)||staUserInfo.getRole().equals(Vars.Role_Principal)) {
            fabAddDocument.setVisibility(View.VISIBLE);
        } else {
            fabAddDocument.setVisibility(View.GONE);
        }
    }

    public void onPhotoClicked(int position, ImageView img) {
        Intent intent = new Intent(activity, ViewImageActivity.class);
        intent.putExtra("photo_document", listPhotos.get(position));
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


    public void updateList(ArrayList<Res_Media> photos) {
        boolean NewList = true;
        if (listPhotos.size() > 0) {
            NewList = false;
        }
        progress_LoadMore.setVisibility(View.GONE);
        if (photos.size() >= 10)
            ListLoading = true;
        else {
            ListLoading = false;
        }
        ArrayList<Res_Media> TempNearBy = new ArrayList();
        TempNearBy.addAll(listPhotos);
        listPhotos.clear();
        for (int i = 0; i < photos.size(); i++) {
            boolean Exist = false;
            for (int j = 0; j < TempNearBy.size(); j++) {
                if (photos.get(i).getId().equals(TempNearBy.get(j).getId())) {
                    TempNearBy.set(j, photos.get(i));
                    Exist = true;
                }
            }
            if (!Exist) {
                TempNearBy.add(photos.get(i));
            }
        }
        listPhotos.addAll(TempNearBy);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (NewList) {
            mAdapter = new PhotoListAdapter(activity, listPhotos, "Teacher", this);
            recyclerAttachment.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (listPhotos.size() > 0) {
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


    public void GetRetro_AccessPhotoList(String last_Id) {
        getRetro_Call(activity, service.getAccessPhotos(staUserInfo.getId(), staUserInfo.getRole(), last_Id), false, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Media mPojo = onPojoBuilder(objectResponse, Result_Media.class);
                if (mPojo.getStatus().equals("1")) {
                    updateList(mPojo.getImage());
                    Refresh_Photo = "0";
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}