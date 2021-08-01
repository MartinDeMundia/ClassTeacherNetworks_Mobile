package com.shamlatech.api_response;
        import java.io.Serializable;
        import java.util.ArrayList;

public class Res_Class_info implements Serializable {

    private String teacher_id;
    private String name;
    private String middle_name;
    private String last_name;
    private String tsc_number;
    private String teacherscode;
    private String initial;
    private String stream_id;
    private String stream_name;
    private String class_id;
    private String class_name;
    private String subject_id;
    private String subject;
    private String school_name;


    public String getTeacher_id() {
        return teacher_id;
    }
    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getMiddle_name() {
        return middle_name;
    }
    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getTsc_number() {
        return tsc_number;
    }
    public void setTsc_number(String tsc_number) {
        this.tsc_number = tsc_number;
    }

    public String getTeacherscode() {
        return teacherscode;
    }
    public void setTeacherscode(String teacherscode) {
        this.teacherscode = teacherscode;
    }

    public String getInitial() {
        return initial;
    }
    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getStream_id() {
        return stream_id;
    }
    public void setStream_id(String stream_id) {
        this.stream_id = stream_id;
    }

    public String getStream_name() {
        return stream_name;
    }
    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

    public String getClass_id() {
        return class_id;
    }
    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }
    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSubject_id() {
        return subject_id;
    }
    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSchool_name() {
        return school_name;
    }
    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }


}

