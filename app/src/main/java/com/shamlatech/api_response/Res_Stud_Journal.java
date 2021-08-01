package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Martin Mundia Mugambi on 12-March-2020.
 */

public class Res_Stud_Journal implements Serializable {
    private String id;
    private String user_id;
    private String emoji;
    private String explanation;
    private String journal_date;
    private String student_id;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getJournal_date() {
        return journal_date;
    }

    public void setJournal_date(String journal_date) {
        this.journal_date = journal_date;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

}

