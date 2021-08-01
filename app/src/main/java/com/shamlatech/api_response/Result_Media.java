package com.shamlatech.api_response;

import java.util.ArrayList;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Result_Media {

    private String message;
    private String status;
    private ArrayList<Res_Media> document;
    private ArrayList<Res_Media> image;
    private ArrayList<Res_Media> video;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Res_Media> getDocument() {
        return document;
    }

    public void setDocument(ArrayList<Res_Media> document) {
        this.document = document;
    }

    public ArrayList<Res_Media> getImage() {
        return image;
    }

    public void setImage(ArrayList<Res_Media> image) {
        this.image = image;
    }

    public ArrayList<Res_Media> getVideo() {
        return video;
    }

    public void setVideo(ArrayList<Res_Media> video) {
        this.video = video;
    }
}
