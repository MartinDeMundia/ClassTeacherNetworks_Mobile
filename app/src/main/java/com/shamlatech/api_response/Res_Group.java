package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Res_Group implements Serializable {

    private String id;

    private String subject_name;

    private String class_id;

    private String created_on;

    private String subject_id;

    private String name;

    private String creator_name;

    private String section_id;

    private String class_name;

    private String creator_id;

    private String section_name;

    private ArrayList<Res_Group_Member> group_members;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
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

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public ArrayList<Res_Group_Member> getGroup_members() {
        return group_members;
    }

    public void setGroup_members(ArrayList<Res_Group_Member> group_members) {
        this.group_members = group_members;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", subject_name = " + subject_name + ", class_id = " + class_id + ", created_on = " + created_on + ", subject_id = " + subject_id + ", name = " + name + ", creator_name = " + creator_name + ", section_id = " + section_id + ", class_name = " + class_name + ", creator_id = " + creator_id + ", section_name = " + section_name + ", group_members = " + group_members + "]";
    }
}
