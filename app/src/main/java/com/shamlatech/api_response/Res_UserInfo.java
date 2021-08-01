package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Res_UserInfo implements Serializable {
    private String id;
    private String do_not_disturb;
    private String vibrate_notification;
    private String first_name;
    private String phone_number;
    private String email;
    private String middle_name;
    private String last_name;
    private String image;
    private String role;
    private String sound_notification;
    private String unread_notifications;

    public String getUnread_notifications() {
        return unread_notifications;
    }

    public void setUnread_notifications(String unread_notifications) {
        this.unread_notifications = unread_notifications;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDo_not_disturb() {
        return do_not_disturb;
    }

    public void setDo_not_disturb(String do_not_disturb) {
        this.do_not_disturb = do_not_disturb;
    }

    public String getVibrate_notification() {
        return vibrate_notification;
    }

    public void setVibrate_notification(String vibrate_notification) {
        this.vibrate_notification = vibrate_notification;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSound_notification() {
        return sound_notification;
    }

    public void setSound_notification(String sound_notification) {
        this.sound_notification = sound_notification;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", do_not_disturb = " + do_not_disturb + ", vibrate_notification = " + vibrate_notification + ", first_name = " + first_name + ", phone_number = " + phone_number + ", email = " + email + ", middle_name = " + middle_name + ", last_name = " + last_name + ", image = " + image + ", role = " + role + ", sound_notification = " + sound_notification + "]";
    }
}
