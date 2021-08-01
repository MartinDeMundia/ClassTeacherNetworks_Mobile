package com.shamlatech.school_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shamlatech.utils.Vars;


public class RoleSelectionActivity extends BaseActivity {

    LinearLayout linearRoleParent, linearRoleTeacher;
    RelativeLayout relativeRoleParent, relativeRoleTeacher;
    ImageView imgRoleParent, imgRoleTeacher;
    String Path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        prepareBundle();

        linearRoleParent = findViewById(R.id.linearRoleParent);
        linearRoleTeacher = findViewById(R.id.linearRoleTeacher);
        relativeRoleParent = findViewById(R.id.relativeRoleParent);
        relativeRoleTeacher = findViewById(R.id.relativeRoleTeacher);
        imgRoleParent = findViewById(R.id.imgRoleParent);
        imgRoleTeacher = findViewById(R.id.imgRoleTeacher);


        relativeRoleParent.setOnClickListener(this);
        relativeRoleTeacher.setOnClickListener(this);
        linearRoleParent.setOnClickListener(this);
        linearRoleTeacher.setOnClickListener(this);
    }

    public void prepareBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("path")) {
                Path = bundle.getString("path");
            }
        }
    }

    public void ParentActive() {
        relativeRoleParent.setSelected(true);
        relativeRoleTeacher.setSelected(false);
        imgRoleParent.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
        imgRoleTeacher.setColorFilter(getResources().getColor(R.color.colorWhite));
    }

    public void TeacherActive() {
        relativeRoleParent.setSelected(false);
        relativeRoleTeacher.setSelected(true);
        imgRoleParent.setColorFilter(getResources().getColor(R.color.colorWhite));
        imgRoleTeacher.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearRoleParent:
            case R.id.relativeRoleParent:
                ParentActive();
                if (Path.equals("activate")) {
                    startActivity(new Intent(RoleSelectionActivity.this, NemisActivateActivity.class).putExtra("role", Vars.Role_Parent));
                } else {
                    startActivity(new Intent(RoleSelectionActivity.this, LoginActivity.class).putExtra("role", Vars.Role_Parent));
                }
                break;
            case R.id.linearRoleTeacher:
            case R.id.relativeRoleTeacher:
                TeacherActive();
                if (Path.equals("activate")) {
                    startActivity(new Intent(RoleSelectionActivity.this, ActivateActivity.class).putExtra("role", Vars.Role_Teacher));
                } else {
                    startActivity(new Intent(RoleSelectionActivity.this, LoginActivity.class).putExtra("role", Vars.Role_Teacher));
                }
                break;
        }
    }

}
