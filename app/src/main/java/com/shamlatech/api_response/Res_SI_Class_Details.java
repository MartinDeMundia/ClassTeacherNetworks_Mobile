package com.shamlatech.api_response;

/**
 * Created by ADMIN on 11-Jun-18.
 */

public class Res_SI_Class_Details {
    private String class_id;
    private String section_id;
    private String section_name;
    private String level;
    private String total_seat;
    private String column;
    private String divides;
    private String class_name;

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTotal_seat() {
        return total_seat;
    }

    public void setTotal_seat(String total_seat) {
        this.total_seat = total_seat;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getDivides() {
        return divides;
    }

    public void setDivides(String divides) {
        this.divides = divides;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    @Override
    public String toString() {
        return "ClassPojo [class_id = " + class_id + ", level = " + level + ", total_seat = " + total_seat + ", column = " + column + ", divides = " + divides + ", class_name = " + class_name + "]";
    }
}