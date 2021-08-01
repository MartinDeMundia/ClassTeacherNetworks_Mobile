package com.shamlatech.api_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Behaviour implements Serializable {
    private Res_Stud_Behaviour_Incident incidents;

    @SerializedName("overall_behaviour_report")
    private String over_all_behaviour_report;

    private String report_download_link;

    private ArrayList<Res_Stud_Behaviour_Overall_Behaviour> overall_behaviour;

    private ArrayList<Res_Stud_Behaviour_Detailed_Behaviour> detailed_behaviour;


    @SerializedName("overall_behaviour_ratting")
    private String over_all_behaviour_ratting;

    public Res_Stud_Behaviour_Incident getIncidents() {
        return incidents;
    }

    public void setIncidents(Res_Stud_Behaviour_Incident incidents) {
        this.incidents = incidents;
    }

    public String getOver_all_behaviour_report() {
        return over_all_behaviour_report;
    }

    public void setOver_all_behaviour_report(String over_all_behaviour_report) {
        this.over_all_behaviour_report = over_all_behaviour_report;
    }

    public ArrayList<Res_Stud_Behaviour_Overall_Behaviour> getOverall_behaviour() {
        return overall_behaviour;
    }

    public void setOverall_behaviour(ArrayList<Res_Stud_Behaviour_Overall_Behaviour> overall_behaviour) {
        this.overall_behaviour = overall_behaviour;
    }

    public ArrayList<Res_Stud_Behaviour_Detailed_Behaviour> getDetailed_behaviour() {
        return detailed_behaviour;
    }

    public void setDetailed_behaviour(ArrayList<Res_Stud_Behaviour_Detailed_Behaviour> detailed_behaviour) {
        this.detailed_behaviour = detailed_behaviour;
    }

    public String getOver_all_behaviour_ratting() {
        return over_all_behaviour_ratting;
    }

    public void setOver_all_behaviour_ratting(String over_all_behaviour_ratting) {
        this.over_all_behaviour_ratting = over_all_behaviour_ratting;
    }

    public String getReport_download_link() {
        return report_download_link;
    }

    public void setReport_download_link(String report_download_link) {
        this.report_download_link = report_download_link;
    }

    @Override
    public String toString() {
        return "ClassPojo [incidents = " + incidents + ", over_all_behaviour_report = " + over_all_behaviour_report + ", overall_behaviour = " + overall_behaviour + ", detailed_behaviour = " + detailed_behaviour + ", over_all_behaviour_ratting = " + over_all_behaviour_ratting + "]";
    }
}
