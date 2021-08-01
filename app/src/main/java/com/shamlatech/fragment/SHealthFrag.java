package com.shamlatech.fragment;

import android.app.Activity;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.shamlatech.activity.LastHealthAddActivity;
import com.shamlatech.activity.parent.AddHealthReport;
import com.shamlatech.adapter.AllergiesListAdapter;
import com.shamlatech.adapter.HealthOccurrenceListAdapter;
import com.shamlatech.api_response.Res_Stud_Health;
import com.shamlatech.api_response.Res_Stud_Health_Known_Allergies;
import com.shamlatech.api_response.Res_Stud_Health_Last_Health_Occurrence;
import com.shamlatech.school_management.R;
import com.shamlatech.utils.Support;
import com.shamlatech.utils.Vars;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import static com.shamlatech.utils.Support.ConvertToFloat;
import static com.shamlatech.utils.Support.FormatDateTimeForShow;
import static com.shamlatech.utils.Support.downloadFile;
import static com.shamlatech.utils.Support.sendEmail;
import static com.shamlatech.utils.Vars.staUserInfo;

/**
 * Created by Dharmalingam Sekar on 09-05-2018.
 */

public class SHealthFrag extends Fragment implements View.OnClickListener {

    View view;
    TextView txtLastHealthConcern, txtActionTaken, txtFurtherActionNeeded, txtOverAllHealth;
    RecyclerView recyclerKnownAllergies, recyclerLastHealthOccurrence;
    LinearLayout linearAddAllergies, linearAddHealthOccurrence;
    LinearLayout linearDownload, linearShare, linearEmail;

    SimpleRatingBar rattingOverAllHealth;

    AllergiesListAdapter mAllergiesAdapter;
    HealthOccurrenceListAdapter mHealthAdapter;

    ArrayList<Res_Stud_Health_Known_Allergies> listAllergies = new ArrayList<>();
    ArrayList<Res_Stud_Health_Last_Health_Occurrence> listHealth = new ArrayList<>();

    Res_Stud_Health studHealthInfo;
    String StudId;
    Dialog pick_Dialog;
    Activity activity;


    NestedScrollView scrollContent;
    View InnerProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_stud_health, container, false);

        listAllergies = new ArrayList<>();
        listHealth = new ArrayList<>();
        InitializeProgress(view);

        linearDownload = view.findViewById(R.id.linearDownload);
        linearShare = view.findViewById(R.id.linearShare);
        linearEmail = view.findViewById(R.id.linearEmail);
        txtLastHealthConcern = view.findViewById(R.id.txtLastHealthConcern);
        txtActionTaken = view.findViewById(R.id.txtActionTaken);
        txtFurtherActionNeeded = view.findViewById(R.id.txtFurtherActionNeeded);
        txtOverAllHealth = view.findViewById(R.id.txtOverAllHealth);

        rattingOverAllHealth = view.findViewById(R.id.rattingOverAllHealth);

        linearAddAllergies = view.findViewById(R.id.linearAddAllergies);
        linearAddHealthOccurrence = view.findViewById(R.id.linearAddHealthOccurrence);

        recyclerKnownAllergies = view.findViewById(R.id.recyclerKnownAllergies);
        recyclerLastHealthOccurrence = view.findViewById(R.id.recyclerLastHealthOccurrence);

        mAllergiesAdapter = new AllergiesListAdapter(activity, listAllergies, SHealthFrag.this);
        RecyclerView.LayoutManager mAllergiesLayoutManager = new LinearLayoutManager(activity);
        recyclerKnownAllergies.setLayoutManager(mAllergiesLayoutManager);
        recyclerKnownAllergies.setItemAnimator(new DefaultItemAnimator());
        recyclerKnownAllergies.setAdapter(mAllergiesAdapter);

        mHealthAdapter = new HealthOccurrenceListAdapter(activity, listHealth);
        RecyclerView.LayoutManager mHealthLayoutManager = new LinearLayoutManager(activity);
        recyclerLastHealthOccurrence.setLayoutManager(mHealthLayoutManager);
        recyclerLastHealthOccurrence.setItemAnimator(new DefaultItemAnimator());
        recyclerLastHealthOccurrence.setAdapter(mHealthAdapter);

        linearDownload.setOnClickListener(this);
        linearShare.setOnClickListener(this);
        linearEmail.setOnClickListener(this);
        linearAddAllergies.setOnClickListener(this);
        linearAddHealthOccurrence.setOnClickListener(this);

        preparePage();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    public void loadData(Bundle bundle) {
        studHealthInfo = new Res_Stud_Health();
        if (bundle != null) {
            if (bundle.containsKey("health_info")) {
                studHealthInfo = (Res_Stud_Health) bundle.getSerializable("health_info");
                StudId = bundle.getString("stud_id");
                if (studHealthInfo != null)
                    PrepareLoadData();
            }
        }
    }

    public void showProgress() {
        InitializeProgress(view);
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

    private void PrepareLoadData() {
        InnerProgress.setVisibility(View.GONE);
        scrollContent.setVisibility(View.VISIBLE);

        listAllergies = studHealthInfo.getKnown_allergies();
        listHealth = studHealthInfo.getLast_occurrences();

        mAllergiesAdapter = new AllergiesListAdapter(activity, listAllergies, SHealthFrag.this);
        recyclerKnownAllergies.setAdapter(mAllergiesAdapter);

        mHealthAdapter = new HealthOccurrenceListAdapter(activity, listHealth);
        recyclerLastHealthOccurrence.setAdapter(mHealthAdapter);

        txtLastHealthConcern.setText(FormatDateTimeForShow(studHealthInfo.getLast_health_concern(), Vars.DatePattern7, "-"));
        txtActionTaken.setText(studHealthInfo.getAction_taken().equals("") ? "-" : studHealthInfo.getAction_taken());
        txtFurtherActionNeeded.setText(studHealthInfo.getFurther_action_needed().equals("") ? "-" : studHealthInfo.getFurther_action_needed());
        txtOverAllHealth.setText(studHealthInfo.getOver_all_health_report());
        rattingOverAllHealth.setRating(ConvertToFloat(studHealthInfo.getOver_all_health_ratting()));
    }

    private void preparePage() {
        if (staUserInfo.getRole().equals(Vars.Role_Parent)) {
            linearAddAllergies.setVisibility(View.VISIBLE);
        } else {
            linearAddAllergies.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearAddAllergies:
                startActivity(new Intent(activity, AddHealthReport.class).putExtra("stud_id", StudId));
                break;
            case R.id.linearAddHealthOccurrence:
                startActivity(new Intent(activity, LastHealthAddActivity.class).putExtra("stud_id", StudId));
                break;
            case R.id.linearDownload:
                if (studHealthInfo.getReport_download_link() != null && !studHealthInfo.getReport_download_link().isEmpty())
                    downloadFile(activity, studHealthInfo.getReport_download_link());
                else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearShare:
                if (studHealthInfo.getReport_download_link() != null && !studHealthInfo.getReport_download_link().isEmpty()) {
                    Support.shareLink(activity, studHealthInfo.getReport_download_link());
                } else
                    Toast.makeText(activity, "Invalid file!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearEmail:
                sendEmail(activity, studHealthInfo.getReport_download_link());
                break;
        }
    }


    public void showAllergyDetails(Res_Stud_Health_Known_Allergies list) {
        if (pick_Dialog == null) {
            pick_Dialog = new Dialog(activity);
            pick_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pick_Dialog.setCancelable(true);
            pick_Dialog.setCanceledOnTouchOutside(true);
            pick_Dialog.setContentView(R.layout.pop_allergy_details);

            TextView txtAllergyName = pick_Dialog.findViewById(R.id.txtAllergyName);
            TextView txtAllergyDetails = pick_Dialog.findViewById(R.id.txtAllergyDetails);

            txtAllergyName.setText(list.getName());
            txtAllergyDetails.setText(list.getDetails());
            pick_Dialog.show();

        } else if (pick_Dialog.isShowing()) {
            pick_Dialog.dismiss();
            showAllergyDetails(list);
        } else {
            pick_Dialog = null;
            showAllergyDetails(list);
        }
    }
}