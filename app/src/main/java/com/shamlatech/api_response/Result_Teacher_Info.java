package com.shamlatech.api_response;

        import java.io.Serializable;
        import java.util.ArrayList;
/**
 * Created by Martin Mundia on 27-AUG-2019.
 */
public class Result_Teacher_Info implements Serializable {

    private String status;
    private String message;

    private String teacher_id;
    private String name;
    private String middle_name;
    private String last_name;
    private String tsc_number;
    private String sex;
    private String phone_verified;
    private String email;
    private String password;
    private String school_id;
    private String designation;
    private String details;
    private String teacherscode;
    private String initial;
    private String stream_id;
    private String stream_name;
    private String class_id;
    private String class_name;
    private String subject_id;
    private String subject;
    private String school_name;

    private ArrayList<Res_Class_info> classlist;
    private ArrayList<Res_SI_School_Details> schoolslist;

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

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone_verified() {
        return phone_verified;
    }
    public void setPhone_verified(String phone_verified) {
        this.phone_verified = phone_verified;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }


    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
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

    public ArrayList<Res_Class_info> getClasslist() {
        return classlist;
    }
    public void setClasslist(ArrayList<Res_Class_info> classlist) {
        this.classlist = classlist;
    }

    public ArrayList<Res_SI_School_Details> getSchoolslist() {
        return schoolslist;
    }
    public void setSchoolslist(ArrayList<Res_SI_School_Details> schoolslist) {
        this.schoolslist = schoolslist;
    }

    @Override
    public String toString() {
        return "ClassPojo [status = " + status + ", message = " + message + "  , classlist = " + classlist + " , schoolslist = " + schoolslist + "  ]";
    }
}

