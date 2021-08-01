package com.shamlatech.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shamlatech.api_response.Res_Group;
import com.shamlatech.api_response.Res_Group_Member;
import com.shamlatech.api_response.Res_Look_AbsentReason;
import com.shamlatech.api_response.Res_Look_Behaviour;
import com.shamlatech.api_response.Res_Look_BehaviourContent;
import com.shamlatech.api_response.Res_Look_ExamList;
import com.shamlatech.api_response.Res_Message_List;
import com.shamlatech.api_response.Res_SI_Class_Details;
import com.shamlatech.api_response.Res_SI_School_Details;
import com.shamlatech.api_response.Res_SI_Teacher;
import com.shamlatech.api_response.Res_School_Events;
import com.shamlatech.api_response.Res_Student_Info;
import com.shamlatech.api_response.Res_Student_Marks;
import com.shamlatech.api_response.Res_Teacher_Class;
import com.shamlatech.api_response.Res_Teacher_ClassStudent;
import com.shamlatech.api_response.Res_Teacher_TeachingSubjects;
import com.shamlatech.api_response.Res_Teacher_TimeTable;
import com.shamlatech.pojo.PojoAttendanceKeyPair;
import com.shamlatech.pojo.PojoChatMessage;
import com.shamlatech.pojo.PojoChatMessageDisplay;
import com.shamlatech.pojo.PojoClassWithSectionList;
import com.shamlatech.pojo.PojoLastMessage;
import com.shamlatech.pojo.PojoMessageList;
import com.shamlatech.pojo.PojoParentWithClassList;
import com.shamlatech.pojo.PojoStudentKeyPare;
import com.shamlatech.pojo.PojoStudentWithClassList;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Martin Mundia Mugambi on 24 OCT 2019.
 */

public class DBAdapter extends SQLiteOpenHelper {
    //adb exec-out run-as com.shamlatech.school_management cat files/school.db> d:/school.db
    private final static String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "school_1.db";
    private static final int DATABASE_VERSION = 1;
    private final Context mContext;
    private String pathToSaveDBFile = "";

    public DBAdapter(Context context, String filePath) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        pathToSaveDBFile = new StringBuffer(filePath).append("/").append(DATABASE_NAME).toString();
    }

    public void prepareDatabase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            Log.d(TAG, "Database exists.");
            int currentDBVersion = Support.ConvertToInteger(Support.GetPrefDefault(mContext, Vars.Pref_DB_Version, "1"));
            if (DATABASE_VERSION != currentDBVersion) {
                Log.d(TAG, "Database version is higher than old.");
                deleteDb();
                try {
                    Support.SetPref(mContext, Vars.Pref_DB_Version, String.valueOf(DATABASE_VERSION));
                    copyDataBase();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else {
            try {
                Support.SetPref(mContext, Vars.Pref_DB_Version, String.valueOf(DATABASE_VERSION));
                copyDataBase();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            File file = new File(pathToSaveDBFile);
            checkDB = file.exists();
        } catch (SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
        return checkDB;
    }


    private void copyDataBase() throws IOException {
        OutputStream os = new FileOutputStream(pathToSaveDBFile);
        InputStream is = mContext.getAssets().open("sqlite/" + DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.flush();
        os.close();
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.close();

    }

    private void deleteDb() {
        File file = new File(pathToSaveDBFile);
        if (file.exists()) {
            file.delete();
            Log.d(TAG, "Database deleted.");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public void ClearTable(String TableName) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        db.execSQL("DELETE FROM " + TableName + " WHERE 1");
        db.close();
    }


    public String AccessUnreadCount(String MyId) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        Cursor c;
        String Result = "0";
        try {
            c = db.rawQuery("SELECT * FROM chatmessage WHERE ReceiverId = '" + MyId + "' AND ReadOn = ''", null);
            Result = c.getCount() + "";
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();

        return Result;
    }

    public void InsertMessageList(ArrayList<Res_Message_List> messageList) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            for (int i = 0; i < messageList.size(); i++) {
                Res_Message_List data = messageList.get(i);
                db.execSQL("DELETE FROM messagelist WHERE Friend_Id = " + data.getFriend_id());
                if (!(data.getFriend_id().equals(staUserInfo.getId()) && data.getRole_id().equals(staUserInfo.getRole()))) {
                    db.execSQL("INSERT INTO messagelist (Friend_Id, Role_Id, Name, Profile_Pic, Online_Status, LastMessageDate) VALUES" +
                            " ('" + Support.ManageText(data.getFriend_id()) + "','" +
                            Support.ManageText(data.getRole_id()) + "','" +
                            Support.ManageText(data.getName()) + "','" +
                            Support.ManageText(data.getImage()) + "','" +
                            Support.ManageText(data.getLast_online()) + "','" +
                            Support.ManageText("") + "');");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public ArrayList<PojoMessageList> AccessMessageList() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<PojoMessageList> listMessage = new ArrayList();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM messagelist ORDER BY LastMessageDate DESC", null);
            if (c.moveToFirst()) {
                do {
                    PojoMessageList message = new PojoMessageList();
                    String Friend_id = c.getString(c.getColumnIndex("Friend_Id"));
                    String Role_id = c.getString(c.getColumnIndex("Role_Id"));

                    message.setFriend_id(Friend_id);
                    message.setFriend_name(c.getString(c.getColumnIndex("Name")));
                    message.setProfile_pic(c.getString(c.getColumnIndex("Profile_Pic")));
                    message.setOnline_status(c.getString(c.getColumnIndex("Online_Status")));
                    message.setRole_id(Role_id);

                    PojoLastMessage last_message = GetLastMessage(staUserInfo.getId() + "_" + staUserInfo.getRole(), Friend_id + "_" + Role_id);
                    message.setLast_message(last_message.getMessage());
                    message.setLast_message_on(last_message.getMessage_on());
                    message.setLast_message_by(last_message.getMessage_by());
                    message.setLast_message_type(last_message.getMessage_type());
                    message.setLast_message_status(last_message.getMessage_status());
                    message.setUnread_count(last_message.getUnread_count());

                    listMessage.add(message);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listMessage;
    }

    public PojoLastMessage GetLastMessage(String MyId, String FriendId) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        Cursor c;
        String Message_Id = "";
        String Last_Message = "";
        int UnRead = 0;
        String LastDate = "0";
        String Type = "0";
        String Status = "0";
        PojoLastMessage last_message = new PojoLastMessage();
        try {
            c = db.rawQuery("SELECT * FROM chatmessage WHERE (SenderId = '" + MyId + "' and ReceiverId = '" + FriendId + "') OR (SenderId = '" + FriendId + "' and ReceiverId = '" + MyId + "')", null);
            if (c.moveToFirst()) {
                do {
                    Message_Id = c.getString(c.getColumnIndex("MessageID"));
                    Last_Message = c.getString(c.getColumnIndex("Message"));
                    LastDate = c.getString(c.getColumnIndex("SentOn"));
                    Type = c.getString(c.getColumnIndex("Type"));
                    if (c.getString(c.getColumnIndex("ReceiverId")).equals(MyId)) {
                        Status = Vars.Message_Status_Mine;
                    } else {
                        Status = Vars.Message_Status_Sent;
                        if (!c.getString(c.getColumnIndex("ReceiveOn")).equals("")) {
                            Status = Vars.Message_Status_Received;
                        }
                        if (!c.getString(c.getColumnIndex("ReadOn")).equals("")) {
                            Status = Vars.Message_Status_Read;
                        }
                    }

                    if (c.getString(c.getColumnIndex("SenderId")).equals(FriendId)) {
                        if (c.getString(c.getColumnIndex("ReadOn")).equals("")) {
                            UnRead++;
                        }
                    }
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        last_message.setMessage_id(Message_Id);
        last_message.setMessage(Last_Message);
        last_message.setUnread_count(UnRead + "");
        last_message.setMessage_on(LastDate);
        last_message.setMessage_type(Type);
        last_message.setMessage_status(Status);

        return last_message;
    }

    public ArrayList<PojoChatMessageDisplay> AccessMessage(String OUserId) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        String LastDate = "";
        ArrayList<PojoChatMessageDisplay> listMessage = new ArrayList();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM chatmessage WHERE ReceiverId = '" + OUserId + "' OR SenderId = '" + OUserId + "' Order BY SentOn ASC", null);
            if (c.moveToFirst()) {
                do {
                    PojoChatMessageDisplay message = new PojoChatMessageDisplay();
                    message.setMessage_id(c.getString(c.getColumnIndex("MessageID")));
                    message.setSender_id(c.getString(c.getColumnIndex("SenderId")));
                    message.setReceiver_id(c.getString(c.getColumnIndex("ReceiverId")));
                    message.setMessage(c.getString(c.getColumnIndex("Message")));
                    message.setRead_on(c.getString(c.getColumnIndex("ReadOn")));
                    message.setSent_on(c.getString(c.getColumnIndex("SentOn")));
                    message.setType(c.getString(c.getColumnIndex("Type")));
                    message.setReceive_on(c.getString(c.getColumnIndex("ReceiveOn")));

                    String Current_Date = Support.Show_Format_Date_For_Chat(c.getString(c.getColumnIndex("SentOn")));
                    if (listMessage.size() == 0 || !LastDate.equals(Current_Date)) {
                        LastDate = Current_Date;
                        PojoChatMessageDisplay message_Date = new PojoChatMessageDisplay();
                        message_Date.setMessage_id("0");
                        message_Date.setSender_id("0");
                        message_Date.setReceiver_id("0");
                        message_Date.setMessage(LastDate);
                        message_Date.setRead_on("0");
                        message_Date.setSent_on("");
                        message_Date.setType("3");
                        message_Date.setReceive_on("0");
                        listMessage.add(message_Date);
                    }

                    listMessage.add(message);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listMessage;
    }


    public void InsertChat(String Message_Id, PojoChatMessage message) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            String Existing = "0";
            Cursor c;
            c = db.rawQuery("SELECT Count(*) AS Existing FROM chatmessage WHERE MessageID = '" + Message_Id + "'", null);
            c.moveToFirst();
            Existing = c.getString(c.getColumnIndex("Existing"));

            if (Existing.equals("0")) {
                db.execSQL("INSERT INTO chatmessage (MessageID, SenderId, ReceiverId, Type, Message, SentOn, ReceiveOn, ReadOn) VALUES" +
                        " ('" + Support.ManageText(Message_Id) + "','" +
                        Support.ManageText(message.getSender_id()) + "','" +
                        Support.ManageText(message.getReceiver_id()) + "','" +
                        Support.ManageText(message.getType()) + "','" +
                        Support.ManageText(message.getMessage()) + "','" +
                        Support.ManageText(message.getSent_on()) + "','" +
                        Support.ManageText(message.getReceive_on()) + "','" +
                        Support.ManageText(message.getRead_on()) + "');");
            } else {
                db.execSQL("UPDATE chatmessage SET " +
                        "SenderId = '" + Support.ManageText(message.getSender_id()) + "', " +
                        "ReceiverId = '" + Support.ManageText(message.getReceiver_id()) + "', " +
                        "Type = '" + Support.ManageText(message.getType()) + "', " +
                        "Message = '" + Support.ManageText(message.getMessage()) + "', " +
                        "SentOn = '" + Support.ManageText(message.getSent_on()) + "', " +
                        "ReceiveOn = '" + Support.ManageText(message.getReceive_on()) + "', " +
                        "ReadOn = '" + Support.ManageText(message.getRead_on()) + "' " +
                        "WHERE MessageID = '" + Message_Id + "';");
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void Update_Message_List(String Table_Name, PojoMessageList Data) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            db.execSQL("DELETE FROM " + Table_Name + " WHERE Friend_Id = " + Data.getFriend_id());
            db.execSQL("INSERT INTO " + Table_Name + " (FriendId, FriendName, ProfilePic, OnlineStatus, LastMessageOn) VALUES" +
                    " ('" + Support.ManageText(Data.getFriend_id()) + "','" +
                    Support.ManageText(Data.getFriend_name()) + "','" +
                    Support.ManageText(Data.getProfile_pic()) + "','" +
                    Support.ManageText(Data.getOnline_status()) + "','" +
                    Support.ManageText(Data.getLast_message_on()) + "');");


        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public boolean FindExistingSender(String friend_id) {
        boolean Result = false;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            Cursor cN = db.rawQuery("SELECT * FROM messagelist WHERE Friend_Id = '" + friend_id + "'", null);
            if (cN.getCount() > 0)
                Result = true;
            cN.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return Result;
    }

    public void RemoveMessage(String MessageID) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        db.execSQL("DELETE FROM chatmessage WHERE MessageID = '" + MessageID + "'");
        db.close();
    }

    public void InsertAbsentReasons(ArrayList<Res_Look_AbsentReason> data) {

        ClearTable("lookup_absent_reason");

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            for (int i = 0; i < data.size(); i++) {
                db.execSQL("INSERT INTO lookup_absent_reason (ID, Reason) VALUES" +
                        " ('" + Support.ManageText(data.get(i).getId()) + "','" +
                        Support.ManageText(data.get(i).getReason()) + "');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public ArrayList<Res_Look_AbsentReason> AccessAbsentReason() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<Res_Look_AbsentReason> listReason = new ArrayList();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM lookup_absent_reason ORDER BY Reason ASC", null);
            if (c.moveToFirst()) {
                do {
                    Res_Look_AbsentReason sub = new Res_Look_AbsentReason();
                    sub.setId(c.getString(c.getColumnIndex("ID")));
                    sub.setReason(c.getString(c.getColumnIndex("Reason")));
                    listReason.add(sub);
                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listReason;
    }

    public ArrayList<PojoAttendanceKeyPair> AccessTimeSlot() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<PojoAttendanceKeyPair> listAttendanceTimeSlot = new ArrayList();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM teachers_timetable ORDER BY Slot_ID ASC", null);
            if (c.moveToFirst()) {
                do {
                    String Time = c.getString(c.getColumnIndex("Time_Slot"));
                    if (!Time.equals("")) {
                        PojoAttendanceKeyPair sub = new PojoAttendanceKeyPair();
                        sub.setSlot(c.getString(c.getColumnIndex("Slot_ID")));
                        sub.setTime(c.getString(c.getColumnIndex("Time_Slot")));
                        listAttendanceTimeSlot.add(sub);
                    }
                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listAttendanceTimeSlot;
    }


    public void InsertExamList(ArrayList<Res_Look_ExamList> data) {

        ClearTable("lookup_exam_list");

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            for (int i = 0; i < data.size(); i++) {
                db.execSQL("INSERT INTO lookup_exam_list (ID, Exam_Name) VALUES" +
                        " ('" + Support.ManageText(data.get(i).getId()) + "','" +
                        Support.ManageText(data.get(i).getExam_name()) + "');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }


    public ArrayList<Res_Look_ExamList> AccessExamList() {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<Res_Look_ExamList> listExam = new ArrayList<>();
        Cursor c;
        try {
            Cursor c1 = db.rawQuery("SELECT * FROM lookup_exam_list ORDER BY ID ASC", null);
            if (c1.moveToFirst()) {
                do {
                    Res_Look_ExamList exam = new Res_Look_ExamList();
                    exam.setId(c1.getString(c1.getColumnIndex("ID")));
                    exam.setExam_name(c1.getString(c1.getColumnIndex("Exam_Name")));

                    listExam.add(exam);
                } while (c1.moveToNext());
            }
            c1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listExam;
    }


    public void InsertBehaviourList(ArrayList<Res_Look_Behaviour> data) {

        ClearTable("lookup_behaviour_title");
        ClearTable("lookup_behaviour_content");

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            for (int i = 0; i < data.size(); i++) {
                db.execSQL("INSERT INTO lookup_behaviour_title (ID, Title) VALUES" +
                        " ('" + Support.ManageText(data.get(i).getId()) + "','" +
                        Support.ManageText(data.get(i).getBehaviour_title()) + "');");

                for (int j = 0; j < data.get(i).getContent().size(); j++) {
                    Res_Look_BehaviourContent res = data.get(i).getContent().get(j);
                    db.execSQL("INSERT INTO lookup_behaviour_content (ID, Title_Id, Content_Name, Actions) VALUES" +
                            " ('" + Support.ManageText(res.getId()) + "','" +
                            Support.ManageText(data.get(i).getId()) + "','" +
                            Support.ManageText(res.getContent_name()) + "','" +
                            Support.ManageText(res.getActions()) + "');");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public ArrayList<Res_Look_Behaviour> AccessBehaviourLookup() {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<Res_Look_Behaviour> listBehaviour = new ArrayList<>();

        try {
            Cursor c = db.rawQuery("SELECT * FROM lookup_behaviour_title  ORDER BY ID ASC", null);
            if (c.moveToFirst()) {
                do {
                    Res_Look_Behaviour behaviour = new Res_Look_Behaviour();
                    String Id = c.getString(c.getColumnIndex("ID"));
                    behaviour.setId(Id);
                    behaviour.setBehaviour_title(c.getString(c.getColumnIndex("Title")));

                    ArrayList<Res_Look_BehaviourContent> listContent = new ArrayList<>();
                    Cursor c1 = db.rawQuery("SELECT * FROM lookup_behaviour_content WHERE Title_Id = '" + Id + "' ORDER BY ID ASC", null);
                    if (c1.moveToFirst()) {
                        do {
                            Res_Look_BehaviourContent content = new Res_Look_BehaviourContent();
                            content.setId(c1.getString(c1.getColumnIndex("ID")));
                            content.setContent_name(c1.getString(c1.getColumnIndex("Content_Name")));
                            content.setActions(c1.getString(c1.getColumnIndex("Actions")));

                            listContent.add(content);
                        } while (c1.moveToNext());
                    }
                    c1.close();

                    behaviour.setContent(listContent);
                    listBehaviour.add(behaviour);
                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listBehaviour;
    }

    public void InsertTeachersTeachingSubject(ArrayList<Res_Teacher_TeachingSubjects> data) {
        ClearTable("teachers_subject");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            for (int i = 0; i < data.size(); i++) {
                db.execSQL("INSERT INTO teachers_subject (ID, Subject_Name, Parts, Parts_Name) VALUES" +
                        " ('" + Support.ManageText(data.get(i).getId()) + "','" +
                        Support.ManageText(data.get(i).getSubject_name()) + "','" +
                        Support.ManageText(data.get(i).getParts()) + "','" +
                        Support.ManageText(data.get(i).getParts_name()) + "');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public ArrayList<Res_Teacher_TeachingSubjects> AccessTeachingSubject(boolean NeedAllSubject, String in_Condition) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        String LastDate = "";
        ArrayList<Res_Teacher_TeachingSubjects> listSubject = new ArrayList();
        if (NeedAllSubject) {
            Res_Teacher_TeachingSubjects sub = new Res_Teacher_TeachingSubjects();
            sub.setId("0");
            sub.setSubject_name("All Subject");
            listSubject.add(sub);
        }
        Cursor c;
        try {
            String Condition = "";
            if (!in_Condition.equals("")) {
                Condition = "Where ID IN (" + in_Condition + ")";
            }
            c = db.rawQuery("SELECT * FROM teachers_subject " + Condition, null);
            if (c.moveToFirst()) {
                do {
                    Res_Teacher_TeachingSubjects sub = new Res_Teacher_TeachingSubjects();
                    sub.setId(c.getString(c.getColumnIndex("ID")));
                    sub.setSubject_name(c.getString(c.getColumnIndex("Subject_Name")));
                    sub.setParts(c.getString(c.getColumnIndex("Parts")));
                    sub.setParts_name(c.getString(c.getColumnIndex("Parts_Name")));
                    listSubject.add(sub);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listSubject;
    }


    public ArrayList AccessSavedSubjectMarks(String user_id, String role_id) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        ArrayList<Res_Student_Marks> listSvdMarks = new ArrayList();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM sub_subject_marks WHERE teacher = '"+ user_id +"' AND school = '"+ role_id +"'", null);
            if (c.moveToFirst()) {
                do {
                    Res_Student_Marks sub = new Res_Student_Marks();
                    sub.setStudentid(c.getString(c.getColumnIndex("studentid")));
                    sub.setTerm(c.getString(c.getColumnIndex("term")));
                    sub.setExamtype(c.getString(c.getColumnIndex("examtype")));
                    sub.setSubject(c.getString(c.getColumnIndex("subjectmain")));
                    sub.setSubjectpart(c.getString(c.getColumnIndex("subsubject")));
                    sub.setMarks(c.getString(c.getColumnIndex("marks")));
                    sub.setSchool(c.getString(c.getColumnIndex("school")));
                    sub.setTeacher(c.getString(c.getColumnIndex("teacher")));
                    listSvdMarks.add(sub);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listSvdMarks;
    }


    public ArrayList AccessSavedMarks(String user_id, String role_id) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        ArrayList<Res_Student_Marks> listSvdMarks = new ArrayList();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM student_marks WHERE teacher = '"+ user_id +"' AND school = '"+ role_id +"'", null);
            if (c.moveToFirst()) {
                do {
                    Res_Student_Marks sub = new Res_Student_Marks();
                    sub.setStudentid(c.getString(c.getColumnIndex("studentid")));
                    sub.setTerm(c.getString(c.getColumnIndex("term")));
                    sub.setExamtype(c.getString(c.getColumnIndex("examtype")));
                    sub.setSubject(c.getString(c.getColumnIndex("subject")));
                    sub.setMarks(c.getString(c.getColumnIndex("marks")));
                    sub.setSchool(c.getString(c.getColumnIndex("school")));
                    sub.setTeacher(c.getString(c.getColumnIndex("teacher")));
                    sub.setNames(c.getString(c.getColumnIndex("names")));
                    listSvdMarks.add(sub);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listSvdMarks;
    }
    public ArrayList<Res_Teacher_TeachingSubjects> AccessSubjects(boolean NeedAllSubject, String in_Condition) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        String LastDate = "";
        ArrayList<Res_Teacher_TeachingSubjects> listSubject = new ArrayList();
        if (NeedAllSubject) {
            Res_Teacher_TeachingSubjects sub = new Res_Teacher_TeachingSubjects();
            sub.setId("0");
            sub.setSubject_name("All Subject");
            listSubject.add(sub);
        }
        Cursor c;
        try {
            String Condition = "";
            if (!in_Condition.equals("")) {
                Condition = "Where ID IN (" + in_Condition + ")";
            }
            c = db.rawQuery("SELECT * FROM teachers_subject GROUP BY Subject_Name" + Condition, null);
            if (c.moveToFirst()) {
                do {
                    Res_Teacher_TeachingSubjects sub = new Res_Teacher_TeachingSubjects();
                    sub.setId(c.getString(c.getColumnIndex("ID")));
                    sub.setSubject_name(c.getString(c.getColumnIndex("Subject_Name")));
                    sub.setParts(c.getString(c.getColumnIndex("Parts")));
                    sub.setParts_name(c.getString(c.getColumnIndex("Parts_Name")));
                    listSubject.add(sub);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listSubject;
    }


    public ArrayList<Res_Teacher_TeachingSubjects> AccessTeachingSubjectBasedStudent(String stud_id) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        String Teaching_subject = "";
        ArrayList<Res_Teacher_TeachingSubjects> listSubject = new ArrayList();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM teachers_class WHERE Class_ID = (SELECT Class_Id FROM teachers_student WHERE Stud_ID = '" + stud_id + "')", null);
            if (c.moveToFirst()) {
                Teaching_subject = c.getString(c.getColumnIndex("Teaching_Subject"));
            }
            c.close();

            c = db.rawQuery("SELECT * FROM teachers_subject WHERE ID in (" + Teaching_subject + ")", null);
            if (c.moveToFirst()) {
                do {
                    Res_Teacher_TeachingSubjects sub = new Res_Teacher_TeachingSubjects();
                    sub.setId(c.getString(c.getColumnIndex("ID")));
                    sub.setSubject_name(c.getString(c.getColumnIndex("Subject_Name")));
                    sub.setParts(c.getString(c.getColumnIndex("Parts")));
                    sub.setParts_name(c.getString(c.getColumnIndex("Parts_Name")));
                    listSubject.add(sub);
                } while (c.moveToNext());
            }
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listSubject;
    }

    public void InsertTeachersTimeTable(ArrayList<Res_Teacher_TimeTable> TimeTableList) {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {

            for (int i = 0; i < TimeTableList.size(); i++) {
                Res_Teacher_TimeTable data = TimeTableList.get(i);
                db.execSQL("INSERT INTO teachers_timetable (Slot_ID, Time_Slot, Mon, Tue, Wed, Thu, Fri, Sat, Sun, Break_Name, Type) VALUES" +
                        " ('" + Support.ManageText(data.getSlot()) + "','" +
                        Support.ManageText(data.getTime()) + "','" +
                        Support.ManageText(data.getMon()) + "','" +
                        Support.ManageText(data.getTue()) + "','" +
                        Support.ManageText(data.getWed()) + "','" +
                        Support.ManageText(data.getThu()) + "','" +
                        Support.ManageText(data.getFri()) + "','" +
                        Support.ManageText("") + "','" +
                        Support.ManageText("") + "','" +
                        Support.ManageText(data.getBreak_name()) + "','" +
                        Support.ManageText(data.getType()) + "');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public ArrayList<Res_Teacher_TimeTable> AccessTeachersTimetable() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<Res_Teacher_TimeTable> listTimeTable = new ArrayList();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM teachers_timetable", null);
            if (c.moveToFirst()) {
                do {
                    Res_Teacher_TimeTable data = new Res_Teacher_TimeTable();
                    data.setSlot(c.getString(c.getColumnIndex("Slot_ID")));
                    data.setTime(c.getString(c.getColumnIndex("Time_Slot")));
                    data.setMon(c.getString(c.getColumnIndex("Mon")));
                    data.setTue(c.getString(c.getColumnIndex("Tue")));
                    data.setWed(c.getString(c.getColumnIndex("Wed")));
                    data.setThu(c.getString(c.getColumnIndex("Thu")));
                    data.setFri(c.getString(c.getColumnIndex("Fri")));
                    data.setBreak_name(c.getString(c.getColumnIndex("Break_Name")));
                    data.setType(c.getString(c.getColumnIndex("Type")));


                    listTimeTable.add(data);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listTimeTable;
    }

    public void saveStudentSubMarks(String studentnames , String textexam , String textsubject , String  texterm ,  String id , String Marks , String uid ,String urole, String subsubject) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            db.execSQL("DELETE FROM sub_subject_marks WHERE studentid='"+ id +"' AND term = '" + texterm + "' AND examtype = '" + textexam + "' AND subjectmain  = '" + textsubject + "' AND subsubject ='"+ subsubject +"'");

            db.execSQL("INSERT INTO sub_subject_marks (studentid, term, examtype, " +
                    "subjectmain, marks, school, teacher, subsubject) VALUES" +
                    " ('" + Support.ManageText(id) + "','" +
                    Support.ManageText(texterm) + "','" +
                    Support.ManageText(textexam) + "','" +
                    Support.ManageText(textsubject) + "','" +
                    Support.ManageText(Marks) + "','" +
                    Support.ManageText(urole) + "','" +
                    Support.ManageText(uid) + "','" +
                    Support.ManageText(subsubject) + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void saveStudentMarks(String studentnames , String textexam , String textsubject , String  texterm ,  String id , String Marks , String uid ,String urole) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            db.execSQL("DELETE FROM student_marks WHERE studentid='"+ id +"' AND term = '" + texterm + "' AND examtype = '" + textexam + "' AND subject  = '" + textsubject + "' ");

            db.execSQL("INSERT INTO student_marks (studentid, term, examtype, " +
                    "subject, marks, school, teacher, names) VALUES" +
                    " ('" + Support.ManageText(id) + "','" +
                    Support.ManageText(texterm) + "','" +
                    Support.ManageText(textexam) + "','" +
                    Support.ManageText(textsubject) + "','" +
                    Support.ManageText(Marks) + "','" +
                    Support.ManageText(urole) + "','" +
                    Support.ManageText(uid) + "','" +
                    Support.ManageText(studentnames) + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }


    public void InsertStudentPaperMarks(ArrayList<Res_Student_Marks> stud_marks , String user_id , String role_id ){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            for (int i = 0; i < stud_marks.size(); i++) {
                Res_Student_Marks data = stud_marks.get(i);

                int papermark = new Double(data.getMarks()).intValue();

                db.execSQL("INSERT INTO sub_subject_marks (studentid, term, examtype, " +
                        "subjectmain, marks, school, teacher, subsubject) VALUES" +
                        " ('" + Support.ManageText(data.getStudentid()) + "','" +
                        Support.ManageText(data.getTerm()) + "','" +
                        Support.ManageText(data.getExamtype()) + "','" +
                        Support.ManageText(data.getSubject()) + "','" +
                        Support.ManageText(String.valueOf(papermark)) + "','" +
                        Support.ManageText(data.getSchool()) + "','" +
                        Support.ManageText(data.getTeacher()) + "','" +
                        Support.ManageText(data.getSubjectpart()) + "');");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void InsertStudentMarks(ArrayList<Res_Student_Marks> stud_marks , String user_id , String role_id ){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {

            for (int i = 0; i < stud_marks.size(); i++) {
                Res_Student_Marks data = stud_marks.get(i);

                db.execSQL("INSERT INTO student_marks (studentid, term, examtype, " +
                        "subject, marks, school, teacher, names) VALUES" +
                        " ('" + Support.ManageText(data.getStudentid()) + "','" +
                        Support.ManageText(data.getTerm()) + "','" +
                        Support.ManageText(data.getExamtype()) + "','" +
                        Support.ManageText(data.getSubject()) + "','" +
                        Support.ManageText(data.getMarks()) + "','" +
                        Support.ManageText(data.getSchool()) + "','" +
                        Support.ManageText(data.getTeacher()) + "','" +
                        Support.ManageText(data.getNames()) + "');");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void InsertTeachersClass(ArrayList<Res_Teacher_Class> myClass) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {

            for (int i = 0; i < myClass.size(); i++) {

                Res_Teacher_Class data = myClass.get(i);
                String Subject_Id = data.getSubject_id();

               // if (Subject_Id != null) {
                    db.execSQL("DELETE FROM teachers_class WHERE Class_ID = '" + data.getClass_id() + "' AND Section_ID = '" + data.getSection_id() + "'");

                        if (Subject_Id != null) {
                            Subject_Id = Subject_Id.replace(", ", ",");
                        }else{
                            Subject_Id ="0";
                        }

                    db.execSQL("INSERT INTO teachers_class (Class_ID, Class_Name, Section_ID, Section_Name, Level_Id, Teaching_Subject, Total_Seat, Divides, SColumn, My_Class) VALUES" +
                            " ('" + Support.ManageText(data.getClass_id()) + "','" +
                            Support.ManageText(data.getClass_name()) + "','" +
                            Support.ManageText(data.getSection_id()) + "','" +
                            Support.ManageText(data.getSection_name()) + "','" +
                            Support.ManageText(data.getLevel()) + "','" +
                            Support.ManageText(Subject_Id) + "','" +
                            Support.ManageText(data.getTotal_seat()) + "','" +
                            Support.ManageText(data.getDivides()) + "','" +
                            Support.ManageText(data.getColumn()) + "','" +
                            data.getMyclass() + "');");


                    for (int j = 0; j < data.getStudent().size(); j++) {
                        Res_Teacher_ClassStudent res = data.getStudent().get(j);
                        db.execSQL("DELETE FROM teachers_student WHERE Stud_ID = '" + res.getId() + "'");
                        db.execSQL("INSERT INTO teachers_student (Stud_ID, First_Name, Middle_Name, " +
                                "Last_Name, Image, Level_Id, Class_Id, Class_Name, Section_Id, Section_Name,AdmnNo,Term,ExamType,Subject,Marks,Seat_Position) VALUES" +
                                " ('" + Support.ManageText(res.getId()) + "','" +
                                Support.ManageText(res.getFirst_name()) + "','" +
                                Support.ManageText(res.getMiddle_name()) + "','" +
                                Support.ManageText(res.getLast_name()) + "','" +
                                Support.ManageText(res.getImage()) + "','" +
                                Support.ManageText(res.getLevel()) + "','" +
                                Support.ManageText(res.getClass_id()) + "','" +
                                Support.ManageText(res.getClass_name()) + "','" +
                                Support.ManageText(res.getSection_id()) + "','" +
                                Support.ManageText(res.getSection_name()) + "','" +
                                Support.ManageText(res.getAdmnno()) + "','" +
                                Support.ManageText(res.getTerm()) + "','" +
                                Support.ManageText(res.getExamtype()) + "','" +
                                Support.ManageText(res.getSubject()) + "','" +
                                Support.ManageText(res.getMarks()) + "','" +
                                Support.ManageText(res.getSeat()) + "');");
                    }
               // }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }







    public ArrayList<Res_Teacher_Class> AccessTeachersClassSubjectMarks(String texterm, String textexam, String textsubject,String uid, String schoo,String class_id,String selSubj) {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        String LastDate = "";
        ArrayList<Res_Teacher_Class> listClass = new ArrayList();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM teachers_class WHERE Section_ID = '"+ class_id +"' ORDER BY My_Class DESC", null);
            if (c.moveToFirst()) {
                do {
                    String className = c.getString(c.getColumnIndex("Class_Name"));
                    String classID = c.getString(c.getColumnIndex("Class_ID"));

                    String sectionName = c.getString(c.getColumnIndex("Section_Name"));
                    String sectionID = c.getString(c.getColumnIndex("Section_ID"));

                    Res_Teacher_Class cls = new Res_Teacher_Class();
                    cls.setClass_id(classID);
                    cls.setClass_name(className);
                    cls.setSection_id(sectionID);
                    cls.setSection_name(sectionName);
                    cls.setLevel(c.getString(c.getColumnIndex("Level_Id")));
                    cls.setSubject_id(c.getString(c.getColumnIndex("Teaching_Subject")));
                    cls.setTotal_seat(c.getString(c.getColumnIndex("Total_Seat")));
                    cls.setDivides(c.getString(c.getColumnIndex("Divides")));
                    cls.setColumn(c.getString(c.getColumnIndex("SColumn")));
                    cls.setIs_my_class(c.getString(c.getColumnIndex("My_Class")));

                    ArrayList<Res_Teacher_ClassStudent> listStud = new ArrayList<>();

                    String sqlSt = "SELECT stud.Stud_ID,stud.First_Name , stud.Middle_Name , stud.Last_Name , stud.Image ," +
                            " stud.Level_Id , stud.Class_Id, stud.Section_Id , stud.AdmnNo , s.term , s.examtype , s.subjectmain , s.marks , stud.Seat_Position " +
                            " FROM teachers_student stud " +
                            " LEFT JOIN sub_subject_marks s ON s.studentid = stud.Stud_ID AND s.examtype = '"+textexam+"' AND s.term = '"+ texterm +"' AND s.subjectmain = '"+ textsubject +"' AND s.subsubject = '"+ selSubj +"' " +
                            " WHERE stud.Class_Id = '" + classID + "' AND stud.Section_Id = '" + sectionID + "' GROUP BY stud.Stud_ID  ORDER BY stud.Stud_ID ASC";

                    Cursor c1 = db.rawQuery(sqlSt , null);
                    if (c1.moveToFirst()) {
                        do {
                            Res_Teacher_ClassStudent stud = new Res_Teacher_ClassStudent();
                            stud.setId(c1.getString(c1.getColumnIndex("Stud_ID")));
                            stud.setFirst_name(c1.getString(c1.getColumnIndex("First_Name")));
                            stud.setMiddle_name(c1.getString(c1.getColumnIndex("Middle_Name")));
                            stud.setLast_name(c1.getString(c1.getColumnIndex("Last_Name")));
                            stud.setImage(c1.getString(c1.getColumnIndex("Image")));
                            stud.setLevel(c1.getString(c1.getColumnIndex("Level_Id")));
                            stud.setClass_id(c1.getString(c1.getColumnIndex("Class_Id")));
                            stud.setSection_id(c1.getString(c1.getColumnIndex("Section_Id")));
                            //added this columns
                            stud.setAdmnno(c1.getString(c1.getColumnIndex("AdmnNo")));
                            stud.setTerm(c1.getString(c1.getColumnIndex("term")));
                            stud.setExamtype(c1.getString(c1.getColumnIndex("examtype")));
                            stud.setSubject(c1.getString(c1.getColumnIndex("subjectmain")));
                            stud.setMarks(c1.getString(c1.getColumnIndex("marks")));

                            stud.setClass_name(className);
                            stud.setSection_name(sectionName);
                            stud.setSeat(c1.getString(c1.getColumnIndex("Seat_Position")));

                            listStud.add(stud);
                        } while (c1.moveToNext());
                    }
                    c1.close();

                    cls.setStudent(listStud);

                    listClass.add(cls);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listClass;
    }



    public ArrayList<Res_Teacher_Class> AccessTeachersClassMarks(String texterm, String textexam, String textsubject,String uid, String schoo,String class_id) {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        String LastDate = "";
        ArrayList<Res_Teacher_Class> listClass = new ArrayList();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM teachers_class WHERE Section_ID = '"+ class_id +"' ORDER BY My_Class DESC", null);
            if (c.moveToFirst()) {
                do {
                    String className = c.getString(c.getColumnIndex("Class_Name"));
                    String classID = c.getString(c.getColumnIndex("Class_ID"));

                    String sectionName = c.getString(c.getColumnIndex("Section_Name"));
                    String sectionID = c.getString(c.getColumnIndex("Section_ID"));

                    Res_Teacher_Class cls = new Res_Teacher_Class();
                    cls.setClass_id(classID);
                    cls.setClass_name(className);
                    cls.setSection_id(sectionID);
                    cls.setSection_name(sectionName);
                    cls.setLevel(c.getString(c.getColumnIndex("Level_Id")));
                    cls.setSubject_id(c.getString(c.getColumnIndex("Teaching_Subject")));
                    cls.setTotal_seat(c.getString(c.getColumnIndex("Total_Seat")));
                    cls.setDivides(c.getString(c.getColumnIndex("Divides")));
                    cls.setColumn(c.getString(c.getColumnIndex("SColumn")));
                    cls.setIs_my_class(c.getString(c.getColumnIndex("My_Class")));

                    ArrayList<Res_Teacher_ClassStudent> listStud = new ArrayList<>();

                    String sqlSt = "SELECT stud.Stud_ID,stud.First_Name , stud.Middle_Name , stud.Last_Name , stud.Image ," +
                            " stud.Level_Id , stud.Class_Id, stud.Section_Id , stud.AdmnNo , s.term , s.examtype , s.subject , s.marks , stud.Seat_Position " +
                            " FROM teachers_student stud " +
                            " LEFT JOIN student_marks s ON s.studentid = stud.Stud_ID AND s.examtype = '"+textexam+"' AND s.term = '"+ texterm +"' AND s.subject = '"+ textsubject +"'" +
                            " WHERE stud.Class_Id = '" + classID + "' AND stud.Section_Id = '" + sectionID + "' GROUP BY stud.Stud_ID  ORDER BY stud.Stud_ID ASC";

                    Cursor c1 = db.rawQuery(sqlSt , null);
                    if (c1.moveToFirst()) {
                        do {
                            Res_Teacher_ClassStudent stud = new Res_Teacher_ClassStudent();
                            stud.setId(c1.getString(c1.getColumnIndex("Stud_ID")));
                            stud.setFirst_name(c1.getString(c1.getColumnIndex("First_Name")));
                            stud.setMiddle_name(c1.getString(c1.getColumnIndex("Middle_Name")));
                            stud.setLast_name(c1.getString(c1.getColumnIndex("Last_Name")));
                            stud.setImage(c1.getString(c1.getColumnIndex("Image")));
                            stud.setLevel(c1.getString(c1.getColumnIndex("Level_Id")));
                            stud.setClass_id(c1.getString(c1.getColumnIndex("Class_Id")));
                            stud.setSection_id(c1.getString(c1.getColumnIndex("Section_Id")));
                            //added this columns
                            stud.setAdmnno(c1.getString(c1.getColumnIndex("AdmnNo")));
                            stud.setTerm(c1.getString(c1.getColumnIndex("term")));
                            stud.setExamtype(c1.getString(c1.getColumnIndex("examtype")));
                            stud.setSubject(c1.getString(c1.getColumnIndex("subject")));
                            stud.setMarks(c1.getString(c1.getColumnIndex("marks")));

                            stud.setClass_name(className);
                            stud.setSection_name(sectionName);
                            stud.setSeat(c1.getString(c1.getColumnIndex("Seat_Position")));

                            listStud.add(stud);
                        } while (c1.moveToNext());
                    }
                    c1.close();

                    cls.setStudent(listStud);

                    listClass.add(cls);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listClass;
    }

    public ArrayList<Res_Teacher_Class> AccessTeachersClass() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        String LastDate = "";
        ArrayList<Res_Teacher_Class> listClass = new ArrayList();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM teachers_class ORDER BY My_Class DESC", null);
            if (c.moveToFirst()) {
                do {
                    String className = c.getString(c.getColumnIndex("Class_Name"));
                    String classID = c.getString(c.getColumnIndex("Class_ID"));

                    String sectionName = c.getString(c.getColumnIndex("Section_Name"));
                    String sectionID = c.getString(c.getColumnIndex("Section_ID"));

                    Res_Teacher_Class cls = new Res_Teacher_Class();
                    cls.setClass_id(classID);
                    cls.setClass_name(className);
                    cls.setSection_id(sectionID);
                    cls.setSection_name(sectionName);
                    cls.setLevel(c.getString(c.getColumnIndex("Level_Id")));
                    cls.setSubject_id(c.getString(c.getColumnIndex("Teaching_Subject")));
                    cls.setTotal_seat(c.getString(c.getColumnIndex("Total_Seat")));
                    cls.setDivides(c.getString(c.getColumnIndex("Divides")));
                    cls.setColumn(c.getString(c.getColumnIndex("SColumn")));
                    cls.setIs_my_class(c.getString(c.getColumnIndex("My_Class")));

                    ArrayList<Res_Teacher_ClassStudent> listStud = new ArrayList<>();
                    Cursor c1 = db.rawQuery("SELECT * FROM teachers_student " +
                            "WHERE Class_Id = '" + classID + "' AND Section_Id = '" + sectionID + "' ORDER BY Stud_ID ASC", null);
                    if (c1.moveToFirst()) {
                        do {
                            Res_Teacher_ClassStudent stud = new Res_Teacher_ClassStudent();
                            stud.setId(c1.getString(c1.getColumnIndex("Stud_ID")));
                            stud.setFirst_name(c1.getString(c1.getColumnIndex("First_Name")));
                            stud.setMiddle_name(c1.getString(c1.getColumnIndex("Middle_Name")));
                            stud.setLast_name(c1.getString(c1.getColumnIndex("Last_Name")));
                            stud.setImage(c1.getString(c1.getColumnIndex("Image")));
                            stud.setLevel(c1.getString(c1.getColumnIndex("Level_Id")));
                            stud.setClass_id(c1.getString(c1.getColumnIndex("Class_Id")));
                            stud.setSection_id(c1.getString(c1.getColumnIndex("Section_Id")));
                            //added this columns
                            stud.setAdmnno(c1.getString(c1.getColumnIndex("AdmnNo")));
                            stud.setTerm(c1.getString(c1.getColumnIndex("Term")));
                            stud.setExamtype(c1.getString(c1.getColumnIndex("ExamType")));
                            stud.setSubject(c1.getString(c1.getColumnIndex("Subject")));
                            stud.setMarks(c1.getString(c1.getColumnIndex("Marks")));

                            stud.setClass_name(className);
                            stud.setSection_name(sectionName);
                            stud.setSeat(c1.getString(c1.getColumnIndex("Seat_Position")));

                            listStud.add(stud);
                        } while (c1.moveToNext());
                    }
                    c1.close();

                    cls.setStudent(listStud);

                    listClass.add(cls);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listClass;
    }

    public ArrayList<Res_Teacher_Class> AccessTeachersClassWithSubjectName() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        String LastDate = "";
        ArrayList<Res_Teacher_Class> listClass = new ArrayList();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM teachers_class ORDER BY My_Class DESC", null);
            if (c.moveToFirst()) {
                do {
                    String className = c.getString(c.getColumnIndex("Class_Name"));
                    String classID = c.getString(c.getColumnIndex("Class_ID"));

                    String sectionName = c.getString(c.getColumnIndex("Section_Name"));
                    String sectionID = c.getString(c.getColumnIndex("Section_ID"));

                    Res_Teacher_Class cls = new Res_Teacher_Class();
                    cls.setClass_id(classID);
                    cls.setClass_name(className);
                    cls.setSection_id(sectionID);
                    cls.setSection_name(sectionName);
                    cls.setLevel(c.getString(c.getColumnIndex("Level_Id")));
                    cls.setSubject_id(c.getString(c.getColumnIndex("Teaching_Subject")));
                    cls.setTotal_seat(c.getString(c.getColumnIndex("Total_Seat")));
                    cls.setDivides(c.getString(c.getColumnIndex("Divides")));
                    cls.setColumn(c.getString(c.getColumnIndex("SColumn")));
                    cls.setIs_my_class(c.getString(c.getColumnIndex("My_Class")));

                    ArrayList<Res_Teacher_ClassStudent> listStud = new ArrayList<>();
                    Cursor c1 = db.rawQuery("SELECT * FROM teachers_student WHERE Class_Id = '" + classID + "' AND Section_Id = '" + sectionID + "' ORDER BY Stud_ID ASC", null);
                    if (c1.moveToFirst()) {
                        do {
                            Res_Teacher_ClassStudent stud = new Res_Teacher_ClassStudent();
                            stud.setId(c1.getString(c1.getColumnIndex("Stud_ID")));
                            stud.setFirst_name(c1.getString(c1.getColumnIndex("First_Name")));
                            stud.setMiddle_name(c1.getString(c1.getColumnIndex("Middle_Name")));
                            stud.setLast_name(c1.getString(c1.getColumnIndex("Last_Name")));
                            stud.setImage(c1.getString(c1.getColumnIndex("Image")));
                            stud.setLevel(c1.getString(c1.getColumnIndex("Level_Id")));
                            stud.setClass_id(c1.getString(c1.getColumnIndex("Class_Id")));
                            stud.setSection_id(c1.getString(c1.getColumnIndex("Section_Id")));
                            stud.setClass_name(className);
                            stud.setSection_name(sectionName);
                            stud.setSeat(c1.getString(c1.getColumnIndex("Seat_Position")));

                            listStud.add(stud);
                        } while (c1.moveToNext());
                    }
                    c1.close();

                    Cursor c2 = db.rawQuery("SELECT * FROM teachers_subject WHERE ID in (" + cls.getSubject_id() + ")", null);
                    if (c2.moveToFirst()) {
                        do {
                            cls.setSubject_name(c2.getString(c2.getColumnIndex("Subject_Name")));
                        } while (c2.moveToNext());
                    }
                    c2.close();

                    cls.setStudent(listStud);

                    listClass.add(cls);
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listClass;
    }

    public void InsertGroup(ArrayList<Res_Group> data) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            for (int j = 0; j < data.size(); j++) {
                Res_Group res = data.get(j);

                db.execSQL("DELETE FROM teachers_group WHERE ID = '" + res.getId() + "'");
                db.execSQL("INSERT INTO teachers_group (ID, Name, Class_Id, " +
                        "Class_Name, Section_Id, Section_Name, Subject_Id, Subject_Name, " +
                        "Creator_Id, Creator_Name, Created_On, Group_Member) VALUES" +
                        " ('" + Support.ManageText(res.getId()) + "','" +
                        Support.ManageText(res.getName()) + "','" +
                        Support.ManageText(res.getClass_id()) + "','" +
                        Support.ManageText(res.getClass_name()) + "','" +
                        Support.ManageText(res.getSection_id()) + "','" +
                        Support.ManageText(res.getSection_name()) + "','" +
                        Support.ManageText(res.getSubject_id()) + "','" +
                        Support.ManageText(res.getSubject_name()) + "','" +
                        Support.ManageText(res.getCreator_id()) + "','" +
                        Support.ManageText(res.getCreator_name()) + "','" +
                        Support.ManageText(res.getCreated_on()) + "','" +
                        Support.ManageText(new Gson().toJson(res.getGroup_members())) + "');");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }

    public void DeleteGroup(String id) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        db.execSQL("DELETE FROM teachers_group WHERE ID = '" + id + "'");
        db.close();
    }

    public ArrayList<Res_Group> AccessGroup() {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<Res_Group> listGroup = new ArrayList<>();
        Cursor c;
        try {
            Cursor c1 = db.rawQuery("SELECT * FROM teachers_group", null);
            if (c1.moveToFirst()) {
                do {
                    Gson gson = new Gson();

                    TypeToken<ArrayList<Res_Group_Member>> token = new TypeToken<ArrayList<Res_Group_Member>>() {
                    };
                    ArrayList<Res_Group_Member> group_members = gson.fromJson(c1.getString(c1.getColumnIndex("Group_Member")), token.getType());

                    Res_Group group = new Res_Group();
                    group.setId(c1.getString(c1.getColumnIndex("ID")));
                    group.setName(c1.getString(c1.getColumnIndex("Name")));
                    group.setClass_id(c1.getString(c1.getColumnIndex("Class_Id")));
                    group.setClass_name(c1.getString(c1.getColumnIndex("Class_Name")));
                    group.setSection_id(c1.getString(c1.getColumnIndex("Section_Id")));
                    group.setSection_name(c1.getString(c1.getColumnIndex("Section_Name")));
                    group.setSubject_id(c1.getString(c1.getColumnIndex("Subject_Id")));
                    group.setSubject_name(c1.getString(c1.getColumnIndex("Subject_Name")));
                    group.setCreator_id(c1.getString(c1.getColumnIndex("Creator_Id")));
                    group.setCreator_name(c1.getString(c1.getColumnIndex("Creator_Name")));
                    group.setCreated_on(c1.getString(c1.getColumnIndex("Created_On")));
                    group.setGroup_members(group_members);
                    listGroup.add(group);
                } while (c1.moveToNext());
            }
            c1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listGroup;
    }


    public ArrayList<PojoStudentKeyPare> AccessStudentListForGroup(String class_id, String section_id, String subject_id) {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<PojoStudentKeyPare> listStudent = new ArrayList<>();
        ArrayList<PojoStudentKeyPare> listNotSelectedStudent = new ArrayList<>();
        Cursor c;
        try {
            Cursor c1 = db.rawQuery("SELECT * FROM teachers_student WHERE Class_Id = '" + class_id + "' AND Section_Id = '" + section_id + "' ORDER BY First_Name ASC", null);
            if (c1.moveToFirst()) {
                do {
                    PojoStudentKeyPare stud = new PojoStudentKeyPare();
                    String Id = c1.getString(c1.getColumnIndex("Stud_ID"));
                    String Name = c1.getString(c1.getColumnIndex("First_Name")) + " " +
                            c1.getString(c1.getColumnIndex("Middle_Name")) + " " +
                            c1.getString(c1.getColumnIndex("Last_Name"));

                    stud.setId(Id);
                    stud.setName(Name);
                    stud.setSelected(false);

                    listStudent.add(stud);
                } while (c1.moveToNext());
            }
            c1.close();

            ArrayList<String> listExistStudent = new ArrayList<>();
            c1 = db.rawQuery("SELECT Group_Member FROM teachers_group WHERE Class_Id = '" + class_id + "' AND Section_Id = '" + section_id + "'  AND Subject_Id = '" + subject_id + "'", null);
            if (c1.moveToFirst()) {
                do {
                    Gson gson = new Gson();

                    TypeToken<ArrayList<Res_Group_Member>> token = new TypeToken<ArrayList<Res_Group_Member>>() {
                    };
                    ArrayList<Res_Group_Member> group_members = gson.fromJson(c1.getString(c1.getColumnIndex("Group_Member")), token.getType());

                    for (int i = 0; i < group_members.size(); i++) {
                        listExistStudent.add(group_members.get(i).getId());
                    }

                } while (c1.moveToNext());
            }
            c1.close();

            for (int i = 0; i < listStudent.size(); i++) {
                String stud_id = listStudent.get(i).getId();
                if (!listExistStudent.contains(stud_id)) {
                    listNotSelectedStudent.add(listStudent.get(i));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listNotSelectedStudent;
    }


    public Res_Teacher_Class AccessMyClass() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        Res_Teacher_Class listClass = null;
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM teachers_class ORDER BY My_Class DESC", null); // WHERE My_Class = 1
            if (c.moveToFirst()) {
                listClass = new Res_Teacher_Class();
                do {
                    String className = c.getString(c.getColumnIndex("Class_Name"));
                    String classID = c.getString(c.getColumnIndex("Class_ID"));

                    String sectionName = c.getString(c.getColumnIndex("Section_Name"));
                    String sectionID = c.getString(c.getColumnIndex("Section_ID"));


                    listClass.setClass_id(classID);
                    listClass.setClass_name(className);
                    listClass.setSection_id(sectionID);
                    listClass.setSection_name(sectionName);
                    listClass.setLevel(c.getString(c.getColumnIndex("Level_Id")));
                    listClass.setSubject_id(c.getString(c.getColumnIndex("Teaching_Subject")));
                    listClass.setTotal_seat(c.getString(c.getColumnIndex("Total_Seat")));
                    listClass.setDivides(c.getString(c.getColumnIndex("Divides")));
                    listClass.setColumn(c.getString(c.getColumnIndex("SColumn")));
                    listClass.setIs_my_class(c.getString(c.getColumnIndex("My_Class")));

                    ArrayList<Res_Teacher_ClassStudent> listStud = new ArrayList<>();
                    Cursor c1 = db.rawQuery("SELECT * FROM teachers_student WHERE Class_Id = '" + classID + "' AND Section_Id = '" + sectionID + "'  ORDER BY Stud_ID ASC", null);
                    if (c1.moveToFirst()) {
                        do {
                            Res_Teacher_ClassStudent stud = new Res_Teacher_ClassStudent();
                            stud.setId(c1.getString(c1.getColumnIndex("Stud_ID")));
                            stud.setFirst_name(c1.getString(c1.getColumnIndex("First_Name")));
                            stud.setMiddle_name(c1.getString(c1.getColumnIndex("Middle_Name")));
                            stud.setLast_name(c1.getString(c1.getColumnIndex("Last_Name")));
                            stud.setImage(c1.getString(c1.getColumnIndex("Image")));
                            stud.setLevel(c1.getString(c1.getColumnIndex("Level_Id")));
                            stud.setClass_id(c1.getString(c1.getColumnIndex("Class_Id")));
                            stud.setSection_id(c1.getString(c1.getColumnIndex("Section_Id")));
                            stud.setClass_name(c1.getString(c1.getColumnIndex("Class_Name")));
                            stud.setSection_name(c1.getString(c1.getColumnIndex("Section_Name")));
                            //added this columns
                            stud.setAdmnno(c1.getString(c1.getColumnIndex("AdmnNo")));
                            stud.setTerm(c1.getString(c1.getColumnIndex("Term")));
                            stud.setExamtype(c1.getString(c1.getColumnIndex("ExamType")));
                            stud.setSubject(c1.getString(c1.getColumnIndex("Subject")));
                            stud.setMarks(c1.getString(c1.getColumnIndex("Marks")));

                            stud.setSeat(c1.getString(c1.getColumnIndex("Seat_Position")));

                            listStud.add(stud);
                        } while (c1.moveToNext());
                    }
                    c1.close();
                    listClass.setStudent(listStud);

                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listClass;
    }


    public ArrayList<PojoStudentWithClassList> AccessStudentListForTeacherSearch() {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<PojoStudentWithClassList> listStudent = new ArrayList<>();
        Cursor c;
        try {
            Cursor c1 = db.rawQuery("SELECT * FROM teachers_student ORDER BY First_Name ASC", null);
            if (c1.moveToFirst()) {
                do {
                    PojoStudentWithClassList stud = new PojoStudentWithClassList();
                    String Id = c1.getString(c1.getColumnIndex("Stud_ID"));
                    String Name = c1.getString(c1.getColumnIndex("First_Name")) + " " +
                            c1.getString(c1.getColumnIndex("Middle_Name")) + " " +
                            c1.getString(c1.getColumnIndex("Last_Name"));
                    String Class = c1.getString(c1.getColumnIndex("Class_Name"));
                    String Section = c1.getString(c1.getColumnIndex("Section_Name"));
                    //String AdmnNo = c1.getString(c1.getColumnIndex("Admission_Number"));

                    stud.setId(Id);
                    stud.setName(Name);
                    stud.setClass_name(Class);
                    stud.setSection_name(Section);
                   // stud.setStudent_code(AdmnNo);

                    listStudent.add(stud);
                } while (c1.moveToNext());
            }
            c1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listStudent;
    }

    public void InsertSchoolEvents(ArrayList<Res_School_Events> data) {
        ClearTable("school_event");
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            for (int i = 0; i < data.size(); i++) {
                db.execSQL("INSERT INTO school_event (Event_ID, Event_Date, Event_Name, Event_Details) VALUES" +
                        " ('" + Support.ManageText(data.get(i).getId()) + "','" +
                        Support.ManageText(data.get(i).getDate()) + "','" +
                        Support.ManageText(data.get(i).getTitle()) + "','" +
                        Support.ManageText(data.get(i).getDetails()) + "');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }


    public ArrayList<Res_School_Events> AccessEventList() {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<Res_School_Events> listEvent = new ArrayList<>();
        Cursor c;
        try {
            Cursor c1 = db.rawQuery("SELECT * FROM school_event", null);
            if (c1.moveToFirst()) {
                do {
                    Res_School_Events event = new Res_School_Events();
                    event.setId(c1.getString(c1.getColumnIndex("Event_ID")));
                    event.setDate(c1.getString(c1.getColumnIndex("Event_Date")));
                    event.setTitle(c1.getString(c1.getColumnIndex("Event_Name")));
                    event.setDetails(c1.getString(c1.getColumnIndex("Event_Details")));
                    listEvent.add(event);
                } while (c1.moveToNext());
            }
            c1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listEvent;
    }

    public ArrayList<PojoParentWithClassList> AccessTeacherClassList() {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<PojoParentWithClassList> listClass = new ArrayList<>();
        Cursor c;
        try {
            Cursor c1 = db.rawQuery("SELECT * FROM teachers_class", null);
            if (c1.moveToFirst()) {
                do {
                    PojoParentWithClassList list = new PojoParentWithClassList();
                    list.setClass_id(c1.getString(c1.getColumnIndex("Class_ID")));
                    list.setClass_name(c1.getString(c1.getColumnIndex("Class_Name")));
                    list.setSection_id(c1.getString(c1.getColumnIndex("Section_ID")));
                    list.setSection_name(c1.getString(c1.getColumnIndex("Section_Name")));
                    listClass.add(list);
                } while (c1.moveToNext());
            }
            c1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listClass;
    }

    public ArrayList<PojoClassWithSectionList> AccessTeacherClassWithSectionList() {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<PojoClassWithSectionList> listClass = new ArrayList<>();
        Cursor c;
        try {
            Cursor c1 = db.rawQuery("SELECT * FROM teachers_class", null);
            if (c1.moveToFirst()) {
                do {
                    PojoClassWithSectionList list = new PojoClassWithSectionList();
                    list.setClass_id(c1.getString(c1.getColumnIndex("Class_ID")));
                    list.setClass_name(c1.getString(c1.getColumnIndex("Class_Name")));
                    list.setSection_id(c1.getString(c1.getColumnIndex("Section_ID")));
                    list.setSection_name(c1.getString(c1.getColumnIndex("Section_Name")));
                    list.setTeaching_subject(c1.getString(c1.getColumnIndex("Teaching_Subject")));
                    listClass.add(list);
                } while (c1.moveToNext());
            }
            c1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listClass;
    }


    public void InsertParentStudent(ArrayList<Res_Student_Info> studList) {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        try {

            for (int j = 0; j < studList.size(); j++) {
                Res_Student_Info stud = studList.get(j);
                db.execSQL("INSERT INTO parent_student (ID, Admission_ID, First_Name, Middle_Name, Last_Name, Image, Level_Name, DOB, Seat, Avg_Grade" +
                        ", Avg_Position, Overall_Behaviour, Behaviour_Rating, School_Details, Class_Details, Class_Teacher, Subject_Teacher) VALUES" +
                        " ('" + Support.ManageText(stud.getId()) + "','" +
                        Support.ManageText(stud.getAdmission_number()) + "','" +
                        Support.ManageText(stud.getFirst_name()) + "','" +
                        Support.ManageText(stud.getMiddle_name()) + "','" +
                        Support.ManageText(stud.getLast_name()) + "','" +
                        Support.ManageText(stud.getImage()) + "','" +
                        Support.ManageText(stud.getLast_name()) + "','" +
                        Support.ManageText(stud.getDOB()) + "','" +
                        Support.ManageText(stud.getSeat()) + "','" +
                        Support.ManageText(stud.getAvg_grade()) + "','" +
                        Support.ManageText(stud.getAvg_position()) + "','" +
                        Support.ManageText(stud.getOverall_behaviour()) + "','" +
                        Support.ManageText(stud.getBehaviour_ratting()) + "','" +
                        Support.ManageText(new Gson().toJson(stud.getSchool_details())) + "','" +
                        Support.ManageText(new Gson().toJson(stud.getClass_details())) + "','" +
                        Support.ManageText(new Gson().toJson(stud.getClass_teacher())) + "','" +
                        Support.ManageText(new Gson().toJson(stud.getSubject_teachers())) + "');");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
    }


    public ArrayList<Res_Student_Info> AccessParentStudent() {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<Res_Student_Info> listStudent = new ArrayList<>();
        Cursor c;
        try {
            Cursor c1 = db.rawQuery("SELECT * FROM parent_student", null);
            if (c1.moveToFirst()) {
                do {
                    Gson gson = new Gson();

                    TypeToken<ArrayList<Res_SI_Teacher>> token = new TypeToken<ArrayList<Res_SI_Teacher>>() {
                    };
                    ArrayList<Res_SI_Teacher> subject_teacher = gson.fromJson(c1.getString(c1.getColumnIndex("Subject_Teacher")), token.getType());
                    Res_SI_Teacher class_teacher = gson.fromJson(c1.getString(c1.getColumnIndex("Class_Teacher")), Res_SI_Teacher.class);
                    Res_SI_Class_Details class_details = gson.fromJson(c1.getString(c1.getColumnIndex("Class_Details")), Res_SI_Class_Details.class);
                    Res_SI_School_Details school_details = gson.fromJson(c1.getString(c1.getColumnIndex("School_Details")), Res_SI_School_Details.class);


                    Res_Student_Info list = new Res_Student_Info();
                    list.setId(c1.getString(c1.getColumnIndex("ID")));
                    list.setAdmission_number(c1.getString(c1.getColumnIndex("Admission_ID")));
                    list.setFirst_name(c1.getString(c1.getColumnIndex("First_Name")));
                    list.setMiddle_name(c1.getString(c1.getColumnIndex("Middle_Name")));
                    list.setLast_name(c1.getString(c1.getColumnIndex("Last_Name")));
                    list.setImage(c1.getString(c1.getColumnIndex("Image")));
                    list.setLevel(c1.getString(c1.getColumnIndex("Level_Name")));
                    list.setDOB(c1.getString(c1.getColumnIndex("DOB")));
                    list.setAvg_grade(c1.getString(c1.getColumnIndex("Avg_Grade")));
                    list.setAvg_position(c1.getString(c1.getColumnIndex("Avg_Position")));
                    list.setOverall_behaviour(c1.getString(c1.getColumnIndex("Overall_Behaviour")));
                    list.setBehaviour_ratting(c1.getString(c1.getColumnIndex("Behaviour_Rating")));
                    list.setSeat(c1.getString(c1.getColumnIndex("Seat")));
                    list.setSubject_teachers(subject_teacher);
                    list.setClass_teacher(class_teacher);
                    list.setClass_details(class_details);
                    list.setSchool_details(school_details);
                    listStudent.add(list);
                } while (c1.moveToNext());
            }
            c1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listStudent;
    }

    public ArrayList<PojoStudentWithClassList> AccessStudentListForParentSearch() {

        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        // TODO Auto-generated method stub
        ArrayList<PojoStudentWithClassList> listStudent = new ArrayList<>();
        Cursor c;
        try {
            Cursor c1 = db.rawQuery("SELECT * FROM parent_student ORDER BY First_Name ASC", null);
            if (c1.moveToFirst()) {
                do {

                    Gson gson = new Gson();
                    Res_SI_Class_Details class_details = gson.fromJson(c1.getString(c1.getColumnIndex("Class_Details")), Res_SI_Class_Details.class);


                    PojoStudentWithClassList stud = new PojoStudentWithClassList();


                    String Id = c1.getString(c1.getColumnIndex("ID"));
                    String Name = c1.getString(c1.getColumnIndex("First_Name")) + " " +
                            c1.getString(c1.getColumnIndex("Middle_Name")) + " " +
                            c1.getString(c1.getColumnIndex("Last_Name"));
                    String Class = class_details.getClass_name();
                    String Section = class_details.getSection_name();

                    stud.setId(Id);
                    stud.setName(Name);
                    stud.setClass_name(Class);
                    stud.setSection_name(Section);

                    listStudent.add(stud);
                } while (c1.moveToNext());
            }
            c1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listStudent;
    }


    private int getVersionId() {
        return Support.ConvertToInteger(Support.GetPrefDefault(mContext, Vars.Pref_DB_Version, "2"));
    }


}

