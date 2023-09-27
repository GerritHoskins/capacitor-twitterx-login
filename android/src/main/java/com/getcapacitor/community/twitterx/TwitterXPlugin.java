package com.getcapacitor.community.twitterx;

import android.content.Intent;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.TokenResponse;
import net.openid.appauth.AuthState;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CapacitorPlugin(name = "TwitterX")
public class TwitterXPlugin extends Plugin {

    public static final String LOG_TAG = "twitterX ";

    private AuthState authState;

    @PluginMethod
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
                    getConfig().getString("clientId"),
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

    @PluginMethod
    public void logout(final PluginCall call) {
        String idToken = authState.getIdToken();
        Uri issuer = authState.getAuthorizationServiceConfiguration().discoveryDoc.getIssuer();
        
        if (idToken == null || issuer == null) {
            call.reject("Error forming logout URL or no idToken available");
            return;
        }

        String logoutUrl = issuer.toString() + "/v1/logout?id_token_hint=" + idToken;

        authState = null;

        // TODO: clear sensitive information in SharedPreferences

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(logoutUrl));
        startActivity(browserIntent);
        
        call.resolve(new JSObject());
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
                               String accessToken = tokenResponse.accessToken;
                               JSObject ret = new JSObject();
                               ret.put("accessToken", accessToken);
                               ret.put("userName", "");
                               ret.put("userId", "");
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
