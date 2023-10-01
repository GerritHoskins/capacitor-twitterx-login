import { WebPlugin } from '@capacitor/core';

import type { TwitterXLoginPlugin, TwitterXLoginResponse } from './definitions';

export class TwitterXLoginWeb extends WebPlugin implements TwitterXLoginPlugin {
  async login(): Promise<TwitterXLoginResponse> {
    this.unimplemented('Not implemented on web.');
    return {
      accessToken: '',
    };
  }

  async logout(): Promise<void> {
    this.unimplemented('Not implemented on web.');
  }
}
