package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Res_Look_Behaviour implements Serializable {
    private ArrayList<Res_Look_BehaviourContent> content;

    private String id;

    private String behaviour_title;

    public ArrayList<Res_Look_BehaviourContent> getContent() {
        return content;
    }

    public void setContent(ArrayList<Res_Look_BehaviourContent> content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBehaviour_title() {
        return behaviour_title;
    }

    public void setBehaviour_title(String behaviour_title) {
        this.behaviour_title = behaviour_title;
    }

    @Override
    public String toString() {
        return  behaviour_title;
    }
}
