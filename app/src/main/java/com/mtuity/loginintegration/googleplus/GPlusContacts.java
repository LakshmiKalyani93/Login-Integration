package com.mtuity.loginintegration.googleplus;

import android.util.Log;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.PersonBuffer;

/**
 * Created by creatives on 2/2/16.
 */
public class GPlusContacts implements ResultCallback<People.LoadPeopleResult> {

    private GoogleApiClient gPlusContactClient = null;
    public GPlusContacts(GoogleApiClient mGoogleApiClient) {
        if (mGoogleApiClient != null) {
            this.gPlusContactClient = mGoogleApiClient;
        }
    }

    public void getContactInfo() {
        Plus.PeopleApi.loadVisible(gPlusContactClient, null)
                .setResultCallback(this);
    }

    @Override
    public void onResult(People.LoadPeopleResult loadPeopleResult) {
        if (loadPeopleResult.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = loadPeopleResult.getPersonBuffer();
            Log.d("GPlusContacts", "personBuffer==============: " + personBuffer.toString());
            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    Log.d("GPlusContacts", "Display name==============: " + personBuffer.get(i).getDisplayName());
                }
            } finally {
                personBuffer.release();
            }
        } else {
            Log.e("GPlusContacts", "Error requesting people data: " + loadPeopleResult.getStatus());
        }

    }
}
