package com.example.project01mvvm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TopicSubmissions implements Serializable {

    @SerializedName("business-work")
    @Expose
    private BusinessWork businessWork;

    public BusinessWork getBusinessWork() {
        return businessWork;
    }

    public void setBusinessWork(BusinessWork businessWork) {
        this.businessWork = businessWork;
    }

}
