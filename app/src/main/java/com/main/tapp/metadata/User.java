package com.main.tapp.metadata;

public class User {

    private String mFullname;
    private String mUid;
    private String mImage;
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

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getLastMsgDate() {
        return mLastMsgDate;
    }

    public void setLastMsgDate(String lastMsgDate) {
        mLastMsgDate = lastMsgDate;
    }
}
