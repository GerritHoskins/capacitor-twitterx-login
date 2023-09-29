import Foundation
import Capacitor
import UIKit

@objc(TwitterXPlugin)
public class TwitterXPlugin: CAPPlugin {
    let LOG_TAG = "twitterX "
    let AUTHORIZATION_ENDPOINT = "https://twitter.com/i/oauth2/authorize"
    let TOKEN_ENDPOINT = "https://api.twitter.com/2/oauth2/token"
    var authState: AuthState?
    var authService: AuthorizationService?
    var config: AuthorizationServiceConfiguration?
    var accessToken = ""
    var refreshToken = ""
    
    @objc func login(_ call: CAPPluginCall) {
        DispatchQueue.global().async {
            do {
                self.config = try AuthorizationServiceConfiguration(
                    authorizationEndpoint: URL(string: self.AUTHORIZATION_ENDPOINT)!,
                    tokenEndpoint: URL(string: self.TOKEN_ENDPOINT)!
                )
                
                self.authState = AuthState(config: self.config!)
                self.authService = AuthorizationService(context: self.bridge?.viewController)
                
                if self.authState!.needsTokenRefresh && !self.refreshToken.isEmpty {
                    self.performTokenRequest(call)
                } else {
                    let request = try AuthorizationRequest(
                        configuration: self.config!,
                        clientId: getConfigValue("clientId") as! String,
                        responseType: ResponseTypeValues.code,
                        redirectUri: URL(string: getConfigValue("redirectUri") as! String)!,
                        scope: getConfigValue("scope") as? String
                    )
                    
                    let authIntent = self.authService!.getAuthorizationRequestIntent(request: request)
                    self.bridge?.viewController.present(authIntent, animated: true, completion: nil)
                }
            } catch {
                call.reject("(self.LOG_TAG)Unexpected exception on open browser for authorization request.", error.localizedDescription)
            }
        }
    }
    
    func performTokenRequest(_ call: CAPPluginCall) {
        let clientId = getConfigValue("clientId") as! String
        
        let tokenRequest = TokenRequest(
            configuration: self.config!,
            grantType: "refresh_token",
            refreshToken: self.refreshToken
        )
        
        self.authService!.performTokenRequest(tokenRequest) { tokenResponse, error in
            if let error = error {
                print("(self.LOG_TAG)AppAuth Token Request failed: (error)")
                call.reject("(self.LOG_TAG)AppAuth Token Request failed")
            } else if let tokenResponse = tokenResponse {
                self.authState!.update(tokenResponse: tokenResponse, error: nil)
                var ret = JSObject()
                ret["accessToken"] = tokenResponse.accessToken
                call.resolve(ret)
            }
        }
    }
    
    @objc func logout(_ call: CAPPluginCall) {
        let idToken = self.authState?.idToken
        let issuer = self.authState?.authorizationServiceConfiguration?.discoveryDocument?.issuer
        
        guard let idToken = idToken, let issuer = issuer else {
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
    
    @objc func handleOauthIntentResult(_ call: CAPPluginCall, _ result: CAPPluginCall) {
        if result.resultCode != 0 {
            if let data = result.data {
                do {
                    let response = try AuthorizationResponse(from: data)
                    let error = try AuthorizationException(from: data)
                    
                    if let error = error {
                        call.reject("(self.LOG_TAG)general error during handling of auth request. (error.localizedDescription)")
                        return
                    }
                    
                    if let response = response {
                        let tokenExchangeRequest = try response.tokenExchangeRequest()
                        
                        self.authService!.performTokenRequest(tokenExchangeRequest) { accessTokenResponse, error in
                            self.authState!.update(accessTokenResponse: accessTokenResponse, error: error)
                            
                            if let error = error {
                                call.reject("(error.code) (error)")
                            } else {
                                if let accessTokenResponse = accessTokenResponse {
                                    self.refreshToken = accessTokenResponse.refreshToken
                                    
                                    self.authState!.performActionWithFreshTokens(self.authService!) { accessToken, idToken, error in
                                        var ret = JSObject()
                                        ret["accessToken"] = accessToken
                                        ret["userName"] = ""
                                        ret["userId"] = ""
                                        call.resolve(ret)
                                    }
                                } else {
                                    var ret = JSObject()
                                    ret["access_token"] = ""
                                    call.resolve(ret)
                                }
                            }
                        }
                    } else {
                        call.reject("(self.LOG_TAG) Authorization failed.")
                    }
                } catch {
                    call.reject("(self.LOG_TAG)general error during handling of auth request. (error.localizedDescription)")
                }
            } else {
                call.reject("(self.LOG_TAG) Authorization failed.")
            }
            else {
                call.reject("(self.LOG_TAG) Authorization failed.")
            }
        }
    }
}
