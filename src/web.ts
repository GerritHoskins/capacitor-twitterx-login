import { WebPlugin } from '@capacitor/core';
import {
  TwitterXPlugin,
  TwitterXLoginResponse,
  TwitterXLoggedResponse,
} from './definitions';

export class TwitterXWeb extends WebPlugin implements TwitterXPlugin {
  constructor() {
    super({
      name: 'Twitter',
      platforms: ['web'],
    });
  }

  isLogged(): Promise<TwitterXLoggedResponse> {
   return Promise.reject('Not implemented on web.');
  }

  login(): Promise<TwitterXLoginResponse> {
    return Promise.reject('Not implemented on web.');
  }

  logout(): Promise<void> {
    return Promise.reject('Not implemented on web.');
  }
}
