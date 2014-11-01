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
    private double startLocationLatitude;
    private double startLocationMagnitude;
    private double endLocationLatitude;
    private double endLocationMagnitude;


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

    public double getStartLocationLatitude() {
        return startLocationLatitude;
    }

    public void setStartLocationLatitude(double startLocationLatitude) {
        this.startLocationLatitude = startLocationLatitude;
    }

    public double getStartLocationMagnitude() {
        return startLocationMagnitude;
    }

    public void setStartLocationMagnitude(double startLocationMagnitude) {
        this.startLocationMagnitude = startLocationMagnitude;
    }

    public double getEndLocationLatitude() {
        return endLocationLatitude;
    }

    public void setEndLocationLatitude(double endLocationLatitude) {
        this.endLocationLatitude = endLocationLatitude;
    }

    public double getEndLocationMagnitude() {
        return endLocationMagnitude;
    }

    public void setEndLocationMagnitude(double endLocationMagnitude) {
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
        dest.writeDouble(getStartLocationLatitude());
        dest.writeDouble(getStartLocationMagnitude());
        dest.writeDouble(getEndLocationLatitude());
        dest.writeDouble(getEndLocationMagnitude());
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
        setStartLocationLatitude(in.readDouble());
        setStartLocationMagnitude(in.readDouble());
        setEndLocationLatitude(in.readDouble());
        setEndLocationMagnitude(in.readDouble());
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
            event.setStartLocationLatitude(source.readDouble());
            event.setStartLocationMagnitude(source.readDouble());
            event.setEndLocationLatitude(source.readDouble());
            event.setEndLocationMagnitude(source.readDouble());
            return event;
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[0];
        }
    };

}
