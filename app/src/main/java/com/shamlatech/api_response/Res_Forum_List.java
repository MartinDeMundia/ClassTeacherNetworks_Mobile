package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 12-Jun-18.
 */

public class Res_Forum_List implements Serializable {
    private String to;
    private String topic;
    private String id;
    private String is_open;
    private String posted_on;
    private String is_fav;
    private String total_comments;
    private String from;
    private String posted_id;
    private String posted_name;
    private String class_id;
    private String class_name;
    private String section_id;
    private String section_name;
    private String type;
    private ArrayList<Res_Forum_Comments> comments;

    public ArrayList<Res_Forum_Comments> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Res_Forum_Comments> comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPosted_on() {
        return posted_on;
    }

    public void setPosted_on(String posted_on) {
        this.posted_on = posted_on;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    public String getIs_fav() {
        return is_fav;
    }

    public void setIs_fav(String is_fav) {
        this.is_fav = is_fav;
    }

    public String getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(String total_comments) {
        this.total_comments = total_comments;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPosted_id() {
        return posted_id;
    }

    public void setPosted_id(String posted_id) {
        this.posted_id = posted_id;
    }

    public String getPosted_name() {
        return posted_name;
    }

    public void setPosted_name(String posted_name) {
        this.posted_name = posted_name;
    }


}
