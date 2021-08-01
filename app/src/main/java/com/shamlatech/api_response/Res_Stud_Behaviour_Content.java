package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Behaviour_Content implements Serializable {
    private String id;

    private String action;

    private String report;

    private String content_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getContent_name() {
        return content_name;
    }

    public void setContent_name(String content_name) {
        this.content_name = content_name;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", action = " + action + ", report = " + report + ", content_name = " + content_name + "]";
    }
}
