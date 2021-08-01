package com.shamlatech.api_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Education implements Serializable {

    private ArrayList<Res_Stud_Education_Terms> terms;

    public ArrayList<Res_Stud_Education_Terms> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<Res_Stud_Education_Terms> terms) {
        this.terms = terms;
    }
}
