package com.shamlatech.api_response;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ADMIN on 08-Jun-18.
 */

public class Res_Stud_Fees implements Serializable {
    private String balance;

    private ArrayList<Res_Stud_Fees_Details> detailed_fees;

    private String status;

    private String paid_amount;

    private String report_download_link;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public ArrayList<Res_Stud_Fees_Details> getDetailed_fees() {
        return detailed_fees;
    }

    public void setDetailed_fees(ArrayList<Res_Stud_Fees_Details> detailed_fees) {
        this.detailed_fees = detailed_fees;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }

    public String getReport_download_link() {
        return report_download_link;
    }

    public void setReport_download_link(String report_download_link) {
        this.report_download_link = report_download_link;
    }

    @Override
    public String toString() {
        return "ClassPojo [balance = " + balance + ", detailed_fees = " + detailed_fees + ", status = " + status + ", paid_amount = " + paid_amount + "]";
    }
}
