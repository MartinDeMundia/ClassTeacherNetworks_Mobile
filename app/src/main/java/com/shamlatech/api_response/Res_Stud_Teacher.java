package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Teacher implements Serializable{
    private ArrayList<Res_Stud_Teacher_Info> subject_teachers;

    private Res_Stud_Teacher_Info class_teacher;

    public ArrayList<Res_Stud_Teacher_Info> getSubject_teachers() {
        return subject_teachers;
    }

    public void setSubject_teachers(ArrayList<Res_Stud_Teacher_Info> subject_teachers) {
        this.subject_teachers = subject_teachers;
    }

    public Res_Stud_Teacher_Info getClass_teacher() {
        return class_teacher;
    }

    public void setClass_teacher(Res_Stud_Teacher_Info class_teacher) {
        this.class_teacher = class_teacher;
    }

    @Override
    public String toString() {
        return "ClassPojo [subject_teachers = " + subject_teachers + ", class_teacher = " + class_teacher + "]";
    }
}
