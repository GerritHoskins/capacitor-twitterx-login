import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(TwitterXLoginPlugin)
public class TwitterXLoginPlugin: CAPPlugin {
    let LOG_TAG = "TwitterXLoginPlugin "
    let AUTHORIZATION_ENDPOINT = "https://twitter.com/i/oauth2/authorize"
    let TOKEN_ENDPOINT = "https://api.twitter.com/2/oauth2/token"
    var accessToken = ""
    var refreshToken = ""

    @objc func login(_ call: CAPPluginCall) {
        call.resolve()
    }

    func performTokenRequest(_ call: CAPPluginCall){
        call.resolve()
    }

    @objc func logout(_ call: CAPPluginCall) {
        call.resolve()
    }
}
