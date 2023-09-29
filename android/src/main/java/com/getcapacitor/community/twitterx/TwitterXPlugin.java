package com.getcapacitor.community.twitterx;

import android.content.Intent;
import android.net.Uri;

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

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CapacitorPlugin(name = "TwitterX")
public class TwitterXPlugin extends Plugin {
    public static final String LOG_TAG = "twitterX ";
    private AuthState authState;
    private AuthorizationService authService;

    @PluginMethod
    public void login(final PluginCall call) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                AuthorizationServiceConfiguration config = new AuthorizationServiceConfiguration(
                        Uri.parse("https://api.twitter.com/oauth/authorize"),
                        Uri.parse("https://api.twitter.com/oauth/token")
                );

                AuthorizationRequest request = new AuthorizationRequest.Builder(
                        config,
                        getConfig().getString("clientId"),
                        ResponseTypeValues.CODE,
                        Uri.parse(getConfig().getString("redirectUri"))
                )
                        .setScope(getConfig().getString("scope"))
                        .build();

                authService = new AuthorizationService(getContext());
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
        if (result.getResultCode() == 0) {
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
