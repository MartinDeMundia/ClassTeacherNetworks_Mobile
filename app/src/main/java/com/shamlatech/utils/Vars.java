package com.shamlatech.utils;

import android.os.Environment;
import android.view.View;

import com.shamlatech.api_response.Res_Notification;
import com.shamlatech.api_response.Res_Student_Info;
import com.shamlatech.api_response.Res_UserInfo;
import com.shamlatech.school_management.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ADMIN on 06-Apr-18.
 */

public class Vars {

    public static final String MyId = "1";
    public static final String APP_Name = "Class Teacher";
    public static final String APP_Version = "1.0.0";
    public static final String MyPref = "SchoolManagement";
   // public static final String FirebaseURL = "https://classteacher-215505.firebaseio.com/";
   public static final String FirebaseURL = "https://classteacher-ada9f.firebaseio.com";
    public static final String FirebaseDB = "SchoolManagement/Android_Dev_1";
  //  public static final String FirebaseStorageURL = "gs://classteacher-215505.appspot.com";
    public static final String FirebaseStorageURL = "gs://classteacher-ada9f.appspot.com";
    public static final String FirebaseUsername = "Admin@admin.com";
    public static final String FirebasePassword = "Admin@123";
    public static String CurrentScreen = "";

    public static final File roots = new File(Environment.getExternalStorageDirectory(), "/classteacher");

    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static View Custom_Progress = null;

    public static final String Static_URL = "http://shamlatech.net/android_portal/school/";
    public static final String Static_Attachment = "http://shamlatech.net/android_portal/school/upload.php";


    public static String Development_URL = "http://shamlatech.net/school_new/index.php/API/";
    public static final String Development_Attachment = Development_URL + "getUploadMedia";

    //public static String Live_URL = "http://classteacher.coreict.co.ke/API/";
    public static String Live_URL = "http://apps.classteacher.school/API/";//removed the index.php
    //public static String Live_URL = "http://192.168.100.16/apps.classteacher.school/API/";

    //public static String Live_URL = "http://192.168.1.210/API/";
    public static final String Live_Attachment = Live_URL + "getUploadMedia";
  public static final String Live_Attachment_Assignment_Soln = Live_URL + "getUploadAssignments";

    public static String IntroContent[] = {"Follow your child's progress in school and get information on school activities",
            "Plan your classes & assignments, monitor class attendance and student behavior",
            "Be there in every step of your child, student or school's journey"};

    public static String SMS_Replace_Front = "";
    public static String SMS_Replace_Rear = "";
    public static String SMS_SenderNumber = "9843805214";

    public static String MessageServiceFlag = "0";
    public static String App_Start = "0";


    public static ArrayList<Res_Notification> listNotification = new ArrayList<>();

    public static final String DatePattern1 = "dd-MM-yyyy hh:mm a";
    public static final String DatePattern2 = "EEE, MMM dd yyyy hh:mm a";
    public static final String DatePattern3 = "dd-MM-yyyy hh:mm a";
    public static final String DatePattern6 = "dd.MM.yyyy";
    public static final String DatePattern7 = "dd/MM/yyyy";
    public static final String DatePattern8 = "dd-MM-yyyy";
    public static final String DatePattern9 = "yyyy-MM-dd";
    public static final String DatePattern10 = "yyyy-MM-dd HH:mm:ss";

    public static Integer IntroBack[] = {R.drawable.img_intro_back_1, R.drawable.img_intro_back_2, R.drawable.img_intro_back_3};
    public static Integer StudentAppBarList[] = {R.color.colorTabLavender, R.color.colorTabGreen,
            R.color.colorTabBlue, R.color.colorTabRed, R.color.colorTabOrange, R.color.colorBtnLavenderNormal};

    public static Integer TeacherAppBarAdditionalList[] = {R.color.colorTabLavender, R.color.colorTabRed,
            R.color.colorTabBlue,R.color.colorTabLavender};

    public static Integer ParentAppBarAdditionalList[] = {R.color.colorTabChristi, R.color.colorTabLavender, R.color.colorTabRed,
            R.color.colorTabBlue,R.color.colorTabChristi};

    //Dummy Data
    public static String strNewMessageCount = "0";

    public static final String Cons_Attachment_Document = "1";
    public static final String Cons_Attachment_Image = "2";
    public static final String Cons_Attachment_Video = "3";

    public static final String ForumTypeT_T = "1";
    public static final String ForumTypeT_P = "2";

    public static final String Role_Parent = "1";
    public static final String Role_Teacher = "2";
    public static final String Role_Principal = "3";


    public static Res_UserInfo staUserInfo = null;
    public static Res_Student_Info staStudentInfo = null;

    public static final String Assignment_Status_Pending = "0";
    public static final String Assignment_Status_Submitted = "1";
    public static final String Assignment_Status_Not_Submitted = "2";
    public static final String Assignment_Status_Cancel = "3";

    public static final String Pref_User_ID = "Pref_User_ID";
    public static final String Pref_Chat_User_ID = "Pref_Chat_User_ID";
    public static final String Pref_User_Info = "Pref_User_Info";
    public static final String Pref_RefreshChat = "Pref_RefreshChat";
    public static final String Pref_RefreshNewChat = "Pref_RefreshNewChat";
    public static final String Pref_DB_Version = "Pref_DB_Version";

    public static final String Message_Status_Mine = "0";
    public static final String Message_Status_Sent = "1";
    public static final String Message_Status_Received = "2";
    public static final String Message_Status_Read = "3";

    public static final String Refresh_Name_Announcement = "0";
    public static String Refresh_First_Announcement = "0";


    public static String Refresh_Recent_Announcement = "1";
    public static String Refresh_Stud_Teacher = "1";
    public static String Refresh_Stud_Education = "1";
    public static String Refresh_Stud_Behaviour = "1";
    public static String Refresh_Stud_Health = "1";
    public static String Refresh_Stud_Attendance = "1";
    public static String Refresh_Document = "1";
    public static String Refresh_Photo = "1";
    public static String Refresh_Video = "1";
    public static String Refresh_Forum_T_T = "1";
    public static String Refresh_Forum_T_P = "1";


    public static final int REQUEST_PHONE_CALL = 123;

}


