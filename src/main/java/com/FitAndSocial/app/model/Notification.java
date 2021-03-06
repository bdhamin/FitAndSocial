package com.FitAndSocial.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by mint on 12-10-14.
 */
@DatabaseTable(tableName = "userNotification")
public class Notification {

    @DatabaseField(generatedId = true, canBeNull = false)
    private int id;
    @DatabaseField(columnName = "authCode", canBeNull = false)
    private String userId;
    @DatabaseField
    private String title;
    @DatabaseField
    private String message;
    @DatabaseField
    private long date;
    @DatabaseField
    private int isRead;

    public Notification(){}

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

    public int isRead() {
        return isRead;
    }

    public void setRead(int isRead) {
        this.isRead = isRead;
    }
}
