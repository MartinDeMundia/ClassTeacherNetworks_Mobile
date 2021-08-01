package com.shamlatech.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.activity.MessageActivity;
import com.shamlatech.adapter.TeacherListWithActionAdapter;
import com.shamlatech.api_response.Res_Stud_Teacher;
import com.shamlatech.api_response.Res_Stud_Teacher_Info;
import com.shamlatech.school_management.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import static com.shamlatech.utils.Support.ShowImage;
import static com.shamlatech.utils.Vars.REQUEST_PHONE_CALL;
import static com.shamlatech.utils.Vars.Role_Teacher;

/**
 * Created by Martin Mundia Mugambi on 24-OCT-2019.
 */

public class STeacherFrag extends Fragment implements View.OnClickListener {

    Activity activity;
    View view;
    TeacherListWithActionAdapter mAdapter;
    ArrayList<Res_Stud_Teacher_Info> listTeachers = new ArrayList<>();
    RecyclerView recyclerSubjectTeachers;

    RelativeLayout relativeClassTeacher;
    ImageView imgClassTeacherImage, imgMessageToClassTeacher, imgCallToClassTeacher, imgMailToClassTeacher;
    TextView txtClassTeacherName;
    Res_Stud_Teacher studTeacherInfo;
    LinearLayout linearContent;
    View InnerProgress = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_stud_teacher, container, false);

        listTeachers = new ArrayList<>();
        InitializeProgress(view);

        recyclerSubjectTeachers = view.findViewById(R.id.recyclerSubjectTeachers);
        txtClassTeacherName = view.findViewById(R.id.txtClassTeacherName);
        relativeClassTeacher = view.findViewById(R.id.relativeClassTeacher);

        imgClassTeacherImage = view.findViewById(R.id.imgClassTeacherImage);
        imgMessageToClassTeacher = view.findViewById(R.id.imgMessageToClassTeacher);
        imgCallToClassTeacher = view.findViewById(R.id.imgCallToClassTeacher);
        imgMailToClassTeacher = view.findViewById(R.id.imgMailToClassTeacher);

        mAdapter = new TeacherListWithActionAdapter(activity, listTeachers, STeacherFrag.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerSubjectTeachers.setLayoutManager(mLayoutManager);
        recyclerSubjectTeachers.setItemAnimator(new DefaultItemAnimator());
        recyclerSubjectTeachers.setAdapter(mAdapter);

        imgMessageToClassTeacher.setOnClickListener(this);
        imgCallToClassTeacher.setOnClickListener(this);
        imgMailToClassTeacher.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void InitializeProgress(View view) {
        linearContent = view.findViewById(R.id.linearContent);
        InnerProgress = (View) view.findViewById(R.id.ProgressInner);
        InnerProgress.setVisibility(View.VISIBLE);
        linearContent.setVisibility(View.INVISIBLE);
        String indicator = activity.getIntent().getStringExtra("indicator");
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.show();
    }

    public void loadData(Bundle bundle) {
        studTeacherInfo = new Res_Stud_Teacher();
        if (bundle != null) {
            if (bundle.containsKey("teacher_info")) {
                studTeacherInfo = (Res_Stud_Teacher) bundle.getSerializable("teacher_info");
                if (studTeacherInfo != null)
                    PrepareLoadData();
            }
        }
    }

    public void showProgress() {
        InitializeProgress(view);
    }

    private void PrepareLoadData() {
        linearContent.setVisibility(View.VISIBLE);
        InnerProgress.setVisibility(View.GONE);
        listTeachers = studTeacherInfo.getSubject_teachers();
        mAdapter = new TeacherListWithActionAdapter(activity, listTeachers, STeacherFrag.this);
        recyclerSubjectTeachers.setAdapter(mAdapter);

        if (!studTeacherInfo.getClass_teacher().getId().equals("")) {
            relativeClassTeacher.setVisibility(View.VISIBLE);
        } else {
            relativeClassTeacher.setVisibility(View.GONE);
        }
        txtClassTeacherName.setText(studTeacherInfo.getClass_teacher().getFirst_name() + " " +
                studTeacherInfo.getClass_teacher().getMiddle_name() + " " +
                studTeacherInfo.getClass_teacher().getLast_name());

        ShowImage(activity, imgClassTeacherImage, studTeacherInfo.getClass_teacher().getImage());

    }

    public void onClickTeacherName(int position) {
        Toast.makeText(activity, "Teachers Clicked", Toast.LENGTH_LONG).show();
    }

    public void onClickMail(int position) {
        sendEmail(listTeachers.get(position).getEmail());
    }

    public void onClickSubjectName(int position) {
        Toast.makeText(activity, "Subject Clicked", Toast.LENGTH_LONG).show();
    }

    public void onClickCall(int position) {
        sendCall(listTeachers.get(position).getPhone_number());
    }

    public void onClickMessage(int position) {
        startActivity(new Intent(activity, MessageActivity.class)
                .putExtra("id", listTeachers.get(position).getId())
                .putExtra("role", Role_Teacher)
                .putExtra("name", listTeachers.get(position).getFirst_name())
                .putExtra("image", listTeachers.get(position).getImage()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgMessageToClassTeacher:
                startActivity(new Intent(activity, MessageActivity.class)
                        .putExtra("id", studTeacherInfo.getClass_teacher().getId())
                        .putExtra("role", Role_Teacher)
                        .putExtra("name", studTeacherInfo.getClass_teacher().getFirst_name())
                        .putExtra("image", studTeacherInfo.getClass_teacher().getImage()));
                break;
            case R.id.imgCallToClassTeacher:
                sendCall(studTeacherInfo.getClass_teacher().getPhone_number());
                break;
            case R.id.imgMailToClassTeacher:
                sendEmail(studTeacherInfo.getClass_teacher().getEmail());
                break;
        }
    }

    protected void sendCall(String phoneNumber) {
        Intent in = new Intent(Intent.ACTION_CALL);
        in.setData(Uri.parse("tel:" + phoneNumber));
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        } else {
            startActivity(in);
        }
    }

    protected void sendEmail(String Email) {
        String[] TO = {Email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }
}