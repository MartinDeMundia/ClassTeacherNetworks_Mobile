package com.shamlatech.api_response;

import java.util.ArrayList;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Result_TeacherInfo {

    private String message;
    private String status;

    private ArrayList<Res_Teacher_Class> my_class;
    private ArrayList<Res_Teacher_Class> subject_class;
    private ArrayList<Res_Teacher_TeachingSubjects> teaching_subjects;
    private ArrayList<Res_Teacher_TimeTable> timetable;
    private ArrayList<Res_Student_Marks> student_marks;
    private ArrayList<Res_Student_Marks> studentpapermarks;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Res_Teacher_Class> getMy_class() {
        return my_class;
    }

    public void setMy_class(ArrayList<Res_Teacher_Class> my_class) {
        this.my_class = my_class;
    }

    public ArrayList<Res_Teacher_Class> getSubject_class() {
        return subject_class;
    }

    public void setSubject_class(ArrayList<Res_Teacher_Class> subject_class) {
        this.subject_class = subject_class;
    }

    public ArrayList<Res_Teacher_TeachingSubjects> getTeaching_subjects() {
        return teaching_subjects;
    }

    public ArrayList<Res_Student_Marks> getStudentpapermarks() {
        return studentpapermarks;
    }

    public ArrayList<Res_Student_Marks> getStudentMarks() {
        return student_marks;
    }

    public void setTeaching_subjects(ArrayList<Res_Teacher_TeachingSubjects> teaching_subjects) {
        this.teaching_subjects = teaching_subjects;
    }

    public ArrayList<Res_Teacher_TimeTable> getTimetable() {
        return timetable;
    }

    public void setTimetable(ArrayList<Res_Teacher_TimeTable> timetable) {
        this.timetable = timetable;
    }
}
