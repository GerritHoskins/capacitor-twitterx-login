import Foundation
import Capacitor
import AppAuth

@objc(TwitterXPlugin)
public class TwitterXPlugin: CAPPlugin
{
    var window: UIWindow?
    var currentAuthorizationFlow: OIDExternalUserAgentSession?
    var authState: OIDAuthState?

    @objc func login(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
             guard let configuration = OIDServiceConfiguration(
            authorizationEndpoint: URL(string: "https://api.twitter.com/oauth/authorize")!,
            tokenEndpoint: URL(string: "https://api.twitter.com/oauth/token")!
        ) else {
            // Handle error.            
            return
        }
        
        guard let clientId = getConfigValue("clientId") as? String ?? "ADD_IN_CAPACITOR_CONFIG_JSON",
              let redirectUri = getConfigValue("redirectUri") as? String ?? "ADD_IN_CAPACITOR_CONFIG_JSON",
              let scope = getConfigValue("scope") as? String ?? "ADD_IN_CAPACITOR_CONFIG_JSON",
        else {
            // Handle missing configuration values error
            return
        }
        
        let request = OIDAuthorizationRequest(
            configuration: configuration,
            clientId: clientId,
            clientSecret: nil,
            scopes: [scope],
            redirectURL: redirectUri,
            responseType: OIDResponseTypeCode,
            additionalParameters: nil
        )
        
        currentAuthorizationFlow = OIDAuthState.authState(byPresenting: request, presenting: self) { authState, error in
            if let authState = authState {
                // Authorization successful, get the token
                let accessToken = authState.lastTokenResponse?.accessToken
                // Handle the access token and other required information
            } else {
                // Authorization failed, handle the error
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
