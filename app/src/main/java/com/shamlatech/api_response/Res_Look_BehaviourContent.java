package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Res_Look_BehaviourContent implements Serializable {

    private String id;
    private String actions;
    private String content_name;
    private String report;
    private String action_taken;
    private String behaviour;

    public String getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(String behaviour) {
        this.behaviour = behaviour;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getAction_taken() {
        return action_taken;
    }

    public void setAction_taken(String action_taken) {
        this.action_taken = action_taken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public String getContent_name() {
        return content_name;
    }

    public void setContent_name(String content_name) {
        this.content_name = content_name;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", actions = " + actions + ", content_name = " + content_name + "]";
    }
}
