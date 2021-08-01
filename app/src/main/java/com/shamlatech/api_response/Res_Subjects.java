package com.shamlatech.api_response;

 import java.io.Serializable;
/**
 * Created by Martin Mundia on 03-May-2021.
 */
public class Res_Subjects implements Serializable {

    private String id;
    private String code;
    private String abbreviation;
    private String description;
    private String is_active;
    private String teacher_id;
    private String class_id;
    private String section_id;
    private String school_id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getIs_active() {
        return is_active;
    }
    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getTeacher_id() {
        return teacher_id;
    }
    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getClass_id() {
        return class_id;
    }
    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getSection_id() {
        return section_id;
    }
    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getSchool_id() {
        return school_id;
    }
    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

}

