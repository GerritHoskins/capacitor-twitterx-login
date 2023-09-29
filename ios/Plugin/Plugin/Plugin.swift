import Foundation
import Capacitor
import AppAuth

@objc(TwitterXPlugin)
public class TwitterXPlugin: CAPPlugin {
    var window: UIWindow?
    var currentAuthorizationFlow: OIDExternalUserAgentSession?
    var authState: OIDAuthState?

    @objc func login(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            guard let authorizationEndpoint = URL(string: "https://api.twitter.com/oauth/authorize"),
                  let tokenEndpoint = URL(string: "https://api.twitter.com/oauth/token") else {
                call.reject("Invalid endpoint URL")
                return
            }

            let configuration = OIDServiceConfiguration(authorizationEndpoint: authorizationEndpoint, tokenEndpoint: tokenEndpoint)

            guard let clientId = self.getConfigValue("clientId") as? String,
                  let redirectUriString = self.getConfigValue("redirectUri") as? String,
                  let redirectUri = URL(string: redirectUriString),
                  let scope = self.getConfigValue("scope") as? String else {
                call.reject("Missing configuration values")
                return
            }

            let request = OIDAuthorizationRequest(configuration: configuration, clientId: clientId, clientSecret: nil, scopes: [scope], redirectURL: redirectUri, responseType: OIDResponseTypeCode, additionalParameters: nil)

            self.currentAuthorizationFlow = OIDAuthState.authState(byPresenting: request, presenting: self.bridge?.viewController ?? self) { authState, error in
                if let authState = authState {
                    let accessToken = authState.lastTokenResponse?.accessToken
                    var result = JSObject()
                    result["accessToken"] = accessToken
                    result["userName"] = ""
                    result["userId"] = ""
                    call.resolve(result)
                } else {
                    // Authorization failed, handle the error
                    call.reject("Authorization error: \(error?.localizedDescription ?? "Unknown error")")
                }
            }
        }
    }

    @objc func logout(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            guard let idToken = authState?.lastTokenResponse?.idToken,
                  let issuer = authState?.lastAuthorizationResponse.request.configuration.issuer,
                  let logoutURL = URL(string: "\(issuer)/v1/logout?id_token_hint=\(idToken)") else {
                call.reject("Error forming logout URL or no idToken available")
                return
            }

            // Clearing the AuthState.
            authState = nil

            // Optionally, if you are storing tokens or any sensitive information in UserDefaults,
            // clear them here as well.

            UIApplication.shared.open(logoutURL)
            call.resolve()
        }
    }

    private func getConfigString(forKey key: String) -> String? {
        // Replace with actual implementation to retrieve the configuration values.
        return nil
    }
}
