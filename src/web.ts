import { WebPlugin } from '@capacitor/core';

import type { TwitterXLoginPlugin, TwitterXLoginResponse } from './definitions';

export class TwitterXLoginWeb extends WebPlugin implements TwitterXLoginPlugin {
  login(): Promise<TwitterXLoginResponse> {
    return Promise.reject('Not implemented on web.');
  }

  logout(): Promise<void> {
    return Promise.reject('Not implemented on web.');
  }
}
