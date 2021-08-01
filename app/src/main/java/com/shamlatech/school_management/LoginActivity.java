package com.shamlatech.school_management;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shamlatech.api_response.Res_Student_Info;
import com.shamlatech.api_response.Result_Forget_Password;
import com.shamlatech.api_response.Result_Group;
import com.shamlatech.api_response.Result_Parent_Student_Details;
import com.shamlatech.api_response.Result_TeacherInfo;
import com.shamlatech.api_response.Result_UserInfo;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.services.AchieveMessageService;
import com.shamlatech.services.BGLookupService;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.AccessUserInfo;
import static com.shamlatech.utils.Support.IsMobile;
import static com.shamlatech.utils.Support.ShowError;
import static com.shamlatech.utils.Support.UpdateProfileInfo;
import static com.shamlatech.utils.Support.ValidateMailId;
import static com.shamlatech.utils.Support.ValidateMobileNumber;
import static com.shamlatech.utils.Vars.staUserInfo;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtTerms;
    EditText edtEmailOrMobileNumber, edtPassword;
    Button btnLogin;
    DBAdapter db;
    String strRole = "";
    ImageView imgBack;
    String forgetSource = "";
    Dialog pick_Dialog;
    TextView txtForgetPassword;
    String strForgetOTP = "";
    String strForgetUser = "";
    ImageView imgPasswordShow;
    Res_Student_Info s = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            db = new DBAdapter(this, this.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }

        prepareBundle();

        txtTerms = findViewById(R.id.txtTerms);
        imgBack = findViewById(R.id.imgBack);
        txtForgetPassword = findViewById(R.id.txtForgetPassword);
        imgPasswordShow = findViewById(R.id.imgPasswordShow);

        edtEmailOrMobileNumber = findViewById(R.id.edtEmailOrMobileNumber);
        edtPassword = findViewById(R.id.edtPassword);
        edtPassword.setTransformationMethod(null);

        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtForgetPassword.setOnClickListener(this);
        imgPasswordShow.setOnClickListener(this);

        imgPasswordShow.callOnClick();
        setSpanClicked();
    }

    public void prepareBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("role")) {
                strRole = bundle.getString("role");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitializeProgress();
    }


    public void InitializeProgress() {
        Vars.Custom_Progress = (View) findViewById(R.id.Custom_Progress);
        Vars.Custom_Progress.setVisibility(View.GONE);
        String indicator = getIntent().getStringExtra("indicator");
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.show();
    }


    public void setSpanClicked() {

        SpannableString ss = new SpannableString("By logging in your agree to the Terms & Conditions and our Privacy Policy");
        ClickableSpan clickableSpanTerms = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Toast.makeText(getApplicationContext(), "Terms", Toast.LENGTH_LONG).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.colorBtnRedClick));
            }
        };
        ClickableSpan clickableSpanPolicy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Toast.makeText(getApplicationContext(), "Policy", Toast.LENGTH_LONG).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(getResources().getColor(R.color.colorBtnRedClick));
            }
        };
        ss.setSpan(clickableSpanTerms, 32, 50, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpanPolicy, 59, 73, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtTerms.setText(ss);
        txtTerms.setMovementMethod(LinkMovementMethod.getInstance());
        txtTerms.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                boolean Error = false;
                String strEmailOrPhoneNumber = edtEmailOrMobileNumber.getText().toString();
                String strPassword = edtPassword.getText().toString();
                //String strRole = Vars.Role_Parent;
                String source = "";

                if (IsMobile(strEmailOrPhoneNumber)) {
                    source = "phone";
                    if (strEmailOrPhoneNumber.equals("") || !ValidateMobileNumber(strEmailOrPhoneNumber)) {
                        edtEmailOrMobileNumber.setError("Please enter valid email or mobile number");
                        Error = true;
                    }
                } else {
                    source = "mail";
                    if (strEmailOrPhoneNumber.equals("") || !ValidateMailId(strEmailOrPhoneNumber)) {
                        edtEmailOrMobileNumber.setError("Please enter valid email or mobile number");
                        Error = true;
                    }
                }
                if (strPassword.equals("")) {
                    edtPassword.setError("Please enter valid password");
                    Error = true;
                }

                if (Error) {
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(500);
                } else {
                    getRetro_Login(strEmailOrPhoneNumber, strPassword, source, strRole);
                }
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.txtForgetPassword:
                showForgetPassword();
                break;
            case R.id.imgPasswordShow:
                if (edtPassword.getTransformationMethod() == null) {
                    imgPasswordShow.setImageResource(R.drawable.ic_password_show);
                    edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    imgPasswordShow.setImageResource(R.drawable.ic_password_hide);
                    edtPassword.setTransformationMethod(null);
                }
                break;
        }
    }

    public void showForgetPassword() {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(this);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(false);
            pick_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_forget_password);

            final ImageView imgClose = pick_Dialog.findViewById(R.id.imgClose);
            final EditText edtForgetEmailOrMobileNumber = pick_Dialog.findViewById(R.id.edtEmailOrMobileNumber);
            final Button btnSend = pick_Dialog.findViewById(R.id.btnSend);

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strEmailOrPhoneNumber = edtForgetEmailOrMobileNumber.getText().toString();
                    boolean Error = false;
                    if (strEmailOrPhoneNumber.equals("")) {
                        edtForgetEmailOrMobileNumber.setError("Please enter email or mobile number");
                    } else {
                        if (IsMobile(strEmailOrPhoneNumber)) {
                            forgetSource = "phone";
                            if (!ValidateMobileNumber(strEmailOrPhoneNumber)) {
                                Error = true;
                                edtForgetEmailOrMobileNumber.setError("Please enter valid email or mobile number");
                            }
                        } else {
                            forgetSource = "mail";
                            if (!ValidateMailId(strEmailOrPhoneNumber)) {
                                Error = true;
                                edtForgetEmailOrMobileNumber.setError("Please enter valid email or mobile number");
                            }
                        }
                        if (!Error) {
                            getRetro_ForgetPassword(strEmailOrPhoneNumber, forgetSource, strRole);
                        }
                    }
                }
            });

            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pick_Dialog.dismiss();
                }
            });

            pick_Dialog.show();

        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showForgetPassword();
        } else {
            pick_Dialog = null;
            showForgetPassword();
        }
    }

    public void showForgetPasswordOTP() {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(this);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            pick_Dialog.setCancelable(false);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_forget_password_code);

            final ImageView imgClose = pick_Dialog.findViewById(R.id.imgClose);
            final EditText edtCode = pick_Dialog.findViewById(R.id.edtCode);
            final Button btnContinue = pick_Dialog.findViewById(R.id.btnContinue);

            edtCode.setHint("Enter Code (" + strForgetOTP + ")");
            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strcode = edtCode.getText().toString();
                    if (strcode.equals("")) {
                        edtCode.setError("Please Enter the code");
                    } else if (!strcode.equals(strForgetOTP)) {
                        edtCode.setError("Invalid Code");
                    } else {
                        showForgetPasswordReset();
                    }
                }
            });

            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pick_Dialog.dismiss();
                }
            });

            pick_Dialog.show();

        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showForgetPasswordOTP();
        } else {
            pick_Dialog = null;
            showForgetPasswordOTP();
        }
    }

    public void showForgetPasswordReset() {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(this);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(false);
            pick_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_forget_password_reset);

            final ImageView imgClose = pick_Dialog.findViewById(R.id.imgClose);
            final EditText edtNewPassword = pick_Dialog.findViewById(R.id.edtNewPassword);
            final EditText edtConfirmPassword = pick_Dialog.findViewById(R.id.edtConfirmPassword);
            final Button btnContinue = pick_Dialog.findViewById(R.id.btnContinue);

            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean Error = false;
                    String strNewPassword = edtNewPassword.getText().toString();
                    String strConfirmPassword = edtConfirmPassword.getText().toString();
                    if (strNewPassword.equals("")) {
                        edtNewPassword.setError("Please enter password");
                        Error = true;
                    }
                    if (strConfirmPassword.equals("")) {
                        edtConfirmPassword.setError("Please enter password");
                        Error = true;
                    }

                    if (!Error && !strNewPassword.equals(strConfirmPassword)) {
                        edtConfirmPassword.setError("Password mismatched");
                        Error = true;
                    }

                    if (!Error) {
                        getRetro_ResetPassword(strForgetUser, strNewPassword, strRole);
                    }
                }
            });

            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pick_Dialog.dismiss();
                }
            });

            pick_Dialog.show();

        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showForgetPasswordReset();
        } else {
            pick_Dialog = null;
            showForgetPasswordReset();
        }
    }


    public void RedirectUser() {
        startService(new Intent(LoginActivity.this, AchieveMessageService.class));
        if (staUserInfo.getRole().equals(Vars.Role_Teacher) || staUserInfo.getRole().equals(Vars.Role_Principal)) {
            getRetro_AccessTeacherDetails(staUserInfo.getId(), staUserInfo.getRole());
            startService(new Intent(LoginActivity.this, BGLookupService.class));
        } else {
            getRetro_AccessParentDetails(staUserInfo.getId(), staUserInfo.getRole());
        }
    }

    public void getRetro_ForgetPassword(final String name, final String source, final String role) {
        getRetro_Call(LoginActivity.this, service.getForgetPassword(name, source, role), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Forget_Password mPojo_Forget = onPojoBuilder(objectResponse, Result_Forget_Password.class);
                if (mPojo_Forget != null) {
                    if (mPojo_Forget.getStatus().equals(API_Code.Success)) {
                        if (source.equals("phone")) {
                            strForgetOTP = mPojo_Forget.getOtp();
                            strForgetUser = mPojo_Forget.getUser_id();
                            showForgetPasswordOTP();
                        } else {
                            if (pick_Dialog != null && pick_Dialog.isShowing())
                                pick_Dialog.dismiss();
                        }
                    } else {
                        ShowError(LoginActivity.this, "Error", mPojo_Forget.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    public void getRetro_ResetPassword(final String user_id, final String password, final String role) {
        getRetro_Call(LoginActivity.this, service.getResetPassword(user_id, password, role), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Forget_Password mPojo_Forget = onPojoBuilder(objectResponse, Result_Forget_Password.class);
                if (mPojo_Forget != null) {
                    if (mPojo_Forget.getStatus().equals(API_Code.Success)) {
                        Toast.makeText(getApplicationContext(), "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                        pick_Dialog.dismiss();
                    } else {
                        ShowError(LoginActivity.this, "Error", mPojo_Forget.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    public void getRetro_Login(final String name, final String password, final String source, final String role) {
        getRetro_Call(LoginActivity.this, service.getLogin(name, password, source, role), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_UserInfo mPojo_Userinfo = onPojoBuilder(objectResponse, Result_UserInfo.class);
                if (mPojo_Userinfo != null) {
                    if (mPojo_Userinfo.getStatus().equals(API_Code.Success)) {
                        UpdateProfileInfo(LoginActivity.this, mPojo_Userinfo.getUser_info());
                        AccessUserInfo(LoginActivity.this);
                        RedirectUser();
                    } else {
                        ShowError(LoginActivity.this, "Error", mPojo_Userinfo.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    public void getRetro_AccessTeacherDetails(final String user_id, final String role_id) {
        getRetro_Call(LoginActivity.this, service.getAccessTeachersDetails(user_id, role_id), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_TeacherInfo mPojo_TeacherInfo = onPojoBuilder(objectResponse, Result_TeacherInfo.class);
                if (mPojo_TeacherInfo != null) {
                    if (mPojo_TeacherInfo.getStatus().equals(API_Code.Success)) {
                        db.ClearTable("teachers_class");
                        db.ClearTable("teachers_student");
                        db.ClearTable("teachers_timetable");
                        db.InsertTeachersTimeTable(mPojo_TeacherInfo.getTimetable());
                        db.InsertTeachersTeachingSubject(mPojo_TeacherInfo.getTeaching_subjects());
                        db.InsertTeachersClass(mPojo_TeacherInfo.getMy_class());
                        db.InsertTeachersClass(mPojo_TeacherInfo.getSubject_class());
                        getRetro_AccessTeacherGroup(user_id, role_id);
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }


    public void getRetro_AccessTeacherGroup(String user_id, String role_id) {
        getRetro_Call(LoginActivity.this, service.getAccessTeachersGroup(user_id, role_id), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Group mPojo_Group = onPojoBuilder(objectResponse, Result_Group.class);
                if (mPojo_Group != null) {
                    if (mPojo_Group.getStatus().equals(API_Code.Success)) {
                        db.ClearTable("teachers_group");
                        db.InsertGroup(mPojo_Group.getGroup());
                        Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    public void getRetro_AccessParentDetails(final String user_id, String role_id) {
        getRetro_Call(LoginActivity.this, service.getAccessParentDetails(user_id, role_id), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Parent_Student_Details mPojo_ParentInfo = onPojoBuilder(objectResponse, Result_Parent_Student_Details.class);
                if (mPojo_ParentInfo != null) {
                    if (mPojo_ParentInfo.getStatus().equals(API_Code.Success)) {

                        db.ClearTable("parent_student");
                        db.InsertParentStudent(mPojo_ParentInfo.getStudents());

                        if (mPojo_ParentInfo.getExpired().equals('1')) {

                            Intent in = new Intent(LoginActivity.this, MPESAExpressActivity.class);
                            in.putExtra("id", user_id);
                            startActivity(in);

                        }else {
                            Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                        }
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

//
//    public void getRetro_ParentLogin(final String name, final String password, final String source, final String role) {
//        getRetro_Call(LoginActivity.this, service.getParentLogin(name, password, source, role), true, new API_Calls.OnApiResult() {
//            @Override
//            public void onSuccess(Response<Object> objectResponse) {
//                Result_UserInfo mPojo_Userinfo = onPojoBuilder(objectResponse, Result_UserInfo.class);
//                if (mPojo_Userinfo != null) {
//                    if (mPojo_Userinfo.getStatus().equals(API_Code.Success)) {
//                        UpdateProfileInfo(LoginActivity.this, mPojo_Userinfo.getUser_info());
//                        AccessUserInfo(LoginActivity.this);
//                        RedirectUser();
//                    } else {
//                        ShowError(LoginActivity.this, "Error", mPojo_Userinfo.getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(String message) {
//
//            }
//        });
//    }
//
//    public void getRetro_TeacherLogin(final String name, final String password, final String source, final String role) {
//        getRetro_Call(LoginActivity.this, service.getTeacherLogin(name, password, source, role), true, new API_Calls.OnApiResult() {
//            @Override
//            public void onSuccess(Response<Object> objectResponse) {
//                Result_UserInfo mPojo_Userinfo = onPojoBuilder(objectResponse, Result_UserInfo.class);
//                if (mPojo_Userinfo != null) {
//                    if (mPojo_Userinfo.getStatus().equals(API_Code.Success)) {
//                        UpdateProfileInfo(LoginActivity.this, mPojo_Userinfo.getUser_info());
//                        AccessUserInfo(LoginActivity.this);
//                        RedirectUser();
//                    } else {
//                        ShowError(LoginActivity.this, "Error", mPojo_Userinfo.getMessage());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(String message) {
//
//            }
//        });
//    }




}
