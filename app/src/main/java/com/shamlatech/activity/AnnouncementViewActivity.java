package com.shamlatech.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.shamlatech.api_response.Res_Announcement;
import com.shamlatech.api_response.Result_Announcement;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.API_Code;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Vars.staUserInfo;

public class AnnouncementViewActivity extends BaseActivity {

    LinearLayout linearAnnouncement;
    TextView txtType, txtDate, txtHeader, txtBody;
    FlexboxLayout flexboxTags;
    TextView txtPostedBy, txtPostedOn;

    Res_Announcement announcement_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_view);

        declareAppBar5("Announcement", "AnnouncementViewActivity");
        declareBottomBar();

        linearAnnouncement = findViewById(R.id.linearAnnouncement);
        txtType = findViewById(R.id.txtType);
        txtDate = findViewById(R.id.txtDate);
        txtHeader = findViewById(R.id.txtHeader);
        txtBody = findViewById(R.id.txtBody);
        flexboxTags = findViewById(R.id.flexboxTags);
        txtPostedBy = findViewById(R.id.txtPostedBy);
        txtPostedOn = findViewById(R.id.txtPostedOn);

        prepareBundle();
    }

    public void prepareBundle() {
        Bundle bundle = getIntent().getExtras();
        announcement_info = new Res_Announcement();
        if (bundle != null) {
            if (bundle.containsKey("announcement_info")) {
                announcement_info = (Res_Announcement) bundle.getSerializable("announcement_info");
                PreparePage();
            } else {
                if (bundle.containsKey("id")) {
                    String id = bundle.getString("id");
                    getRetro_AccessAnnouncement(staUserInfo.getId(), staUserInfo.getRole(), id);
                }
            }
        }
    }

    private void PreparePage() {
        LayoutInflater layoutInflater = getLayoutInflater();
        txtType.setText(announcement_info.getType());
        txtDate.setText(Html.fromHtml(Support.FormatDateTimeForShow(announcement_info.getDate(), Vars.DatePattern7, "")));
        txtHeader.setText(announcement_info.getTitle());
        txtBody.setText(announcement_info.getBody());
        txtPostedBy.setText(announcement_info.getPosted_by());
        txtPostedOn.setText(Support.FormatDateTimeForShow(announcement_info.getPosted_on(), Vars.DatePattern1, ""));

        String[] tags = announcement_info.getTags().split(",");
        View view;
        flexboxTags.removeAllViews();
        for (int i = 0; i < tags.length; i++) {
            if (!tags[i].trim().equals("")) {
                view = layoutInflater.inflate(R.layout.announcement_tag, flexboxTags, false);
                RelativeLayout relativeTag = view.findViewById(R.id.relativeTag);
                TextView textView = (TextView) view.findViewById(R.id.txtSubject);
                textView.setText(tags[i].trim());
                flexboxTags.addView(relativeTag);
            }
        }
    }

    public void getRetro_AccessAnnouncement(String user_id, String role_id, String id) {
        getRetro_Call(AnnouncementViewActivity.this, service.getAccessSingleAnnouncement(
                user_id, role_id, id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Announcement mPojo_Announcement = onPojoBuilder(objectResponse, Result_Announcement.class);
                        if (mPojo_Announcement != null) {
                            if (mPojo_Announcement.getStatus().equals(API_Code.Success)) {
                                announcement_info = mPojo_Announcement.getAnnouncement().get(0);
                                PreparePage();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }
}
