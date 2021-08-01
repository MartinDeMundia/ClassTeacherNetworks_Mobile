package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by ADMIN on 12-Jun-18.
 */

public class Res_Absnote_List implements Serializable {
    private String id;
    private String adate;
    private String anote;
    private String  auserid;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuserid() {
        return auserid;
    }

    public void setAuserid(String auserid) {
        this.auserid = auserid;
    }

    public String getAdate() {
        return adate;
    }

    public void setAdate(String adate) {
        this.adate = adate;
    }

    public String getAnote() {
        return anote;
    }
    public void setAnote(String anote) {
        this.anote = anote;
    }




}
