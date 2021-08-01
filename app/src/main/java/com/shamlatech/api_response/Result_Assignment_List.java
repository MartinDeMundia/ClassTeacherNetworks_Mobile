package com.shamlatech.api_response;

import java.util.ArrayList;

/**
 * Created by ADMIN on 11-Jun-18.
 */

public class Result_Assignment_List {
    private String message;

    private ArrayList<Res_Assignment_List> assignment;

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Res_Assignment_List> getAssignment() {
        return assignment;
    }

    public void setAssignment(ArrayList<Res_Assignment_List> assignment) {
        this.assignment = assignment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", assignment = " + assignment + ", status = " + status + "]";
    }
}
