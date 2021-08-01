package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Health_Known_Allergies implements Serializable {
    private String id;

    private String updated_date;

    private String details;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", updated_date = " + updated_date + ", details = " + details + ", name = " + name + "]";
    }
}
