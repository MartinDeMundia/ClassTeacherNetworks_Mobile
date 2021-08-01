package com.shamlatech.api_response;

/**
 * Created by ADMIN on 07-Jun-18.
 */

public class Result_Check_Activation {

    private String message;
    private String status;
    private String otp;


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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
