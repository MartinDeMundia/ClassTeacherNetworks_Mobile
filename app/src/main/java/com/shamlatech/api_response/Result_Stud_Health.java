package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Result_Stud_Health implements Serializable {
    private String message;

    private String status;

    private Res_Stud_Health health;

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

    public Res_Stud_Health getHealth() {
        return health;
    }

    public void setHealth(Res_Stud_Health health) {
        this.health = health;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", status = " + status + ", health = " + health + "]";
    }
}
