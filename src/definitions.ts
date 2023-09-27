/// <reference types="@capacitor/cli" />

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    TwitterX: TwitterXPluginOptions;
  }
}

export interface TwitterXPlugin {
  login(): Promise<TwitterXLoginResponse>;
  logout(): Promise<void>;
}

export interface TwitterXLoginResponse {
  authToken: string;
  authTokenSecret: string;
  userName: string;
  userID: string;
}

export interface TwitterXPluginOptions {
  consumerKey?: string;
  consumerSecret?: string;
}
