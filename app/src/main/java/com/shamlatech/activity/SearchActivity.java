package com.shamlatech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.shamlatech.activity.teacher.StudentInfoActivity;
import com.shamlatech.adapter.SearchListAdapter;
import com.shamlatech.pojo.PojoStudentWithClassList;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

import static com.shamlatech.utils.Vars.Role_Parent;
import static com.shamlatech.utils.Vars.Role_Principal;
import static com.shamlatech.utils.Vars.Role_Teacher;
import static com.shamlatech.utils.Vars.staUserInfo;

public class SearchActivity extends BaseActivity {

    EditText edtSearch;
    RecyclerView recyclerSearch;
    ImageView imgAppBack;
    SearchListAdapter mAdapter;
    ArrayList<PojoStudentWithClassList> listWholeStudent = new ArrayList<>();
    ArrayList<PojoStudentWithClassList> listStudent = new ArrayList<>();
    String Tab = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        declareDb();

        listStudent = new ArrayList<>();
        listWholeStudent = new ArrayList<>();
        if (staUserInfo.getRole().equals(Role_Teacher) || staUserInfo.getRole().equals(Role_Principal)) {
            listWholeStudent = db.AccessStudentListForTeacherSearch();
        } else {
            listWholeStudent = db.AccessStudentListForParentSearch();
        }


        imgAppBack = findViewById(R.id.imgAppBack);
        edtSearch = findViewById(R.id.edtSearch);

        recyclerSearch = findViewById(R.id.recyclerSearch);

        mAdapter = new SearchListAdapter(this, listStudent);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerSearch.setLayoutManager(mLayoutManager);
        recyclerSearch.setItemAnimator(new DefaultItemAnimator());

        imgAppBack.setOnClickListener(this);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String strSearch = s.toString();
                listStudent = new ArrayList<>();
                if (strSearch.trim().length() > 0) {
                    for (int i = 0; i < listWholeStudent.size(); i++) {
                        if (listWholeStudent.get(i).getName().toLowerCase().contains(strSearch.toLowerCase())) {
                            listStudent.add(listWholeStudent.get(i));
                        }
                    }
                }
                mAdapter = new SearchListAdapter(SearchActivity.this, listStudent);
                recyclerSearch.setAdapter(mAdapter);
            }
        });
        PreparePage();
    }

    private void PreparePage() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("tab")) {
                Tab = bundle.getString("tab");
            }
        }
    }

    public void onSearchClick(String stud_id) {
        if (staUserInfo.getRole().equals(Vars.Role_Teacher) || staUserInfo.getRole().equals(Role_Principal)) {
            if (Tab.equals("")) {
                Intent in = new Intent();
                in.putExtra("tab", "");
                in.putExtra("stud_id", stud_id);
                in.putExtra("place", "Info");
                setResult(RESULT_OK, in);
                finish();
            } else {
                Intent in = new Intent();
                in.putExtra("tab", Tab);
                in.putExtra("stud_id", stud_id);
                in.putExtra("place", "Teacher");
                setResult(RESULT_OK, in);
                finish();
            }
        } else {
            Tab = (Tab.equals("")) ? "TEACHER" : Tab;
            Intent in = new Intent();
            in.putExtra("tab", Tab);
            in.putExtra("stud_id", stud_id);
            in.putExtra("place", "Parent");
            setResult(RESULT_OK, in);
            finish();

        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
