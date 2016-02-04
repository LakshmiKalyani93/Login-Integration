package com.mtuity.loginintegration.googleplus;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by creatives on 29/1/16.
 */
public class GPlusProcessUserInfo {


    private GoogleApiClient gPlusClient = null;
    private GPlusResponse gPlusRes = null;

    public GPlusProcessUserInfo(GoogleApiClient mGoogleApiClient, GPlusResponse gPlusResponse) {

        if (mGoogleApiClient != null && gPlusResponse != null) {
            this.gPlusClient = mGoogleApiClient;
            gPlusRes = gPlusResponse;
        } else {
            throw new NullPointerException("GoogleApiClient or GPlusResponse is null");
        }
    }

    /**
     * API to update signed in user information
     */
    public void getUserInfo() {
        if (gPlusClient != null && gPlusRes != null) {
            Person signedInUser = Plus.PeopleApi.getCurrentPerson(gPlusClient);
            GPlusUserDetailsPojo userPojo = new GPlusUserDetailsPojo();

            if (signedInUser != null) {
                userPojo.setSignedInUser(signedInUser);

                if (signedInUser.hasDisplayName()) {
                    String userName = signedInUser.getDisplayName();
                    userPojo.setUserName(userName);
                }

                if (signedInUser.hasTagline()) {
                    String tagLine = signedInUser.getTagline();
                    userPojo.setUserTagLine(tagLine);
                }

                if (signedInUser.hasAboutMe()) {
                    String aboutMe = signedInUser.getAboutMe();
                    userPojo.setUserAboutMe(aboutMe);
                }

                if (signedInUser.hasBirthday()) {
                    String birthday = signedInUser.getBirthday();
                    userPojo.setUserBirthday(birthday);
                }

                if (signedInUser.hasCurrentLocation()) {
                    String userLocation = signedInUser.getCurrentLocation();
                    userPojo.setUserLocation(userLocation);
                }

                String userEmail = Plus.AccountApi.getAccountName(gPlusClient);
                if (userEmail != null) {
                    userPojo.setUserEmail(userEmail);
                }


                if (signedInUser.hasImage()) {
                    String userProfilePicUrl = signedInUser.getImage().getUrl();
                    // default size is 50x50 in pixels.changes it to desired size
                    int profilePicRequestSize = GPlusConstants.TWO_FIFTY;

                    userProfilePicUrl = userProfilePicUrl.substring(GPlusConstants.ZERO,
                            userProfilePicUrl.length() - GPlusConstants.TWO) + profilePicRequestSize;
                    /**
                     * Background task to download user profile picture
                     */
                    new GPlusProfilePicAsyncTask(userPojo, gPlusRes)
                            .execute(userProfilePicUrl);

                }
            }

        }
    }
}
