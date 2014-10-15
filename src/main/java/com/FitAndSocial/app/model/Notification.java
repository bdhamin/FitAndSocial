package com.FitAndSocial.app.model;

import java.util.Date;

/**
 * Created by mint on 12-10-14.
 */
public class Notification {

    private int id;
    private String userId;
    private String title;
    private String message;
    private long date;

    public Notification(){}

    public Notification(int id, String userId, String title, String message, long date){
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(long date){this.date = date;}

    public long getDate(){return date;}

}
