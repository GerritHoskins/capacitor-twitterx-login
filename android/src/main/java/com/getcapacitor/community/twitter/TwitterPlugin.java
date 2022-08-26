package com.getcapacitor.community.twitter;
import static com.twitter.sdk.android.core.TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE;

import android.content.Intent;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

@CapacitorPlugin(name = "Twitter", requestCodes = { DEFAULT_AUTH_REQUEST_CODE })
public class TwitterPlugin extends Plugin {
    private TwitterAuthClient authClient;
    private TwitterInstance twitterInstance = null;

    @Override()
    public void load() {
        twitterInstance = new TwitterInstance(getContext(), getActivity());
        authClient = twitterInstance.authClient;
        super.load();
    }

    @PluginMethod()
    public void login(final PluginCall call) {

        if(twitterInstance == null) {
            twitterInstance = new TwitterInstance(getContext(), getActivity());
        }

        twitterInstance.authClient.authorize(getActivity(), new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                JSObject ret = new JSObject();
                ret.put("authToken", result.data.getAuthToken().token);
                ret.put("authTokenSecret", result.data.getAuthToken().secret);
                ret.put("userName", result.data.getUserName());
                ret.put("userID", result.data.getUserId());
                call.resolve(ret);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("DEBUG", "OH NO!! THERE WAS AN ERROR");
                call.reject("error", exception);
            }
        });
    }

    @PluginMethod()
    public void logout(PluginCall call) {
        if(twitterInstance == null) {
            twitterInstance = new TwitterInstance(getContext(), getActivity());
        }
        twitterInstance.authClient.cancelAuthorize();
        SessionManager sessionManager = TwitterCore.getInstance().getSessionManager();
        sessionManager.clearActiveSession();
        call.resolve();
    }

    @PluginMethod()
    public void isLogged(PluginCall call) {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        JSObject ret = new JSObject();

        if (session != null) {
            TwitterAuthToken authToken = session.getAuthToken();

            String token = authToken.token;
            String secret = authToken.secret;

            ret.put("in", true);
            ret.put("authToken", token);
            ret.put("authTokenSecret", secret);
        } else {
            ret.put("in", false);
        }

        call.resolve(ret);
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {

        if(twitterInstance == null) {
            twitterInstance = new TwitterInstance(getContext(), getActivity());
        }

        if (requestCode == 140) {
            twitterInstance.authClient.onActivityResult(requestCode, resultCode, data);
        } else {
            super.handleOnActivityResult(requestCode, resultCode, data);
        }
    }
}
