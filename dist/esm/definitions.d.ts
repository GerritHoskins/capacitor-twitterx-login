declare module '@capacitor/cli' {
    interface PluginsConfig {
        TwitterXLoginPlugin: TwitterXConfig;
    }
}
export interface TwitterXLoginPlugin {
    login(): Promise<TwitterXLoginResponse>;
    logout(): Promise<void>;
}
export interface TwitterXLoginResponse {
    accessToken: string;
    userName?: string;
    userId?: string;
}
export interface TwitterXConfig {
    clientId?: string;
    redirectUri?: string;
    scope?: string | string[];
}
