package com.getcapacitor.community.twitterx;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResult;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenRequest;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CapacitorPlugin(name = "TwitterX")
public class TwitterXPlugin extends Plugin {
    public static final String LOG_TAG = "twitterX ";
    private static final String AUTHORIZATION_ENDPOINT = "https://twitter.com/i/oauth2/authorize";
    private static final String TOKEN_ENDPOINT = "https://api.twitter.com/2/oauth2/token";
    private AuthState authState;
    private AuthorizationService authService;
    private AuthorizationServiceConfiguration config;

    private String accessToken = "";

    @PluginMethod
    public void login(final PluginCall call) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                config = new AuthorizationServiceConfiguration(
                        Uri.parse(AUTHORIZATION_ENDPOINT),
                        Uri.parse(TOKEN_ENDPOINT)
                );

                authState = new AuthState(config);
                authService = new AuthorizationService(getContext());

                if(authState.getNeedsTokenRefresh() && !accessToken.isEmpty()) {
                    performTokenRequest();
                } else {
                    AuthorizationRequest request = new AuthorizationRequest.Builder(
                            config,
                            getConfig().getString("clientId"),
                            ResponseTypeValues.CODE,
                            Uri.parse(getConfig().getString("redirectUri"))
                    )
                            .setScope(getConfig().getString("scope"))
                            .build();

                    //authService = new AuthorizationService(getContext());
                    Intent authIntent = authService.getAuthorizationRequestIntent(request);
                    startActivityForResult(call, authIntent, "handleOauthIntentResult");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                call.reject(LOG_TAG + "Unexpected exception on open browser for authorization request.", exception);
            }
        });
    }

    public void performTokenRequest() {
        String clientId = getConfig().getString("clientId");

        TokenRequest tokenRequest = new TokenRequest.Builder(config, clientId)
                .setGrantType("refresh_token")
                //.setRefreshToken("bWRWa3gzdnk3WHRGU1o0bmRRcTJ5VUxWX1lZTDdJSUtmaWcxbTVxdEFXcW5tOjE2MjIxNDc3NDM5MTQ6MToxOnJ0OjE")
                .setRefreshToken(accessToken)
                .build();

        authService.performTokenRequest(tokenRequest, (tokenResponse, exception) -> {
            if (exception != null) {
                Log.e("AppAuth", "Token Request failed", exception);
            } else if (tokenResponse != null) {
                // Update AuthState with the Token Response
                authState.update(tokenResponse, null);
                accessToken = tokenResponse.accessToken;
            }
        });
    }

    @PluginMethod
    public void logout(final PluginCall call) {
        String idToken = authState.getIdToken();
        Uri issuer = Uri.parse(Objects.requireNonNull(Objects.requireNonNull(authState.getAuthorizationServiceConfiguration()).discoveryDoc).getIssuer());

        if (idToken == null || issuer == null) {
            call.reject("Error forming logout URL or no idToken available");
            return;
        }

        String logoutUrl = issuer.toString() + "/v1/logout?id_token_hint=" + idToken;

        authState = null;

        // TODO: clear sensitive information in SharedPreferences

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(logoutUrl));
        getContext().startActivity(browserIntent);

        call.resolve(new JSObject());
    }

    @ActivityCallback
    private void handleOauthIntentResult(PluginCall call, ActivityResult result) {
        if (result.getResultCode() != 0) {
            Intent data = result.getData();
            if (data != null) {
                AuthorizationResponse response = AuthorizationResponse.fromIntent(data);
                AuthorizationException error = AuthorizationException.fromIntent(data);

                if (response != null) {
                    authService.performTokenRequest(
                            response.createTokenExchangeRequest(),
                            (tokenResponse, exception) -> {

                                if (tokenResponse != null) {
                                    String accessToken = tokenResponse.accessToken;
                                    JSObject ret = new JSObject();
                                    ret.put("accessToken", accessToken);
                                    ret.put("userName", "");
                                    ret.put("userId", "");
                                    call.resolve(ret);
                                } else {
                                    call.reject(LOG_TAG + " Unexpected exception on handling intent result.");
                                }
                            }
                    );
                } else {
                    call.reject(LOG_TAG + " Authorization failed.");
                }
            } else {
                call.reject(LOG_TAG + " Data is null.");
            }
        }
    }
}
