package com.mtuity.loginintegration.googleplus;

import android.graphics.Bitmap;

import com.google.android.gms.plus.model.people.Person;

/**
 * Created by creatives on 28/1/16.
 */
public class GPlusUserDetailsPojo {


    private String userName;
    private String userTagLine;
    private String userAboutMe;
    private String userBirthday;
    private String userLocation;
    private String userEmail;
    private Bitmap userProfileImage;
    private Person signedInUser;

    public Bitmap getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(Bitmap userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    /**
     * @return userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return userLocation
     */
    public String getUserLocation() {
        return userLocation;
    }

    /**
     * @param userLocation
     */
    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    /**
     * @return userBirthday
     */
    public String getUserBirthday() {
        return userBirthday;
    }

    /**
     * @param userBirthday
     */
    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    /**
     * @return userAboutMe
     */
    public String getUserAboutMe() {
        return userAboutMe;
    }

    /**
     * @param userAboutMe
     */
    public void setUserAboutMe(String userAboutMe) {
        this.userAboutMe = userAboutMe;
    }

    /**
     * @return userTagLine
     */
    public String getUserTagLine() {
        return userTagLine;
    }

    /**
     * @param userTagLine
     */
    public void setUserTagLine(String userTagLine) {
        this.userTagLine = userTagLine;
    }

    /**
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @param signedInUser
     */
    public void setSignedInUser(Person signedInUser) {
        this.signedInUser = signedInUser;
    }

    /**
     * @return signedInUser
     */
    public Person getSignedInUser() {
        return signedInUser;
    }

    @Override
    public String toString() {
        return "Username :" + userName + "  userTagLine : " + userTagLine +
                " userAboutMe : " + userAboutMe +
                "  userBirthday : " + userBirthday +
                "  userLocation: " + userLocation +
                "  userEmail: " + userEmail + " SignedInUser: " + signedInUser;
    }


}
