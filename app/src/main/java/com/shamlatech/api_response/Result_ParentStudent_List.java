package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by Martin Mundia on 27-AUG-2019.
 */
public class Result_ParentStudent_List implements Serializable {
    private String status;
    private String message;

    private String parentid;
    private String firstname;
    private String lastname;
    private String phone1;
    private String phone2;
    private String email;
    private String occupation;
    private String password;

    private ArrayList<Res_ParentStudent_List> parentstudentlist;
    private ArrayList<Res_SI_School_Details> schoolslist;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Res_ParentStudent_List> getParentstudentlist() {
        return parentstudentlist;
    }

    public void setParentstudentlist(ArrayList<Res_ParentStudent_List> parentstudentlist) {
        this.parentstudentlist = parentstudentlist;
    }

    public ArrayList<Res_SI_School_Details> getSchoolslist() {
        return schoolslist;
    }

    public void setSchoolslist(ArrayList<Res_SI_School_Details> schoolslist) {
        this.schoolslist = schoolslist;
    }

    @Override
    public String toString() {
        return "ClassPojo [status = " + status + ", message = " + message + "  , parentstudentlist = " + parentstudentlist + " , schoolslist = " + schoolslist + "  ]";
    }
}

