package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Education_Marks implements Serializable{

    private String total_mark;

    private String subject_name;

    private String grade;

    private String student_mark;

    private ArrayList<Res_Stud_Education_Parts_Marks> part_marks;

    public String getTotal_mark ()
    {
        return total_mark;
    }

    public void setTotal_mark (String total_mark)
    {
        this.total_mark = total_mark;
    }

    public String getSubject_name ()
    {
        return subject_name;
    }

    public void setSubject_name (String subject_name)
    {
        this.subject_name = subject_name;
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

    public ArrayList<Res_Stud_Education_Parts_Marks> getPart_marks() {
        return part_marks;
    }

    public void setPart_marks(ArrayList<Res_Stud_Education_Parts_Marks> part_marks) {
        this.part_marks = part_marks;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [total_mark = "+total_mark+", subject_name = "+subject_name+", grade = "+grade+", student_mark = "+student_mark+"]";
    }
}
