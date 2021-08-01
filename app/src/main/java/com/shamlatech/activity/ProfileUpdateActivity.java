package com.shamlatech.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.shamlatech.api_response.Result_UserInfo;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.ShowError;
import static com.shamlatech.utils.Support.ShowImage;
import static com.shamlatech.utils.Vars.staUserInfo;

public class ProfileUpdateActivity extends BaseActivity {
    public static int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 12345;
    EditText edtName, edtPhoneNumber, edtEmail, edtPassword;
    RelativeLayout relativeUpdate;
    CircleImageView imgTeacherPic;
    TextView txtUploadPicture, txtChangePassword;
    String picture_path = "";
    Dialog pick_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile_update);

        declareAppBar5("PROFILE", "ProfileUpdateActivity");
        declareBottomBar();
        activeBottomBar("profile");

        imgTeacherPic = findViewById(R.id.imgTeacherPic);
        txtUploadPicture = findViewById(R.id.txtUploadPicture);
        txtChangePassword = findViewById(R.id.txtChangePassword);

        edtName = findViewById(R.id.edtName);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        relativeUpdate = findViewById(R.id.relativeUpdate);
        relativeUpdate.setOnClickListener(this);
        txtUploadPicture.setOnClickListener(this);
        txtChangePassword.setOnClickListener(this);

        InitializeProgress();
        PreparePage();
    }

    public void InitializeProgress() {
        Vars.Custom_Progress = (View) findViewById(R.id.Custom_Progress);
        Vars.Custom_Progress.setVisibility(View.GONE);
        String indicator = getIntent().getStringExtra("indicator");
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.show();
    }

    public void PreparePage() {
        edtName.setText(staUserInfo.getFirst_name() + " " + staUserInfo.getMiddle_name() + " " + staUserInfo.getLast_name());
        edtPhoneNumber.setText(staUserInfo.getPhone_number());
        edtEmail.setText(staUserInfo.getEmail());
        ShowImage(this, imgTeacherPic, staUserInfo.getImage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeUpdate:
                if (isValid()) {
                    getRetro_UpdateProfile(staUserInfo.getId(), staUserInfo.getRole(),
                            edtName.getText().toString(), edtPhoneNumber.getText().toString(),
                            edtEmail.getText().toString()/*, edtPassword.getText().toString()*/);
                }
                break;
            case R.id.txtUploadPicture:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ProfileUpdateActivity.super.requestAppPermissions(new
                            String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    pickImage();
                }
                break;
            case R.id.txtChangePassword:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        super.onPermissionsGranted(requestCode);
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            pickImage();
        }
    }

    private void pickImage() {
        ImagePicker.with(this)
                .setToolbarColor("#00A99E")
                .setStatusBarColor("#018f86")
                .setToolbarTextColor("#FFFFFF")
                .setToolbarIconColor("#FFFFFF")
                .setProgressBarColor("#ed1b25")
                .setBackgroundColor("#212121")
                .setCameraOnly(false)
                .setMultipleMode(false)
                .setFolderMode(true)
                .setShowCamera(true)
                .setFolderTitle("Albums")
                .setImageTitle("Galleries")
                .setDoneTitle("Done")
                .setSavePath("School Management")
                .setAlwaysShowDoneButton(true)
                .setKeepScreenOn(true)
                .start();
    }

    private boolean isValid() {
        boolean isValid = true;
        if (edtName.getText().toString().isEmpty()) {
            edtName.setError("Please Enter Name");
            isValid = false;
        }
        if (edtPhoneNumber.getText().toString().isEmpty()) {
            edtPhoneNumber.setError("Please Enter Phone Number");
            isValid = false;
        }
        if (edtEmail.getText().toString().isEmpty()) {
            edtEmail.setError("Please Enter Email");
            isValid = false;
        }
       /* if (edtPassword.getText().toString().isEmpty()) {
            edtPassword.setError("Please Enter Password");
            isValid = false;
        }*/
        return isValid;
    }


    public void showProfileUpdateApproval() {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(ProfileUpdateActivity.this);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_profile_update);

            LinearLayout linearContainer = pick_Dialog.findViewById(R.id.linearContainer);
            linearContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pick_Dialog.dismiss();
                }
            });
            pick_Dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });

            pick_Dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });

            pick_Dialog.show();

        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showProfileUpdateApproval();
        } else {
            pick_Dialog = null;
            showProfileUpdateApproval();
        }
    }


    public void getRetro_UpdateProfile(final String userId, final String role, final String name, final String phone_number,
                                       final String email/*, final String password*/) {
        MultipartBody.Part body = null;
        if (!picture_path.isEmpty()) {
            File file = new File(picture_path);
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        }

        RequestBody reqId = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody reqRole = RequestBody.create(MediaType.parse("text/plain"), role);
        RequestBody reqName = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody reqPhone = RequestBody.create(MediaType.parse("text/plain"), phone_number);
        RequestBody reqEmail = RequestBody.create(MediaType.parse("text/plain"), email);
//        RequestBody reqPassword = RequestBody.create(MediaType.parse("text/plain"), password);


        getRetro_Call(ProfileUpdateActivity.this,
                service.getUpdateProfile(reqId, reqRole, reqName, reqPhone, reqEmail, /*reqPassword,*/ body),
                true, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_UserInfo mPojo_Userinfo = onPojoBuilder(objectResponse, Result_UserInfo.class);
                        if (mPojo_Userinfo != null) {
                            if (mPojo_Userinfo.getStatus().equals(API_Code.Success)) {
//                                UpdateProfileInfo(ProfileUpdateActivity.this, mPojo_Userinfo.getUser_info());
//                                AccessUserInfo(ProfileUpdateActivity.this);
                                showProfileUpdateApproval();
                            } else {
                                ShowError(ProfileUpdateActivity.this, "Error", mPojo_Userinfo.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            CropImage.activity(Uri.fromFile(new File(images.get(0).getPath())))
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setAspectRatio(4, 4)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Support.refreshMediaScanner(this, resultUri);
                picture_path = resultUri.getPath();
                ShowImage(this, imgTeacherPic, new File(picture_path));
            }
        }
    }
}
