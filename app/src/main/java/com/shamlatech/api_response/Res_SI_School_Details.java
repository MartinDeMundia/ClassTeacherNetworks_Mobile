package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 11-Jun-18.
 */

public class Res_SI_School_Details implements Serializable {
    private String school_logo;
    private String school_name;
    private String school_id;

    private String website;
    private String email;
    private String address;
    private String telephone;

    public String getSchool_logo ()
    {
        return school_logo;
    }
    public void setSchool_logo (String school_logo)
    {
        this.school_logo = school_logo;
    }

    public String getSchool_name ()
    {
        return school_name;
    }
    public void setSchool_name (String school_name)
    {
        this.school_name = school_name;
    }

    public String getSchool_id ()
    {
        return school_id;
    }
    public void setSchool_id (String school_id)
    {
        this.school_id = school_id;
    }

    public String getWebsite()
    {
        return website;
    }
    public void setWebsite(String website)
    {
        this.website = website;
    }

    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAddress()
    {
        return address;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getTelephone()
    {
        return telephone;
    }
    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [school_logo = "+school_logo+", school_name = "+school_name+", school_id = "+school_id+"]";
    }
}