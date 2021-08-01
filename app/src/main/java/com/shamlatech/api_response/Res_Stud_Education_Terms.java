package com.shamlatech.api_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Res_Stud_Education_Terms implements Serializable {
    @SerializedName("detail_marks")
    private ArrayList<Res_Stud_Education_Detailed_Marks> detailed_marks;
    private ArrayList<Res_Stud_Education_Assignment> assignments;
    private ArrayList<Res_Subject_Report> subjectreports;
    private ArrayList<Res_Stud_Education_Overall_Marks> overall_marks;
    private String overall_performance_grade;
    private String report_download_link;
    private String term_name;
    private String subject_report_download_link;

    public String getTerm_name() {
        return term_name;
    }

    public void setTerm_name(String term_name) {
        this.term_name = term_name;
    }

    public ArrayList<Res_Stud_Education_Detailed_Marks> getDetailed_marks() {
        return detailed_marks;
    }

    public void setDetailed_marks(ArrayList<Res_Stud_Education_Detailed_Marks> detailed_marks) {
        this.detailed_marks = detailed_marks;
    }

    public ArrayList<Res_Stud_Education_Assignment> getAssignments() {
        return assignments;
    }

    public ArrayList<Res_Subject_Report> getSubjectReports() {
        return subjectreports;
    }

    public void setAssignments(ArrayList<Res_Stud_Education_Assignment> assignments) {
        this.assignments = assignments;
    }

    public ArrayList<Res_Stud_Education_Overall_Marks> getOverall_marks() {
        return overall_marks;
    }

    public void setOverall_marks(ArrayList<Res_Stud_Education_Overall_Marks> overall_marks) {
        this.overall_marks = overall_marks;
    }

    public String getOverall_performance_grade() {
        return overall_performance_grade;
    }

    public void setOverall_performance_grade(String overall_performance_grade) {
        this.overall_performance_grade = overall_performance_grade;
    }

    public String getReport_download_link() {
        return report_download_link;
    }

    public void setReport_download_link(String report_download_link) {
        this.report_download_link = report_download_link;
    }

    public String getSubjectReport_download_link() {
        return subject_report_download_link;
    }

    public void setSubjectReport_download_link(String subject_report_download_link) {
        this.subject_report_download_link = subject_report_download_link;
    }

}
