package com.shamlatech.school_management;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.activity.NotificationActivity;
import com.shamlatech.activity.parent.ParentsDashboard;
import com.shamlatech.activity.teacher.TeachersDashboard;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.Vars;

import retrofit2.Response;
import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.staUserInfo;

public class NotificationView extends Activity  implements View.OnClickListener {
    TextView textCreatedOn,txtHeader,txtBody, txtAppHeader;
    Button snoozeButton;
    String notificationid;
    NestedScrollView nestedScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        txtAppHeader = findViewById(R.id.txtAppBarHeader);
        txtAppHeader.setText("NOTIFICATIONS");

        snoozeButton = findViewById(R.id.snoozeButton);
        snoozeButton.setOnClickListener(this);

        nestedScrollView  = findViewById(R.id.nestedScrollView);
        textCreatedOn = findViewById(R.id.txtDate);
        txtHeader = findViewById(R.id.txtHeader);
        txtBody = findViewById(R.id.txtBody);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String content = bundle.getString("content");
        String created_on = bundle.getString("created_on");
        notificationid =  bundle.getString("id");

        textCreatedOn.setText(created_on);
        txtHeader.setText(title);
        txtBody.setText(content);
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.snoozeButton:
                getRetro_Call(this, service.snoozeNotification(notificationid),
                        false, new API_Calls.OnApiResult() {
                            @Override
                            public void onSuccess(Response<Object> objectResponse) {
                                nestedScrollView.setVisibility(View.GONE);
                            }
                            @Override
                            public void onFailure(String message) {
                            }
                        });
                 Toast.makeText(this, "Snoozing notification..", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String title = bundle.getString("title");
        String content = bundle.getString("content");
        String created_on = bundle.getString("created_on");
        notificationid =  bundle.getString("id");
        textCreatedOn.setText(created_on);
        txtHeader.setText(title);
        txtBody.setText(content);

    }


}