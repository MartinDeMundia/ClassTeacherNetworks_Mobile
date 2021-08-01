package com.shamlatech.school_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.shamlatech.activity.AnnouncementViewActivity;
import com.shamlatech.activity.ForumViewActivity;
import com.shamlatech.activity.MessageActivity;
import com.shamlatech.activity.MessageListActivity;
import com.shamlatech.activity.NotificationActivity;
import com.shamlatech.activity.ProfileActivity;
import com.shamlatech.activity.parent.ParentStudentActivity;
import com.shamlatech.activity.parent.ParentsAdditionalActivity;
import com.shamlatech.activity.parent.ParentsDashboard;
import com.shamlatech.activity.teacher.ClassToolsActivity;
import com.shamlatech.activity.teacher.StudentInfoActivity;
import com.shamlatech.activity.teacher.TeachersAdditionalActivity;
import com.shamlatech.activity.teacher.TeachersDashboard;
import com.shamlatech.activity.teacher.TeachersStudentActivity;
import com.shamlatech.services.MessageService;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.Vars;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.App_Start;
import static com.shamlatech.utils.Vars.MessageServiceFlag;
import static com.shamlatech.utils.Vars.Role_Principal;
import static com.shamlatech.utils.Vars.staUserInfo;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    final String CONS_NOTIFICATION_TYPE_UPDATE_TEACHER = "1";
    final String CONS_NOTIFICATION_TYPE_UPDATE_MARKS = "2";
    final String CONS_NOTIFICATION_TYPE_UPDATE_ASSIGNMENT = "3";
    final String CONS_NOTIFICATION_TYPE_UPDATE_BEHAVIOUR = "4";
    final String CONS_NOTIFICATION_TYPE_UPDATE_HEALTH = "5";
    final String CONS_NOTIFICATION_TYPE_UPDATE_ATTENDANCE = "6";
    final String CONS_NOTIFICATION_TYPE_UPDATE_EVENTS = "7";
    final String CONS_NOTIFICATION_TYPE_UPDATE_DOCUMENT = "8";
    final String CONS_NOTIFICATION_TYPE_UPDATE_PHOTO = "9";
    final String CONS_NOTIFICATION_TYPE_UPDATE_VIDEO = "10";
    final String CONS_NOTIFICATION_TYPE_CREATE_FORUMS_TEACHER = "11";
    final String CONS_NOTIFICATION_TYPE_CREATE_FORUMS_PARENT = "12";
    final String CONS_NOTIFICATION_TYPE_REPLY_FORUMS = "13";
    final String CONS_NOTIFICATION_TYPE_NEW_ANNOUNCEMENT = "14";
    final String CONS_NOTIFICATION_TYPE_UPDATE_PROFILE = "15";
    final String CONS_NOTIFICATION_TYPE_MESSAGE = "16";
    final String CONS_NOTIFICATION_TYPE_TIMETABLE = "17";
    FrameLayout frameContent;
    int prevFragmentPosition = 1;
    String role = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        declareDb();
        declareAppBar1("My Dashboard", false);
        declareBottomBar();

        frameContent = findViewById(R.id.frameContent);

        if (MessageServiceFlag.equals("0")) {
            try {
                MessageServiceFlag = "1";
                startService(new Intent(HomeActivity.this, MessageService.class));
            } catch (Exception e) {
            }
        }

        if (App_Start.equals("0")) {
            getRetro_UpdateToken(FirebaseInstanceId.getInstance().getToken());
            App_Start = "1";
        }
        linearMenuDashboard.callOnClick();
        prepareBundle();
    }

    public void UpdateFragment(Fragment fragment, String Title, int currentFragmentPosition) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (prevFragmentPosition > currentFragmentPosition) {
            //animate left
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        } else if (prevFragmentPosition < currentFragmentPosition) {
            //animate right
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        prevFragmentPosition = currentFragmentPosition;
        fragmentTransaction.replace(R.id.frameContent, fragment);
        fragmentTransaction.commit();

    }

    public void UpdateStudentFragment(Fragment fragment, String tab, String stud_id, String innertab) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("tab", tab);
        bundle.putSerializable("stud_id", stud_id);
        bundle.putSerializable("innertab", innertab);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameContent, fragment);
        fragmentTransaction.addToBackStack("test");
        fragmentTransaction.commit();
    }


    public void UpdateTeacherActionFragment(Fragment fragment, String tab) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("tab", tab);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameContent, fragment);
        fragmentTransaction.addToBackStack("test");
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_RESULT && resultCode == RESULT_OK) {
            String Tab = data.getStringExtra("tab");
            String Stud_id = data.getStringExtra("stud_id");
            String place = data.getStringExtra("place");
            if (place.equals("Teacher")) {
                UpdateStudentFragment(new TeachersStudentActivity(), Tab, Stud_id, "");
            } else if (place.equals("Parent")) {
                UpdateStudentFragment(new ParentStudentActivity(), Tab, Stud_id, "");
            } else if (place.equals("Info")) {
                UpdateStudentFragment(new StudentInfoActivity(), Tab, Stud_id, "");
            }

        }
    }

    public void prepareBundle() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("bundle")) {
                Bundle bundle = b.getBundle("bundle");
                String Role_Id = bundle.getString("role_id");
                role = bundle.getString("role_id");
                String Type = bundle.getString("type");
                String Type_Id = bundle.getString("type_id");
                String Stud_Id = bundle.getString("student_id");

                Intent intent = null;
                if (Role_Id.equals(Vars.Role_Teacher) || Role_Id.equals(Role_Principal)) {
                    switch (Type) {
                        case CONS_NOTIFICATION_TYPE_UPDATE_TEACHER:
                            UpdateStudentFragment(new TeachersStudentActivity(), "TEACHER", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_MARKS:
                            UpdateStudentFragment(new TeachersStudentActivity(), "EDUCATION", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_ASSIGNMENT:
                            UpdateStudentFragment(new TeachersStudentActivity(), "EDUCATION", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_BEHAVIOUR:
                            UpdateStudentFragment(new TeachersStudentActivity(), "BEHAVIOUR", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_HEALTH:
                            UpdateStudentFragment(new TeachersStudentActivity(), "HEALTH", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_ATTENDANCE:
                            UpdateStudentFragment(new TeachersStudentActivity(), "ATTENDANCE", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_EVENTS:
                            UpdateStudentFragment(new TeachersAdditionalActivity(), "CALENDAR", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_DOCUMENT:
                            UpdateStudentFragment(new TeachersAdditionalActivity(), "MEDIA", Stud_Id, "DOCUMENT");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_PHOTO:
                            UpdateStudentFragment(new TeachersAdditionalActivity(), "MEDIA", Stud_Id, "PHOTO");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_VIDEO:
                            UpdateStudentFragment(new TeachersAdditionalActivity(), "MEDIA", Stud_Id, "VIDEO");
                            break;
                        case CONS_NOTIFICATION_TYPE_CREATE_FORUMS_TEACHER:
                            UpdateStudentFragment(new TeachersAdditionalActivity(), "FORUMS", Stud_Id, "TEACHER");
                            break;
                        case CONS_NOTIFICATION_TYPE_CREATE_FORUMS_PARENT:
                            UpdateStudentFragment(new TeachersAdditionalActivity(), "FORUMS", Stud_Id, "PARENT");
                            break;
                        case CONS_NOTIFICATION_TYPE_REPLY_FORUMS:
                            intent = new Intent(getApplicationContext(), ForumViewActivity.class)
                                    .putExtra("forum_id", Type_Id);
                            break;
                        case CONS_NOTIFICATION_TYPE_NEW_ANNOUNCEMENT:
                            intent = new Intent(getApplicationContext(), AnnouncementViewActivity.class)
                                    .putExtra("id", Type_Id);
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_PROFILE:
                            UpdateFragment(new ProfileActivity(), "Settings", 4);
                            break;
                        case CONS_NOTIFICATION_TYPE_MESSAGE:

                            String sender_id = bundle.getString("sender_id");
                            String sender_role = bundle.getString("sender_role");
                            String sender_name = bundle.getString("sender_name");
                            String sender_image = bundle.getString("sender_image");

                            intent = new Intent(getApplicationContext(), MessageActivity.class)
                                    .putExtra("id", sender_id)
                                    .putExtra("role", sender_role)
                                    .putExtra("name", sender_name)
                                    .putExtra("image", sender_image);
                            break;
                        case CONS_NOTIFICATION_TYPE_TIMETABLE:
                            UpdateTeacherActionFragment(new ClassToolsActivity(), "TIMETABLE");
                            break;

                    }
                } else {
                    switch (Type) {
                        case CONS_NOTIFICATION_TYPE_UPDATE_TEACHER:
                            UpdateStudentFragment(new ParentStudentActivity(), "TEACHER", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_MARKS:
                            UpdateStudentFragment(new ParentStudentActivity(), "EDUCATION", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_ASSIGNMENT:
                            UpdateStudentFragment(new ParentStudentActivity(), "EDUCATION", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_BEHAVIOUR:
                            UpdateStudentFragment(new ParentStudentActivity(), "BEHAVIOUR", Stud_Id, "BEHAVIOUR");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_HEALTH:
                            UpdateStudentFragment(new ParentStudentActivity(), "HEALTH", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_ATTENDANCE:
                            UpdateStudentFragment(new ParentStudentActivity(), "ATTENDANCE", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_EVENTS:
                            UpdateStudentFragment(new ParentsAdditionalActivity(), "CALENDAR", "", "");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_DOCUMENT:
                            UpdateStudentFragment(new ParentsAdditionalActivity(), "MEDIA", Stud_Id, "DOCUMENT");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_PHOTO:
                            UpdateStudentFragment(new ParentsAdditionalActivity(), "MEDIA", Stud_Id, "PHOTO");
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_VIDEO:
                            UpdateStudentFragment(new ParentsAdditionalActivity(), "MEDIA", Stud_Id, "VIDEO");
                            break;
                        case CONS_NOTIFICATION_TYPE_CREATE_FORUMS_TEACHER:
                            UpdateStudentFragment(new ParentsAdditionalActivity(), "FORUMS", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_CREATE_FORUMS_PARENT:
                            UpdateStudentFragment(new ParentsAdditionalActivity(), "FORUMS", Stud_Id, "");
                            break;
                        case CONS_NOTIFICATION_TYPE_REPLY_FORUMS:
                            intent = new Intent(getApplicationContext(), ForumViewActivity.class)
                                    .putExtra("forum_id", Type_Id);
                            break;
                        case CONS_NOTIFICATION_TYPE_NEW_ANNOUNCEMENT:
                            intent = new Intent(getApplicationContext(), AnnouncementViewActivity.class)
                                    .putExtra("id", Type_Id);
                            break;
                        case CONS_NOTIFICATION_TYPE_UPDATE_PROFILE:
                            UpdateFragment(new ProfileActivity(), "Settings", 4);
                            break;
                        case CONS_NOTIFICATION_TYPE_MESSAGE:

                            String sender_id = bundle.getString("sender_id");
                            String sender_role = bundle.getString("sender_role");
                            String sender_name = bundle.getString("sender_name");
                            String sender_image = bundle.getString("sender_image");

                            intent = new Intent(getApplicationContext(), MessageActivity.class)
                                    .putExtra("id", sender_id)
                                    .putExtra("role", sender_role)
                                    .putExtra("name", sender_name)
                                    .putExtra("image", sender_image);
                            break;
                    }
                }
                if (intent != null)
                    startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearMenuDashboard:
                if (staUserInfo.getRole().equals(Vars.Role_Teacher) || staUserInfo.getRole().equals(Vars.Role_Principal)) {
                    UpdateFragment(new TeachersDashboard(), "My Dashboard", 1);
                } else {
                    UpdateFragment(new ParentsDashboard(), "My Dashboard", 1);
                }
                break;
            case R.id.linearMenuMessage:
                UpdateFragment(new MessageListActivity(), "Messages", 2);
                break;
            case R.id.linearMenuNotification:
                UpdateFragment(new NotificationActivity(), "Notifications", 3);
                break;

            case R.id.linearMenuShareApp:
                UpdateFragment(new ShareAppActivity(), "Share Classteacher App", 4);
                break;

            case R.id.linearMenuProfile:
                UpdateFragment(new ProfileActivity(), "Settings", 5);
                break;

            default:
                super.onClick(v);
        }
    }


    public void getRetro_UpdateToken(String Token) {
        getRetro_Call(HomeActivity.this, service.getUpdateToken(staUserInfo.getId(), staUserInfo.getRole(), Token),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {

                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

}
