package com.shamlatech.api_response;

        import java.io.Serializable;
        import java.util.ArrayList;
/**
 * Created by Martin Mundia on 26-APRIL-2021.
 */
public class Result_Classes implements Serializable {

    private ArrayList<Res_Class_info> streamlist;
    private ArrayList<Res_Class_info> classlist;

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
        return "ClassPojo [ streamlist = " + streamlist + " , classlist = " + classlist + "  ]";
    }
}

