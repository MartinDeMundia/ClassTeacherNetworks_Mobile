package com.shamlatech.api_response;

import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Result_Announcement {
    private String message;

    private ArrayList<Res_Announcement> announcement;

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Res_Announcement> getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(ArrayList<Res_Announcement> announcement) {
        this.announcement = announcement;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", announcement = " + announcement + ", status = " + status + "]";
    }
}
