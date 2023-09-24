import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.TokenResponse;

public class OAuthClient {
    private final Context context;

     public OAuthClient(Context context) {
        this.context = context;
     }

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
    startActivityForResult(authIntent, AUTH_REQUEST_CODE);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                                // Handle the access token and other required information
                            } else {
                                // Token request failed, handle the error
                            }
                        }
                    }
                );
            } else {
                // Authorization failed, handle the error
            }
        }
    }
}