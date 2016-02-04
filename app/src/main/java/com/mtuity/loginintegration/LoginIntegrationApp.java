package com.mtuity.loginintegration;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;

/**
 * Created by creatives on 4/2/16.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class LoginIntegrationApp extends Application {
    public static final String TAG = LoginIntegrationApp.class.getSimpleName();


    private static LoginIntegrationApp instance;

    public LoginIntegrationApp() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static LoginIntegrationApp get() {
        return instance;
    }

}
