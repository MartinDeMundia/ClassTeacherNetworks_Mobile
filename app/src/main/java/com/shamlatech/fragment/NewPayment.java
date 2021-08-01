package com.shamlatech.fragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.activity.MyWebView;
import com.shamlatech.api_response.Res_UserInfo;
import com.shamlatech.school_management.R;
import com.shamlatech.services.MyWebCilent;
import com.shamlatech.utils.PaymentsUtil;
import com.shamlatech.utils.Support;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPayment extends Fragment {

    EditText name;
    EditText email;
    EditText phone;
    EditText amount;
    View payBtn;

    public NewPayment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_payment, container, false);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone_number);
        amount = view.findViewById(R.id.amount);
        payBtn = view.findViewById(R.id.pay_btn);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
            }
        });
        return view;
    }

    private void pay() {
        if (TextUtils.isEmpty(amount.getText().toString())) {
            Toast.makeText(getContext(), "Please enter an amount to pay.", Toast.LENGTH_SHORT).show();
            amount.requestFocus();
            amount.startAnimation(shakeError());
            return;
        }
        if (TextUtils.isEmpty(name.getText().toString())) {
            Toast.makeText(getContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            name.startAnimation(shakeError());
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            Toast.makeText(getContext(), "Email address not valid", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            email.startAnimation(shakeError());
        }
        if (TextUtils.isEmpty(phone.getText().toString())||!Support.ValidateMobileNumber(phone.getText().toString().trim())) {
            Toast.makeText(getContext(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            phone.startAnimation(shakeError());
            return;
        }
        //show confirm dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.confirm_dialog, null, false);
        View retry = view.findViewById(R.id.btn_retry);
        View cancel = view.findViewById(R.id.btn_cancel);
        TextView message=view.findViewById(R.id.confirm_text);
        String st="Dear "+name.getText().toString()+", you are about to pay <b>Ksh."+amount.getText().toString()+"</b> using mobile number <b>"+phone.getText().toString()+"</b>.Terms and  conditions apply";
        message.setText(Html.fromHtml(st));
        view.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale_up));
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                initiatePayment(name.getText().toString().trim(),email.getText().toString().trim()
                        ,amount.getText().toString().trim(),phone.getText().toString().trim());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void initiatePayment(String fullName, String em, String am, String ph) {
        if(!PaymentsUtil.isNetworkAvailable(getContext())){
            Snackbar.make(payBtn,"Could not connect to internet.Please check your network",Snackbar.LENGTH_SHORT).show();
            return;
        }
        Call<ResponseBody> call = PaymentsUtil.getInstance().initiatePayment(fullName, em, am, ph);
        Toast.makeText(getContext(), "Initiating payments...", Toast.LENGTH_LONG).show();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    String body;
                    try {
                         body=response.body().string();
                        JSONObject jsonObject=new JSONObject(body);
                        String link=jsonObject.getString("link");
                        Intent intent=new Intent(getContext(), MyWebView.class);
                        intent.putExtra("url",link);
                        startActivity(intent);
                        if(getActivity()!=null){
                            getActivity().finish();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getContext(), "Payment did not complete successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Payment did not complete successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Res_UserInfo userInfo = Support.AccessUserInfo(getContext());
        name.setText(userInfo.getFirst_name() + " " + userInfo.getLast_name());
        email.setText(userInfo.getEmail());
        phone.setText(userInfo.getPhone_number());
    }

    private TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(3));
        return shake;
    }

}
