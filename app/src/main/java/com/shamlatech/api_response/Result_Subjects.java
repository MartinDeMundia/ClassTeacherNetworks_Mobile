
package com.shamlatech.api_response;

        import java.io.Serializable;
        import java.util.ArrayList;
/**
 * Created by Martin Mundia on 26-APRIL-2021.
 */
public class Result_Subjects implements Serializable {
    private ArrayList<Res_Subjects> subjectlist;
    public ArrayList<Res_Subjects> getSubjects() {
        return subjectlist;
    }
    public void setSubjects(ArrayList<Res_Subjects> subjectlist) {
        this.subjectlist = subjectlist;
    }
    @Override
    public String toString() {
        return "ClassPojo [ subjectlist = " + subjectlist + " ]";
    }
}

