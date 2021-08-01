package com.shamlatech.api_request;

/**
 * Created by  Dharmalingam Sekar  on 07-11-2017.
 */

public class ReqPojoUpdateToken {
    String user_id;
    String role_id;
    String token;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }
}
