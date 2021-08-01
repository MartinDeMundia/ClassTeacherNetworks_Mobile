package com.shamlatech.api_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Education_Assignment implements Serializable {

    private String id;
    private String details;
    private String status;
    private String name;
    private String due_date;
    private String given_date;
    private String submit_on;
    private String comment;
    private String subject_id;
    private String subject_name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getGiven_date() {
        return given_date;
    }

    public void setGiven_date(String given_date) {
        this.given_date = given_date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSubmit_on() {
        return submit_on;
    }

    public void setSubmit_on(String submit_on) {
        this.submit_on = submit_on;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", details = " + details + ", status = " + status + ", name = " + name + ", due_date = " + due_date + ", given_date = " + given_date + ", comment = " + comment + "]";
    }
}
