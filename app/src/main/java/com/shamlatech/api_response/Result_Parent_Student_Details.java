package com.shamlatech.api_response;

import java.util.ArrayList;

public class Result_Parent_Student_Details {
    private String message;

    private ArrayList<Res_Student_Info> students;

    private String status;
    private String expired;

    public String getMessage() {
        return message;
    }

    public String getExpired() {
        return message;
    }

    public void setExpired(String expired) {
        this.message = expired;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Res_Student_Info> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Res_Student_Info> students) {
        this.students = students;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", students = " + students  + ", expired = " + expired  + ", status = " + status + "]";
    }
}
