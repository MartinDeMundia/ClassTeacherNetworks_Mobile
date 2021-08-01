package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Fees_Details implements Serializable {
    private ArrayList<Res_Stud_Fees_Details_Content> content;

    private String amount;

    private String id;

    private String title;

    public ArrayList<Res_Stud_Fees_Details_Content> getContent() {
        return content;
    }

    public void setContent(ArrayList<Res_Stud_Fees_Details_Content> content) {
        this.content = content;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ClassPojo [content = " + content + ", amount = " + amount + ", id = " + id + ", title = " + title + "]";
    }
}
