package com.mtuity.loginintegration.googleplus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by creatives on 29/1/16.
 */
public class GPlusProfilePicAsyncTask extends AsyncTask<String, Void, Bitmap> {


    private final GPlusUserDetailsPojo pojo;
    private GPlusResponse gPlusResponse = null;

    public GPlusProfilePicAsyncTask(GPlusUserDetailsPojo userPojo, GPlusResponse gPlusRes) {
        pojo = userPojo;
        gPlusResponse = gPlusRes;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap profilePic = null;
        try {
            URL downloadURL = new URL(params[GPlusConstants.ZERO]);
            HttpURLConnection conn = (HttpURLConnection) downloadURL
                    .openConnection();
            int responseCode = conn.getResponseCode();
            if (responseCode != GPlusConstants.TWO_HUNDRED)
                throw new ConnectException("Error in connection");
            InputStream is = conn.getInputStream();
            profilePic = BitmapFactory.decodeStream(is);
        } catch (IOException | IllegalStateException e) {
            Log.i("IOException or IllegalstateException", "Error in streaming the user details" + e);
        }
        return profilePic;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null && pojo != null && gPlusResponse != null) {
            pojo.setUserProfileImage(result);
            gPlusResponse.isGPlusSignInSuccess(true, pojo);

        }

    }

}