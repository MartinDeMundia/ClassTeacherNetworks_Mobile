package com.shamlatech.api_response;

import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Result_Stud_Teacher {
    private String message;

    private String status;

    private Res_Stud_Teacher teachers;

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

    public Res_Stud_Teacher getTeachers ()
    {
        return teachers;
    }

    public void setTeachers (Res_Stud_Teacher teachers)
    {
        this.teachers = teachers;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", teachers = "+teachers+"]";
    }
}
