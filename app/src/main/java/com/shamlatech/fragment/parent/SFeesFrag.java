package com.shamlatech.fragment.parent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shamlatech.activity.teacher.PaymentsActivity;
import com.shamlatech.adapter.FeeListAdapter;
import com.shamlatech.api_response.Res_Stud_Fees;
import com.shamlatech.api_response.Res_Stud_Fees_Details;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import static com.shamlatech.utils.Support.downloadFile;
import static com.shamlatech.utils.Support.sendEmail;

/**
 * Created by Dharmalingam Sekar on 09-05-2018.
 */

public class SFeesFrag extends Fragment implements View.OnClickListener {

    View view;
    Activity activity;

    TextView txtAmountPaid, txtBalance, txtFeeStatus;
    RecyclerView recyclerFeeStructure;

    LinearLayout linearShare, linearEmail, linearDownload,linearIpay;

    FeeListAdapter mAdapter;
    ArrayList<Res_Stud_Fees_Details> listFees = new ArrayList<>();

    Res_Stud_Fees studFeesInfo;
    NestedScrollView scrollContent;
    View InnerProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_stud_fees_ipay, container, false);

        listFees = new ArrayList<>();
        InitializeProgress(view);

        txtAmountPaid = view.findViewById(R.id.txtAmountPaid);
        txtBalance = view.findViewById(R.id.txtBalance);
        txtFeeStatus = view.findViewById(R.id.txtFeeStatus);

        recyclerFeeStructure = view.findViewById(R.id.recyclerFeeStructure);

        linearShare = view.findViewById(R.id.linearShare);
        linearEmail = view.findViewById(R.id.linearEmail);
        linearDownload = view.findViewById(R.id.linearDownload);

        mAdapter = new FeeListAdapter(activity, listFees);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerFeeStructure.setLayoutManager(mLayoutManager);
        recyclerFeeStructure.setItemAnimator(new DefaultItemAnimator());
        recyclerFeeStructure.setAdapter(mAdapter);

        linearShare.setOnClickListener(this);
        linearEmail.setOnClickListener(this);
        linearDownload.setOnClickListener(this);

        //added the ipay button
        linearIpay = view.findViewById(R.id.linearIpay);
        linearIpay.setOnClickListener(this);

        return view;
    }

    public void InitializeProgress(View view) {
        scrollContent = view.findViewById(R.id.scrollContent);
        InnerProgress = (View) view.findViewById(R.id.ProgressInner);
        InnerProgress.setVisibility(View.VISIBLE);
        scrollContent.setVisibility(View.GONE);
        String indicator = activity.getIntent().getStringExtra("indicator");
        AVLoadingIndicatorView avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        avi.setIndicator(indicator);
        avi.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public void loadData(Bundle bundle) {
        studFeesInfo = new Res_Stud_Fees();
        if (bundle != null) {
            if (bundle.containsKey("fees_info")) {
                studFeesInfo = (Res_Stud_Fees) bundle.getSerializable("fees_info");
                if (studFeesInfo != null)
                    PrepareLoadData();
            }
        }
    }

    private void PrepareLoadData() {
        scrollContent.setVisibility(View.VISIBLE);
        InnerProgress.setVisibility(View.GONE);
        listFees = studFeesInfo.getDetailed_fees();
//        if (!studFeesInfo.getPaid_amount().isEmpty())
            txtAmountPaid.setText(Html.fromHtml(Support.FormatFee(studFeesInfo.getPaid_amount(), false)));
//        else
//            txtAmountPaid.setText(Html.fromHtml(Support.FormatFee("0", false)));
//        if (!studFeesInfo.getBalance().isEmpty())
            txtBalance.setText(Html.fromHtml(Support.FormatFee(studFeesInfo.getBalance(), true)));
//        else
//            txtBalance.setText(Html.fromHtml(Support.FormatFee("0", false)));
        txtFeeStatus.setText(studFeesInfo.getStatus());

        mAdapter = new FeeListAdapter(activity, listFees);
        recyclerFeeStructure.setAdapter(mAdapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearDownload:
                if (studFeesInfo.getReport_download_link() != null && !studFeesInfo.getReport_download_link().isEmpty())
                    downloadFile(activity, studFeesInfo.getReport_download_link());
                else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearShare:
                if (studFeesInfo.getReport_download_link() != null && !studFeesInfo.getReport_download_link().isEmpty()) {
                    Support.shareLink(activity, studFeesInfo.getReport_download_link());
                } else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearEmail:
                sendEmail(activity, studFeesInfo.getReport_download_link());
                break;

            case R.id.linearIpay:
                Intent intent = new Intent(getActivity(), PaymentsActivity.class);
                startActivity(intent);
                break;
        }
    }
}