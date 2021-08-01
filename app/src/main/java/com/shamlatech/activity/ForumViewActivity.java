package com.shamlatech.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shamlatech.adapter.ForumReplyListAdapter;
import com.shamlatech.api_response.Res_Forum_Comments;
import com.shamlatech.api_response.Res_Forum_List;
import com.shamlatech.api_response.Result_Forum_List;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.FormatDateTimeForShow;
import static com.shamlatech.utils.Vars.Refresh_Forum_T_P;
import static com.shamlatech.utils.Vars.Refresh_Forum_T_T;
import static com.shamlatech.utils.Vars.staUserInfo;

public class ForumViewActivity extends BaseActivity {

    TextView txtTopic, txtCreatorAndDate, txtSendTo;
    ImageView imgFav, imgReply;
    RecyclerView recyclerForumReply;
    RelativeLayout relativeReplyPanel, relativeAchievePanel;
    EditText edtReply;

    ArrayList<Res_Forum_Comments> listReply = new ArrayList<>();
    ForumReplyListAdapter mAdapter;

    Res_Forum_List forumInfo = new Res_Forum_List();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_view);

        declareAppBar4("Forum", "AnnouncementViewActivity");
        UpdateAppBarColor(R.color.colorTabBlue);

        listReply = new ArrayList<>();

        txtTopic = findViewById(R.id.txtTopic);
        txtCreatorAndDate = findViewById(R.id.txtCreatorAndDate);
        txtSendTo = findViewById(R.id.txtSendTo);

        imgFav = findViewById(R.id.imgFav);
        imgReply = findViewById(R.id.imgReply);

        recyclerForumReply = findViewById(R.id.recyclerForumReply);

        relativeReplyPanel = findViewById(R.id.relativeReplyPanel);
        relativeAchievePanel = findViewById(R.id.relativeAchievePanel);

        edtReply = findViewById(R.id.edtReply);

        mAdapter = new ForumReplyListAdapter(this, listReply);
        RecyclerView.LayoutManager mExamLayoutManager = new LinearLayoutManager(this);
        recyclerForumReply.setLayoutManager(mExamLayoutManager);
        recyclerForumReply.setItemAnimator(new DefaultItemAnimator());
        recyclerForumReply.setAdapter(mAdapter);

        imgFav.setOnClickListener(this);
        imgReply.setOnClickListener(this);

        prepareBundle();
    }

    public void prepareBundle() {
        Bundle bundle = getIntent().getExtras();
        forumInfo = new Res_Forum_List();
        if (bundle != null) {
            if (bundle.containsKey("forum_info")) {
                forumInfo = (Res_Forum_List) bundle.getSerializable("forum_info");
                PreparePage();
                getRetro_AccessSingleForums(staUserInfo.getId(), staUserInfo.getRole(), forumInfo.getId());
            } else if (bundle.containsKey("forum_id")) {
                String forum_id = bundle.getString("forum_id");
                getRetro_AccessSingleForums(staUserInfo.getId(), staUserInfo.getRole(), forum_id);
            }
        }
    }

    public void PreparePage() {
        txtTopic.setText(forumInfo.getTopic());
        txtCreatorAndDate.setText("POSTED BY " + forumInfo.getPosted_name() + " | " + FormatDateTimeForShow(forumInfo.getPosted_on(), Vars.DatePattern1, ""));
        if (forumInfo.getTo().equals(Vars.Role_Teacher) || forumInfo.getTo().equals(Vars.Role_Principal)) {
            txtSendTo.setText("SEND TO " + forumInfo.getClass_name() + forumInfo.getSection_name() + " Teacher");
        } else {
            txtSendTo.setText("SEND TO " + forumInfo.getClass_name() + forumInfo.getSection_name() + " Parent");
        }
        if (forumInfo.getTo().equals(Vars.Role_Teacher) && forumInfo.getFrom().equals(Vars.Role_Teacher)) {
            txtSendTo.setVisibility(View.GONE);
        } else if (forumInfo.getTo().equals(Vars.Role_Principal) && forumInfo.getFrom().equals(Vars.Role_Principal)) {
            txtSendTo.setVisibility(View.GONE);
        } else {
            txtSendTo.setVisibility(View.VISIBLE);
        }

        if (forumInfo.getIs_open().equals("1")) {
            relativeReplyPanel.setVisibility(View.VISIBLE);
            relativeAchievePanel.setVisibility(View.GONE);
        } else {
            relativeReplyPanel.setVisibility(View.GONE);
            relativeAchievePanel.setVisibility(View.VISIBLE);
        }
        if (forumInfo.getIs_fav().equals("1"))
            imgFav.setColorFilter(getResources().getColor(R.color.colorCommonOrangeDark));
        else
            imgFav.setColorFilter(getResources().getColor(R.color.colorGray));

        if (forumInfo.getComments() != null) {
            listReply = forumInfo.getComments();
            mAdapter = new ForumReplyListAdapter(this, listReply);
            recyclerForumReply.setAdapter(mAdapter);
        }
        PrepareMenu();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgFav:
                String newFav = forumInfo.getIs_fav().equals("0") ? "1" : "0";
                forumInfo.setIs_fav(newFav);
                if (forumInfo.getIs_fav().equals("1"))
                    imgFav.setColorFilter(getResources().getColor(R.color.colorCommonOrangeDark));
                else
                    imgFav.setColorFilter(getResources().getColor(R.color.colorGray));
                getRetro_UpdateFavForums(staUserInfo.getId(), staUserInfo.getRole(), forumInfo.getId(), forumInfo.getIs_fav());
                break;
            case R.id.imgReply:
                String strReply = edtReply.getText().toString().trim();
                if (!strReply.equals("")) {
                    edtReply.setText("");
                    getRetro_ReplyForums(staUserInfo.getId(), staUserInfo.getRole(), forumInfo.getId(), strReply);
                }
                break;
            default:
                super.onClick(v);
                break;
        }
    }


    public void PrepareMenu() {
        popup = new PopupMenu(ForumViewActivity.this, imgAppMenu);
        popup.inflate(R.menu.forum_menu);

        if (forumInfo.getIs_open().equals("1")) {
            popup.getMenu().findItem(R.id.item_Achieve).setVisible(true);
            popup.getMenu().findItem(R.id.item_Open).setVisible(false);
        } else {
            popup.getMenu().findItem(R.id.item_Achieve).setVisible(false);
            popup.getMenu().findItem(R.id.item_Open).setVisible(true);
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_Achieve:
                        forumInfo.setIs_open("0");
                        getRetro_UpdateOpenForums(staUserInfo.getId(), staUserInfo.getRole(), forumInfo.getId(), forumInfo.getIs_open());
                        break;
                    case R.id.item_Open:
                        forumInfo.setIs_open("1");
                        getRetro_UpdateOpenForums(staUserInfo.getId(), staUserInfo.getRole(), forumInfo.getId(), forumInfo.getIs_open());
                        break;
                }
                return false;
            }
        });
    }

    public void getRetro_AccessSingleForums(String user_id, String role_id, String forum_id) {
        getRetro_Call(this, service.getAccessSingleForum(user_id, role_id, forum_id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Forum_List mPojo_Forum = onPojoBuilder(objectResponse, Result_Forum_List.class);
                        if (mPojo_Forum != null) {
                            forumInfo = mPojo_Forum.getForums_list().get(0);
                            PreparePage();
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public void getRetro_UpdateFavForums(String user_id, String role_id, String forum_id, String fav) {
        getRetro_Call(this, service.getUpdateFavForum(user_id, role_id, forum_id, fav),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Forum_List mPojo_Forum = onPojoBuilder(objectResponse, Result_Forum_List.class);
                        if (mPojo_Forum != null) {
                            Refresh_Forum_T_T = "1";
                            Refresh_Forum_T_P = "1";
                            forumInfo = mPojo_Forum.getForums_list().get(0);
                            PreparePage();
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }


    public void getRetro_UpdateOpenForums(String user_id, String role_id, String forum_id, String open) {
        getRetro_Call(this, service.getUpdateOpenForum(user_id, role_id, forum_id, open),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Forum_List mPojo_Forum = onPojoBuilder(objectResponse, Result_Forum_List.class);
                        if (mPojo_Forum != null) {
                            Refresh_Forum_T_T = "1";
                            Refresh_Forum_T_P = "1";
                            forumInfo = mPojo_Forum.getForums_list().get(0);
                            PreparePage();
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public void getRetro_ReplyForums(String user_id, String role_id, String forum_id, String comment) {
        getRetro_Call(this, service.getReplyForum(user_id, role_id, forum_id, comment),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Forum_List mPojo_Forum = onPojoBuilder(objectResponse, Result_Forum_List.class);
                        if (mPojo_Forum != null) {
                            Refresh_Forum_T_T = "1";
                            Refresh_Forum_T_P = "1";
                            forumInfo = mPojo_Forum.getForums_list().get(0);
                            PreparePage();
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }


}

