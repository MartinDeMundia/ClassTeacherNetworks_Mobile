package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Behaviour_Overall_Behaviour implements Serializable {
    private String id;

    private String behaviour_title;

    private String report;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBehaviour_title() {
        return behaviour_title;
    }

    public void setBehaviour_title(String behaviour_title) {
        this.behaviour_title = behaviour_title;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", behaviour_title = " + behaviour_title + ", report = " + report + "]";
    }
}
