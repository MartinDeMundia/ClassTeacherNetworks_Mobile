package com.shamlatech.pojo;

import java.io.Serializable;

/**
 * Created by Dharmalingam Sekar on 08-05-2018.
 */

public class PojoMessageList implements Serializable{
    public String friend_id;
    public String role_id;
    public String friend_name;
    public String profile_pic;
    public String online_status;
    public String last_message;
    public String last_message_on;
    public String last_message_by;
    public String last_message_status;
    public String last_message_type;
    public String unread_count;

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public String getLast_message_on() {
        return last_message_on;
    }

    public void setLast_message_on(String last_message_on) {
        this.last_message_on = last_message_on;
    }

    public String getLast_message_by() {
        return last_message_by;
    }

    public void setLast_message_by(String last_message_by) {
        this.last_message_by = last_message_by;
    }

    public String getLast_message_status() {
        return last_message_status;
    }

    public void setLast_message_status(String last_message_status) {
        this.last_message_status = last_message_status;
    }

    public String getLast_message_type() {
        return last_message_type;
    }

    public void setLast_message_type(String last_message_type) {
        this.last_message_type = last_message_type;
    }

    public String getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(String unread_count) {
        this.unread_count = unread_count;
    }
}
