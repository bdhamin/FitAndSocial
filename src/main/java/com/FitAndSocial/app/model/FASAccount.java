package com.FitAndSocial.app.model;

/**
 * Created by mint on 1-10-14.
 */
public class FASAccount {

    private String providerKey;
    private String providerType;
    public int age;
    public String gender;
    public String firstName;
    public String lastName;
    public String userEmail;
    private String details;
    private String nickname;
    private String activitiesOfInterest;

    public FASAccount(){}


    /**
     * Authentication Model
     */
    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    /**
     * Personal Information Model
     */
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * General User Information Model
     */
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getActivitiesOfInterest() {
        return activitiesOfInterest;
    }

    public void setActivitiesOfInterest(String activitiesOfInterest) {
        this.activitiesOfInterest = activitiesOfInterest;
    }
}
