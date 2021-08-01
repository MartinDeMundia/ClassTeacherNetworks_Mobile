package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 11-Jun-18.
 */

public class Res_SI_Teacher implements Serializable {
    private String id;

    private String subject_name;

    private String first_name;

    private String phone_number;

    private String email;

    private String subject_id;

    private String middle_name;

    private String last_name;

    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
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

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
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

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", subject_name = " + subject_name + ", first_name = " + first_name + ", phone_number = " + phone_number + ", email = " + email + ", subject_id = " + subject_id + ", middle_name = " + middle_name + ", last_name = " + last_name + ", image = " + image + "]";
    }
}