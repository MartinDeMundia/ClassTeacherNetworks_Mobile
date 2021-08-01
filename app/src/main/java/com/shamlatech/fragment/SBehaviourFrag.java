package com.shamlatech.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.shamlatech.activity.teacher.UpdateBehaviourActivity;
import com.shamlatech.adapter.DetailedBehaviourViewAdapter;
import com.shamlatech.adapter.OverAllBehaviourViewAdapter;
import com.shamlatech.api_response.Res_Stud_Behaviour;
import com.shamlatech.api_response.Res_Stud_Behaviour_Detailed_Behaviour;
import com.shamlatech.api_response.Res_Stud_Behaviour_Overall_Behaviour;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import static com.shamlatech.utils.Support.ConvertToFloat;
import static com.shamlatech.utils.Support.downloadFile;
import static com.shamlatech.utils.Support.getFileNameFromUrl;
import static com.shamlatech.utils.Support.sendEmail;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Dharmalingam Sekar on 09-05-2018.
 */

public class SBehaviourFrag extends Fragment implements View.OnClickListener {

    View view;
    TextView txtIncidents, txtActionTaken, txtOverAllBehaviour;
    SimpleRatingBar rattingOverAllBehaviour;
    RecyclerView recyclerOverAllBehaviour, recyclerDetailedBehaviour;
    FloatingActionButton fabEditBehaviour;

    ArrayList<Res_Stud_Behaviour_Overall_Behaviour> listOverAllBehaviour = new ArrayList<>();
    ArrayList<Res_Stud_Behaviour_Detailed_Behaviour> listDetailedBehaviour = new ArrayList<>();
    LinearLayout linearDownload, linearShare, linearEmail;

    OverAllBehaviourViewAdapter mOverAllBehaviourAdapter;
    DetailedBehaviourViewAdapter mDetailedBehaviourAdapter;
    String StudId;
    Res_Stud_Behaviour studBehaviourInfo;
    Activity activity;


    NestedScrollView scrollContent;
    View InnerProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_stud_behaviour, container, false);

        listOverAllBehaviour = new ArrayList<>();
        listDetailedBehaviour = new ArrayList<>();

        InitializeProgress(view);

        linearDownload = view.findViewById(R.id.linearDownload);
        linearShare = view.findViewById(R.id.linearShare);
        linearEmail = view.findViewById(R.id.linearEmail);
        txtIncidents = view.findViewById(R.id.txtIncidents);
        txtActionTaken = view.findViewById(R.id.txtActionTaken);
        txtOverAllBehaviour = view.findViewById(R.id.txtOverAllBehaviour);

        rattingOverAllBehaviour = view.findViewById(R.id.rattingOverAllBehaviour);

        recyclerOverAllBehaviour = view.findViewById(R.id.recyclerOverAllBehaviour);
        recyclerDetailedBehaviour = view.findViewById(R.id.recyclerDetailedBehaviour);

        fabEditBehaviour = view.findViewById(R.id.fabEditBehaviour);

        mOverAllBehaviourAdapter = new OverAllBehaviourViewAdapter(activity, listOverAllBehaviour);
        RecyclerView.LayoutManager mPerformanceLayoutManager = new LinearLayoutManager(activity);
        recyclerOverAllBehaviour.setLayoutManager(mPerformanceLayoutManager);
        recyclerOverAllBehaviour.setItemAnimator(new DefaultItemAnimator());
        recyclerOverAllBehaviour.setAdapter(mOverAllBehaviourAdapter);

        mDetailedBehaviourAdapter = new DetailedBehaviourViewAdapter(activity, listDetailedBehaviour);
        RecyclerView.LayoutManager mDetailsLayoutManager = new LinearLayoutManager(activity);
        recyclerDetailedBehaviour.setLayoutManager(mDetailsLayoutManager);
        recyclerDetailedBehaviour.setItemAnimator(new DefaultItemAnimator());
        recyclerDetailedBehaviour.setAdapter(mDetailedBehaviourAdapter);

        linearDownload.setOnClickListener(this);
        linearShare.setOnClickListener(this);
        linearEmail.setOnClickListener(this);
        fabEditBehaviour.setOnClickListener(this);
        preparePage();
        return view;
    }


    public void InitializeProgress(View view) {
        scrollContent = view.findViewById(R.id.scrollContent);
        InnerProgress = (View) view.findViewById(R.id.ProgressInner);
        InnerProgress.setVisibility(View.VISIBLE);
        scrollContent.setVisibility(View.INVISIBLE);
        String indicator = activity.getIntent().getStringExtra("indicator");
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.show();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (Refresh_Stud_Behaviour.equals("1")) {
//            InnerProgress.setVisibility(View.VISIBLE);
//        } else {
//            InnerProgress.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public void loadData(Bundle bundle) {
        studBehaviourInfo = new Res_Stud_Behaviour();
        if (bundle != null) {
            if (bundle.containsKey("behaviour_info")) {
                studBehaviourInfo = (Res_Stud_Behaviour) bundle.getSerializable("behaviour_info");
                StudId = bundle.getString("stud_id");
                if (studBehaviourInfo != null)
                    PrepareLoadData();
            }
        }
    }

    public void showProgress() {
        InitializeProgress(view);
    }

    private void PrepareLoadData() {
        InnerProgress.setVisibility(View.GONE);
        scrollContent.setVisibility(View.VISIBLE);

        listOverAllBehaviour = studBehaviourInfo.getOverall_behaviour();
        listDetailedBehaviour = studBehaviourInfo.getDetailed_behaviour();

        mOverAllBehaviourAdapter = new OverAllBehaviourViewAdapter(activity, listOverAllBehaviour);
        recyclerOverAllBehaviour.setAdapter(mOverAllBehaviourAdapter);

        mDetailedBehaviourAdapter = new DetailedBehaviourViewAdapter(activity, listDetailedBehaviour);
        recyclerDetailedBehaviour.setAdapter(mDetailedBehaviourAdapter);

        txtIncidents.setText(studBehaviourInfo.getIncidents().getCount());
        txtActionTaken.setText(studBehaviourInfo.getIncidents().getAction_taken());
        txtOverAllBehaviour.setText(studBehaviourInfo.getOver_all_behaviour_report());
        rattingOverAllBehaviour.setRating(ConvertToFloat(studBehaviourInfo.getOver_all_behaviour_ratting()));
    }

    private void preparePage() {
        if (staUserInfo.getRole().equals(Vars.Role_Teacher) || staUserInfo.getRole().equals(Vars.Role_Principal)) {
            fabEditBehaviour.setVisibility(View.VISIBLE);
        } else {
            fabEditBehaviour.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabEditBehaviour:
                startActivity(new Intent(activity, UpdateBehaviourActivity.class)
                        .putExtra("stud_id", StudId)
                        .putExtra("behaviour_info", studBehaviourInfo));
                break;
            case R.id.linearDownload:
                if (studBehaviourInfo.getReport_download_link() != null && !studBehaviourInfo.getReport_download_link().isEmpty())
                    downloadFile(activity, studBehaviourInfo.getReport_download_link());
                else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearShare:
                if (studBehaviourInfo.getReport_download_link() != null && !studBehaviourInfo.getReport_download_link().isEmpty()) {
                    Support.shareLink(activity, studBehaviourInfo.getReport_download_link());
                } else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearEmail:
                sendEmail(activity, studBehaviourInfo.getReport_download_link());
                break;
        }
    }

}