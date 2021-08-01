package com.shamlatech.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shamlatech.adapter.DetailMarkListAdapter;
import com.shamlatech.adapter.DetailPartMarkListAdapter;
import com.shamlatech.api_response.Res_Stud_Education_Marks;
import com.shamlatech.api_response.Res_Stud_Education_Parts_Marks;
import com.shamlatech.api_response.Res_Student_Info;
import com.shamlatech.api_response.Res_Teacher_Class;
import com.shamlatech.api_response.Res_Teacher_TimeTable;
import com.shamlatech.api_response.Res_UserInfo;
import com.shamlatech.pojo.PojoClassPosition;
import com.shamlatech.school_management.Index;
import com.shamlatech.school_management.R;
import com.shamlatech.services.MessageService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.shamlatech.utils.Vars.App_Start;
import static com.shamlatech.utils.Vars.Pref_Chat_User_ID;
import static com.shamlatech.utils.Vars.Pref_User_ID;
import static com.shamlatech.utils.Vars.Pref_User_Info;
import static com.shamlatech.utils.Vars.Refresh_First_Announcement;
import static com.shamlatech.utils.Vars.Refresh_Name_Announcement;
import static com.shamlatech.utils.Vars.Refresh_Recent_Announcement;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by ADMIN on 06-Apr-18.
 */

public class Support {

    static Dialog pick_Dialog;

    public static SharedPreferences sharedpreferences;
    private String TAG = Support.class.getSimpleName();

    public static String ManageText(String Input) {
        Input = (Input == null) ? "" : Input;
        String Output = Input.replace("'", "''");
        return Output;
    }

    public static String GetDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static boolean IsNetworkNotConnected(Activity activity) {
        if (activity != null) {
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() == null) {
                ShowAlertWithTitle(activity, "Internet connection is not available, please check if Mobile Data or Wifi is On");
            }
            return cm.getActiveNetworkInfo() == null;
        }
        return false;
    }

    public static void SetPref(Context con, String Key, String Value) {
        sharedpreferences = con.getSharedPreferences(Vars.MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(Key, Value);
        edit.commit();
    }

    public static String GetPref(Context con, String Key) {
        sharedpreferences = con.getSharedPreferences(Vars.MyPref, Context.MODE_PRIVATE);
        String Value = "";
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, "");
        }
        return Value;
    }

    public static String GetPrefDefault(Context con, String Key, String Default) {
        sharedpreferences = con.getSharedPreferences(Vars.MyPref, Context.MODE_PRIVATE);
        String Value = Default;
        if (sharedpreferences.contains(Key)) {
            Value = sharedpreferences.getString(Key, Default);
        }
        return Value;
    }

    public static void showProgress(View v) {
        v.setVisibility(View.VISIBLE);
    }

    public static void hideProgress(View v) {
        v.setVisibility(View.GONE);
    }

    public static void hideKeyboard(Context context) {
        try {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            if ((((Activity) context).getCurrentFocus() != null) && (((Activity) context).getCurrentFocus().getWindowToken() != null)) {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String GetUTCCurrentTimeMillis() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long time = cal.getTimeInMillis();
        return time + "";
    }

    public static String GetCurrentTimeMillis() {
        return System.currentTimeMillis() + "";
    }

    public static String GetCurrentUTCTimeFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(c.getTime());
    }

    public static boolean ValidateMobileNumber(String mobile_no) {
        if (mobile_no.length() == 10) {
            return true;
        }
        return false;
    }

    public static boolean IsMobile(String value) {
        boolean Result = true;
        if (value.length() == 0) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            String Chars = value.charAt(i) + "";
            try {
                Integer.parseInt(Chars);
            } catch (Exception e) {
                Result = false;
            }
        }
        return Result;
    }

    public static boolean ValidatePassword(String password) {
        if (password.length() > 5) {
            return true;
        }
        return false;
    }

    public static boolean ValidateMailId(String Mail_Id) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(Mail_Id).matches();
    }

    public static String FormatFee(String value, boolean postfix) {
        DecimalFormat formatter;
        if (postfix) {
            formatter = new DecimalFormat("#,###,###.00");
            if (value.isEmpty() || value.equals("0") || value.equals("0.0")) {
                return "<small><sup>KES </sup></small> <b>0.00</b>";
            }
        } else {
            formatter = new DecimalFormat("#,###,###");
            if (value.isEmpty() || value.equals("0") || value.equals("0.0")) {
                return "<small><sup>KES </sup></small> <b>0</b>";
            }
        }
        return "<small><sup>KES</sup></small> <b>" + formatter.format(ConvertToDouble(value)) + "</b>";
    }

    public static int ConvertToInteger(String value) {
        int result = 0;
        if (value != null && !value.trim().equals("")) {
            result = Integer.parseInt(value);
        }
        return result;
    }

    public static double ConvertToDouble(String value) {
        double result = 0.0;
        if (value != null && !value.trim().equals("")) {
            result = Double.parseDouble(value);
        }
        return result;
    }

    public static long ConvertToLong(String value) {
        long result = 0;
        try {
            if (value != null && !value.trim().equals("")) {
                result = Long.parseLong(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static float ConvertToFloat(String value) {
        float result = 0f;
        try {
            if (value != null && !value.trim().equals("")) {
                result = Float.parseFloat(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void ShowAlertWithTitle(Context con, String Message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setTitle("Alert");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void ShowError(Context con, String Title, String Message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void ShowAlertWithOutTitle(Context con, String Message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(con);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static Date FormatDateFromString(String newDate, String Pattern) {
        Date ReturnDate = null;
        SimpleDateFormat format = new SimpleDateFormat(Pattern);
        try {
            ReturnDate = format.parse(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ReturnDate;
    }

    public static String FormatDateFromDate(Date newDate, String Pattern) {
        String ReturnDate = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(Pattern);
            ReturnDate = format.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnDate;
    }

    public static String GetCurrentDateInFormat(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        return format.format(c.getTime());
    }

    public static String FormatDateTimeForShow(String InputDate, String Pattern, String Default) {
        String ReturnDate = Default;
        if (InputDate.equalsIgnoreCase("0000-00-00 00:00:00") || InputDate.equalsIgnoreCase("")) {
            return ReturnDate;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date newDate = format.parse(InputDate);
            format = new SimpleDateFormat(Pattern);
            format.setTimeZone(TimeZone.getDefault());
            ReturnDate = format.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnDate;
    }

    public static String FormatDateForShow(String InputDate, String Pattern, String Default) {
        String ReturnDate = Default;
        if (InputDate.equalsIgnoreCase("0000-00-00") || InputDate.equalsIgnoreCase("")) {
            return ReturnDate;
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date newDate = format.parse(InputDate);
            format = new SimpleDateFormat(Pattern);
            format.setTimeZone(TimeZone.getDefault());
            ReturnDate = format.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnDate;
    }


    public static void showToast(Context context, String mMsg) {
        Toast.makeText(context, mMsg, Toast.LENGTH_SHORT).show();
    }

    public static void ShowImage(Context mCon, ImageView img, String url) {
        if (url != null && !url.equals(""))
            Picasso.with(mCon).load(url).fit().into(img);
    }

    public static void ShowImage(Context mCon, ImageView img, File file) {
        if (file != null && file.isFile())
            Picasso.with(mCon).load(file).fit().into(img);
    }

    public static String Show_Format_Date_For_Message_List(String Input_Sec) {
        String MessageDate = Show_Format_Date_For_Chat(Input_Sec);
        String Output_Date = "";
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar c = Calendar.getInstance();
        String TodayDate = format.format(c.getTime());
        if (MessageDate.equals(TodayDate)) {
            Output_Date = Show_Format_Date_Chat_With_TimeOnly(Input_Sec);
        } else {
            Output_Date = MessageDate + " " + Show_Format_Date_Chat_With_TimeOnly(Input_Sec);
        }
        return Output_Date;
    }

    public static String Show_Format_Date_Chat_With_TimeOnly(String Input_Sec) {
        String Output_Date = "";
        try {
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ConvertToLong(Input_Sec));
            String Input_Date = fromUser.format(calendar.getTime());

            Date UTC_TIME = fromUser.parse(Input_Date);
            fromUser.setTimeZone(TimeZone.getDefault());
            Input_Date = fromUser.format(UTC_TIME);
            SimpleDateFormat myFormat = new SimpleDateFormat("hh:mm a");
            Output_Date = myFormat.format(fromUser.parse(Input_Date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Output_Date;
    }

    public static String Show_Format_Date_For_Chat(String Input_Sec) {
        String Output_Date = "";
        try {
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ConvertToLong(Input_Sec));
            String Input_Date = fromUser.format(calendar.getTime());

            Date UTC_TIME = fromUser.parse(Input_Date);
            fromUser.setTimeZone(TimeZone.getDefault());
            Input_Date = fromUser.format(UTC_TIME);
            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Output_Date = myFormat.format(fromUser.parse(Input_Date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Output_Date;
    }


    public static void UpdateProfileInfo(Context con, Res_UserInfo userinfo) {

        Gson gson = new Gson();
        String jsonUser = gson.toJson(userinfo);
        Support.SetPref(con, Pref_User_ID, userinfo.getId());
        Support.SetPref(con, Pref_Chat_User_ID, userinfo.getId() + "_" + userinfo.getRole());
        Support.SetPref(con, Pref_User_Info, jsonUser);
    }

    public static void Logout(Context con) {
        App_Start = "0";
        Support.SetPref(con, Vars.Pref_User_ID, "");
        Support.SetPref(con, Vars.Pref_User_Info, "");
        Support.SetPref(con, Pref_Chat_User_ID, "");
        con.stopService(new Intent(con, MessageService.class));
        Intent in = new Intent(con, Index.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        con.startActivity(in);
    }

    public static Res_UserInfo AccessUserInfo(Context con) {
        Gson gson = new Gson();
        staUserInfo = gson.fromJson(Support.GetPref(con, Pref_User_Info), Res_UserInfo.class);
        return staUserInfo;
    }

    public static ArrayList<PojoClassPosition> PrepareClassLayout(Res_Teacher_Class myClass) {
        ArrayList<PojoClassPosition> listPosition = new ArrayList<>();
        if (myClass != null) {
            for (int i = 1; i <= ConvertToInteger(myClass.getTotal_seat()); i++) {
                String id = "";
                String position = i + "";
                String name = "";
                String status = "free";
                boolean selection = false;
                for (int j = 0; j < myClass.getStudent().size(); j++) {
                    if (myClass.getStudent().get(j).getSeat().equals("" + i)) {
                        id = myClass.getStudent().get(j).getId();
                        name = myClass.getStudent().get(j).getFirst_name();
                        status = "placed";
                    }
                }

                PojoClassPosition pos = new PojoClassPosition();
                pos.setId(id);
                pos.setPosition(position);
                pos.setName(name);
                pos.setStatus(status);
                pos.setSelection(selection);
                listPosition.add(pos);
            }
        }
        return listPosition;
    }


    public static ArrayList<PojoClassPosition> PrepareSeatForStudent(Res_Student_Info student) {
        ArrayList<PojoClassPosition> listPosition = new ArrayList<>();
        for (int i = 1; i <= ConvertToInteger(student.getClass_details().getTotal_seat()); i++) {
            String id = "";
            String position = i + "";
            String name = "";
            String status = "free";
            boolean selection = false;
            if (student.getSeat().equals("" + i)) {
                id = student.getId();
                name = student.getFirst_name();
                status = "placed";
            }

            PojoClassPosition pos = new PojoClassPosition();
            pos.setId(id);
            pos.setPosition(position);
            pos.setName(name);
            pos.setStatus(status);
            pos.setSelection(selection);
            listPosition.add(pos);
        }
        return listPosition;
    }

    public static boolean CanReloadAPI(String APIName) {
        boolean result = true;
        switch (APIName) {
            case Refresh_Name_Announcement:
                result = (Refresh_First_Announcement.equals(Refresh_Recent_Announcement)) ? false : true;
                break;
        }
        return result;
    }

    public static void refreshMediaScanner(Activity activity, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            scanIntent.setData(uri);
            activity.sendBroadcast(scanIntent);
        } else {
            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, uri);
            activity.sendBroadcast(intent);
        }
    }

    public static void downloadFile(Activity activity, String url) {
        try {
            Toast.makeText(activity, "Download Started", Toast.LENGTH_LONG).show();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(Support.getFileNameFromUrl(url));
            request.setDescription("Downloading File...");

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void downloadFile(Activity activity, String url, String fileName) {
        try {
            Toast.makeText(activity, "Download Started", Toast.LENGTH_LONG).show();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle(fileName);
            request.setDescription("Downloading File...");

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getFileNameFromUrl(String url) {
        String fileName = "";
        try {
            if (url != null && !url.isEmpty()) {
                URI uri = new URI(url);
                String path = uri.getPath();
                fileName = path.substring(path.lastIndexOf('/') + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static void sendEmail(Activity activity, String attachment) {
        try {
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Attendance Report");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, attachment);
            activity.startActivity(Intent.createChooser(emailIntent, "Send email"));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void shareLink(Activity activity, String link) {
        try {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            share.putExtra(Intent.EXTRA_SUBJECT, "Attendance");
            share.putExtra(Intent.EXTRA_TEXT, link);
            activity.startActivity(Intent.createChooser(share, "Share Attendance!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Res_Teacher_TimeTable> loadJSONTimetableFromAsset(Context con) {
        ArrayList<Res_Teacher_TimeTable> timeTablesList = new ArrayList<>();
        String json = null;
        try {
            InputStream is = con.getAssets().open("timetable.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("timetable");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Res_Teacher_TimeTable table = new Res_Teacher_TimeTable();
                table.setSlot(jo_inside.getString("slot"));
                table.setTime(jo_inside.getString("time"));
                table.setMon(jo_inside.getString("mon"));
                table.setTue(jo_inside.getString("tue"));
                table.setWed(jo_inside.getString("wed"));
                table.setThu(jo_inside.getString("thu"));
                table.setFri(jo_inside.getString("fri"));
                table.setBreak_name(jo_inside.getString("break_name"));
                table.setType(jo_inside.getString("type"));

                timeTablesList.add(table);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return timeTablesList;
    }

    public static void showPartMarks(Activity act, Res_Stud_Education_Marks marks) {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(act);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_part_marks);

            RecyclerView recyclerPartMarks;
            TextView txtSubjectName, txtMark, txtGrade;

            recyclerPartMarks = pick_Dialog.findViewById(R.id.recyclerPartMarks);
            txtSubjectName = pick_Dialog.findViewById(R.id.txtSubjectName);
            txtMark = pick_Dialog.findViewById(R.id.txtMark);
            txtGrade = pick_Dialog.findViewById(R.id.txtGrade);

            txtSubjectName.setText(marks.getSubject_name());
            txtMark.setText(marks.getStudent_mark() + "/" + marks.getTotal_mark());
            txtGrade.setText(marks.getGrade());

            DetailPartMarkListAdapter mAdapter = new DetailPartMarkListAdapter(act, marks.getPart_marks());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(act);
            recyclerPartMarks.setLayoutManager(mLayoutManager);
            recyclerPartMarks.setItemAnimator(new DefaultItemAnimator());
            recyclerPartMarks.setAdapter(mAdapter);

            pick_Dialog.show();
        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showPartMarks(act, marks);
        } else {
            pick_Dialog = null;
            showPartMarks(act, marks);
        }
    }
}
