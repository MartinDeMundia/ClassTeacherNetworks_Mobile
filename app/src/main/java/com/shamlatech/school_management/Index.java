package com.shamlatech.school_management;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.shamlatech.api_response.Result_Group;
import com.shamlatech.api_response.Result_Parent_Student_Details;
import com.shamlatech.api_response.Result_TeacherInfo;
import com.shamlatech.api_response.Result_UserInfo;
import com.shamlatech.services.BGLookupService;
import com.shamlatech.services.MessageService;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import retrofit2.Response;

import static android.support.v4.app.NotificationCompat.*;
import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.AccessUserInfo;
import static com.shamlatech.utils.Support.UpdateProfileInfo;
import static com.shamlatech.utils.Vars.App_Start;
import static com.shamlatech.utils.Vars.Pref_Chat_User_ID;
import static com.shamlatech.utils.Vars.Pref_User_ID;
import static com.shamlatech.utils.Vars.staUserInfo;

public class Index extends  BaseActivity {

//    Alias : ClassTeacher
//    Key Store Password : Teacher@123
//    Key Password : Teacher@1234

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


     /*   button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
*/
        ///addNotification(0);
        ///addNotification(1);

        declareDb();
        prepareBundle();
    }

    private void addNotification(Integer notificationId) {

        // prepare intent which is triggered if the
        // notification is selected

        Intent intent = new Intent(this, NotificationView.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        intent.putExtra("message", "This is a notification message");
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject")
                .setSmallIcon(R.drawable.messageicon)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.messageicon, "Call", pIntent)
                .addAction(R.drawable.messageicon, "More", pIntent)
                .addAction(R.drawable.messageicon, "And more", pIntent).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, n);


}
    public void prepareBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (!Support.GetPrefDefault(Index.this, Vars.Pref_User_ID, "").equals("")) {
                if (Support.GetPref(getApplicationContext(), Pref_User_ID).equals(bundle.getString("user_id"))) {
                    Intent in = new Intent(Index.this, HomeActivity.class);
                    in.putExtra("bundle", bundle);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                } else {
                    RedirectPage();
                }
            } else {
                RedirectPage();
            }
        } else {
            RedirectPage();
        }
    }

    public void RedirectPage() {
        Handler temp = new Handler();
        Runnable OnlineRunnable = new Runnable() {
            @Override
            public void run() {
                if (Support.GetPrefDefault(Index.this, Vars.Pref_User_ID, "").equals("")) {
                    Intent in = new Intent(Index.this, IntroActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(in);
                } else {
                    AccessUserInfo(Index.this);
                    getRetro_AccessUserInfo(Support.GetPrefDefault(Index.this, Vars.Pref_User_ID, ""), staUserInfo.getRole());
                    startService(new Intent(Index.this, BGLookupService.class));
                }
            }
        };
        temp.postDelayed(OnlineRunnable, 2000);
    }

    @Override
    public void onClick(View v) {

    }

    public void RedirectUser() {
        if (staUserInfo.getRole().equals(Vars.Role_Teacher) || staUserInfo.getRole().equals(Vars.Role_Principal)) {
            getRetro_AccessTeacherDetails(staUserInfo.getId(), staUserInfo.getRole());
        } else {
            getRetro_AccessParentDetails(staUserInfo.getId(), staUserInfo.getRole());
        }
    }


    public void getRetro_AccessUserInfo(String user_id, String role_id) {
        getRetro_Call(Index.this, service.getAccessUserInfo(user_id, role_id), false, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_UserInfo mPojo_Userinfo = onPojoBuilder(objectResponse, Result_UserInfo.class);
                if (mPojo_Userinfo != null) {
                    if (mPojo_Userinfo.getStatus().equals(API_Code.Success)) {
                        UpdateProfileInfo(Index.this, mPojo_Userinfo.getUser_info());
                        AccessUserInfo(Index.this);
                        RedirectUser();
                    }
                    else{
                        App_Start = "0";
                        db.ClearTable("chatmessage");
                        db.ClearTable("messagelist");
                        db.ClearTable("school_event");
                        db.ClearTable("teachers_class");
                        db.ClearTable("teachers_student");
                        db.ClearTable("teachers_subject");
                        Support.SetPref(Index.this, Vars.Pref_User_ID, "");
                        Support.SetPref(Index.this, Vars.Pref_User_Info, "");
                        Support.SetPref(Index.this, Pref_Chat_User_ID, "");
                        staUserInfo = null;
                        stopService(new Intent(Index.this, MessageService.class));
                        Intent in = new Intent(Index.this, IntroActivity.class);
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

    public void getRetro_AccessTeacherDetails(final String user_id, final String role_id) {
        getRetro_Call(Index.this, service.getAccessTeachersDetails(user_id, role_id), false, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_TeacherInfo mPojo_TeacherInfo = onPojoBuilder(objectResponse, Result_TeacherInfo.class);
                if (mPojo_TeacherInfo != null) {
                    if (mPojo_TeacherInfo.getStatus().equals(API_Code.Success)) {
                        db.ClearTable("teachers_class");
                        db.ClearTable("teachers_student");
                        db.ClearTable("teachers_timetable");
                        db.ClearTable("student_marks");
                        db.ClearTable("sub_subject_marks");
                        db.InsertTeachersTimeTable(mPojo_TeacherInfo.getTimetable());
                        db.InsertTeachersTeachingSubject(mPojo_TeacherInfo.getTeaching_subjects());
                        db.InsertTeachersClass(mPojo_TeacherInfo.getMy_class());
                        ////db.InsertTeachersClass(mPojo_TeacherInfo.getSubject_class());
                        db.InsertStudentMarks(mPojo_TeacherInfo.getStudentMarks(),user_id,role_id);
                        db.InsertStudentPaperMarks(mPojo_TeacherInfo.getStudentpapermarks(),user_id,role_id);
                        getRetro_AccessTeacherGroup(user_id, role_id);
                    }
                }
            }
            @Override
            public void onFailure(String message) {
                Log.e("Error", message);
            }
        });
    }

    public void getRetro_AccessTeacherGroup(String teacher_id, String role_id) {
        getRetro_Call(Index.this, service.getAccessTeachersGroup(teacher_id, role_id), false, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Group mPojo_Group = onPojoBuilder(objectResponse, Result_Group.class);
                if (mPojo_Group != null) {
                    if (mPojo_Group.getStatus().equals(API_Code.Success)) {
                        db.ClearTable("teachers_group");
                        db.InsertGroup(mPojo_Group.getGroup());
                        Intent in = new Intent(Index.this, HomeActivity.class);
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


    public void getRetro_AccessParentDetails(String user_id, String role_id) {
        getRetro_Call(Index.this, service.getAccessParentDetails(user_id, role_id), false, new API_Calls.OnApiResult() {
            @Override
            public void onSuccess(Response<Object> objectResponse) {
                Result_Parent_Student_Details mPojo_ParentInfo = onPojoBuilder(objectResponse, Result_Parent_Student_Details.class);
                if (mPojo_ParentInfo != null) {
                    if (mPojo_ParentInfo.getStatus().equals(API_Code.Success)) {
                        db.ClearTable("parent_student");
                        db.InsertParentStudent(mPojo_ParentInfo.getStudents());
                        Intent in = new Intent(Index.this, HomeActivity.class);
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

}
