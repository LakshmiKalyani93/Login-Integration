package com.mtuity.loginintegration.googleplus;

import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;
import com.mtuity.loginintegration.MainActivity;


/**
 * Created by creatives on 28/1/16.
 */
public class GPlus implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final android.app.Activity mActivity;

    // contains all possible error codes for when a client fails to connect to
    // Google Play services
    private ConnectionResult mConnectionResult;

    // used to update the flags
    GPlusFlags mFlag = null;
    private GPlusResponse gPlusResponse;

    // For communicating with Google APIs
    private GoogleApiClient mGoogleApiClient;

    public GPlus(android.app.Activity activity, GPlusResponse response) {

        if (activity == null) {
            throw new NullPointerException("Context should not be null");
        } else {
            this.mActivity = activity;
            // Initializing google plus api client
            mGoogleApiClient = buildGoogleAPIClient();
            if (response != null) {
                this.gPlusResponse = response;
            }
            mFlag = new GPlusFlags();
        }
    }

    /**
     * API to return GoogleApiClient Make sure to create new after revoking
     * access or for first time sign in
     *
     * @return GoogleApiClient
     */
    public GoogleApiClient buildGoogleAPIClient() {


        return new GoogleApiClient.Builder(mActivity).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    public void requestConnection() {
        // make sure to initiate connection
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void disConnect() {
        // disconnect api if it is connected
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
        mGoogleApiClient.disconnect();
    }


    /**
     * API to handle sign out of user
     */
    public void gPlusSignOut() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            gPlusResponse.isGPlusConnected(false);
            gPlusResponse.isGPlusSignInSuccess(false, null);
        }

    }

    /**
     * API to handler sign in of user If error occurs while connecting process
     * it in processSignInError() api
     */
    public void gPlusSignIn() {

        if (!mGoogleApiClient.isConnecting() && mFlag != null) {
            processSignInError();
            mFlag.setmSignInClicked(true);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mFlag != null && mActivity != null) {

            if (mFlag.ismSignInClicked()) {
                Toast.makeText(mActivity, "Signed In Successfully",
                        Toast.LENGTH_LONG).show();
                gPlusResponse.isGPlusConnected(true);
            }
        }


        new GPlusContacts(mGoogleApiClient).getContactInfo();
        new GPlusProcessUserInfo(mGoogleApiClient, gPlusResponse).getUserInfo();

    }


    @Override
    public void onConnectionSuspended(int i) {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution() && mActivity != null) {
            Toast.makeText(mActivity, "There was a problem in connecting to the server..",
                    Toast.LENGTH_LONG).show();
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), mActivity,
                    GPlusConstants.ERROR_DIALOG_REQUEST_CODE).show();
            return;
        }

        if (mFlag != null && !mFlag.ismIntentInProgress()) {
            mConnectionResult = connectionResult;

            if (mFlag.ismSignInClicked()) {
                processSignInError();
            }
        }
    }


    /**
     * API to process sign in error Handle error based on ConnectionResult
     */
    private void processSignInError() {
        if (mConnectionResult != null && mFlag != null && mActivity != null && mConnectionResult.hasResolution()) {
            try {
                mFlag.setmIntentInProgress(true);
                mConnectionResult.startResolutionForResult(mActivity,
                        GPlusConstants.SIGN_IN_REQUEST_CODE);
                gPlusResponse.updateGPlusGlags(mFlag, mGoogleApiClient);
            } catch (IntentSender.SendIntentException e) {
                mFlag.setmIntentInProgress(false);
                mGoogleApiClient.connect();
            }
        }
    }

    /**
     * API to revoke granted access After revoke user will be asked to grant
     * permission on next sign in
     */
    public void processRevokeRequest() {

        if (mGoogleApiClient.isConnected() && mActivity != null && gPlusResponse != null) {

            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback() {
                        @Override
                        public void onResult(Result result) {
                            Toast.makeText(mActivity,
                                    "User permissions are revoked",
                                    Toast.LENGTH_LONG).show();
                            gPlusResponse.isRevokeAccessAndDisconnect(false);
                            mGoogleApiClient = buildGoogleAPIClient();
                            mGoogleApiClient.connect();
                        }

                    });

        }
    }

    /**
     * @param title      sets the title
     * @param contentUri parses the uri content
     */
    public void gPlusSharePost(String title, String contentUri) {
        // Launch the Google+ share dialog with attribution to your app.
        if (mActivity != null && mActivity instanceof MainActivity) {
            Intent shareIntent = new PlusShare.Builder(mActivity)
                    .setType("text/plain")
                    .setText(title)
                    .setContentUrl(Uri.parse(contentUri))
                    .getIntent();

            mActivity.startActivityForResult(shareIntent, 0);
        }


    }

    /**
     * Picks the available image files from the mobile device
     */
    public void gPlusShareImage() {
        if (mActivity != null && mActivity instanceof MainActivity) {
            Intent photoPicker = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPicker.setType("image/*");
            mActivity.startActivityForResult(photoPicker, GPlusConstants.PICK_MEDIA_REQUEST_CODE);
        }

    }

    /**
     * Picks available video files from the mobile device
     */
    public void gPlusShareVideo() {
        if (mActivity != null && mActivity instanceof MainActivity) {
            Intent photoPicker = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPicker.setType("video/*");
            mActivity.startActivityForResult(photoPicker, GPlusConstants.PICK_MEDIA_REQUEST_CODE);
        }

    }



    /**
     * @param selectedFile        the selected media either video or image
     * @param mime                selected media uri
     * @param postTitle           title of the post
     * @param currentSignedInUser the gplus current user
     */
    public void gPlusShareMedia(Uri selectedFile, String mime, String postTitle, Person currentSignedInUser) {

        if (mActivity != null && mActivity instanceof MainActivity) {

            PlusShare.Builder share = new PlusShare.Builder(mActivity);
            share.setText(postTitle);
            if (currentSignedInUser != null) {
                share.setRecipients(currentSignedInUser, null);
            }
            share.addStream(selectedFile);
            share.setType(mime);
            mActivity.startActivityForResult(share.getIntent(), GPlusConstants.SHARE_MEDIA_REQUEST_CODE);
        }
    }

}
