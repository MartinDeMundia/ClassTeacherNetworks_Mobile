package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Attendance implements Serializable {
    private String over_all_attendance_report;

    private String subject_missed;

    private String reason;

    private String last_missed_date;

    private String over_all_attendance_ratting;

    private String report_download_link;

    private String last_missed_time;

    private ArrayList<Res_Stud_Attendance_Details> detailed_attendance;

    public ArrayList<Res_Stud_Attendance_Details> getDetailed_attendance() {
        return detailed_attendance;
    }

    public void setDetailed_attendance(ArrayList<Res_Stud_Attendance_Details> detailed_attendance) {
        this.detailed_attendance = detailed_attendance;
    }

    public String getOver_all_attendance_report() {
        return over_all_attendance_report;
    }

    public void setOver_all_attendance_report(String over_all_attendance_report) {
        this.over_all_attendance_report = over_all_attendance_report;
    }

    public String getSubject_missed() {
        return subject_missed;
    }

    public void setSubject_missed(String subject_missed) {
        this.subject_missed = subject_missed;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLast_missed_date() {
        return last_missed_date;
    }

    public void setLast_missed_date(String last_missed_date) {
        this.last_missed_date = last_missed_date;
    }

    public String getOver_all_attendance_ratting() {
        return over_all_attendance_ratting;
    }

    public void setOver_all_attendance_ratting(String over_all_attendance_ratting) {
        this.over_all_attendance_ratting = over_all_attendance_ratting;
    }

    public String getLast_missed_time() {
        return last_missed_time;
    }

    public void setLast_missed_time(String last_missed_time) {
        this.last_missed_time = last_missed_time;
    }

    public String getReport_download_link() {
        return report_download_link;
    }

    public void setReport_download_link(String report_download_link) {
        this.report_download_link = report_download_link;
    }

    @Override
    public String toString() {
        return "ClassPojo [over_all_attendance_report = " + over_all_attendance_report + ", subject_missed = " + subject_missed + ", reason = " + reason + ", last_missed_date = " + last_missed_date + ", over_all_attendance_ratting = " + over_all_attendance_ratting + ", last_missed_time = " + last_missed_time + "]";
    }
}
