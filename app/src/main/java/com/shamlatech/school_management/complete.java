package com.shamlatech.school_management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class complete extends AppCompatActivity {
    String fn ="" , ln="" , ad = "" , apn ="" , prof="";
    String strPhone = "", strPassword = "", strRole = "", strOTP = "";
    private String login_url = "http://apps.classteacher.school/index.php/API/complete";
    EditText f,l,a,ap,p;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreparePage();
        Button save = findViewById(R.id.buttonOk);
        f = findViewById(R.id.edtFirstname);
        l = findViewById(R.id.edtLastname);
        a = findViewById(R.id.edtEmailaddress);
        ap = findViewById(R.id.edtPhone1);
        p = findViewById(R.id.edtprofession);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fn = f.getText().toString();
                ln = l.getText().toString();
                ad = a.getText().toString();
                apn = ap.getText().toString();
                prof = p.getText().toString();

                if (fn=="") {
                    Toast.makeText(getApplicationContext(),"First name is required",Toast.LENGTH_LONG).show();
                }
                    else if(ln==""){
                    Toast.makeText(getApplicationContext(),"Last name is required",Toast.LENGTH_LONG).show();
                }
                else if(ad==""){
                    Toast.makeText(getApplicationContext(),"Address is required",Toast.LENGTH_LONG).show();
                } else if(prof==""){
                    Toast.makeText(getApplicationContext(),"Profession is required",Toast.LENGTH_LONG).show();

                }else {
                    login();
                }
            }
        });


    }

    private void PreparePage() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("phone")) {
                strPhone = bundle.getString("phone");
                strPassword = bundle.getString("password");
                strRole = bundle.getString("role");
                strOTP = bundle.getString("otp");
            }
        }
    }

    public void RedirectSuccess() {
        Handler temp = new Handler();
        Runnable OnlineRunnable = new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(complete.this, LoginActivity.class);
                in.putExtra("role", strRole);
                in .putExtra("phone", strPhone);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
            }
        };
        temp.postDelayed(OnlineRunnable, 3000);
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(complete.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //Populate the request parameters
            request.put("fname", fn);
            request.put("lname", ln);
            request.put("apn", apn);
            request.put("address", ad);
            request.put("prof", prof);
            request.put("p", strPhone);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //Check if user got logged in successfully

                            if (response.getInt("status") == 0) {

                                Toast.makeText(getApplicationContext(),
                                        "An occurred", Toast.LENGTH_SHORT).show();

                            }else if (response.getInt("status") == 1) {
                                RedirectSuccess();
                            }
                            else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }



}
