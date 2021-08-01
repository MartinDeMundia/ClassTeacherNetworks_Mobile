package com.shamlatech.api_response;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Martin Mundia M on 12-Sep-2019.
 */

public class Res_Student_Marks implements Serializable {

    private String studentid;
    private String term;
    private String examtype;
    private String subject;
    private String marks;
    private String school;
    private String teacher;
    private String names;
    private String subjectpart;

    public String getStudentid() {
        return studentid;
    }
    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getTerm() {
        return term;
    }
    public void setTerm(String term) {
        this.term = term;
    }

    public String getExamtype() {
        return examtype;
    }

    public void setExamtype(String examtype) {
        this.examtype = examtype;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getSubjectpart() {
        return subjectpart;
    }

    public void setSubjectpart(String subjectpart) {
        this.subjectpart = subjectpart;
    }
}
