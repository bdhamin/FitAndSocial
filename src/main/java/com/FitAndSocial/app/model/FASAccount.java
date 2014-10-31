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
    private byte[] imageBytes;

    public FASAccount(){}

    public FASAccount(String providerKey, String providerType, int age, String gender, String firstName, String lastName,
                      String userEmail, String details, String nickname, String activitiesOfInterest, byte[] imageBytes){
        this.providerKey = providerKey;
        this.providerType = providerType;
        this.age = age;
        this.gender = gender;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.details = details;
        this.nickname = nickname;
        this.activitiesOfInterest = activitiesOfInterest;
        this.imageBytes = imageBytes;
    }

    public FASAccount(Parcel account){
        readFromParcel(account);
    }


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

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
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
        dest.writeInt(imageBytes.length);
        dest.writeByteArray(imageBytes);
    }


    private void readFromParcel(Parcel in){
        this.providerKey = in.readString();
        this.providerType = in.readString();
        this.age = in.readInt();
        this.gender = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.userEmail = in.readString();
        this.details = in.readString();
        this.nickname = in.readString();
        this.activitiesOfInterest = in.readString();
        this.imageBytes = new byte[in.readInt()];
        in.readByteArray(imageBytes);
    }


    public static final Creator<FASAccount> CREATOR = new Creator<FASAccount>() {
        @Override
        public FASAccount createFromParcel(Parcel source) {
            return new FASAccount(source);
        }

        @Override
        public FASAccount[] newArray(int size) {
            return new FASAccount[0];
        }
    };


}
