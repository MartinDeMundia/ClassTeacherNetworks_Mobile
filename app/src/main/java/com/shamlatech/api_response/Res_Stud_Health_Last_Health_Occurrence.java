package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Health_Last_Health_Occurrence implements Serializable {
    private String id;

    private String updated_date;

    private String details;

    private String name;

    private String action;

    private String concern_date;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUpdated_date ()
    {
        return updated_date;
    }

    public void setUpdated_date (String updated_date)
    {
        this.updated_date = updated_date;
    }

    public String getDetails ()
    {
        return details;
    }

    public void setDetails (String details)
    {
        this.details = details;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getAction ()
    {
        return action;
    }

    public void setAction (String action)
    {
        this.action = action;
    }

    public String getConcern_date ()
    {
        return concern_date;
    }

    public void setConcern_date (String concern_date)
    {
        this.concern_date = concern_date;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", updated_date = "+updated_date+", details = "+details+", name = "+name+", action = "+action+", concern_date = "+concern_date+"]";
    }
}
