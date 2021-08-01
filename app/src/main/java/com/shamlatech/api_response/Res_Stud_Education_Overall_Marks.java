package com.shamlatech.api_response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Education_Overall_Marks implements Serializable{

    @SerializedName("total_marks")
    private String total_mark;
    private String id;
    private String exam_name;
    private String grade;
    @SerializedName("student_marks")
    private String student_mark;

    public String getTotal_mark ()
    {
        return total_mark;
    }

    public void setTotal_mark (String total_mark)
    {
        this.total_mark = total_mark;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getExam_name ()
    {
        return exam_name;
    }

    public void setExam_name (String exam_name)
    {
        this.exam_name = exam_name;
    }

    public String getGrade ()
    {
        return grade;
    }

    public void setGrade (String grade)
    {
        this.grade = grade;
    }

    public String getStudent_mark ()
    {
        return student_mark;
    }

    public void setStudent_mark (String student_mark)
    {
        this.student_mark = student_mark;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [total_mark = "+total_mark+", id = "+id+", exam_name = "+exam_name+", grade = "+grade+", student_mark = "+student_mark+"]";
    }
}
