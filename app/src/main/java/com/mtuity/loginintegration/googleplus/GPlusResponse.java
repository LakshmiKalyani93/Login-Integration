package com.mtuity.loginintegration.googleplus;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by creatives on 28/1/16.
 */
public interface GPlusResponse {

    void isGPlusSignInSuccess(boolean isSuccess, GPlusUserDetailsPojo userDetailsPojo);

    void isRevokeAccessAndDisconnect(boolean isRevoked);

    void isGPlusConnected(boolean isConnected);


    void updateGPlusGlags(GPlusFlags mFlag, GoogleApiClient mGoogleApiClient);
}
