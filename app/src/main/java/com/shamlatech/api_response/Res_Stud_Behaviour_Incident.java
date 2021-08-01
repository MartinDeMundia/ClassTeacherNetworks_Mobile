package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Behaviour_Incident implements Serializable {
    private String count;

    private String action_taken;

    public String getCount ()
    {
        return count;
    }

    public void setCount (String count)
    {
        this.count = count;
    }

    public String getAction_taken ()
    {
        return action_taken;
    }

    public void setAction_taken (String action_taken)
    {
        this.action_taken = action_taken;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [count = "+count+", action_taken = "+action_taken+"]";
    }
}
