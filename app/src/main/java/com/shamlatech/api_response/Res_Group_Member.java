package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Res_Group_Member implements Serializable {

    private String id;

    private String name;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", name = "+name+"]";
    }
}
