package com.shamlatech.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shamlatech.api_response.Res_Media;
import com.shamlatech.api_response.Result_Media;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.FormatDateTimeForShow;
import static com.shamlatech.utils.Vars.Role_Principal;
import static com.shamlatech.utils.Vars.Role_Teacher;
import static com.shamlatech.utils.Vars.staUserInfo;

public class ViewImageActivity extends BaseActivity {

    public static int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 12345;
    String URL = "";
    RelativeLayout relativeZoomImage;
    ImageView imgImage, imgZoomImage;
    TextView txtDocumentTitle, txtDocumentDetails, txtUploadedBy, txtUploadedOn, txtTo;
    Res_Media photo_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        declareAppBar7("Photo", "");

        relativeZoomImage = findViewById(R.id.relativeZoomImage);
        imgZoomImage = findViewById(R.id.imgZoomImage);
        imgImage = findViewById(R.id.imgImage);
        txtDocumentTitle = findViewById(R.id.txtDocumentTitle);
        txtDocumentDetails = findViewById(R.id.txtDocumentDetails);
        txtUploadedBy = findViewById(R.id.txtUploadedBy);
        txtUploadedOn = findViewById(R.id.txtUploadedOn);
        txtTo = findViewById(R.id.txtTo);

        imgImage.setOnClickListener(this);

        PreparePage();

    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            downloadFile(photo_info.getUrl(), photo_info.getFile_name());
        }
    }

    private void PreparePage() {
        Bundle bundle = getIntent().getExtras();
        photo_info = new Res_Media();
        if (bundle != null) {
            if (bundle.containsKey("photo_document")) {
                photo_info = (Res_Media) bundle.getSerializable("photo_document");
                Support.ShowImage(this, imgImage, photo_info.getUrl());
                Support.ShowImage(this, imgZoomImage, photo_info.getUrl());
                txtDocumentTitle.setText(photo_info.getTitle());
                txtDocumentDetails.setText(photo_info.getDetails());
                txtTo.setText("To :" + photo_info.getClass_name() + photo_info.getSection_name());
                txtUploadedBy.setText(photo_info.getUploader_name());
                txtUploadedOn.setText(FormatDateTimeForShow(photo_info.getDate_time(), Vars.DatePattern1, ""));
                preparePage();
            }
        }
    }


    private void preparePage() {
        if (staUserInfo.getId().equals(photo_info.getUploader_id()) && staUserInfo.getRole().equals(Role_Teacher)) {
            imgAppRemove.setVisibility(View.VISIBLE);
        } else if (staUserInfo.getId().equals(photo_info.getUploader_id()) && staUserInfo.getRole().equals(Role_Principal)) {
            imgAppRemove.setVisibility(View.VISIBLE);
        } else {
            imgAppRemove.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (relativeZoomImage.getVisibility() == View.VISIBLE) {
            relativeZoomImage.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAppDownload:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ViewImageActivity.super.requestAppPermissions(new
                            String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    downloadFile(photo_info.getUrl(), photo_info.getFile_name());
                }
                break;
            case R.id.imgImage:
                relativeZoomImage.setVisibility(View.VISIBLE);
                break;
            case R.id.imgAppRemove:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure, want to delete this?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                                GetRetro_RemoveDocument(photo_info.getId());
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
