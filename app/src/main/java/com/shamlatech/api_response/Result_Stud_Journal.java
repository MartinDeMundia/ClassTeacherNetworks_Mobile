package com.shamlatech.api_response;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Martin Mundia on 12-March-2020.
 */

public class Result_Stud_Journal implements Serializable {
    private String message;
    private String status;
    private ArrayList<Res_Stud_Journal> journals;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Res_Stud_Journal> getJournals() {
        return journals;
    }

    public void setJournals(ArrayList<Res_Stud_Journal> journals) {
        this.journals = journals;
    }
    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", journals = " + journals + "]";
    }
}
