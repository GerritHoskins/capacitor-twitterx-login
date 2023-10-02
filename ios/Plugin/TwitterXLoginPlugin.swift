import Foundation
import Capacitor
import AppAuth

@objc(TwitterXLoginPlugin)
public class TwitterXLoginPlugin: CAPPlugin {
    let logTag = "TwitterXLoginPlugin"
    let authorizationEndpoint = URL(string: "https://twitter.com/i/oauth2/authorize")
    let tokenEndpoint = URL(string: "https://api.twitter.com/2/oauth2/token")
    var config: OIDServiceConfiguration?
    var authState: OIDAuthState?
    var currentAuthorizationFlow: OIDExternalUserAgentSession?

    @objc func login(_ call: CAPPluginCall) {
        guard let clientId = getConfig().getString("clientId"),
              let redirectUri = getConfig().getString("redirectUri"),
              let authorizationEndpoint = authorizationEndpoint,
              let tokenEndpoint = tokenEndpoint else {
            call.reject("Configuration Error")
            return
        }

        let scopes: [String] = (getConfig().getArray("scope", [""]))?.compactMap { $0 as? String } ?? [""]
        let config = OIDServiceConfiguration(authorizationEndpoint: authorizationEndpoint, tokenEndpoint: tokenEndpoint)

        let request = OIDAuthorizationRequest(
            configuration: config,
            clientId: clientId,
            clientSecret: nil,
            scopes: scopes,
            redirectURL: URL(string: redirectUri)!,
            responseType: OIDResponseTypeCode,
            additionalParameters: nil
        )

        if let authState = authState, authState.lastTokenResponse != nil {
            let response = authState.lastAuthorizationResponse
            performTokenRequest(call, authorizationResponse: response)
        } else {
            currentAuthorizationFlow = OIDAuthState.authState(byPresenting: request, presenting: self.bridge?.viewController ?? UIViewController()) { authState, error in
                if let authState = authState {
                    let authToken = authState.lastTokenResponse?.accessToken
                    self.authState = authState
                    call.resolve([
                        "accessToken": authToken ?? "",
                        "userName": "",
                        "userId": ""
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
        guard let idToken = self.authState?.lastTokenResponse,
              let issuer = self.config?.discoveryDocument?.issuer else {
            call.reject("\(logTag) Error forming logout URL or no idToken available")
            return
        }

        let logoutUrl = "\(issuer)/v1/logout?id_token_hint=\(idToken)"

        authState = nil
        // TODO: clear sensitive information in UserDefaults

        if let url = URL(string: logoutUrl) {
            UIApplication.shared.open(url)
        }
        call.resolve()
    }
}
