package com.shamlatech.api_response;

        import java.io.Serializable;
        import java.util.ArrayList;
/**
 * Created by Martin Mundia on 04-MAY-2021.
 */
public class Result_Student_Class implements Serializable {
    private ArrayList<Res_Class_info> studentclasslist;
    private ArrayList<Res_Class_info> streamlist;
    private ArrayList<Res_Class_info> classlist;

    public ArrayList<Res_Class_info> getStudentClasslist() {
        return studentclasslist;
    }
    public void setStudentClasslist(ArrayList<Res_Class_info> studentclasslist) {
        this.studentclasslist = studentclasslist;
    }
    public ArrayList<Res_Class_info> getStreamlist() {
        return streamlist;
    }
    public void setStreamlist(ArrayList<Res_Class_info> streamlist) {
        this.streamlist = streamlist;
    }
    public ArrayList<Res_Class_info> getClasslist() {
        return classlist;
    }
    public void setClasslist(ArrayList<Res_Class_info> classlist) {
        this.classlist = classlist;
    }

    @Override
    public String toString() {
        return "ClassPojo [ studentclasslist = " + studentclasslist + " , streamlist = "+ streamlist +" , classlist = "+ classlist +"  ]";
    }
}

