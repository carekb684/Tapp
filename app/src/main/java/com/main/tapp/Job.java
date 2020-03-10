package com.main.tapp;

import java.io.Serializable;

public class Job implements Serializable {
    private Integer mId;
    private String mTitle;
    private String mDescription;
    private Double mEstTime;
    private Integer mSalary;
    private String mDate;
    private String mDateTime;
    private String mCreatedDate;
    private String mCreatedByUID;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Job title(String title) {
        mTitle = title;
        return this;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public Job description(String description) {
        mDescription = description;
        return this;
    }

    public Double getEstTime() {
        return mEstTime;
    }

    public void setEstTime(Double estTime) {
        this.mEstTime = estTime;
    }

    public Job estTime(Double estTime) {
        mEstTime = estTime;
        return this;
    }

    public Integer getSalary() {
        return mSalary;
    }

    public void setSalary(Integer salary) {
        this.mSalary = salary;
    }

    public Job salary(Integer salary) {
        mSalary = salary;
        return this;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public Job date(String date) {
        mDate = date;
        return this;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String dateTime) {
        this.mDateTime = dateTime;
    }

    public Job dateTime(String dateTime) {
        mDateTime = dateTime;
        return this;
    }

    public String getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        this.mCreatedDate = createdDate;
    }

    public Job createdDate(String createdDate) {
        mCreatedDate = createdDate;
        return this;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public Job id(Integer id) {
        mId = id;
        return this;
    }

    public String getCreatedByUID() {
        return mCreatedByUID;
    }

    public void setCreatedByUID(String createdByUID) {
        mCreatedByUID = createdByUID;
    }

    public Job createdByUID(String createdByUID) {
        mCreatedByUID = createdByUID;
        return this;
    }
}
