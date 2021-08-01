package com.shamlatech.api_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Health implements Serializable {
    private String last_health_concern;
    @SerializedName("overall_health_report")
    private String over_all_health_report;
    private String action_taken;
    @SerializedName("overall_health_ratting")
    private String over_all_health_ratting;
    private String further_action_needed;
    private String report_download_link;

    private ArrayList<Res_Stud_Health_Last_Health_Occurrence> last_occurrences;
    private ArrayList<Res_Stud_Health_Known_Allergies> known_allergies;


    public String getLast_health_concern() {
        return last_health_concern;
    }

    public void setLast_health_concern(String last_health_concern) {
        this.last_health_concern = last_health_concern;
    }

    public String getOver_all_health_report() {
        return over_all_health_report;
    }

    public void setOver_all_health_report(String over_all_health_report) {
        this.over_all_health_report = over_all_health_report;
    }

    public String getAction_taken() {
        return action_taken;
    }

    public void setAction_taken(String action_taken) {
        this.action_taken = action_taken;
    }

    public String getOver_all_health_ratting() {
        return over_all_health_ratting;
    }

    public void setOver_all_health_ratting(String over_all_health_ratting) {
        this.over_all_health_ratting = over_all_health_ratting;
    }

    public ArrayList<Res_Stud_Health_Known_Allergies> getKnown_allergies() {
        return known_allergies;
    }

    public void setKnown_allergies(ArrayList<Res_Stud_Health_Known_Allergies> known_allergies) {
        this.known_allergies = known_allergies;
    }

    public String getFurther_action_needed() {
        return further_action_needed;
    }

    public void setFurther_action_needed(String further_action_needed) {
        this.further_action_needed = further_action_needed;
    }

    public ArrayList<Res_Stud_Health_Last_Health_Occurrence> getLast_occurrences() {
        return last_occurrences;
    }

    public void setLast_occurrences(ArrayList<Res_Stud_Health_Last_Health_Occurrence> last_occurrences) {
        this.last_occurrences = last_occurrences;
    }

    public String getReport_download_link() {
        return report_download_link;
    }

    public void setReport_download_link(String report_download_link) {
        this.report_download_link = report_download_link;
    }

    @Override
    public String toString() {
        return "ClassPojo [last_health_concern = " + last_health_concern + ", over_all_health_report = " + over_all_health_report + ", action_taken = " + action_taken + ", over_all_health_ratting = " + over_all_health_ratting + ", known_allergies = " + known_allergies + ", further_action_needed = " + further_action_needed + ", last_occurrences = " + last_occurrences + "]";
    }
}
