import { WebPlugin } from '@capacitor/core';
import { TwitterXPlugin, TwitterXLoginResponse } from './definitions';
export declare class TwitterXWeb extends WebPlugin implements TwitterXPlugin {
    login(): Promise<TwitterXLoginResponse>;
    logout(): Promise<void>;
}
