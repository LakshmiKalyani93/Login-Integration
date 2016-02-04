package com.mtuity.loginintegration.twitter.services;

import android.util.Log;

import com.mtuity.loginintegration.LoginIntegrationApp;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * Created by creatives on 4/2/16.
 */
public class RetrofitErrorHandler implements ErrorHandler {
    private static final String ERROR_TAG = RetrofitErrorHandler.class.getSimpleName();

    public RetrofitErrorHandler(LoginIntegrationApp loginIntegrationApp) {
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        try {
            return cause;
        } catch (Exception e) {
            Log.e(ERROR_TAG, "Error while handling Retrofit error" + e);
            return e;
        }
    }
}
