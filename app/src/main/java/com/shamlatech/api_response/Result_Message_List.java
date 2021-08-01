package com.shamlatech.api_response;

import java.util.ArrayList;

public class Result_Message_List {
    private String message;

    private String status;

    private ArrayList<Res_Message_List> message_list;

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

    public ArrayList<Res_Message_List> getMessage_list() {
        return message_list;
    }

    public void setMessage_list(ArrayList<Res_Message_List> message_list) {
        this.message_list = message_list;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", message_list = " + message_list + "]";
    }
}
