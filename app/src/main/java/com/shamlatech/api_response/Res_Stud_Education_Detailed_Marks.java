package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Education_Detailed_Marks implements Serializable {

    private String id;
    private String exam_name;
    private String grade;
    private ArrayList<Res_Stud_Education_Marks> marks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public ArrayList<Res_Stud_Education_Marks> getMarks() {
        return marks;
    }

    public void setMarks(ArrayList<Res_Stud_Education_Marks> marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", exam_name = " + exam_name + ", marks = " + marks + "]";
    }
}
