package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by Martin Mundia on 23-OCT-2019.
 */
public class Result_Subsubjects implements Serializable {
    private String id;
    private String response;
    private ArrayList<Res_Subsubjects> subsubjects;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<Res_Subsubjects> getSubsubjects() {
        return subsubjects;
    }

    public void setSubsubjects(ArrayList<Res_Subsubjects> subsubjects) {
        this.subsubjects = subsubjects;
    }
    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", response = " + response + " ,subsubjects = " + subsubjects + " ]";
    }
}





