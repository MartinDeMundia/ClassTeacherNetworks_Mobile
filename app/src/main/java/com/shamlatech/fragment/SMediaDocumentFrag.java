package com.shamlatech.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.activity.teacher.AddDocumentActivity;
import com.shamlatech.activity.teacher.AssignmentSolutionActivity;
import com.shamlatech.adapter.DocumentListAdapter;
import com.shamlatech.api_response.Res_Media;
import com.shamlatech.api_response.Result_Media;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.RuntimePermissionsFragment;
import com.shamlatech.utils.Vars;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.FormatDateTimeForShow;
import static com.shamlatech.utils.Support.downloadFile;
import static com.shamlatech.utils.Vars.Refresh_Document;
import static com.shamlatech.utils.Vars.Refresh_Video;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Martin Mundia Mugambi on 17-05-2018.
 */

public class SMediaDocumentFrag extends RuntimePermissionsFragment implements View.OnClickListener {

    public static int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 12345;
    View view;
    TextView txtNoDocument;
    RecyclerView recyclerAttachment;
    FloatingActionButton fabAddDocument;
    DocumentListAdapter mAdapter;
    ArrayList<Res_Media> listDocument = new ArrayList<>();

    RelativeLayout progress_LoadMore;
    SwipeRefreshLayout swipeRefreshLayout;

    int pastVisibleItems, visibleItemCount, totalItemCount;
    Activity activity;
    Dialog pick_Dialog;
    String strURL = "", strFileName = "";
    private boolean ListLoading = false;
    private String last_id = "1";
    String assignmentitle = "";
    String assignmentid = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_attachment, container, false);

        listDocument = new ArrayList<>();

        txtNoDocument = view.findViewById(R.id.txtNoDocument);
        recyclerAttachment = view.findViewById(R.id.recyclerAttachment);
        fabAddDocument = view.findViewById(R.id.fabAddDocument);
        progress_LoadMore = view.findViewById(R.id.progress_LoadMore);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        fabAddDocument.setOnClickListener(this);

        txtNoDocument.setText("No Document");

        final LinearLayoutManager mLinearLayout = new LinearLayoutManager(activity);
        mAdapter = new DocumentListAdapter(activity, listDocument, "Teacher", this);
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
                                last_id = listDocument.get(listDocument.size() - 1).getId();
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
        if (activity!=null) {
            if (Refresh_Document.equals("1")) {
                loadPage("-1");
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && activity!=null) {
            if (Refresh_Document.equals("1")) {
                loadPage("-1");
            }
        }
    }


    public void loadPage(String last_Id) {
        if (last_Id == "-1") {
            listDocument = new ArrayList<>();
        }
        GetRetro_AccessDocumentList(last_Id);
    }

    private void preparePage() {
        if (staUserInfo.getRole().equals(Vars.Role_Teacher)||staUserInfo.getRole().equals(Vars.Role_Principal)) {
            fabAddDocument.setVisibility(View.VISIBLE);
        } else {
            fabAddDocument.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddDocument:
                startActivity(new Intent(activity, AddDocumentActivity.class));
                break;
            case R.id.btnSubmitSolution:
                Intent intent = new Intent(activity, AssignmentSolutionActivity.class);
                intent.putExtra("assignmentitle", assignmentitle);
                intent.putExtra("assignmentid", assignmentid);
                startActivity(intent);
                break;

        }
    }

    public void updateList(ArrayList<Res_Media> document) {
        boolean NewList = true;
        if (listDocument.size() > 0) {
            NewList = false;
        }
        progress_LoadMore.setVisibility(View.GONE);
        if (document.size() >= 10)
            ListLoading = true;
        else {
            ListLoading = false;
        }
        ArrayList<Res_Media> TempNearBy = new ArrayList();
        TempNearBy.addAll(listDocument);
        listDocument.clear();
        for (int i = 0; i < document.size(); i++) {
            boolean Exist = false;
            for (int j = 0; j < TempNearBy.size(); j++) {
                if (document.get(i).getId().equals(TempNearBy.get(j).getId())) {
                    TempNearBy.set(j, document.get(i));
                    Exist = true;
                }
            }
            if (!Exist) {
                TempNearBy.add(document.get(i));
            }
        }
        listDocument.addAll(TempNearBy);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (NewList) {
            mAdapter = new DocumentListAdapter(activity, listDocument, "Teacher", this);
            recyclerAttachment.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (listDocument.size() > 0) {
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

    public void onDeleteClicked(final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("Are you sure, want to delete this?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                        GetRetro_RemoveDocument(listDocument.get(position).getId(), position);
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


    public void showViewDocument(final Res_Media list) {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(activity);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_attachment_document);

            TextView txtDocumentTitle = pick_Dialog.findViewById(R.id.txtDocumentTitle);
            TextView txtDocumentDetails = pick_Dialog.findViewById(R.id.txtDocumentDetails);
            TextView txtDocumentName = pick_Dialog.findViewById(R.id.txtDocumentName);
            TextView txtUploadedBy = pick_Dialog.findViewById(R.id.txtUploadedBy);
            TextView txtUploadedOn = pick_Dialog.findViewById(R.id.txtUploadedOn);
            TextView txtTo = pick_Dialog.findViewById(R.id.txtTo);

            Button submitSolution = pick_Dialog.findViewById(R.id.btnSubmitSolution);
            submitSolution.setOnClickListener(this);

            txtDocumentTitle.setText(list.getTitle());
            txtDocumentDetails.setText(list.getDetails());
            txtDocumentName.setText(Html.fromHtml("Attachment : <u>" + list.getFile_name() + "</u>"));
            txtUploadedBy.setText(list.getUploader_name());
            txtTo.setText("To :" + list.getClass_name() + list.getSection_name());

     /*       String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String dateAddedISO = sdf.format(list.getDate_time());*/

            txtUploadedOn.setText(list.getDate_time());
            //txtUploadedOn.setText(FormatDateTimeForShow(list.getDate_time(), Vars.DatePattern1, ""));
            assignmentitle = list.getTitle();
            assignmentid = list.getId();
            String[] assignmentSplit = assignmentitle.split(":");
            if(assignmentSplit[0].trim().equals("ASSIGNMENT")){
                submitSolution.setVisibility(View.VISIBLE);
            }else{
                submitSolution.setVisibility(View.GONE);
            }
            txtDocumentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    strURL = list.getUrl();
                    strFileName = list.getFile_name();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        SMediaDocumentFrag.super.requestAppPermissions(new
                                String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                    } else {
                        downloadFile(activity,strURL, strFileName);
                    }
                }
            });

            pick_Dialog.show();
        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showViewDocument(list);
        } else {
            pick_Dialog = null;
            showViewDocument(list);
        }
    }

    public void GetRetro_RemoveDocument(String id, final int position) {
        getRetro_Call(activity, service.getRemoveDocument(staUserInfo.getId(), staUserInfo.getRole(), id), false, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Media mPojo = onPojoBuilder(objectResponse, Result_Media.class);
                if (mPojo.getStatus().equals("1")) {
                    listDocument.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    public void GetRetro_AccessDocumentList(String last_Id) {
        getRetro_Call(activity, service.getAccessDocuments(staUserInfo.getId(), staUserInfo.getRole(), last_Id), false, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Media mPojo = onPojoBuilder(objectResponse, Result_Media.class);
                if (mPojo.getStatus().equals("1")) {
                    Refresh_Document = "0";
                    updateList(mPojo.getDocument());
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }


   /* public void downloadFile(String url, String fileName) {
        Toast.makeText(activity, "Download Started", Toast.LENGTH_LONG).show();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(fileName);
        request.setDescription("Downloading File...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }*/

    @Override
    public void onPermissionsGranted(int requestCode) {
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            downloadFile(activity,strURL, strFileName);

        }
    }

    @Override
    public void onPermissionsDenied(int requestCode) {

    }
}