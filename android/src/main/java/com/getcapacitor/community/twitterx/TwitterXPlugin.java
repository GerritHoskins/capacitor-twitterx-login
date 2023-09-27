package com.getcapacitor.community.twitterx;

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
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CapacitorPlugin(name = "TwitterX", requestCodes = { TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE })
public class TwitterXPlugin extends Plugin {

    public static final String LOG_TAG = "twitterX ";

    private TwitterXInstance twitterInstance = null;

    @Override()
    public void load() {
        twitterInstance = new TwitterInstance(getContext(), getActivity(), getConfig());
        super.load();
    }

    @PluginMethod()
    public void login(final PluginCall call) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
             AuthorizationServiceConfiguration config = new AuthorizationServiceConfiguration(
                    new Uri("https://api.twitter.com/oauth/authorize"),
                    new Uri("https://api.twitter.com/oauth/token")
                );

                AuthorizationRequest request = new AuthorizationRequest.Builder(
                    config,
                    getConfig().getString("consumerKey"),
                    ResponseTypeValues.CODE,
                    new Uri(getConfig().getString("redirectUri"))
                )
                    .setScope(getConfig().getString("scope"))
                    .build();

                AuthorizationService authService = new AuthorizationService(getContext());
                Intent authIntent = authService.getAuthorizationRequestIntent(request);
                startActivityForResult(call, authIntent, "handleOauthIntentResult");
            } catch (Exception exception) {
                exception.printStackTrace();
                call.reject(LOG_TAG + "Unexpected exception on open browser for authorization request.", exception);
            }
        });
    }

    @PluginMethod()
    public void logout(PluginCall call) {
        getInstance().authClient.cancelAuthorize();
        SessionManager<TwitterSession> sessionManager = TwitterCore.getInstance().getSessionManager();
        sessionManager.clearActiveSession();
        call.resolve();
    }

    @ActivityCallback
    private void handleOauthIntentResult(PluginCall call, ActivityResult result) {
        if (requestCode == AUTH_REQUEST_CODE) {
           AuthorizationResponse response = AuthorizationResponse.fromIntent(data);
           AuthorizationException error = AuthorizationException.fromIntent(data);

           if (response != null) {
               // Authorization successful, exchange authorization code for access token
               authService.performTokenRequest(
                   response.createTokenExchangeRequest(),
                   new AuthorizationService.TokenResponseCallback() {
                       @Override
                       public void onTokenRequestCompleted(
                           TokenResponse tokenResponse, AuthorizationException exception) {

                           if (tokenResponse != null) {
                               // Access token retrieved successfully
                               String accessToken = tokenResponse.accessToken;
                               JSObject ret = new JSObject();
                               ret.put("authToken", accessToken);
                               call.resolve();
                           } else {
                              call.reject(LOG_TAG + "Unexpected exception on handling intent result.")
                           }
                       }
                   }
               );
           } else {
               call.reject(LOG_TAG + "Authorization failed.")
           }
       }
    }
}
