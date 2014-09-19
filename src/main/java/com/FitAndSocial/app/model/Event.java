package com.FitAndSocial.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mint on 19-9-14.
 */
public class Event{

    private String activityTypeName;
    private int distance;
    private int duration;
    private String date;
    private String time;
    private long userId;
    private long startLocationLatitude;
    private long startLocationMagnitude;
    private long endLocationLatitude;
    private long endLocationMagnitude;

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

}
