package com.shamlatech.api_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Res_Teacher_TeachingSubjects implements Serializable {
    @SerializedName("subject_id")
    private String id;
    @SerializedName("name")
    private String subject_name;
    @SerializedName("is_part")
    private String parts;
    @SerializedName("part")
    private String parts_name;

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

    public String getParts() {
        return parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public String getParts_name() {
        return parts_name;
    }

    public void setParts_name(String parts_name) {
        this.parts_name = parts_name;
    }

    @Override
    public String toString() {
        return subject_name;
    }
}
