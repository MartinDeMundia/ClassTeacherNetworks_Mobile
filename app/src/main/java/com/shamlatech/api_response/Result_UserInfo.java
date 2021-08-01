package com.shamlatech.api_response;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Result_UserInfo {
    private Res_UserInfo user_info;

    private String message;
    private String expired;
    private String status;


    public Res_UserInfo getUser_info() {
        return user_info;
    }



    public String getExpired() {
        return message;
    }

    public void setExpired(String expired) {
        this.message = expired;
    }


    public void setUser_info(Res_UserInfo user_info) {
        this.user_info = user_info;
    }

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

    @Override
    public String toString() {
        return "ClassPojo [user_info = " + user_info + ", message = " + message + ", expired = " + expired  +  ", status = " + status + "]";
    }
}
