package com.shamlatech.school_management;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shamlatech.activity.MessageListActivity;
import com.shamlatech.activity.NotificationActivity;
import com.shamlatech.activity.ProfileActivity;
import com.shamlatech.activity.SearchActivity;
import com.shamlatech.activity.parent.ParentsDashboard;
import com.shamlatech.activity.teacher.StudentInfoActivity;
import com.shamlatech.activity.teacher.TeachersDashboard;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.RuntimePermissionsActivity;
import com.shamlatech.utils.Vars;

import static com.shamlatech.utils.Support.ConvertToInteger;
import static com.shamlatech.utils.Support.ShowImage;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Martin Mundia Mugambi on 10-03-2020.
 */

public class BaseActivity extends RuntimePermissionsActivity implements View.OnClickListener, API_Calls.APIResInter {

    final int SEARCH_RESULT = 123;
    public DBAdapter db;
    public RelativeLayout relativeAppBar;
    public ImageView imgAppBack, imgAppSearch, imgAppMenu, imgAppBarImage, imgAppDownload, imgAppRemove;
    public TextView txtAppHeader, txtTyping;
    public LinearLayout linearMenuDashboard, linearMenuMessage, linearMenuNotification,linearMenuShareApp, linearMenuProfile,linearSurvey;
    public ImageView imgMenuDashboard, imgMenuMessage, imgMenuNotification,imgMenuShareApp, imgMenuProfile;
    public View viewIndicatorSurvey,viewIndicatorDashboard, viewIndicatorMessage, viewIndicatorNotification,viewIndicatorShareApp, viewIndicatorProfile;
    public PopupMenu popup;
    View includeAppBar1, includeAppBar3, includeAppBar8;
    String StudId = "";

    public void declareDb() {
        try {
            db = new DBAdapter(this, this.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }
    }

    public void declareAppBar1(String headerName, boolean Back) {
        includeAppBar1 = findViewById(R.id.includeAppBar1);
        includeAppBar3 = findViewById(R.id.includeAppBar3);
        includeAppBar8 = findViewById(R.id.includeAppBar8);

        includeAppBar1.setVisibility(View.VISIBLE);
        includeAppBar3.setVisibility(View.GONE);
        includeAppBar8.setVisibility(View.GONE);

        imgAppBack = findViewById(R.id.imgAppBack1);
        relativeAppBar = findViewById(R.id.relativeAppBar1);
        imgAppSearch = findViewById(R.id.imgAppSearch1);
        txtAppHeader = findViewById(R.id.txtAppBarHeader1);

        txtAppHeader.setText(headerName);

        if (Back)
            imgAppBack.setVisibility(View.VISIBLE);
        else imgAppBack.setVisibility(View.GONE);
        imgAppBack.setOnClickListener(this);
        imgAppSearch.setOnClickListener(this);

        declareDb();
    }

    public void declareAppBar2(String headerName, String currentScreen) {
        imgAppBack = findViewById(R.id.imgAppBack);
        relativeAppBar = findViewById(R.id.relativeAppBar);
        txtAppHeader = findViewById(R.id.txtAppBarHeader);

        txtAppHeader.setText(headerName);

        imgAppBack.setOnClickListener(this);

        Vars.CurrentScreen = currentScreen;
        declareDb();
    }

    public void declareAppBar3(String headerName, String imgURL, String currentScreen, String student_id) {

        includeAppBar1 = findViewById(R.id.includeAppBar1);
        includeAppBar3 = findViewById(R.id.includeAppBar3);
        includeAppBar8 = findViewById(R.id.includeAppBar8);

        includeAppBar1.setVisibility(View.GONE);
        includeAppBar3.setVisibility(View.VISIBLE);
        includeAppBar8.setVisibility(View.GONE);

        imgAppBack = findViewById(R.id.imgAppBack3);
        imgAppSearch = findViewById(R.id.imgAppSearch3);
        txtAppHeader = findViewById(R.id.txtAppBarHeader3);
        imgAppBarImage = findViewById(R.id.imgAppBarImage3);
        relativeAppBar = findViewById(R.id.relativeAppBar3);

        txtAppHeader.setText(headerName);

        imgAppBack.setOnClickListener(this);
        imgAppSearch.setOnClickListener(this);
        imgAppBarImage.setOnClickListener(this);
        txtAppHeader.setOnClickListener(this);

        ShowImage(this, imgAppBarImage, imgURL);

        StudId = student_id;

        Vars.CurrentScreen = currentScreen;
        declareDb();
    }

    public void declareAppBar4(String headerName, String currentScreen) {
        imgAppBack = findViewById(R.id.imgAppBack);
        relativeAppBar = findViewById(R.id.relativeAppBar);
        imgAppMenu = findViewById(R.id.imgAppMenu);
        txtAppHeader = findViewById(R.id.txtAppBarHeader);

        txtAppHeader.setText(headerName);

        imgAppBack.setOnClickListener(this);
        imgAppMenu.setOnClickListener(this);

        Vars.CurrentScreen = currentScreen;
        declareDb();
    }

    public void declareAppBar5(String headerName, String currentScreen) {
        imgAppBack = findViewById(R.id.imgAppBack);
        relativeAppBar = findViewById(R.id.relativeAppBar);
        txtAppHeader = findViewById(R.id.txtAppBarHeader);

        txtAppHeader.setText(headerName);

        imgAppBack.setOnClickListener(this);

        Vars.CurrentScreen = currentScreen;
        declareDb();
    }

    public void declareAppBar6(String headerName, String imgURL, String currentScreen) {
        imgAppBack = findViewById(R.id.imgAppBack);
        txtAppHeader = findViewById(R.id.txtAppBarHeader);
        imgAppBarImage = findViewById(R.id.imgAppBarImage);
        relativeAppBar = findViewById(R.id.relativeAppBar);
        txtTyping = findViewById(R.id.txtTyping);

        txtAppHeader.setText(headerName);

        imgAppBack.setOnClickListener(this);

        ShowImage(this, imgAppBarImage, imgURL);

        Vars.CurrentScreen = currentScreen;
        declareDb();
    }

    public void declareAppBar7(String headerName, String currentScreen) {
        imgAppBack = findViewById(R.id.imgAppBack);
        relativeAppBar = findViewById(R.id.relativeAppBar);
        imgAppDownload = findViewById(R.id.imgAppDownload);
        txtAppHeader = findViewById(R.id.txtAppBarHeader);
        imgAppRemove = findViewById(R.id.imgAppRemove);

        txtAppHeader.setText(headerName);

        imgAppBack.setOnClickListener(this);
        imgAppDownload.setOnClickListener(this);
        imgAppRemove.setOnClickListener(this);

        Vars.CurrentScreen = currentScreen;
        declareDb();
    }

    public void declareAppBar8(String headerName) {
        includeAppBar1 = findViewById(R.id.includeAppBar1);
        includeAppBar3 = findViewById(R.id.includeAppBar3);
        includeAppBar8 = findViewById(R.id.includeAppBar8);

        includeAppBar1.setVisibility(View.GONE);
        includeAppBar3.setVisibility(View.GONE);
        includeAppBar8.setVisibility(View.VISIBLE);

        imgAppBack = findViewById(R.id.imgAppBack8);
        relativeAppBar = findViewById(R.id.relativeAppBar8);
        txtAppHeader = findViewById(R.id.txtAppBarHeader8);

        txtAppHeader.setText(headerName);
        imgAppBack.setOnClickListener(this);

    }

    public void UpdateAppBarColor(Integer color) {
        int statusBarColor = 0;
        if (color == R.color.colorTabLavender) {
            statusBarColor = Color.parseColor("#5E2F80");
        } else if (color == R.color.colorTabGreen) {
            statusBarColor = Color.parseColor("#619f44");
        } else if (color == R.color.colorTabBlue) {
            statusBarColor = Color.parseColor("#2599ca");
        } else if (color == R.color.colorTabRed) {
            statusBarColor = Color.parseColor("#e74446");
        } else if (color == R.color.colorTabOrange) {
            statusBarColor = Color.parseColor("#da8527");
        } else if (color == R.color.colorTabChristi) {
            statusBarColor = Color.parseColor("#6c8708");
        } else if (color == R.color.colorTabPrimary) {
            statusBarColor = Color.parseColor("#018f86");
        }
        relativeAppBar.setBackgroundColor(getResources().getColor(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);

        }
    }

    public void UpdateAppBarHeader(String strHeader) {
        txtAppHeader.setText(strHeader);
    }

    public void declareBottomBar() {

        linearMenuMessage = findViewById(R.id.linearMenuMessage);
        linearMenuNotification = findViewById(R.id.linearMenuNotification);
        linearMenuProfile = findViewById(R.id.linearMenuProfile);
        linearMenuDashboard = findViewById(R.id.linearMenuDashboard);
        linearMenuShareApp =   findViewById(R.id.linearMenuShareApp);

        viewIndicatorMessage = findViewById(R.id.viewIndicatorMessage);
        viewIndicatorNotification = findViewById(R.id.viewIndicatorNotification);
        viewIndicatorProfile = findViewById(R.id.viewIndicatorProfile);
        viewIndicatorDashboard = findViewById(R.id.viewIndicatorDashboard);
        viewIndicatorShareApp = findViewById(R.id.viewIndicatorShareApp);

        imgMenuMessage = findViewById(R.id.imgMenuMessage);
        imgMenuNotification = findViewById(R.id.imgMenuNotification);
        imgMenuProfile = findViewById(R.id.imgMenuProfile);
        imgMenuDashboard = findViewById(R.id.imgMenuDashboard);
        imgMenuShareApp = findViewById(R.id.imgMenuShareApp);

        linearMenuMessage.setOnClickListener(this);
        linearMenuNotification.setOnClickListener(this);
        linearMenuProfile.setOnClickListener(this);
        linearMenuDashboard.setOnClickListener(this);
        linearMenuShareApp.setOnClickListener(this);


        viewIndicatorMessage.setVisibility(View.GONE);
        viewIndicatorNotification.setVisibility(View.GONE);
        viewIndicatorProfile.setVisibility(View.GONE);
        viewIndicatorDashboard.setVisibility(View.GONE);
        viewIndicatorShareApp.setVisibility(View.GONE);

        if (staUserInfo != null)
            ShowImage(this, imgMenuProfile, staUserInfo.getImage());
        updateBottomBar();
    }

    public void updateBottomBar() {

        Vars.strNewMessageCount = db.AccessUnreadCount(staUserInfo.getId() + "_" + staUserInfo.getRole());
        if (ConvertToInteger(Vars.strNewMessageCount) > 0) {
            imgMenuMessage.setImageResource(R.drawable.ic_menu_message_active);
        } else {
            imgMenuMessage.setImageResource(R.drawable.ic_menu_message_normal);
        }

        if (ConvertToInteger(staUserInfo.getUnread_notifications()) > 0) {
            imgMenuNotification.setImageResource(R.drawable.ic_menu_notification_active);
        } else {
            imgMenuNotification.setImageResource(R.drawable.ic_menu_notification_normal);
        }
    }

    public void activeBottomBar(String page) {
        if (page.equals("message")) {
            viewIndicatorMessage.setVisibility(View.VISIBLE);
            viewIndicatorNotification.setVisibility(View.GONE);
            viewIndicatorProfile.setVisibility(View.GONE);
            viewIndicatorDashboard.setVisibility(View.GONE);
        } else if (page.equals("notification")) {
            viewIndicatorMessage.setVisibility(View.GONE);
            viewIndicatorNotification.setVisibility(View.VISIBLE);
            viewIndicatorProfile.setVisibility(View.GONE);
            viewIndicatorDashboard.setVisibility(View.GONE);
        } else if (page.equals("profile")) {
            viewIndicatorMessage.setVisibility(View.GONE);
            viewIndicatorNotification.setVisibility(View.GONE);
            viewIndicatorProfile.setVisibility(View.VISIBLE);
            viewIndicatorDashboard.setVisibility(View.GONE);
        } else if (page.equals("dashboard")) {
            viewIndicatorMessage.setVisibility(View.GONE);
            viewIndicatorNotification.setVisibility(View.GONE);
            viewIndicatorProfile.setVisibility(View.GONE);
            viewIndicatorDashboard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAppBack1:
            case R.id.imgAppBack3:
            case R.id.imgAppBack:
            case R.id.imgAppBack8:
                onBackPressed();
                break;
            case R.id.imgAppSearch3:
            case R.id.imgAppSearch1:
                Intent in = new Intent(BaseActivity.this, SearchActivity.class);
                startActivityForResult(in, SEARCH_RESULT);
                break;
            case R.id.linearMenuDashboard:
                if (staUserInfo.getRole().equals(Vars.Role_Teacher) || staUserInfo.getRole().equals(Vars.Role_Principal)) {
                    if (!Vars.CurrentScreen.equals("TeachersDashboard")) {
                        startActivity(new Intent(BaseActivity.this, TeachersDashboard.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                } else {
                    if (!Vars.CurrentScreen.equals("ParentsDashboard")) {
                        startActivity(new Intent(BaseActivity.this, ParentsDashboard.class));
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                }
                break;
            case R.id.linearMenuMessage:
                if (!Vars.CurrentScreen.equals("MessageListActivity")) {
                    startActivity(new Intent(BaseActivity.this, MessageListActivity.class));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
                break;
            case R.id.linearMenuNotification:
                if (!Vars.CurrentScreen.equals("NotificationActivity")) {
                    startActivity(new Intent(BaseActivity.this, NotificationActivity.class));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
                break;

            case R.id.linearMenuShareApp:
                if (!Vars.CurrentScreen.equals("ShareAppActivity")) {
                    startActivity(new Intent(BaseActivity.this, ShareAppActivity.class));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
                break;

            case R.id.linearMenuProfile:
                if (!Vars.CurrentScreen.equals("ProfileActivity")) {
                    startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
                break;



            case R.id.imgAppMenu:
                popup.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }


}
