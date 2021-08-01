package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 12-Jun-18.
 */

public class Result_Forum_List implements Serializable {
    private String message;

    private String status;

    private ArrayList<Res_Forum_List> forums_list;

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

    public ArrayList<Res_Forum_List> getForums_list() {
        return forums_list;
    }

    public void setForums_list(ArrayList<Res_Forum_List> forums_list) {
        this.forums_list = forums_list;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", forums_list = " + forums_list + "]";
    }
}
