package com.getcapacitor.community.twitterx;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.getcapacitor.PluginConfig;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import  com.twitter.sdk.android.core.Twitter;

public class TwitterXInstance extends Application {

    public TwitterAuthClient authClient;
    public TwitterConfig config;
    private String consumerKey = "";
    private String consumerSecret = "";

    public Activity activity;
    public Context context;

    TwitterXInstance(Context context, Activity activity, PluginConfig pluginConfig) {
        this.consumerKey = pluginConfig.getString("consumerKey");
        this.consumerSecret = pluginConfig.getString("consumerSecret");
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
