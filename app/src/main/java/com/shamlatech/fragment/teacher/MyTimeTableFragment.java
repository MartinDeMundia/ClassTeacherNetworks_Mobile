package com.shamlatech.fragment.teacher;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.adapter.TimeTableListHorizontalAdapter;
import com.shamlatech.adapter.TimeTableListVerticalAdapter;
import com.shamlatech.api_response.Res_Teacher_TimeTable;
import com.shamlatech.api_response.Result_TeacherInfo;
import com.shamlatech.database.DBAdapter;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Support;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.loadJSONTimetableFromAsset;
import static com.shamlatech.utils.Vars.staUserInfo;


/**
 * Created by Martin Mundia Mugambi on 04-12-2019.
 */

public class MyTimeTableFragment extends Fragment implements View.OnClickListener {

    View view;

    LinearLayout linearShare, linearEmail, linearDownload;
    RecyclerView recyclerTimeTableHorizontal, recyclerTimeTableVertical;
    TimeTableListHorizontalAdapter mAdapterHorizontal;
    TimeTableListVerticalAdapter mAdapterVertical;
    ArrayList<Res_Teacher_TimeTable> listTimeTable = new ArrayList<>();
    DBAdapter db;
    Activity activity;
    RelativeLayout relativeVerticalView, relativeHorizontalView;

    Spinner  spinnerTerm ;
    ArrayList<String> listTerm = new ArrayList<String>() {{
        add("Term 1");
        add("Term 2");
        add("Term 3");
    }};

    LinearLayout linearHorizontal, linearVertical;
    TextView txtHorizontalLabel, txtVerticalLabel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_my_time_table, container, false);

        try {
            db = new DBAdapter(activity, activity.getFilesDir().getAbsolutePath());
            db.prepareDatabase();
        } catch (Exception e) {
        }


        spinnerTerm = view.findViewById(R.id.spinnerTerm);
        ArrayAdapter adapterTerm = new ArrayAdapter(activity, R.layout.simple_spinner_item, listTerm);
        adapterTerm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTerm.setAdapter(adapterTerm);

        spinnerTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, final View view, int i, long l) {

                String user_id = staUserInfo.getId();
                String role_id = staUserInfo.getRole();
                String term =  spinnerTerm.getSelectedItem().toString();

                getRetro_Call(activity, service.getTimeTableDetails(user_id, role_id,term), false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_TeacherInfo mPojo_TeacherInfo = onPojoBuilder(objectResponse, Result_TeacherInfo.class);
                        if (mPojo_TeacherInfo != null) {
                            if (mPojo_TeacherInfo.getStatus().equals(API_Code.Success)) {
                                db.ClearTable("teachers_timetable");
                                db.InsertTeachersTimeTable(mPojo_TeacherInfo.getTimetable());


                                listTimeTable = new ArrayList<>();
                                listTimeTable = db.AccessTeachersTimetable();

                                mAdapterHorizontal = new TimeTableListHorizontalAdapter(activity, listTimeTable);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                                recyclerTimeTableHorizontal.setLayoutManager(mLayoutManager);
                                recyclerTimeTableHorizontal.setItemAnimator(new DefaultItemAnimator());
                                recyclerTimeTableHorizontal.setAdapter(mAdapterHorizontal);

                                mAdapterVertical = new TimeTableListVerticalAdapter(activity, listTimeTable);
                                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(activity);
                                recyclerTimeTableVertical.setLayoutManager(mLayoutManager1);
                                recyclerTimeTableVertical.setItemAnimator(new DefaultItemAnimator());
                                recyclerTimeTableVertical.setAdapter(mAdapterVertical);

                                Snackbar.make(view, "Loaded Timetable.", Snackbar.LENGTH_LONG).setAction("Action", null).show();



                            }
                        }
                    }
                    @Override
                    public void onFailure(String message) {
                        Log.e("Error", message);
                    }
                });








            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        listTimeTable = new ArrayList<>();
      //  prepareTimeTable();
        listTimeTable = db.AccessTeachersTimetable();

        relativeVerticalView = view.findViewById(R.id.relativeVerticalView);
        relativeHorizontalView = view.findViewById(R.id.relativeHorizontalView);
        recyclerTimeTableVertical = view.findViewById(R.id.recyclerTimeTableVertical);
        recyclerTimeTableHorizontal = view.findViewById(R.id.recyclerTimeTableHorizontal);

        linearShare = view.findViewById(R.id.linearShare);
        linearEmail = view.findViewById(R.id.linearEmail);
        linearDownload = view.findViewById(R.id.linearDownload);

        linearHorizontal = view.findViewById(R.id.linearHorizontal);
        linearVertical = view.findViewById(R.id.linearVertical);
        txtHorizontalLabel = view.findViewById(R.id.txtHorizontalLabel);
        txtVerticalLabel = view.findViewById(R.id.txtVerticalLabel);

        linearShare.setOnClickListener(this);
        linearEmail.setOnClickListener(this);
        linearDownload.setOnClickListener(this);


        mAdapterHorizontal = new TimeTableListHorizontalAdapter(activity, listTimeTable);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerTimeTableHorizontal.setLayoutManager(mLayoutManager);
        recyclerTimeTableHorizontal.setItemAnimator(new DefaultItemAnimator());
        recyclerTimeTableHorizontal.setAdapter(mAdapterHorizontal);

        mAdapterVertical = new TimeTableListVerticalAdapter(activity, listTimeTable);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(activity);
        recyclerTimeTableVertical.setLayoutManager(mLayoutManager1);
        recyclerTimeTableVertical.setItemAnimator(new DefaultItemAnimator());
        recyclerTimeTableVertical.setAdapter(mAdapterVertical);

        linearHorizontal.setOnClickListener(this);
        linearVertical.setOnClickListener(this);

        linearHorizontal.callOnClick();
        return view;
    }


    private void prepareTimeTable() {
        listTimeTable = loadJSONTimetableFromAsset(getActivity());
        Log.e("Test", "Test");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onClick(View v) {
        String user_id = staUserInfo.getId();
        String role_id = staUserInfo.getRole();
        String term =  spinnerTerm.getSelectedItem().toString();
        String bTerm = "";
        try {
            bTerm = URLEncoder.encode(term, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String clsdlink = "http://apps.classteacher.school/admin/t_pdf/"+user_id+"/0/2019/"+ bTerm +"/teacher/Timetable";
        switch (v.getId()) {
            case R.id.linearHorizontal:
                linearHorizontal.setSelected(true);
                linearVertical.setSelected(false);
                txtVerticalLabel.setTextColor(activity.getResources().getColor(R.color.colorDarkGray));
                txtHorizontalLabel.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                relativeVerticalView.setVisibility(View.GONE);
                relativeHorizontalView.setVisibility(View.VISIBLE);
                break;
            case R.id.linearVertical:
                linearHorizontal.setSelected(false);
                linearVertical.setSelected(true);
                txtHorizontalLabel.setTextColor(activity.getResources().getColor(R.color.colorDarkGray));
                txtVerticalLabel.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                relativeVerticalView.setVisibility(View.VISIBLE);
                relativeHorizontalView.setVisibility(View.GONE);
                break;
            case R.id.linearShare:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String sharedString = "Link to time table : "+ clsdlink;
                shareIntent.putExtra(Intent.EXTRA_TEXT,sharedString);
                startActivity(shareIntent);
                break;
            case R.id.linearEmail:
                String subject = "Timetable";
                String bodyText = "This is a link to the time table " +  clsdlink;
                String mailto = "mailto: enteremail@classteacher.com" +
                        "?cc=" + "martin@coreict.co.ke" +
                        "&subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(bodyText);

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(activity, "No Email App found.", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.linearDownload:
                if (clsdlink != null && !clsdlink.isEmpty())
                    Support.downloadFile(activity,clsdlink);
                else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}