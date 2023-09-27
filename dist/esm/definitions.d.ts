declare module '@capacitor/cli' {
  interface PluginsConfig {
    Twitter: TwitterXPluginOptions;
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
