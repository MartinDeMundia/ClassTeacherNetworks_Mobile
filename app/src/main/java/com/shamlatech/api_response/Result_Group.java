package com.shamlatech.api_response;

import java.util.ArrayList;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Result_Group {

    private String message;

    private String status;

    private ArrayList<Res_Group> group;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Res_Group> getGroup() {
        return group;
    }

    public void setGroup(ArrayList<Res_Group> group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", group = " + group + "]";
    }
}
