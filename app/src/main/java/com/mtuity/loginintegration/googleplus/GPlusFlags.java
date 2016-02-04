package com.mtuity.loginintegration.googleplus;

/**
 * Created by creatives on 28/1/16.
 */
public class GPlusFlags {
    private boolean mSignInClicked;
    private boolean mIntentInProgress;

    public void setmIntentInProgress(boolean mIntentInProgress) {
        this.mIntentInProgress = mIntentInProgress;
    }

    public boolean ismSignInClicked() {
        return mSignInClicked;
    }

    public void setmSignInClicked(boolean mSignInClicked) {
        this.mSignInClicked = mSignInClicked;
    }


    public boolean ismIntentInProgress() {
        return mIntentInProgress;
    }
}
