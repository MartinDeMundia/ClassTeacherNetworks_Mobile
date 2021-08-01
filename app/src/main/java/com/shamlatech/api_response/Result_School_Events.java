package com.shamlatech.api_response;

import java.util.ArrayList;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Result_School_Events {

    private String message;

    private String status;

    private ArrayList<Res_School_Events> events;

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

    public ArrayList<Res_School_Events> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Res_School_Events> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", events = " + events + "]";
    }

}
