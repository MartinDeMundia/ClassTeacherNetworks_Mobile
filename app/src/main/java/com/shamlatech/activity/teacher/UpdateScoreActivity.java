package com.shamlatech.activity.teacher;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.api_response.Res_Teacher_TeachingSubjects;
import com.shamlatech.api_response.Result_Common;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.ConvertToDouble;
import static com.shamlatech.utils.Support.ConvertToInteger;
import static com.shamlatech.utils.Vars.staUserInfo;

public class UpdateScoreActivity extends BaseActivity {

    Spinner spinnerSubject;
    EditText edtScore, edtPart1, edtPart2, edtPart3, edtComments;
    RelativeLayout relativeDone;
    String StudId;
    String ExamId;
    String ExamName;
    ArrayAdapter<Res_Teacher_TeachingSubjects> adapterSubject;
    LinearLayout linearSingleMark, linearPart1, linearPart2, linearPart3;
    TextView txtSingleMarkName, txtPart1MarkName, txtPart2MarkName, txtPart3MarkName;

    ArrayList<Res_Teacher_TeachingSubjects> listTeachingSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_score);

        declareAppBar2("English Mid-Term Exam", "UpdateScoreActivity");
        declareBottomBar();
        UpdateAppBarColor(R.color.colorTabGreen);

        spinnerSubject = findViewById(R.id.spinnerSubject);
        edtScore = findViewById(R.id.edtScore);
        edtPart1 = findViewById(R.id.edtPart1);
        edtPart2 = findViewById(R.id.edtPart2);
        edtPart3 = findViewById(R.id.edtPart3);

        linearSingleMark = findViewById(R.id.linearSingleMark);
        linearPart1 = findViewById(R.id.linearPart1);
        linearPart2 = findViewById(R.id.linearPart2);
        linearPart3 = findViewById(R.id.linearPart3);

        txtSingleMarkName = findViewById(R.id.txtSingleMarkName);
        txtPart1MarkName = findViewById(R.id.txtPart1MarkName);
        txtPart2MarkName = findViewById(R.id.txtPart2MarkName);
        txtPart3MarkName = findViewById(R.id.txtPart3MarkName);

        edtComments = findViewById(R.id.edtComments);
        relativeDone = findViewById(R.id.relativeDone);

        relativeDone.setOnClickListener(this);

        edtScore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                double score = ConvertToDouble(editable.toString());
                if (score > 100) {
                    edtScore.setText("");
                    edtScore.setError("Please enter the valid marks");
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(500);
                }
            }
        });

        PreparePage();

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listTeachingSubject.get(position).getParts().equals("0")) {
                    linearSingleMark.setVisibility(View.VISIBLE);
                    linearPart1.setVisibility(View.GONE);
                    linearPart2.setVisibility(View.GONE);
                    linearPart3.setVisibility(View.GONE);
                } else {
                    String parts = listTeachingSubject.get(position).getParts_name();
                    String[] listParts = parts.split(",");
                    if (listParts.length > 2) {
                        linearSingleMark.setVisibility(View.GONE);
                        linearPart1.setVisibility(View.VISIBLE);
                        linearPart2.setVisibility(View.VISIBLE);
                        linearPart3.setVisibility(View.VISIBLE);
                        txtPart1MarkName.setText(listParts[0]);
                        txtPart2MarkName.setText(listParts[1]);
                        txtPart3MarkName.setText(listParts[2]);
                    } else if (listParts.length > 1) {
                        linearSingleMark.setVisibility(View.GONE);
                        linearPart1.setVisibility(View.VISIBLE);
                        linearPart2.setVisibility(View.VISIBLE);
                        linearPart3.setVisibility(View.GONE);
                        txtPart1MarkName.setText(listParts[0]);
                        txtPart2MarkName.setText(listParts[1]);
                    } else if (listParts.length > 0) {
                        linearSingleMark.setVisibility(View.GONE);
                        linearPart1.setVisibility(View.VISIBLE);
                        linearPart2.setVisibility(View.GONE);
                        linearPart3.setVisibility(View.GONE);
                        txtPart1MarkName.setText(listParts[0]);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitializeProgress();
    }

    public void InitializeProgress() {
        Vars.Custom_Progress = (View) findViewById(R.id.Custom_Progress);
        Vars.Custom_Progress.setVisibility(View.GONE);
        String indicator = getIntent().getStringExtra("indicator");
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.show();
    }

    private void PreparePage() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("stud_id")) {
                StudId = bundle.getString("stud_id");
                ExamId = bundle.getString("exam_id");
                ExamName = bundle.getString("exam_name");
                listTeachingSubject = db.AccessTeachingSubjectBasedStudent(StudId);
                adapterSubject = new ArrayAdapter<Res_Teacher_TeachingSubjects>(this, R.layout.simple_spinner_item, listTeachingSubject);
                adapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSubject.setAdapter(adapterSubject);

                declareAppBar2(ExamName, "UpdateScoreActivity");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeDone:
                String subject_id = listTeachingSubject.get(spinnerSubject.getSelectedItemPosition()).getId();
                String marks = edtScore.getText().toString().trim();
                String part1 = edtPart1.getText().toString().trim();
                String part2 = edtPart2.getText().toString().trim();
                String part3 = edtPart3.getText().toString().trim();
                String comments = edtComments.getText().toString().trim();
                if (marks.equals("")) {
                    int mark = ConvertToInteger(part1) + ConvertToInteger(part2) + ConvertToInteger(part3);
                    marks = mark + "";
                }
                if (marks.equalsIgnoreCase("") || marks.equalsIgnoreCase("0")) {
                    edtScore.setError("Please enter the marks");
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(500);
                } else {
                    getRetro_UpdateMarks(staUserInfo.getId(), staUserInfo.getRole(), StudId, ExamId, subject_id, marks, part1, part2, part3, comments);
                }
                break;
            default:
                super.onClick(v);
        }
    }


    public void getRetro_UpdateMarks(String user_id, String role_id, String stud_id, String exam_id, String subject_id, String marks, String part1, String part2, String part3, String comments) {
        getRetro_Call(UpdateScoreActivity.this, service.getUpdateStudentMark(user_id, role_id, stud_id, exam_id, subject_id, marks, part1, part2, part3, comments),
                true, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Common mPojo_Assignment = onPojoBuilder(objectResponse, Result_Common.class);
                        if (mPojo_Assignment != null) {
                            if (mPojo_Assignment.getStatus().equals(API_Code.Success)) {
                                Vars.Refresh_Stud_Education = "1";
                                Toast.makeText(getApplicationContext(), "Marks Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }
}
