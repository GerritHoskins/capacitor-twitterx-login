import { WebPlugin } from '@capacitor/core';
import type { TwitterXLoginPlugin, TwitterXLoginResponse } from './definitions';
export declare class TwitterXLoginWeb extends WebPlugin implements TwitterXLoginPlugin {
    login(): Promise<TwitterXLoginResponse>;
    logout(): Promise<void>;
}
