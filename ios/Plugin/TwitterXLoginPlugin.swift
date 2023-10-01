import Foundation
import Capacitor
import AppAuth

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(TwitterXLoginPlugin)
public class TwitterXLoginPlugin: CAPPlugin {
    let LOG_TAG = "TwitterXLoginPlugin "
    let AUTHORIZATION_ENDPOINT = "https://twitter.com/i/oauth2/authorize"
    let TOKEN_ENDPOINT = "https://api.twitter.com/2/oauth2/token"
    var authState: OIDAuthState? = nil
    var authService: OIDAuthorizationService? = nil
    var config: OIDServiceConfiguration? = nil
    var currentAuthorizationFlow: OIDExternalUserAgentSession?
    var request: OIDAuthorizationRequest?
    var accessToken: String? = nil
    var refreshToken: String? = nil

    @objc func login(_ call: CAPPluginCall) {
        guard let clientId = getConfig().getString("clientId") else { return }
        guard let redirectUri = getConfig().getString("redirectUri") else { return }
        let scopes: [String]? = getConfig().getString("scope") != nil ? [getConfig().getString("scope")!] : nil

        self.config = OIDServiceConfiguration(
            authorizationEndpoint: URL(string: "https://api.twitter.com/oauth/authorize")!,
            tokenEndpoint: URL(string: "https://api.twitter.com/oauth/access_token")!
        )
        if(self.config != nil) {
            request = OIDAuthorizationRequest(
                configuration: self.config!,
                clientId: clientId,
                clientSecret: nil,
                scopes: scopes,
                redirectURL: URL(string: redirectUri)!,
                responseType: OIDResponseTypeCode,
                additionalParameters: nil
            )
        }

        if (self.authState != nil && self.refreshToken != nil) {
            guard let response = self.authState?.lastTokenResponse as? OIDAuthorizationResponse else {
                return
            }
            self.performTokenRequest(call, authorizationResponse: response);
        } else {
            currentAuthorizationFlow = OIDAuthState.authState(byPresenting: request!, presenting: (self.bridge?.viewController)!) { authState, error in

                if let authState = authState {
                    let authToken = authState.lastTokenResponse?.accessToken
                    self.refreshToken = authState.lastTokenResponse?.refreshToken
                    call.resolve([
                        "accessToken": authToken ?? "",
                        "userName": authToken ?? "",
                        "userId": authToken ?? ""
                    ])
                } else {
                    call.reject("Authorization error: \(error?.localizedDescription ?? "Unknown error")")
                }

            }
        }
    }

    func performTokenRequest(_ call: CAPPluginCall, authorizationResponse: OIDAuthorizationResponse) {
        let tokenExchangeRequest = OIDTokenRequest(
            configuration: authorizationResponse.request.configuration,
            grantType: OIDGrantTypeAuthorizationCode,
            authorizationCode: authorizationResponse.authorizationCode,
            redirectURL: authorizationResponse.request.redirectURL,
            clientID: authorizationResponse.request.clientID,
            clientSecret: authorizationResponse.request.clientSecret,
            scopes: nil,
            refreshToken: nil,
            codeVerifier: nil,
            additionalParameters: nil
        )

        OIDAuthorizationService.perform(tokenExchangeRequest) { response, error in
            if let response = response {
                self.authState = OIDAuthState(authorizationResponse: authorizationResponse, tokenResponse: response)
                call.resolve([
                    "accessToken": response.accessToken ?? "",
                    "idToken": response.idToken ?? "",
                    "refreshToken": response.refreshToken ?? ""
                ])
            } else {
                call.reject("Token exchange error: \(error?.localizedDescription ?? "Unknown error")")
            }
        }
    }

    @objc func logout(_ call: CAPPluginCall) {
        let idToken = self.authState?.lastTokenResponse
        let issuer = self.config?.discoveryDocument?.issuer

        guard  (idToken != nil), (issuer != nil) else {
            call.reject("(self.LOG_TAG)Error forming logout URL or no idToken available")
            return
        }

        let logoutUrl = "(issuer)/v1/logout?id_token_hint=(idToken)"

        self.authState = nil

        // TODO: clear sensitive information in UserDefaults

        if let url = URL(string: logoutUrl) {
            UIApplication.shared.open(url)
        }

        call.resolve()
    }
}
