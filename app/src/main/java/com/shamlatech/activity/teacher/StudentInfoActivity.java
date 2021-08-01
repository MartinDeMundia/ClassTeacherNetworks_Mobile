package com.shamlatech.activity.teacher;

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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.shamlatech.activity.MessageActivity;
import com.shamlatech.adapter.StudentParentListAdapter;
import com.shamlatech.api_response.Res_SI_Parent_Details;
import com.shamlatech.api_response.Result_Student_Info;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.BaseFragment;
import com.shamlatech.school_management.HomeActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.REQUEST_PHONE_CALL;
import static com.shamlatech.utils.Vars.Role_Parent;
import static com.shamlatech.utils.Vars.staStudentInfo;

public class StudentInfoActivity extends BaseFragment implements View.OnClickListener {


    public static String StudId;

    ImageView imgStudent, imgSchoolLogo;
    TextView txtStudentName, txtSchoolName, txtClassName, txtAvgGrade, txtAvgPosition, txtBehaviourStatus, txtGender;
    SimpleRatingBar rattingOverAllBehaviour;
    StudentParentListAdapter mAdapter;
    RecyclerView recyclerParents;
    ArrayList<Res_SI_Parent_Details> listParents;
    TextView txtViewReport, txtAdmissionNumber;
    Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_student_info, container, false);

        ((BaseActivity) activity).declareAppBar8("My Student");
        InitializeProgress(view);

        listParents = new ArrayList<>();

        imgStudent = view.findViewById(R.id.imgStudent);
        imgSchoolLogo = view.findViewById(R.id.imgSchoolLogo);
        recyclerParents = view.findViewById(R.id.recyclerParents);

        txtAdmissionNumber = view.findViewById(R.id.txtAdmissionNumber);
        txtViewReport = view.findViewById(R.id.txtViewReport);
        txtStudentName = view.findViewById(R.id.txtStudentName);
        txtSchoolName = view.findViewById(R.id.txtSchoolName);
        txtClassName = view.findViewById(R.id.txtClassName);
        txtGender = view.findViewById(R.id.txtGender);
        txtAvgGrade = view.findViewById(R.id.txtAvgGrade);
        txtAvgPosition = view.findViewById(R.id.txtAvgPosition);
        txtBehaviourStatus = view.findViewById(R.id.txtBehaviourStatus);
        rattingOverAllBehaviour = view.findViewById(R.id.rattingOverAllBehaviour);


        mAdapter = new StudentParentListAdapter(activity, listParents, StudentInfoActivity.this);
        final LinearLayoutManager mLinearLayout = new LinearLayoutManager(activity);
        recyclerParents.setLayoutManager(mLinearLayout);
        recyclerParents.setItemAnimator(new DefaultItemAnimator());
        recyclerParents.setAdapter(mAdapter);

        txtViewReport.setOnClickListener(this);

        PreparePage();

        return view;
    }

    public void InitializeProgress(View view) {
        Vars.Custom_Progress = (View) view.findViewById(R.id.Custom_Progress);
        Vars.Custom_Progress.setVisibility(View.GONE);
        String indicator = activity.getIntent().getStringExtra("indicator");
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) activity).UpdateAppBarColor(R.color.colorTabPrimary);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtViewReport:
                ((HomeActivity) activity).UpdateStudentFragment(new TeachersStudentActivity(), "TEACHER", StudId, "");
                break;
        }
    }


    private void PreparePage() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("stud_id")) {
                StudId = bundle.getString("stud_id");
                getRetro_AccessStudentInfo(StudId);
            }
        }
    }

    public void getRetro_AccessStudentInfo(String stud_id) {
        getRetro_Call(activity, service.getAccessStudentInfo(stud_id),
                true, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Student_Info mPojo_Student = onPojoBuilder(objectResponse, Result_Student_Info.class);
                        if (mPojo_Student != null) {
                            if (mPojo_Student.getStatus().equals(API_Code.Success)) {
                                Vars.staStudentInfo = mPojo_Student.getStudent();
                                ((BaseActivity) activity).declareAppBar8(staStudentInfo.getFirst_name());
                                UpdatePage();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    private void UpdatePage() {
        txtAdmissionNumber.setText("Admission No : " + Vars.staStudentInfo.getAdmission_number());
        txtStudentName.setText(Vars.staStudentInfo.getFirst_name());
        txtSchoolName.setText(Vars.staStudentInfo.getSchool_details().getSchool_name());
        txtGender.setText(Vars.staStudentInfo.getGender());
        txtClassName.setText("Class " + Vars.staStudentInfo.getClass_details().getClass_name() + " " +
                Vars.staStudentInfo.getClass_details().getSection_name());
        if (Vars.staStudentInfo.getAvg_grade() != null && !Vars.staStudentInfo.getAvg_grade().isEmpty())
            txtAvgGrade.setText(Vars.staStudentInfo.getAvg_grade());
        else
            txtAvgGrade.setText("-");
        if (Vars.staStudentInfo.getAvg_position() != null && !Vars.staStudentInfo.getAvg_position().isEmpty())
            txtAvgPosition.setText(Vars.staStudentInfo.getAvg_position());
        else
            txtAvgPosition.setText("-");
        txtBehaviourStatus.setText(Vars.staStudentInfo.getOverall_behaviour());

        rattingOverAllBehaviour.setRating(Support.ConvertToFloat(Vars.staStudentInfo.getBehaviour_ratting()));

        Support.ShowImage(activity, imgStudent, Vars.staStudentInfo.getImage());
        Support.ShowImage(activity, imgSchoolLogo, Vars.staStudentInfo.getSchool_details().getSchool_logo());

        listParents = Vars.staStudentInfo.getParent_details();

        mAdapter = new StudentParentListAdapter(activity, listParents, StudentInfoActivity.this);
        recyclerParents.setAdapter(mAdapter);
    }


    public void onClickCall(int position) {
        sendCall(Vars.staStudentInfo.getParent_details().get(position).getPhone_number());
    }

    public void onClickMail(int position) {
        sendEmail(Vars.staStudentInfo.getParent_details().get(position).getEmail());
    }

    public void onClickMessage(int position) {
        startActivity(new Intent(activity, MessageActivity.class)
                .putExtra("id", Vars.staStudentInfo.getParent_details().get(position).getId())
                .putExtra("role", Role_Parent)
                .putExtra("name", Vars.staStudentInfo.getParent_details().get(position).getFirst_name())
                .putExtra("image", Vars.staStudentInfo.getParent_details().get(position).getImage()));
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
}
