package com.shamlatech.activity.teacher;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.shamlatech.api_response.Result_Common;
import com.shamlatech.pojo.PojoParentWithClassList;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.shamlatech.utils.Support.ShowError;
import static com.shamlatech.utils.Support.hideProgress;
import static com.shamlatech.utils.Support.showProgress;
import static com.shamlatech.utils.Vars.Refresh_Document;
import static com.shamlatech.utils.Vars.Refresh_Photo;
import static com.shamlatech.utils.Vars.Refresh_Video;
import static com.shamlatech.utils.Vars.staUserInfo;

public class AddDocumentActivity extends BaseActivity {

    public static int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 12345;
    public static int REQUEST_CODE_OPEN = 123;
    TextView txtAttachmentName;
    ImageView imgAttachment;
    RadioButton radioDocument, radioPhoto, radioVideo;
    Spinner spinnerViewers;
    RelativeLayout relativeDone;
    String pathToFile = "";
    String attachmentType_id = "", attachmentType_name = "";
    EditText edtTitle, edtDetails;
    ArrayList<PojoParentWithClassList> listClass;


    ArrayAdapter<PojoParentWithClassList> adapterViewers;

    public static String getFilePath1(Context ctx, Uri uri) {
        ContentResolver cr = ctx.getContentResolver();
        String file_path = null;
        Cursor cursor = cr.query(uri,
                new String[]{android.provider.MediaStore.MediaColumns.DATA},
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            file_path = cursor.getString(0);
            cursor.close();
        } else {
            file_path = uri.getPath();
        }
        return file_path;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getFilePath(Context context, Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                String storageDefinition;


                if("primary".equalsIgnoreCase(type)){

                    return Environment.getExternalStorageDirectory() + "/" + split[1];

                } else {

                    if(Environment.isExternalStorageRemovable()){
                        storageDefinition = "EXTERNAL_STORAGE";

                    } else{
                        storageDefinition = "SECONDARY_STORAGE";
                    }

                    return System.getenv(storageDefinition) + "/" + split[1];
                }

            } else if (isDownloadsDocument(uri)) {// DownloadsProvider

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);

            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);

        declareAppBar2("Upload", "AddDocumentActivity");
        UpdateAppBarColor(R.color.colorTabRed);

        edtTitle = findViewById(R.id.edtTitle);
        edtDetails = findViewById(R.id.edtDetails);

        txtAttachmentName = findViewById(R.id.txtAttachmentName);
        imgAttachment = findViewById(R.id.imgAttachment);

        radioDocument = findViewById(R.id.radioDocument);
        radioPhoto = findViewById(R.id.radioPhoto);
        radioVideo = findViewById(R.id.radioVideo);

        spinnerViewers = findViewById(R.id.spinnerViewers);

        relativeDone = findViewById(R.id.relativeDone);

        listClass = db.AccessTeacherClassList();
        adapterViewers = new ArrayAdapter<PojoParentWithClassList>(this, R.layout.simple_spinner_item, listClass);
        adapterViewers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerViewers.setAdapter(adapterViewers);


        imgAttachment.setOnClickListener(this);
        relativeDone.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitializeProgress();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            ShowChooser();
        }
    }

    public void InitializeProgress() {
        Vars.Custom_Progress = (View) findViewById(R.id.Custom_Progress);
        Vars.Custom_Progress.setVisibility(View.GONE);
        String indicator = getIntent().getStringExtra("indicator");
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAttachment:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    AddDocumentActivity.super.requestAppPermissions(new
                            String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    ShowChooser();
                }
                break;
            case R.id.relativeDone:
                String class_id = "0";
                String section_id = "0";
                String strTitle = edtTitle.getText().toString();
                String strDetails = edtDetails.getText().toString();
                String strPath = pathToFile;
                String strAttachmentType_id = attachmentType_id;
                String strAttachmentType_name = attachmentType_name;
                if (listClass.size() > 0) {
                    class_id = listClass.get(spinnerViewers.getSelectedItemPosition()).getClass_id();
                    section_id = listClass.get(spinnerViewers.getSelectedItemPosition()).getSection_id();
                }
                if (listClass.size() == 0) {
                    ShowError(AddDocumentActivity.this, "Sorry", "You could not upload any file at this time");
                } else if (strPath.equals("")) {
                    ShowError(AddDocumentActivity.this, "Error", "Please attach file");
                } else {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("user_id", staUserInfo.getId());
                        json.put("role_id", staUserInfo.getRole());
                        json.put("title", strTitle);
                        json.put("details", strDetails);
                        json.put("path", strPath);
                        json.put("type_id", strAttachmentType_id);
                        json.put("type_name", strAttachmentType_name);
                        json.put("class_id", class_id);
                        json.put("section_id", section_id);

                        getRetro_UploadDocument(json);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            default:
                super.onClick(v);
        }

    }

    public void ShowChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent.createChooser(intent, "Choose File to Upload.."), REQUEST_CODE_OPEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_OPEN) {
            if (resultCode == Activity.RESULT_OK) {
                pathToFile = data.getDataString();
                try {
                    pathToFile = getFilePath(this, data.getData());
                    UpdateFileType();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void UpdateFileType() {
        attachmentType_id = "";
        File file = new File(pathToFile);
        String type = getMimeType(pathToFile);
        if (type == null) {
            Support.ShowAlertWithOutTitle(this, "Invalid File. Please try again");
            pathToFile = "";
            txtAttachmentName.setText("");
        } else {
            txtAttachmentName.setText(file.getName());
            attachmentType_name = type;
            if (type.startsWith("image")) {
                attachmentType_id = Vars.Cons_Attachment_Image;
                radioPhoto.setChecked(true);
            } else if (type.startsWith("video")) {
                attachmentType_id = Vars.Cons_Attachment_Video;
                radioVideo.setChecked(true);
            } else {
                attachmentType_id = Vars.Cons_Attachment_Document;
                radioDocument.setChecked(true);
            }
        }
    }

    public void getRetro_UploadDocument(final JSONObject itemData) throws JSONException {
        showProgress(Vars.Custom_Progress);
        final MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("user_id", itemData.getString("user_id"));
        builder.addFormDataPart("role_id", itemData.getString("role_id"));
        builder.addFormDataPart("title", itemData.getString("title"));
        builder.addFormDataPart("details", itemData.getString("details"));
        builder.addFormDataPart("type_id", itemData.getString("type_id"));
        builder.addFormDataPart("type_name", itemData.getString("type_name"));
        builder.addFormDataPart("class_id", itemData.getString("class_id"));
        builder.addFormDataPart("section_id", itemData.getString("section_id"));


        File f = new File(itemData.getString("path"));
        String file_path = f.getAbsolutePath();
        RequestBody file_body = RequestBody.create(MediaType.parse(itemData.getString("type_name")), f);
        String file_Name = file_path.substring(file_path.lastIndexOf("/") + 1);
        builder.addFormDataPart("attachment", file_Name, file_body);

        Refresh_Photo = "1";
        Refresh_Document = "1";
        Refresh_Video = "1";

        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(600, TimeUnit.SECONDS)
                        .build();

                RequestBody request_body = builder.build();

                Request request = new Request.Builder()
                        .url(Vars.Live_Attachment)
                        .post(request_body)
                        .build();

                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideProgress(Vars.Custom_Progress);
                            }
                        });
                        throw new IOException("Error : " + response);
                    } else {
                        Gson gson = new Gson();
                        String json = response.body().string();
                        final Result_Common mPojo = gson.fromJson(json, Result_Common.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String Status = mPojo.getStatus();
                                hideProgress(Vars.Custom_Progress);
                                if (Status.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "Upload Successfully", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgress(Vars.Custom_Progress);
                        }
                    });
                    FirebaseCrash.report(e);
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}
