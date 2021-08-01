package com.shamlatech.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.shamlatech.api_response.Result_UserInfo;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.BaseFragment;
import com.shamlatech.school_management.IntroActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.services.MessageService;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;
import com.squareup.picasso.Picasso;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.App_Start;
import static com.shamlatech.utils.Vars.Pref_Chat_User_ID;
import static com.shamlatech.utils.Vars.staUserInfo;

public class ProfileActivity extends BaseFragment implements View.OnClickListener {

    ImageView imgProfilePic;
    TextView txtName, txtEditProfile;
    TextView txtSignout;
    RelativeLayout relativeAboutApp, relativeHelpSupport, relativeSendFeedback;
    ToggleButton toggleSoundNotification, toggleVibrateNotification, toggleDND;
    Activity activity;

    Dialog pick_Dialog;
    String strSound = "0";
    String strVibrate = "0";
    String strDND = "0";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_teacher_profile, container, false);
        declareDb(activity);

        imgProfilePic = view.findViewById(R.id.imgProfilePic);

        txtName = view.findViewById(R.id.txtSubject);
        txtEditProfile = view.findViewById(R.id.txtEditProfile);
        txtSignout = view.findViewById(R.id.txtSignout);

        relativeAboutApp = view.findViewById(R.id.relativeAboutApp);
        relativeHelpSupport = view.findViewById(R.id.relativeHelpSupport);
        relativeSendFeedback = view.findViewById(R.id.relativeSendFeedback);

        toggleSoundNotification = view.findViewById(R.id.toggleSoundNotification);
        toggleVibrateNotification = view.findViewById(R.id.toggleVibrateNotification);
        toggleDND = view.findViewById(R.id.toggleDND);

        relativeAboutApp.setOnClickListener(this);
        relativeHelpSupport.setOnClickListener(this);
        relativeSendFeedback.setOnClickListener(this);
        txtEditProfile.setOnClickListener(this);
        txtSignout.setOnClickListener(this);

        PreparePage();

        toggleSoundNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                strSound = b ? "1" : "0";
                updateNotification();
            }
        });

        toggleVibrateNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                strVibrate = b ? "1" : "0";
                updateNotification();
            }
        });

        toggleDND.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                strDND = b ? "1" : "0";
                updateNotification();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) activity).declareAppBar1("Settings", false);
        ((BaseActivity) activity).UpdateAppBarColor(R.color.colorTabPrimary);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }


    public void updateNotification() {
        getRetro_UpdateNotification(staUserInfo.getId(), staUserInfo.getRole(), strSound, strVibrate, strDND);
    }

    public static void ShowImage(Context mCon, ImageView img, String url) {
        if (url != null && !url.equals(""))
            Picasso.with(mCon).load(url).fit().into(img);
    }

    public void PreparePage() {
        txtName.setText(staUserInfo.getFirst_name() + " " + staUserInfo.getMiddle_name() + " " + staUserInfo.getLast_name());
        ShowImage(activity, imgProfilePic, staUserInfo.getImage());
        strSound = staUserInfo.getSound_notification();
        strVibrate = staUserInfo.getVibrate_notification();
        strDND = staUserInfo.getDo_not_disturb();


        if (staUserInfo.getSound_notification().equals("0")) {
            toggleSoundNotification.setChecked(false);
        } else {
            toggleSoundNotification.setChecked(true);
        }

        if (staUserInfo.getVibrate_notification().equals("0")) {
            toggleVibrateNotification.setChecked(false);
        } else {
            toggleVibrateNotification.setChecked(true);
        }

        if (staUserInfo.getDo_not_disturb().equals("0")) {
            toggleDND.setChecked(false);
        } else {
            toggleDND.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeAboutApp:
                break;
            case R.id.relativeHelpSupport:
                break;
            case R.id.relativeSendFeedback:
                showFeedback();
                break;
            case R.id.txtEditProfile:
                startActivity(new Intent(activity, ProfileUpdateActivity.class));
                break;
            case R.id.txtSignout:
                App_Start = "0";
                db.ClearTable("chatmessage");
                db.ClearTable("messagelist");
                db.ClearTable("school_event");
                db.ClearTable("teachers_class");
                db.ClearTable("teachers_student");
                db.ClearTable("teachers_subject");
                Support.SetPref(activity, Vars.Pref_User_ID, "");
                Support.SetPref(activity, Vars.Pref_User_Info, "");
                Support.SetPref(activity, Pref_Chat_User_ID, "");
                staUserInfo = null;
                activity.stopService(new Intent(activity, MessageService.class));
                Intent in = new Intent(activity, IntroActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                break;
        }
    }

    private void showFeedback() {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(activity);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_feed_back);

            final EditText edtFeedback;
            TextView txtFeedbackReport;
            ImageView imgSend;

            edtFeedback = pick_Dialog.findViewById(R.id.edtFeedback);
            txtFeedbackReport = pick_Dialog.findViewById(R.id.txtFeedbackReport);
            imgSend = pick_Dialog.findViewById(R.id.imgSend);

            setSpanClicked(txtFeedbackReport);

            imgSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strTopic = edtFeedback.getText().toString().trim();
                    if (strTopic.equals("")) {
                        Toast.makeText(activity, "Please enter the feedback", Toast.LENGTH_LONG).show();
                    } else {
                        pick_Dialog.dismiss();
                        getRetro_SendFeedback(staUserInfo.getId(), staUserInfo.getRole(), strTopic);
                    }
                }
            });

            pick_Dialog.show();
        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showFeedback();
        } else {
            pick_Dialog = null;
            showFeedback();
        }
    }


    public void setSpanClicked(TextView tv) {

        SpannableString ss = new SpannableString("We will respond via email to feedback and questions. You may also send feedback to feedback@classteacher.com");
        ClickableSpan clickableSpanMailFeedback = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Toast.makeText(activity, "Mail", Toast.LENGTH_LONG).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setFakeBoldText(true);
                ds.setColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        };
        ss.setSpan(clickableSpanMailFeedback, 83, 108, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(ss);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setHighlightColor(Color.TRANSPARENT);
    }

    public void getRetro_UpdateNotification(String user_id, String role_id, String sound, String vibrate, String dnd) {
        getRetro_Call(activity, service.getUpdateNotifications(user_id, role_id, sound, vibrate, dnd),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_UserInfo mPojo_User = onPojoBuilder(objectResponse, Result_UserInfo.class);
                        if (mPojo_User != null) {
                            staUserInfo = mPojo_User.getUser_info();
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }


    public void getRetro_SendFeedback(String user_id, String role_id, String feedback) {
        getRetro_Call(activity, service.getSendFeedback(user_id, role_id, feedback),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Toast.makeText(activity, "Thanks for your feedback", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }
}
