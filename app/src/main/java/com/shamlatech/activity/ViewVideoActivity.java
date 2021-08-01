package com.shamlatech.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;
import com.shamlatech.api_response.Res_Media;
import com.shamlatech.api_response.Result_Media;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.Vars;

import java.io.IOException;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.FormatDateTimeForShow;
import static com.shamlatech.utils.Vars.Role_Principal;
import static com.shamlatech.utils.Vars.Role_Teacher;
import static com.shamlatech.utils.Vars.staUserInfo;

public class ViewVideoActivity extends BaseActivity {

    public static int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 12345;
    TextView txtDocumentTitle, txtDocumentDetails, txtUploadedBy, txtUploadedOn, txtTo;
    Res_Media video_info;
    FullscreenVideoLayout videoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);

        declareAppBar7("Videos", "");

        txtDocumentTitle = findViewById(R.id.txtDocumentTitle);
        txtDocumentDetails = findViewById(R.id.txtDocumentDetails);
        txtTo = findViewById(R.id.txtTo);
        txtUploadedBy = findViewById(R.id.txtUploadedBy);
        txtUploadedOn = findViewById(R.id.txtUploadedOn);
        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);

        PreparePage();

    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            downloadFile(video_info.getUrl(), video_info.getFile_name());
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void PreparePage() {
        Bundle bundle = getIntent().getExtras();
        video_info = new Res_Media();
        if (bundle != null) {
            if (bundle.containsKey("video_document")) {
                video_info = (Res_Media) bundle.getSerializable("video_document");
                txtDocumentTitle.setText(video_info.getTitle());
                txtDocumentDetails.setText(video_info.getDetails());
                txtTo.setText("To :" + video_info.getClass_name() + video_info.getSection_name());
                txtUploadedBy.setText(video_info.getUploader_name());
                txtUploadedOn.setText(FormatDateTimeForShow(video_info.getDate_time(), Vars.DatePattern1, ""));
                Uri videoUri = Uri.parse(video_info.getUrl());
                try {
                    videoLayout.setVideoURI(videoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                preparePage();
            }
        }
    }

    private void preparePage() {
        if (staUserInfo.getId().equals(video_info.getUploader_id()) && staUserInfo.getRole().equals(Role_Teacher)) {
            imgAppRemove.setVisibility(View.VISIBLE);
        } else if (staUserInfo.getId().equals(video_info.getUploader_id()) && staUserInfo.getRole().equals(Role_Principal)) {
            imgAppRemove.setVisibility(View.VISIBLE);
        } else {
            imgAppRemove.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAppDownload:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ViewVideoActivity.super.requestAppPermissions(new
                            String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    downloadFile(video_info.getUrl(), video_info.getFile_name());
                }
                break;
            case R.id.imgAppRemove:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure, want to delete this?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                                GetRetro_RemoveDocument(video_info.getId());
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
                break;
            default:
                super.onClick(v);
        }
    }

    public void GetRetro_RemoveDocument(String id) {
        getRetro_Call(this, service.getRemoveDocument(staUserInfo.getId(), staUserInfo.getRole(), id), false, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Media mPojo = onPojoBuilder(objectResponse, Result_Media.class);
                if (mPojo.getStatus().equals("1")) {
                    finish();
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    public void downloadFile(String url, String fileName) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(fileName);
        request.setDescription("Downloading File...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager manager = (DownloadManager) getApplication().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}
