package com.shamlatech.api_response;

import java.io.Serializable;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Res_Look_ExamList implements Serializable {
    private String id;

    private String exam_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }
}
