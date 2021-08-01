package com.shamlatech.api_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
/**
 * Created by Martin Mundia on 23-Oct-2019.
 */
public class Res_Subsubjects implements Serializable {

    private String id;
    private String subject_name;

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

    @Override
    public String toString() {
        return subject_name;
    }
}
