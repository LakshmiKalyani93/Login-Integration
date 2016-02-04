package com.mtuity.loginintegration;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.model.people.Person;
import com.mtuity.loginintegration.googleplus.GPlus;
import com.mtuity.loginintegration.googleplus.GPlusConstants;
import com.mtuity.loginintegration.googleplus.GPlusFlags;
import com.mtuity.loginintegration.googleplus.GPlusResponse;
import com.mtuity.loginintegration.googleplus.GPlusUserDetailsPojo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    // layout for showing user control buttons
    private LinearLayout userOptionsLayout;
    // layout for showing signed in user info
    private LinearLayout userInfoLayout;

    private ImageView userProfilePic;
    private TextView userName;
    private TextView userEmail;
    private TextView userLocation;
    private TextView userTagLine;
    private TextView userAboutMe;
    private TextView userBirthday;

    Button signOutButton;
    Button userInfoButton;
    Button revokeAccessButton;
    private GPlus gplus = null;
    private GPlusFlags mGPlusFlags;
    private GoogleApiClient mGPlusClient;
    private Button sharePostBtn;
    private Button attachImageBtn;
    private Person currentSignedInUser;
    private Button attachVideoBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_plus_layout);
        initialiseViews();
        registerClicks();

        gplus = new GPlus(MainActivity.this, new GPlusResponse() {

            @Override
            public void isGPlusSignInSuccess(boolean isSuccess, GPlusUserDetailsPojo userDetailsPojo) {
                if (isSuccess && userDetailsPojo != null) {
                    Log.i("UserDetails", "UserDetails========================================= " + userDetailsPojo.toString());
                    currentSignedInUser = userDetailsPojo.getSignedInUser();
                    userProfilePic.setImageBitmap(userDetailsPojo.getUserProfileImage());
                    userName.setText(userDetailsPojo.getUserName());
                    userEmail.setText(userDetailsPojo.getUserEmail());
                    userLocation.setText(userDetailsPojo.getUserLocation());
                    userAboutMe.setText(userDetailsPojo.getUserAboutMe());
                    userTagLine.setText(userDetailsPojo.getUserTagLine());
                    userBirthday.setText(userDetailsPojo.getUserBirthday());
                }
            }

            @Override
            public void isRevokeAccessAndDisconnect(boolean isRevoked) {
                processUIUpdate(isRevoked);
            }

            @Override
            public void isGPlusConnected(boolean isConnected) {
                processUIUpdate(isConnected);
            }

            @Override
            public void updateGPlusGlags(GPlusFlags mFlag, GoogleApiClient mGoogleApiClient) {
                if (mFlag != null && mGoogleApiClient != null) {
                    mGPlusFlags = mFlag;
                    mGPlusClient = mGoogleApiClient;
                }
            }
        });

        gplus.requestConnection();

    }

    /**
     * Registering the click events..
     */
    private void registerClicks() {
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.user_options_button).setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        userInfoButton.setOnClickListener(this);
        revokeAccessButton.setOnClickListener(this);
        sharePostBtn.setOnClickListener(this);
        attachImageBtn.setOnClickListener(this);
        attachVideoBtn.setOnClickListener(this);
    }

    /**
     * Initialise the views
     */
    private void initialiseViews() {
        userOptionsLayout = (LinearLayout) findViewById(R.id.user_options_layout);
        userInfoLayout = (LinearLayout) findViewById(R.id.user_info_layout);

        signOutButton = (Button) findViewById(R.id.sign_out_button);
        userInfoButton = (Button) findViewById(R.id.show_userinfo_button);
        revokeAccessButton = (Button) findViewById(R.id.revoke_access_button);
        sharePostBtn = (Button) findViewById(R.id.share_post_button);
        attachImageBtn = (Button) findViewById(R.id.attach_image_button);
        attachVideoBtn = (Button) findViewById(R.id.attach_video_button);


        userProfilePic = (ImageView) findViewById(R.id.user_profile_pic);
        userName = (TextView) findViewById(R.id.user_name);
        userEmail = (TextView) findViewById(R.id.user_email);
        userLocation = (TextView) findViewById(R.id.user_location);
        userAboutMe = (TextView) findViewById(R.id.user_aboutme);
        userBirthday = (TextView) findViewById(R.id.user_birthday);
        userTagLine = (TextView) findViewById(R.id.user_tagLine);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (gplus != null) {
            gplus.requestConnection();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gplus != null) {
            gplus.disConnect();
        }
    }


    /**
     * Handle Button onCLick Events based upon their view ID
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.sign_in_button:
                if (gplus != null) {
                    gplus.gPlusSignIn();
                }
                break;
            case R.id.sign_out_button:
                if (gplus != null) {
                    gplus.gPlusSignOut();
                }
                break;
            case R.id.show_userinfo_button:
                userOptionsLayout.setVisibility(View.INVISIBLE);
                userInfoLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.revoke_access_button:
                if (gplus != null) {
                    gplus.processRevokeRequest();
                }
                break;
            case R.id.share_post_button:
                if (gplus != null) {
                    gplus.gPlusSharePost("Mtuity", "http://mtuity.com");
                }
                break;
            case R.id.attach_image_button:
                if (gplus != null) {
                    gplus.gPlusShareImage();
                }
                break;
            case R.id.attach_video_button:
                if (gplus != null) {
                    gplus.gPlusShareVideo();
                }
                break;
            case R.id.user_options_button:
                userInfoLayout.setVisibility(View.INVISIBLE);
                userOptionsLayout.setVisibility(View.VISIBLE);
                break;
        }


    }


    /**
     * API to update layout views based upon user signed in status
     * @param isUserSignedIn the logged in user over google plus integration.
     */
    private void processUIUpdate(boolean isUserSignedIn) {
        if (isUserSignedIn) {
            findViewById(R.id.sign_in_button).setEnabled(false);
            signOutButton.setEnabled(true);
            userInfoButton.setEnabled(true);
            revokeAccessButton.setEnabled(true);
            sharePostBtn.setEnabled(true);
            attachImageBtn.setEnabled(true);
            attachVideoBtn.setEnabled(true);

        } else {
            findViewById(R.id.sign_in_button).setEnabled(true);
            signOutButton.setEnabled(false);
            userInfoButton.setEnabled(false);
            revokeAccessButton.setEnabled(false);
            sharePostBtn.setEnabled(false);
            attachImageBtn.setEnabled(false);
            attachVideoBtn.setEnabled(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPlusConstants.SIGN_IN_REQUEST_CODE && mGPlusFlags != null) {
            if (resultCode != RESULT_OK) {
                mGPlusFlags.setmSignInClicked(false);
                //mSignInClicked = false;
            }
            mGPlusFlags.setmIntentInProgress(false);
            //mIntentInProgress = false
            if (mGPlusClient != null && !mGPlusClient.isConnecting()) {
                mGPlusClient.connect();
            }
        } else if (requestCode == GPlusConstants.PICK_MEDIA_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                ContentResolver mContentResolver = this.getContentResolver();
                String mime = mContentResolver.getType(selectedImage);
                if (gplus != null && mGPlusClient != null && mGPlusClient.isConnected()) {
                    gplus.gPlusShareMedia(selectedImage, mime, "Sample Demo Post !!", currentSignedInUser);

                }
            }

        } else if (requestCode == GPlusConstants.SHARE_MEDIA_REQUEST_CODE && data != null) {
            Log.i("SharedMedia", "Shared Media ---------" + data.toString());

        }
    }


}
