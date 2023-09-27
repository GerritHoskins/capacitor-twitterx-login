import { WebPlugin } from '@capacitor/core';

import type { TwitterXPlugin, TwitterXLoginResponse } from './definitions';

export class TwitterXWeb extends WebPlugin implements TwitterXPlugin {
  login(): Promise<TwitterXLoginResponse> {
    return Promise.reject('Not implemented on web.');
  }

  logout(): Promise<void> {
    return Promise.reject('Not implemented on web.');
  }
}
