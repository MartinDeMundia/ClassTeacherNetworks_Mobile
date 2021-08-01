package com.shamlatech.pojo;

/**
 * Created by Dharmalingam Sekar on 09-05-2018.
 */

public class PojoParentWithClassList {
    String class_id, section_id, class_name, section_name, teaching_subject;


    public String getTeaching_subject() {
        return teaching_subject;
    }

    public void setTeaching_subject(String teaching_subject) {
        this.teaching_subject = teaching_subject;
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

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    @Override
    public String toString() {
        return class_name + section_name + " Parents";
    }
}
