package com.shamlatech.pojo;

/**
 * Created by Shamla Tech on 19-05-2018.
 */

public class PojoTimeTable {
    private String time;
    private String sat;
    private String thu;
    private String wed;
    private String mon;
    private String slot;
    private String tue;
    private String sun;
    private String break_name;
    private String fri;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getWed() {
        return wed;
    }

    public void setWed(String wed) {
        this.wed = wed;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getTue() {
        return tue;
    }

    public void setTue(String tue) {
        this.tue = tue;
    }

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }

    public String getBreak_name() {
        return break_name;
    }

    public void setBreak_name(String break_name) {
        this.break_name = break_name;
    }

    public String getFri() {
        return fri;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    @Override
    public String toString() {
        return "ClassPojo [time = " + time + ", sat = " + sat + ", thu = " + thu + ", wed = " + wed + ", mon = " + mon + ", slot = " + slot + ", tue = " + tue + ", sun = " + sun + ", is_break = " + break_name + ", fri = " + fri + "]";
    }
}
