package com.shamlatech.api_response;

import java.util.ArrayList;

public class Result_Notification {
    private String message;

    private String status;

    private ArrayList<Res_Notification> notification_list;

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

    public ArrayList<Res_Notification> getNotification_list() {
        return notification_list;
    }

    public void setNotification_list(ArrayList<Res_Notification> notification_list) {
        this.notification_list = notification_list;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", notification_list = " + notification_list + "]";
    }
}
