package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Result_MyClass implements Serializable {
    private String message;

    private ArrayList<Res_Teacher_Class> my_class;

    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Res_Teacher_Class> getMy_class() {
        return my_class;
    }

    public void setMy_class(ArrayList<Res_Teacher_Class> my_class) {
        this.my_class = my_class;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", my_class = " + my_class + ", status = " + status + "]";
    }
}
