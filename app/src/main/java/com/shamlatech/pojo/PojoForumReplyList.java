package com.shamlatech.pojo;

/**
 * Created by Dharmalingam Sekar on 10-05-2018.
 */

public class PojoForumReplyList {
    String id;
    String comment;
    String comment_on;
    String comment_by_id;
    String comment_by_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_on() {
        return comment_on;
    }

    public void setComment_on(String comment_on) {
        this.comment_on = comment_on;
    }

    public String getComment_by_id() {
        return comment_by_id;
    }

    public void setComment_by_id(String comment_by_id) {
        this.comment_by_id = comment_by_id;
    }

    public String getComment_by_name() {
        return comment_by_name;
    }

    public void setComment_by_name(String comment_by_name) {
        this.comment_by_name = comment_by_name;
    }
}
