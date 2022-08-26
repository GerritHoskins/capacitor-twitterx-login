import { WebPlugin } from '@capacitor/core';
import { TwitterPlugin, TwitterLoginResponse, TwitterLoggedResponse } from './definitions';
export declare class TwitterWeb extends WebPlugin implements TwitterPlugin {
    constructor();
    isLogged(): Promise<TwitterLoggedResponse>;
    login(): Promise<TwitterLoginResponse>;
    logout(): Promise<void>;
}
