package com.FitAndSocial.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mint on 19-9-14.
 */
public class Event{

    private String title;
    private String type;
    private int durationMin;
    private int durationMax;
    private int distance;
    private long activityDate;
    private long activityTime;
    private long user;
    private long startLocationLatitude;
    private long startLocationMagnitude;
    private long endLocationLatitude;
    private long endLocationMagnitude;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public long getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(long activityDate) {
        this.activityDate = activityDate;
    }

    public long getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(long activityTime) {
        this.activityTime = activityTime;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getStartLocationLatitude() {
        return startLocationLatitude;
    }

    public void setStartLocationLatitude(long startLocationLatitude) {
        this.startLocationLatitude = startLocationLatitude;
    }

    public long getStartLocationMagnitude() {
        return startLocationMagnitude;
    }

    public void setStartLocationMagnitude(long startLocationMagnitude) {
        this.startLocationMagnitude = startLocationMagnitude;
    }

    public long getEndLocationLatitude() {
        return endLocationLatitude;
    }

    public void setEndLocationLatitude(long endLocationLatitude) {
        this.endLocationLatitude = endLocationLatitude;
    }

    public long getEndLocationMagnitude() {
        return endLocationMagnitude;
    }

    public void setEndLocationMagnitude(long endLocationMagnitude) {
        this.endLocationMagnitude = endLocationMagnitude;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public int getDurationMax() {
        return durationMax;
    }

    public void setDurationMax(int durationMax) {
        this.durationMax = durationMax;
    }
}
