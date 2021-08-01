package com.shamlatech.school_management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.api_response.Result_Check_Activation;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;
import retrofit2.Response;
import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.ShowError;
import static com.shamlatech.utils.Support.ValidateMobileNumber;
import static com.shamlatech.utils.Support.ValidatePassword;


public class NemisActivateActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtTerms;
    EditText edtNemisNumber;//, edtPassword;
    Button btnActivate;
    //CheckBox cbkTerms;
    String strRole = "";
    //ImageView imgBack;
    //ImageView imgPasswordShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nemis_activity_activate);

        prepareBundle();

        //txtTerms = findViewById(R.id.txtTerms);
        //imgBack = findViewById(R.id.imgBack);
        //imgPasswordShow = findViewById(R.id.imgPasswordShow);

        edtNemisNumber = findViewById(R.id.edtNemisNumber);

        //edtPassword = findViewById(R.id.edtPassword);

        btnActivate = findViewById(R.id.btnActivate);

        //cbkTerms = findViewById(R.id.cbkTerms);

        //imgPasswordShow.setOnClickListener(this);
        btnActivate.setOnClickListener(this);
        //imgBack.setOnClickListener(this);

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
/*        SpannableString ss = new SpannableString("I agree with the Terms & Conditions and our Privacy Policy");
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
        ss.setSpan(clickableSpanTerms, 17, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpanPolicy, 44, 58, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtTerms.setText(ss);
        txtTerms.setMovementMethod(LinkMovementMethod.getInstance());
        txtTerms.setHighlightColor(Color.TRANSPARENT);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnActivate:
                boolean Error = false;
                String strNemisNumber = edtNemisNumber.getText().toString();
                //String strPassword = edtPassword.getText().toString();

                if (strNemisNumber.equals("")) {
                    edtNemisNumber.setError("Please enter valid phone number!");
                    Error = true;
                }
              /*  if (strPassword.equals("") || !ValidatePassword(strPassword)) {
                    edtPassword.setError("Please enter valid password");
                    Error = true;
                }*/
               /* if (!Error && !cbkTerms.isChecked()) {
                    Error = true;
                    Toast.makeText(getApplicationContext(), "Please accept the term & condition", Toast.LENGTH_LONG).show();
                }*/

                if (Error) {
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(500);
                } else {
                    getRetro_CheckActivation(strNemisNumber,strRole);
                }
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgPasswordShow:
              /*  if (edtPassword.getTransformationMethod() == null) {
                    imgPasswordShow.setImageResource(R.drawable.ic_password_show);
                    edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    imgPasswordShow.setImageResource(R.drawable.ic_password_hide);
                    edtPassword.setTransformationMethod(null);
                }*/
                break;
        }
    }

    public void getRetro_CheckActivation(final String nemisnumber, final String role) {

        startActivity(new Intent(NemisActivateActivity.this,ParentsFillActivity.class)
                .putExtra("nemisnumber", nemisnumber)
                .putExtra("role", role));

      /*
        Bypass if parent is activated
        getRetro_Call(NemisActivateActivity.this, service.getCheckNemis(nemisnumber, role), true, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {

                Result_Check_Activation mPojo_Active = onPojoBuilder(objectResponse, Result_Check_Activation.class);
                if (mPojo_Active != null) {
                    if (mPojo_Active.getStatus().equals(API_Code.Success)) {

                        startActivity(new Intent(NemisActivateActivity.this,ParentsFillActivity.class)
                                .putExtra("nemisnumber", nemisnumber)
                                .putExtra("role", role));
                    } else {
                        ShowError(NemisActivateActivity.this, "Error", mPojo_Active.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(String message) {

            }
        });
        */

    }
}
