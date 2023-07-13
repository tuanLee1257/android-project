package com.example.project01mvvm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class _3dRenders implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("approved_on")
    @Expose
    private String approvedOn;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(String approvedOn) {
        this.approvedOn = approvedOn;
    }

}
