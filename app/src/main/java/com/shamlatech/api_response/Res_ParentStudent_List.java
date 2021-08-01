package com.shamlatech.api_response;
import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by ADMIN on 12-Jun-18.
 */

public class Res_ParentStudent_List implements Serializable {
    private String studentid;
    private String studentcode;
    private String studentname;
    private String classname;
    private String  img;

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getStudentcode() {
        return studentcode;
    }

    public void setStudentcode(String studentcode) {
        this.studentcode = studentcode;
    }

    public String getStudentname() {
        return studentname;
    }

    public void setStudentname(String studentname) {
        this.studentname = studentname;
    }

    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }


}
