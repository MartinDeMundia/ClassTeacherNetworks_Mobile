package com.shamlatech.school_management;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.widget.LikeView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static java.sql.DriverManager.println;

public class FBComments extends Activity {

    LinearLayout btnLoginToLike;
    LikeView likeView;
    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //noinspection deprecation
        FacebookSdk.sdkInitialize(getApplicationContext());


        try {
            PackageInfo info = getPackageManager().getPackageInfo("bbr.classteacher.app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:",
                        "KeyHash: "
                                + Base64.encodeToString(md.digest(),
                                Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("KeyHash !!!!!!!:", e.getMessage());

        } catch (NoSuchAlgorithmException e) {
            Log.i("KeyHash $$$$$$$:", e.getMessage());
        }


        setContentView(R.layout.fbcomment);
        //FacebookSdk.setApplicationId("848642805548988");

        initInstances();
        initCallbackManager();
        refreshButtonsState();

        ///likeView = findViewById(R.id.likeView);//////

        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
        likeView.setObjectIdAndType(
                "http://inthecheesefactory.com/blog/understand-android-activity-launchmode/en",
                LikeView.ObjectType.OPEN_GRAPH);

    }

    private void initInstances() {
        btnLoginToLike = findViewById(R.id.btnLoginToLike);
        likeView = findViewById(R.id.likeView);
        //noinspection deprecation
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        //noinspection deprecation
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);

        btnLoginToLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(FBComments.this, Arrays.asList("public_profile"));
            }
        });
    }

    private void initCallbackManager() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                refreshButtonsState();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
    }

    private void refreshButtonsState() {
        if (!isLoggedIn()) {
            btnLoginToLike.setVisibility(View.VISIBLE);
            likeView.setVisibility(View.GONE);
        } else {
            btnLoginToLike.setVisibility(View.GONE);
            likeView.setVisibility(View.VISIBLE);
        }
    }

    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle Facebook Login Result
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

