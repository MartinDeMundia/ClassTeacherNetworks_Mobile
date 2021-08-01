package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Result_Stud_Fees implements Serializable {
    private String message;

    private String status;

    private Res_Stud_Fees fees;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Res_Stud_Fees getFees ()
    {
        return fees;
    }

    public void setFees (Res_Stud_Fees fees)
    {
        this.fees = fees;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", fees = "+fees+"]";
    }
}
