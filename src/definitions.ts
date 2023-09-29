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
  accessToken: string;
  userName: string;
  userId: string;
}

export interface TwitterXPluginOptions {
  clientId?: string;
  redirectUri?: string;
  scope?: string | string[];
}
