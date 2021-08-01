package com.shamlatech.api_response;

        import java.io.Serializable;
        import java.util.ArrayList;
/**
 * Created by Martin Mundia on 27-AUG-2019.
 */
public class Result_Exam_List implements Serializable {
    private String id;
    private String examname;
    private ArrayList<Res_Exams> exams;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getExamname() {
        return examname;
    }

    public void setExamname(String examname) {
        this.examname = examname;
    }

    public ArrayList<Res_Exams> getExams() {
        return exams;
    }

    public void setExams(ArrayList<Res_Exams> exams) {
        this.exams = exams;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", examname = " + examname + " ,exams = " + exams + " ]";
    }
}
