declare module '@capacitor/cli' {
    interface PluginsConfig {
        Twitter: TwitterXPluginOptions;
    }
}
export interface TwitterXPlugin {
    isLogged(): Promise<TwitterXLoggedResponse>;
    login(): Promise<TwitterXLoginResponse>;
    logout(): Promise<void>;
}
export interface TwitterXLoginResponse {
    authToken: string;
    authTokenSecret: string;
    userName: string;
    userID: string;
}
export interface TwitterXLoggedResponse {
    in: boolean;
    out: boolean;
}
export interface TwitterXPluginOptions {
    consumerKey?: string;
    consumerSecret?: string;
}
