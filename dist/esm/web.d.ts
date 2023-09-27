import { WebPlugin } from '@capacitor/core';
import { TwitterXPlugin, TwitterXLoginResponse, TwitterXLoggedResponse } from './definitions';
export declare class TwitterXWeb extends WebPlugin implements TwitterXPlugin {
    constructor();
    isLogged(): Promise<TwitterXLoggedResponse>;
    login(): Promise<TwitterXLoginResponse>;
    logout(): Promise<void>;
}
