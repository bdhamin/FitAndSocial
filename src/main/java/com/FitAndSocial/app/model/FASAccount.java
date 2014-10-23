package com.FitAndSocial.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mint on 1-10-14.
 */
public class FASAccount implements Parcelable{

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(providerKey);
        dest.writeString(providerType);
        dest.writeInt(age);
        dest.writeString(gender);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(userEmail);
        dest.writeString(details);
        dest.writeString(nickname);
        dest.writeString(activitiesOfInterest);
    }


    private void readFromParcel(Parcel in){
        providerKey = in.readString();
        providerType = in.readString();
        age = in.readInt();
        gender = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        userEmail = in.readString();
        details = in.readString();
        nickname = in.readString();
        activitiesOfInterest = in.readString();
    }


    public static final Creator<FASAccount> CREATOR = new Creator<FASAccount>() {
        @Override
        public FASAccount createFromParcel(Parcel source) {
            FASAccount account = new FASAccount();
            account.providerKey = source.readString();
            account.providerType = source.readString();
            account.age = source.readInt();
            account.gender = source.readString();
            account.firstName = source.readString();
            account.lastName = source.readString();
            account.userEmail = source.readString();
            account.details = source.readString();
            account.nickname = source.readString();
            account.activitiesOfInterest = source.readString();
            return account;
        }

        @Override
        public FASAccount[] newArray(int size) {
            return new FASAccount[0];
        }
    };

}
