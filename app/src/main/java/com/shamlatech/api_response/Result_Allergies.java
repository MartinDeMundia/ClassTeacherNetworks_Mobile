package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Result_Allergies implements Serializable {
    private String message;

    private String status;

    private ArrayList<Res_Stud_Health_Known_Allergies> allergies;

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

    public ArrayList<Res_Stud_Health_Known_Allergies> getAllergies() {
        return allergies;
    }

    public void setAllergies(ArrayList<Res_Stud_Health_Known_Allergies> allergies) {
        this.allergies = allergies;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", allergies = " + allergies + "]";
    }
}
