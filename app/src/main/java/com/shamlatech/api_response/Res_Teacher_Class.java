package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Res_Teacher_Class implements Serializable {

    private String subject_name;
    private String class_id;
    private String section_id;
    private String section_name;
    private String level;
    private String subject_id;
    private String class_name;
    private String is_my_class;
    private String total_seat;
    private String divides;
    private String column;
    private String myclass;
    private ArrayList<Res_Teacher_ClassStudent> student;


    public String getMyclass() {
        return myclass;
    }

    public void setMyclass(String myclass) {
        this.myclass = myclass;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getIs_my_class() {
        return is_my_class;
    }

    public void setIs_my_class(String is_my_class) {
        this.is_my_class = is_my_class;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ArrayList<Res_Teacher_ClassStudent> getStudent() {
        return student;
    }

    public void setStudent(ArrayList<Res_Teacher_ClassStudent> student) {
        this.student = student;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getTotal_seat() {
        return total_seat;
    }

    public void setTotal_seat(String total_seat) {
        this.total_seat = total_seat;
    }

    public String getDivides() {
        return divides;
    }

    public void setDivides(String divides) {
        this.divides = divides;
    }

    @Override
    public String toString() {
        return "ClassPojo [subject_name = " + subject_name + ", class_id = " + class_id + ", level = " + level + ", student = " + student + ", subject_id = " + subject_id + ", class_name = " + class_name + "]";
    }
}
