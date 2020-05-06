package com.main.tapp.metadata;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;

public class User {

    private String mFullname;
    private String mUid;
    private RoundedBitmapDrawable mBImage;
    private String mLastMsgDate;

    public String getFullname() {
        return mFullname;
    }

    public void setFullname(String fullname) {
        mFullname = fullname;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public RoundedBitmapDrawable getImage() {
        return mBImage;
    }

    public void setImage(RoundedBitmapDrawable image) {
        mBImage = image;
    }

    public String getLastMsgDate() {
        return mLastMsgDate;
    }

    public void setLastMsgDate(String lastMsgDate) {
        mLastMsgDate = lastMsgDate;
    }

    public User uid(String uid) {
        mUid = uid;
        return this;
    }
}
