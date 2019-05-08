package com.nvcreation.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private Date mTime;
    private boolean mSolved;
    private String mSuspect;
    private String mSuspectPhone;
    private boolean mRequiresPolice; //Chapter 8 - Challenge 1

    public Crime(){
        this(UUID.randomUUID());
        /* Commented in Chapter 14 - Databases
        mId = UUID.randomUUID();
        mDate = new Date();
        mTime = new Date();*/
    }
    public Crime(UUID id){
        mId = id;
        mDate = new Date();
    }


    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    // Chapter 8 - Challenge 1

    public boolean ismRequiresPolice() {
        return mRequiresPolice;
    }

    public void setmRequiresPolice(boolean mRequiresPolice) {
        this.mRequiresPolice = mRequiresPolice;
    }

    public Date getmTime() {
        return mTime;
    }

    public void setmTime(Date mTime) {
        this.mTime = mTime;
    }


    public String getmSuspect() {
        return mSuspect;
    }

    public void setmSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    public String getmSuspectPhone() {
        return mSuspectPhone;
    }

    public void setmSuspectPhone(String mSuspectPhone) {
        this.mSuspectPhone = mSuspectPhone;
    }

    public String getPhotoFilename() {
        return "IMG_" + getmId().toString() + ".jpg";
    }
}
