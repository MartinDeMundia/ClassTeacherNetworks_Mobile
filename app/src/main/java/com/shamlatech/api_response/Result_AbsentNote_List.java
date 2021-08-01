package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by Martin Mundia on 27-AUG-2019.
 */
public class Result_AbsentNote_List implements Serializable {
    private String anote;
    private String adate;
    private ArrayList<Res_Absnote_List> notes_list;


    public String getNote() {
        return anote;
    }

    public void setNote(String anote) {
        this.anote = anote;
    }

    public String getAdate() {
        return adate;
    }

    public void setAdate(String adate) {
        this.adate = adate;
    }

    public ArrayList<Res_Absnote_List> getAnote_list() {
        return notes_list;
    }

    public void setAnote_list(ArrayList<Res_Absnote_List> notes_list) {
        this.notes_list = notes_list;
    }

    @Override
    public String toString() {
        return "ClassPojo [anote = " + anote + ", adate = " + adate + "  , notes_list = " + notes_list + "  ]";
    }
}
