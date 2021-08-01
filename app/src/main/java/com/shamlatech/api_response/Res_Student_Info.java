package com.shamlatech.api_response;

import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by ADMIN on 11-Jun-18.
 */

public class Res_Student_Info {


    private String id;
    private String admission_number;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String image;
    private String level;
    private Integer complete;
    private String DOB;
    private String gender;
    private String avg_grade;
    private String avg_position;
    private String overall_behaviour;
    private String behaviour_ratting;
    private String studied_years = "2019";
    private String current_year = "2019";
    private String seat;
    private ArrayList<Res_SI_Teacher> subject_teachers;
    private Res_SI_Teacher class_teacher;
    private Res_SI_Class_Details class_details;
    private Res_SI_School_Details school_details;
    private ArrayList<Res_SI_Parent_Details> parent_details;

    public String getAdmission_number() {
        return admission_number;
    }

    public void setAdmission_number(String admission_number) {
        this.admission_number = admission_number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<Res_SI_Parent_Details> getParent_details() {
        return parent_details;
    }

    public void setParent_details(ArrayList<Res_SI_Parent_Details> parent_details) {
        this.parent_details = parent_details;
    }

    public String getAvg_grade() {
        return avg_grade;
    }

    public void setAvg_grade(String avg_grade) {
        this.avg_grade = avg_grade;
    }

    public String getAvg_position() {
        return avg_position;
    }

    public void setAvg_position(String avg_position) {
        this.avg_position = avg_position;
    }

    public String getOverall_behaviour() {
        return overall_behaviour;
    }

    public void setOverall_behaviour(String overall_behaviour) {
        this.overall_behaviour = overall_behaviour;
    }

    public String getBehaviour_ratting() {
        return behaviour_ratting;
    }

    public void setBehaviour_ratting(String behaviour_ratting) {
        this.behaviour_ratting = behaviour_ratting;
    }

    public Res_SI_School_Details getSchool_details() {
        return school_details;
    }

    public void setSchool_details(Res_SI_School_Details school_details) {
        this.school_details = school_details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLevel() {
        return level;
    }

    public Integer getComplete() {
        return complete;
    }
    public void setComplete(Integer complete) {
        this.complete = complete;
    }
    public void setLevel(String level) {
        this.level = level;
    }

    public ArrayList<Res_SI_Teacher> getSubject_teachers() {
        return subject_teachers;
    }

    public void setSubject_teachers(ArrayList<Res_SI_Teacher> subject_teachers) {
        this.subject_teachers = subject_teachers;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Res_SI_Teacher getClass_teacher() {
        return class_teacher;
    }

    public void setClass_teacher(Res_SI_Teacher class_teacher) {
        this.class_teacher = class_teacher;
    }

    public Res_SI_Class_Details getClass_details() {
        return class_details;
    }

    public void setClass_details(Res_SI_Class_Details class_details) {
        this.class_details = class_details;
    }

    public String getStudied_years() {
        return studied_years;
    }

    public void setStudied_years(String studied_years) {
        this.studied_years = studied_years;
    }

    public String getCurrent_year() {
        return current_year;
    }

    public void setCurrent_year(String current_year) {
        this.current_year = current_year;
    }



    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", first_name = " + first_name + ", level = " + level + ", subject_teachers = " + subject_teachers + ", seat = " + seat + ", DOB = " + DOB + ", middle_name = " + middle_name + ", last_name = " + last_name + ", image = " + image + ", class_teacher = " + class_teacher + ", class_details = " + class_details + "]";
    }
}
