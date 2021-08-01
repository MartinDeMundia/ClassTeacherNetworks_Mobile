package com.shamlatech.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shamlatech.activity.parent.ParentStudentActivity;
import com.shamlatech.activity.parent.ParentsAdditionalActivity;
import com.shamlatech.activity.teacher.TeachersAdditionalActivity;
import com.shamlatech.activity.teacher.TeachersStudentActivity;
import com.shamlatech.adapter.NotificationAdapter;
import com.shamlatech.api_response.Res_Notification;
import com.shamlatech.api_response.Result_Notification;
import com.shamlatech.school_management.BaseActivity;
import com.shamlatech.school_management.BaseFragment;
import com.shamlatech.school_management.HomeActivity;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.API_Calls;
import com.shamlatech.utils.RecyclerItemTouchHelper;
import com.shamlatech.utils.SimpleDividerItemDecoration;
import com.shamlatech.utils.Vars;

import java.util.ArrayList;

import retrofit2.Response;

import static com.shamlatech.utils.API_Calls.getRetro_Call;
import static com.shamlatech.utils.API_Calls.onPojoBuilder;
import static com.shamlatech.utils.API_Calls.service;
import static com.shamlatech.utils.Support.ConvertToInteger;
import static com.shamlatech.utils.Vars.Role_Principal;
import static com.shamlatech.utils.Vars.listNotification;
import static com.shamlatech.utils.Vars.staUserInfo;

public class NotificationActivity extends BaseFragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    final String CONS_NOTIFICATION_TYPE_UPDATE_TEACHER = "1";
    final String CONS_NOTIFICATION_TYPE_UPDATE_MARKS = "2";
    final String CONS_NOTIFICATION_TYPE_UPDATE_ASSIGNMENT = "3";
    final String CONS_NOTIFICATION_TYPE_UPDATE_BEHAVIOUR = "4";
    final String CONS_NOTIFICATION_TYPE_UPDATE_HEALTH = "5";
    final String CONS_NOTIFICATION_TYPE_UPDATE_ATTENDANCE = "6";
    final String CONS_NOTIFICATION_TYPE_UPDATE_EVENTS = "7";
    final String CONS_NOTIFICATION_TYPE_UPDATE_DOCUMENT = "8";
    final String CONS_NOTIFICATION_TYPE_UPDATE_PHOTO = "9";
    final String CONS_NOTIFICATION_TYPE_UPDATE_VIDEO = "10";
    final String CONS_NOTIFICATION_TYPE_CREATE_FORUMS_TEACHER = "11";
    final String CONS_NOTIFICATION_TYPE_CREATE_FORUMS_PARENT = "12";
    final String CONS_NOTIFICATION_TYPE_REPLY_FORUMS = "13";
    final String CONS_NOTIFICATION_TYPE_NEW_ANNOUNCEMENT = "14";
    final String CONS_NOTIFICATION_TYPE_UPDATE_PROFILE = "15";
    RecyclerView recyclerNotification;
    RelativeLayout relative_List, progress_LoadMore, relative_No_List;
    SwipeRefreshLayout swipeRefreshLayout;
    int pastVisibleItems, visibleItemCount, totalItemCount;
    Activity activity;
    private NotificationAdapter mAdapter;
    private boolean ListLoading = false;
    private String last_id = "1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notification, container, false);

        recyclerNotification = view.findViewById(R.id.recyclerNotification);
        relative_List = view.findViewById(R.id.relative_List);
        progress_LoadMore = view.findViewById(R.id.progress_LoadMore);
        relative_No_List = view.findViewById(R.id.relative_No_List);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        mAdapter = new NotificationAdapter(activity, listNotification, NotificationActivity.this);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerNotification.setLayoutManager(mLayoutManager);
        recyclerNotification.setItemAnimator(new DefaultItemAnimator());
        recyclerNotification.addItemDecoration(new SimpleDividerItemDecoration(activity, R.drawable.line_divider_1));
        recyclerNotification.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerNotification);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPage("-1");
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (ListLoading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            if (!listNotification.isEmpty()) {
                                last_id = listNotification.get(listNotification.size() - 1).getId();
                                ListLoading = false;
                                progress_LoadMore.setVisibility(View.VISIBLE);
                                loadPage(last_id);
                            }
                        }
                    }
                }
            }
        });

        loadPage("-1");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) activity).declareAppBar1("Notifications", false);
        ((BaseActivity) activity).UpdateAppBarColor(R.color.colorTabPrimary);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }


    public void loadPage(String last_Id) {
        if (last_Id == "-1") {
            listNotification = new ArrayList<>();
        }
        getRetro_AccessNotifications(staUserInfo.getId(), staUserInfo.getRole(), last_Id);
    }

    public void validateList() {
        if (listNotification.size() > 0) {
            relative_No_List.setVisibility(View.GONE);
        } else {
            relative_No_List.setVisibility(View.VISIBLE);
        }
    }


    public void onNotificationClicked(int position) {
        if (staUserInfo.getRole().equals(Vars.Role_Teacher) || staUserInfo.getRole().equals(Role_Principal)) {
            switch (listNotification.get(position).getType()) {
                case CONS_NOTIFICATION_TYPE_UPDATE_TEACHER:
                    ((HomeActivity) activity).UpdateStudentFragment(new TeachersStudentActivity(), "TEACHER", listNotification.get(position).getStudent_id(), "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_MARKS:
                    ((HomeActivity) activity).UpdateStudentFragment(new TeachersStudentActivity(), "EDUCATION", listNotification.get(position).getStudent_id(), "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_ASSIGNMENT:
                    ((HomeActivity) activity).UpdateStudentFragment(new TeachersStudentActivity(), "EDUCATION", listNotification.get(position).getStudent_id(), "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_BEHAVIOUR:
                    ((HomeActivity) activity).UpdateStudentFragment(new TeachersStudentActivity(), "BEHAVIOUR", listNotification.get(position).getStudent_id(), "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_HEALTH:
                    ((HomeActivity) activity).UpdateStudentFragment(new TeachersStudentActivity(), "HEALTH", listNotification.get(position).getStudent_id(), "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_ATTENDANCE:
                    ((HomeActivity) activity).UpdateStudentFragment(new TeachersStudentActivity(), "ATTENDANCE", listNotification.get(position).getStudent_id(), "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_EVENTS:
                    ((HomeActivity) activity).UpdateStudentFragment(new TeachersAdditionalActivity(), "CALENDAR", "", "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_DOCUMENT:
                    ((HomeActivity) activity).UpdateStudentFragment(new TeachersAdditionalActivity(), "MEDIA", "", "DOCUMENT");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_PHOTO:
                    ((HomeActivity) activity).UpdateStudentFragment(new TeachersAdditionalActivity(), "MEDIA", "", "PHOTO");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_VIDEO:
                    ((HomeActivity) activity).UpdateStudentFragment(new TeachersAdditionalActivity(), "MEDIA", "", "VIDEO");
                    break;
                case CONS_NOTIFICATION_TYPE_CREATE_FORUMS_TEACHER:
                    ((HomeActivity) activity).UpdateStudentFragment(new TeachersAdditionalActivity(), "FORUMS", "", "TEACHER");
                    break;
                case CONS_NOTIFICATION_TYPE_CREATE_FORUMS_PARENT:
                    ((HomeActivity) activity).UpdateStudentFragment(new TeachersAdditionalActivity(), "FORUMS", "", "PARENT");
                    break;
                case CONS_NOTIFICATION_TYPE_REPLY_FORUMS:
                    startActivity(new Intent(activity, ForumViewActivity.class)
                            .putExtra("forum_id", listNotification.get(position).getType_id()));
                    break;
                case CONS_NOTIFICATION_TYPE_NEW_ANNOUNCEMENT:
                    startActivity(new Intent(activity, AnnouncementViewActivity.class)
                            .putExtra("id", listNotification.get(position).getType_id()));
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_PROFILE:
                    ((HomeActivity) activity).UpdateFragment(new ProfileActivity(), "SETTINGS", 3);
                    break;
            }
        } else {
            switch (listNotification.get(position).getType()) {
                case CONS_NOTIFICATION_TYPE_UPDATE_TEACHER:
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentStudentActivity(), "TEACHER", listNotification.get(position).getStudent_id(), "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_MARKS:
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentStudentActivity(), "EDUCATION", listNotification.get(position).getStudent_id(), "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_ASSIGNMENT:
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentStudentActivity(), "EDUCATION", listNotification.get(position).getStudent_id(), "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_BEHAVIOUR:
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentStudentActivity(), "BEHAVIOUR", listNotification.get(position).getStudent_id(), "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_HEALTH:
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentStudentActivity(), "HEALTH", listNotification.get(position).getStudent_id(), "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_ATTENDANCE:
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentStudentActivity(), "ATTENDANCE", listNotification.get(position).getStudent_id(), "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_EVENTS:
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentsAdditionalActivity(), "CALENDAR", "", "");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_DOCUMENT:
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentsAdditionalActivity(), "MEDIA", "", "DOCUMENT");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_PHOTO:
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentsAdditionalActivity(), "MEDIA", "", "PHOTO");
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_VIDEO:
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentsAdditionalActivity(), "MEDIA", "", "VIDEO");
                    break;
                case CONS_NOTIFICATION_TYPE_CREATE_FORUMS_TEACHER:
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentsAdditionalActivity(), "FORUMS", "", "");
                    break;
                case CONS_NOTIFICATION_TYPE_CREATE_FORUMS_PARENT:
                    ((HomeActivity) activity).UpdateStudentFragment(new ParentsAdditionalActivity(), "FORUMS", "", "");
                    break;
                case CONS_NOTIFICATION_TYPE_REPLY_FORUMS:
                    startActivity(new Intent(activity, ForumViewActivity.class)
                            .putExtra("forum_id", listNotification.get(position).getType_id()));
                    break;
                case CONS_NOTIFICATION_TYPE_NEW_ANNOUNCEMENT:
                    startActivity(new Intent(activity, AnnouncementViewActivity.class)
                            .putExtra("id", listNotification.get(position).getType_id()));
                    break;
                case CONS_NOTIFICATION_TYPE_UPDATE_PROFILE:
                    ((HomeActivity) activity).UpdateFragment(new ProfileActivity(), "SETTINGS", 4);
                    break;
            }
        }
        getRetro_ReadNotifications(staUserInfo.getId(), staUserInfo.getRole(), listNotification.get(position).getId(), position);
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotificationAdapter.MyViewHolder) {
            if (listNotification.get(position).getIs_read().equals("0")) {
                staUserInfo.setUnread_notifications((ConvertToInteger(staUserInfo.getUnread_notifications()) - 1) + "");
                ((HomeActivity) activity).updateBottomBar();
            }
            getRetro_RemoveNotifications(staUserInfo.getId(), staUserInfo.getRole(), listNotification.get(position).getId());
            mAdapter.removeItem(viewHolder.getAdapterPosition());
            validateList();

        }
    }

    public void updateList(ArrayList<Res_Notification> notification) {
        boolean NewList = true;
        if (listNotification.size() > 0) {
            NewList = false;
        }
        progress_LoadMore.setVisibility(View.GONE);
        if (notification.size() >= 20)
            ListLoading = true;
        else {
            ListLoading = false;
        }
        ArrayList<Res_Notification> TempNearBy = new ArrayList();
        TempNearBy.addAll(listNotification);
        listNotification.clear();
        for (int i = 0; i < notification.size(); i++) {
            boolean Exist = false;
            for (int j = 0; j < TempNearBy.size(); j++) {
                if (notification.get(i).getId().equals(TempNearBy.get(j).getId())) {
                    TempNearBy.set(j, notification.get(i));
                    Exist = true;
                }
            }
            if (!Exist) {
                TempNearBy.add(notification.get(i));
            }
        }
        listNotification.addAll(TempNearBy);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (NewList) {
            mAdapter = new NotificationAdapter(activity, listNotification, NotificationActivity.this);
            recyclerNotification.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        validateList();
    }


    public void getRetro_AccessNotifications(String user_id, String role_id, String last_id) {
        getRetro_Call(activity, service.getAccessNotification(user_id, role_id, last_id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Result_Notification mPojo_Notification = onPojoBuilder(objectResponse, Result_Notification.class);
                        if (mPojo_Notification != null) {
                            updateList(mPojo_Notification.getNotification_list());
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }
    public void deleteNotification(String noteid) {
        Toast.makeText(activity,"Deleting notification...",Toast.LENGTH_SHORT).show();
        getRetro_RemoveNotifications(staUserInfo.getId(),staUserInfo.getRole(), noteid);
    }
    public void getRetro_RemoveNotifications(String user_id, String role_id, String id) {
        getRetro_Call(activity, service.getRemoveNotification(user_id, role_id, id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        Toast.makeText(activity,"Deleted!",Toast.LENGTH_SHORT).show();
                        loadPage("-1");
                    }
                    @Override
                    public void onFailure(String message) {

                    }
                });
    }

    public void getRetro_ReadNotifications(String user_id, String role_id, String id,
                                           final int position) {
        getRetro_Call(activity, service.getReadNotification(user_id, role_id, id),
                false, new API_Calls.OnApiResult() {
                    @Override
                    public void onSuccess(Response<Object> objectResponse) {
                        listNotification.get(position).setIs_read("1");
                        mAdapter.notifyDataSetChanged();
                        staUserInfo.setUnread_notifications((ConvertToInteger(staUserInfo.getUnread_notifications()) - 1) + "");
                        ((HomeActivity) activity).updateBottomBar();
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }
}
