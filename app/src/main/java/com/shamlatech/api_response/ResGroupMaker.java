package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by Dharmalingam Sekar on 08-05-2018.
 */

public class ResGroupMaker implements Serializable {
    String id, name, subject, level;
    String[] student;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String[] getStudent() {
        return student;
    }

    public void setStudent(String[] student) {
        this.student = student;
    }
}
