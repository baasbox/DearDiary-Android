package com.baasbox.deardiary.model;


/**
 * Created by Andrea Tortorella on 24/01/14.
 */
public class Note {

    private String mId;
    private String mTitle;
    private String mNote;

    public Note(){}

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

}
