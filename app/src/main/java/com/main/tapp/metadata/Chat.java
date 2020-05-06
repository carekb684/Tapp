package com.main.tapp.metadata;

import java.util.Date;

public class Chat {

    private String mSender;
    private String mReceiver;
    private String mMessage;
    private Date mTimestamp;

    public String getSender() {
        return mSender;
    }

    public void setSender(String fullname) {
        mSender = fullname;
    }

    public String getReceiver() {
        return mReceiver;
    }

    public void setReceiver(String uid) {
        mReceiver = uid;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String image) {
        mMessage = image;
    }


    public Date getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Date timestamp) {
        mTimestamp = timestamp;
    }
}
