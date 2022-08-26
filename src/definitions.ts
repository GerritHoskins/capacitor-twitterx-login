/// <reference types="@capacitor/cli" />

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    Twitter: TwitterPluginOptions;
  }
}

export interface TwitterPlugin {
  isLogged(): Promise<TwitterLoggedResponse>;
  login(): Promise<TwitterLoginResponse>;
  logout(): Promise<void>;
}
export interface TwitterLoginResponse {
  authToken: string;
  authTokenSecret: string;
  userName: string;
  userID: string;
}
export interface TwitterLoggedResponse {
  in: boolean;
  out: boolean
}
export interface TwitterPluginOptions {
  consumerKey?: string;
  consumerSecret?: string;
}
