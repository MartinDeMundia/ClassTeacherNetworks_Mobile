package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Result_Stud_Attendance implements Serializable {
    private String message;

    private String status;

    private Res_Stud_Attendance attendance;

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

    public Res_Stud_Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Res_Stud_Attendance attendance) {
        this.attendance = attendance;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", attendance = " + attendance + "]";
    }
}
