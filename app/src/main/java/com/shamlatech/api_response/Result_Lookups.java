package com.shamlatech.api_response;

import java.util.ArrayList;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Result_Lookups {
    private String message;

    private ArrayList<Res_Look_ExamList> exam_list;

    private String status;

    private ArrayList<Res_Look_Behaviour> behaviour;

    private ArrayList<Res_Look_AbsentReason> absent_reason;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Res_Look_ExamList> getExam_list() {
        return exam_list;
    }

    public void setExam_list(ArrayList<Res_Look_ExamList> exam_list) {
        this.exam_list = exam_list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Res_Look_Behaviour> getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(ArrayList<Res_Look_Behaviour> behaviour) {
        this.behaviour = behaviour;
    }

    public ArrayList<Res_Look_AbsentReason> getAbsent_reason() {
        return absent_reason;
    }

    public void setAbsent_reason(ArrayList<Res_Look_AbsentReason> absent_reason) {
        this.absent_reason = absent_reason;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", exam_list = " + exam_list + ", status = " + status + ", behaviour = " + behaviour + ", absent_reason = " + absent_reason + "]";
    }
}
