package com.shamlatech.school_management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.shamlatech.api_response.Result_Check_Activation;
import com.shamlatech.api_response.Result_Common;
import com.shamlatech.services.SmsListener;
import com.shamlatech.services.SmsReceiver;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.ShowError;
import static com.shamlatech.utils.Support.hideKeyboard;
import static com.shamlatech.utils.Support.showToast;


public class VerifyActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtStep, txtComments, txtResent, txtOTP;
    PinEntryEditText pinOTP;
    LinearLayout linearPinEnter, linearVerify;
    LoadingButton loadingButton;
    Handler someHandler;
    Runnable OnlineRunnable;
    int CountDown = 60;
//    SmsReceiver smsReceiver;

    String strPhone = "", strPassword = "", strRole = "", strOTP = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

//        smsReceiver = new SmsReceiver();

        txtOTP = findViewById(R.id.txtOTP);
        txtStep = findViewById(R.id.txtStep);
        txtComments = findViewById(R.id.txtComments);
        txtResent = findViewById(R.id.txtResent);

        pinOTP = findViewById(R.id.pinOTP);

        linearPinEnter = findViewById(R.id.linearPinEnter);
        linearVerify = findViewById(R.id.linearVerify);

        loadingButton = (LoadingButton) findViewById(R.id.loadingButton);
        loadingButton.startLoading();

        txtResent.setOnClickListener(this);
        loadingButton.setOnClickListener(this);

        pinOTP.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                if (str.length() == 4) {
                    hideKeyboard(VerifyActivity.this);
                    String Code = pinOTP.getText().toString();
                    if (Code.equals(strOTP)) {
                        setUpPage(2);
                        getRetro_ActivateUser(strPhone, strPassword, strRole);
                    } else {
                        pinOTP.setText("");
                        pinOTP.setError("Invalid Code");
                        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        vib.vibrate(500);
                    }
                }
            }
        });

//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                pinOTP.setText(messageText);
//            }
//        });

        PreparePage();
        setUpPage(1);
        StartCountDown();
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

    private void PreparePage() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("phone")) {
                strPhone = bundle.getString("phone");
                strPassword = bundle.getString("password");
                strRole = bundle.getString("role");
                strOTP = bundle.getString("otp");
                txtOTP.setText(strOTP);
            }
        }
    }

    public void setUpPage(int step) {
        if (step == 1) {
            txtStep.setText("PART 1 OF 2");
            txtComments.setText(R.string.Verify_Step_1);
            linearPinEnter.setVisibility(View.VISIBLE);
            linearVerify.setVisibility(View.GONE);
        } else if (step == 2) {
            txtStep.setText("PART 2 OF 2");
            txtComments.setText(R.string.Verify_Step_2);
            linearPinEnter.setVisibility(View.GONE);
            linearVerify.setVisibility(View.VISIBLE);
        } else if (step == 3) {
            txtStep.setText("PART 2 OF 2");
            txtComments.setText(R.string.Verify_Step_3);
            loadingButton.loadingSuccessful();
            linearPinEnter.setVisibility(View.GONE);
            linearVerify.setVisibility(View.VISIBLE);
            RedirectSuccess();
        } else if (step == 4) {
            txtStep.setText("PART 2 OF 2");
            txtComments.setText(R.string.Verify_Step_4);
            loadingButton.loadingFailed();
            linearPinEnter.setVisibility(View.GONE);
            linearVerify.setVisibility(View.VISIBLE);
        }
    }

    public void RedirectSuccess() {
        Handler temp = new Handler();
        Runnable OnlineRunnable = new Runnable() {
            @Override
            public void run() {
          /*      Intent in = new Intent(VerifyActivity.this, LoginActivity.class);
                in.putExtra("role", strRole);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);*/
                if(strRole.equals("1")) {
                    Intent in = new Intent(VerifyActivity.this, complete.class);
                    in.putExtra("role", strRole);
                    in .putExtra("phone", strPhone);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                }
                else{

                    Intent in = new Intent(VerifyActivity.this, LoginActivity.class);
                    in.putExtra("role", strRole);
                    in .putExtra("phone", strPhone);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                }



            }
        };
        temp.postDelayed(OnlineRunnable, 3000);
    }

    public void StartCountDown() {
        someHandler = new Handler();
        OnlineRunnable = new Runnable() {
            @Override
            public void run() {
                if (CountDown > 0) {
                    txtResent.setOnClickListener(null);
                    txtResent.setText(Html.fromHtml("<u>Resend in " + CountDown + "s</u>"));
                    CountDown--;
                    someHandler.postDelayed(this, 1000);
                } else {
                    txtResent.setText(Html.fromHtml("<u>Resend</u>"));
                    txtResent.setOnClickListener(VerifyActivity.this);
                }
            }
        };

        someHandler.postDelayed(OnlineRunnable, 1);
    }

    public void resendOTP() {
        StartCountDown();
        getRetro_CheckActivation(strPhone, strRole);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtResent:
                CountDown = 60;
                resendOTP();
                break;
            case R.id.loadingButton:
                onBackPressed();
                break;
        }
    }


    public void getRetro_CheckActivation(final String phone, final String role) {
        getRetro_Call(VerifyActivity.this, service.getCheckActivation(phone, role), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Check_Activation mPojo_Common = onPojoBuilder(objectResponse, Result_Check_Activation.class);
                if (mPojo_Common != null) {
                    if (mPojo_Common.getStatus().equals(API_Code.Success)) {
                        strOTP = mPojo_Common.getOtp();
                        txtOTP.setText(strOTP);
                        showToast(VerifyActivity.this, "OTP Sent");
                    } else {
                        ShowError(VerifyActivity.this, "Error", mPojo_Common.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    public void getRetro_ActivateUser(final String phone, final String password, final String role) {
        getRetro_Call(VerifyActivity.this, service.getActivateUser(phone, password, role), false, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Common mPojo_Common = onPojoBuilder(objectResponse, Result_Common.class);
                if (mPojo_Common != null) {
                    if (mPojo_Common.getStatus().equals(API_Code.Success)) {
                        setUpPage(3);
                    } else {
                        setUpPage(4);
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
