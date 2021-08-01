package com.shamlatech.api_response;

/**
 * Created by ADMIN on 11-Jun-18.
 */

public class Result_Student_Info {

    private String message;
    private Res_Student_Info student;
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Res_Student_Info getStudent() {
        return student;
    }

    public void setStudent(Res_Student_Info student) {
        this.student = student;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", student = " + student + ", status = " + status + "]";
    }
}
