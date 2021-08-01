package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Res_Teacher_ClassStudent implements Serializable {

    private String id;
    private String first_name;
    private String class_id;
    private String section_id;
    private String image;
    private String level;
    private String seat;
    private String middle_name;
    private String last_name;
    private String class_name;
    private String section_name;
    private String student_code;

    private String admnno;
    private String term;
    private String examtype;
    private String subject;
    private String marks;


    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getExamtype() {
        return examtype;
    }

    public void setExamtype(String examtype) {
        this.examtype = examtype;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }


    public String getAdmnno() {
        return admnno;
    }

    public void setAdmnno(String admnno) {
        this.admnno = admnno;
    }


    public String getStudent_code() {
        return student_code;
    }

    public void setStudent_code(String student_code) {
        this.student_code = student_code;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
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

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
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

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    @Override
    public String toString() {
        return "ClassPojo [ admnno = " + admnno + ",  term = " + term + ", examtype = " + examtype + ", subject = " + subject + ", marks = " + marks + ", student_code = " + student_code + ",   id = " + id + ", first_name = " + first_name + ", class_id = " + class_id + ", level = " + level + ", seat = " + seat + ", middle_name = " + middle_name + ", last_name = " + last_name + ", class_name = " + class_name + "]";
    }
}
