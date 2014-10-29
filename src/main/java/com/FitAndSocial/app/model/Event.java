package com.FitAndSocial.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mint on 19-9-14.
 */
public class Event implements Parcelable{

    private String title;
    private String type;
    private int durationMin;
    private int durationMax;
    private int distance;
    private long activityDate;
    private long activityTime;
    private String user;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(type);
        dest.writeInt(durationMin);
        dest.writeInt(durationMax);
        dest.writeInt(distance);
        dest.writeLong(activityDate);
        dest.writeLong(activityTime);
        dest.writeString(user);
        dest.writeLong(startLocationLatitude);
        dest.writeLong(startLocationMagnitude);
        dest.writeLong(endLocationLatitude);
        dest.writeLong(endLocationMagnitude);
    }

    private void readFromParcel(Parcel in){
        title = in.readString();
        type = in.readString();
        durationMin = in.readInt();
        durationMax = in.readInt();
        distance = in.readInt();
        activityDate = in.readLong();
        activityTime = in.readLong();
        user = in.readString();
        startLocationLatitude = in.readLong();
        startLocationMagnitude = in.readLong();
        endLocationLatitude = in.readLong();
        endLocationMagnitude = in.readLong();
    }

    public final static Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            Event event = new Event();
            event.title = source.readString();
            event.type = source.readString();
            event.durationMin = source.readInt();
            event.durationMax = source.readInt();
            event.distance = source.readInt();
            event.activityDate = source.readLong();
            event.activityTime = source.readLong();
            event.user = source.readString();
            event.startLocationLatitude = source.readLong();
            event.startLocationMagnitude = source.readLong();
            event.endLocationLatitude = source.readLong();
            event.endLocationMagnitude = source.readLong();
            return event;
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[0];
        }
    };

}
