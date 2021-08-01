package com.shamlatech.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.api_response.Result_UserInfo;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.AccessUserInfo;
import static com.shamlatech.utils.Support.ShowError;
import static com.shamlatech.utils.Support.ShowImage;
import static com.shamlatech.utils.Support.UpdateProfileInfo;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Shamla Tech on 19-07-2018.
 */

public class ChangePasswordActivity extends BaseActivity {
    EditText edtOldPassword, edtNewPassword, edtRetypePassword;
    RelativeLayout relativeUpdate;
    CircleImageView imgTeacherPic;
    TextView txtName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        declareAppBar5("CHANGE PASSWORD", "ChangePasswordActivity");
        declareBottomBar();
        activeBottomBar("profile");

        txtName = findViewById(R.id.txtSubject);
        imgTeacherPic = findViewById(R.id.imgTeacherPic);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtRetypePassword = findViewById(R.id.edtRetypePassword);
        relativeUpdate = findViewById(R.id.relativeUpdate);

        relativeUpdate.setOnClickListener(this);

        PreparePage();
    }

    public void PreparePage() {
        txtName.setText(staUserInfo.getFirst_name() + " " + staUserInfo.getMiddle_name() + " " + staUserInfo.getLast_name());
        ShowImage(this, imgTeacherPic, staUserInfo.getImage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeUpdate:
                if (isValid()) {
                    getRetro_ChangePassword(staUserInfo.getId(), staUserInfo.getRole(),
                            edtOldPassword.getText().toString(), edtNewPassword.getText().toString());
                }
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    public void getRetro_ChangePassword(final String userId, final String role, final String oldPassword, final String newPassword) {
        getRetro_Call(ChangePasswordActivity.this, service.getUpdatePassword(userId, role, oldPassword, newPassword), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_UserInfo mPojo_Userinfo = onPojoBuilder(objectResponse, Result_UserInfo.class);
                if (mPojo_Userinfo != null) {
                    if (mPojo_Userinfo.getStatus().equals(API_Code.Success)) {
                        UpdateProfileInfo(ChangePasswordActivity.this, mPojo_Userinfo.getUser_info());
                        AccessUserInfo(ChangePasswordActivity.this);
                        Toast.makeText(ChangePasswordActivity.this, "Password Updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        ShowError(ChangePasswordActivity.this, "Error", mPojo_Userinfo.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private boolean isValid() {
        boolean isValid = true;
        if (edtOldPassword.getText().toString().isEmpty()) {
            edtOldPassword.setError("Please Enter Old Password");
            isValid = false;
        }
        if (edtNewPassword.getText().toString().isEmpty()) {
            edtNewPassword.setError("Please Enter New Password");
            isValid = false;
        }
        if (edtRetypePassword.getText().toString().isEmpty()) {
            edtRetypePassword.setError("Please Retype New Password");
            isValid = false;
        }
        if (!edtNewPassword.getText().toString().isEmpty() && !edtRetypePassword.getText().toString().isEmpty()
                && !edtNewPassword.getText().toString().equals(edtRetypePassword.getText().toString())) {
            Toast.makeText(this, "New passwords doesn't match!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

}
