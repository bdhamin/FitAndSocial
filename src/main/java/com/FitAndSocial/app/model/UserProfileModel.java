package com.FitAndSocial.app.model;

/**
 * Created by mint on 17-10-14.
 */

/**
 * Represents the backend profile class
 */
public class UserProfileModel {

    private String nickname;
    private String activitiesOfInterest;
    private String details;

    public UserProfileModel(){}

    public UserProfileModel(String nickname, String activitiesOfInterest, String details){
        this.nickname = nickname;
        this.activitiesOfInterest = activitiesOfInterest;
        this.details = details;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getActivities() {
        return activitiesOfInterest;
    }

    public void setActivities(String activitiesOfInterest) {
        this.activitiesOfInterest = activitiesOfInterest;
    }

    public String getAbout() {
        return details;
    }

    public void setAbout(String details) {
        this.details = details;
    }
}
