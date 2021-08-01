package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Education_Parts_Marks implements Serializable {

    private String name;

    private String total_mark;

    private String student_mark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal_mark() {
        return total_mark;
    }

    public void setTotal_mark(String total_mark) {
        this.total_mark = total_mark;
    }

    public String getStudent_mark() {
        return student_mark;
    }

    public void setStudent_mark(String student_mark) {
        this.student_mark = student_mark;
    }
}
