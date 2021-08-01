package com.shamlatech.school_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.constants.Transtype;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.Env;
import com.androidstudy.daraja.util.TransactionType;
import com.shamlatech.utils.Vars;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MPESAExpressActivity extends AppCompatActivity {
    LinearLayout linearRoleParent, linearRoleTeacher;
    RelativeLayout relativeRoleParent, relativeRoleTeacher;
    String user_id;
    Integer amount=270;
    ImageView imgRoleParent, imgRoleTeacher;
    @BindView(R.id.editTextPhoneNumber)
    EditText editTextPhoneNumber;
    @BindView(R.id.sendButton)
    Button sendButton;

    //Declare Daraja :: Global Variable
    Daraja daraja;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesaexpress);
        ButterKnife.bind(this);
        linearRoleParent = findViewById(R.id.linearTerm);
        linearRoleTeacher = findViewById(R.id.linearYear);
        relativeRoleParent = findViewById(R.id.relativeRoleParent);
        relativeRoleTeacher = findViewById(R.id.relativeRoleTeacher);
        sendButton = findViewById(R.id.sendButton);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);

        prepareBundle();
        relativeRoleParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentActive();
                amount=100;
            }
        });
        linearRoleTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherActive();
                amount=270;
            }
        });

        //Init Daraja
        //TODO :: REPLACE WITH YOUR OWN CREDENTIALS  :: THIS IS SANDBOX DEMO
        daraja = Daraja.with("Uku3wUhDw9z0Otdk2hUAbGZck8ZGILyh", "JDjpQBm5HpYwk38b", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(MPESAExpressActivity.this.getClass().getSimpleName(), accessToken.getAccess_token());
                Toast.makeText(MPESAExpressActivity.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(MPESAExpressActivity.this.getClass().getSimpleName(), error);
            }
        });

        //TODO :: THIS IS A SIMPLE WAY TO DO ALL THINGS AT ONCE!!! DON'T DO THIS :)
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Phone Number from User Input
                phoneNumber = editTextPhoneNumber.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumber)) {
                    editTextPhoneNumber.setError("Please Provide a Phone Number");
                    return;
                }

                //TODO :: REPLACE WITH YOUR OWN CREDENTIALS  :: THIS IS SANDBOX DEMO
                LNMExpress lnmExpress = new LNMExpress(
                        "174379",
                        "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                        TransactionType.CustomerBuyGoodsOnline, // TransactionType.CustomerPayBillOnline  <- Apply any of these two
                        ""+amount,
                        "254708374149",
                        "174379",
                        phoneNumber,
                        "http://mycallbackurl.com/checkout.php",
                        "001ABC",
                        "App Subscription"
                );

                //This is the
                daraja.requestMPESAExpress(lnmExpress,
                        new DarajaListener<LNMResult>() {
                            @Override
                            public void onResult(@NonNull LNMResult lnmResult) {
                                Log.i(MPESAExpressActivity.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                            }

                            @Override
                            public void onError(String error) {
                                Log.i(MPESAExpressActivity.this.getClass().getSimpleName(), error);
                            }
                        }
                );
            }
        });
    }

    public void ParentActive() {
        relativeRoleParent.setSelected(true);
        relativeRoleTeacher.setSelected(false);

    }

    public void TeacherActive() {
        relativeRoleParent.setSelected(false);
        relativeRoleTeacher.setSelected(true);

    }
    public void prepareBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("id")) {
                user_id = bundle.getString("id");
            }
        }
    }


}
