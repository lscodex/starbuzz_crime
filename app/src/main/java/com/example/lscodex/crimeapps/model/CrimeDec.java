package com.example.lscodex.crimeapps.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lscodex on 1.08.2017.
 */

public class CrimeDec {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;

    //constructor
    public CrimeDec() {
        this(UUID.randomUUID());

    }

    //another constructor
    public CrimeDec(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public String getmSuspect() {
        return mSuspect;
    }

    public void setmSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String tittle) {
        mTitle = tittle;

    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }


}
