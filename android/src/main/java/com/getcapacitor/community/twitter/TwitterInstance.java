package com.getcapacitor.community.twitter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import  com.twitter.sdk.android.core.Twitter;

public class TwitterInstance extends Application {

    public TwitterAuthClient authClient;
    public TwitterConfig config;
    private String consumerKey = "oDIRqTkT1NaNjwXxqzFrxSWFh";
    private String consumerSecret = "B2L4Oc4bdSbS9YMdqdI192oSj542meqSbNGZS9vzQQgdCAHwyR";

    public Activity activity;
    public Context context;

    TwitterInstance(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.config = new TwitterConfig.Builder(activity)
            .logger(new DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(new TwitterAuthConfig(consumerKey, consumerSecret))
            .debug(true)
            .build();

        Twitter.initialize(config);
        this.authClient = new TwitterAuthClient();
    }
}
