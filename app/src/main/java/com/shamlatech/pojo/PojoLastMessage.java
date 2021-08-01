package com.shamlatech.pojo;

/**
 * Created by SUN on 18-11-2017.
 */

public class PojoLastMessage {
    public String message_id;
    public String message;
    public String message_on;
    public String message_by;
    public String message_type;
    public String message_status;
    public String unread_count;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_on() {
        return message_on;
    }

    public void setMessage_on(String message_on) {
        this.message_on = message_on;
    }

    public String getMessage_by() {
        return message_by;
    }

    public void setMessage_by(String message_by) {
        this.message_by = message_by;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessage_status() {
        return message_status;
    }

    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }

    public String getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(String unread_count) {
        this.unread_count = unread_count;
    }
}
