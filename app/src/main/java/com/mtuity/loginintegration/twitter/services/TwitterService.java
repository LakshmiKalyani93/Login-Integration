package com.mtuity.loginintegration.twitter.services;

import com.mtuity.loginintegration.LoginIntegrationApp;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by creatives on 4/2/16.
 */
public class TwitterService {

    private static final String BASE_SERVER_URL = "https://api.twitter.com/1.1/";
    private TwitterApi twitterApi;

    private TwitterService() {
        //add constructor..
    }

    public void TwitterService() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_SERVER_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL) //NONE for no logs
                .setErrorHandler(new RetrofitErrorHandler(LoginIntegrationApp.get()))
                .build();

        twitterApi = restAdapter.create(TwitterApi.class);
    }

    public TwitterApi getApi() {
        if (twitterApi == null) {
            TwitterService();
        }
        return twitterApi;
    }
}
