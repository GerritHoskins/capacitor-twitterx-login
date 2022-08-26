import { WebPlugin } from '@capacitor/core';
import {
  TwitterPlugin,
  TwitterLoginResponse,
  TwitterLoggedResponse,
} from './definitions';

export class TwitterWeb extends WebPlugin implements TwitterPlugin {
  constructor() {
    super({
      name: 'Twitter',
      platforms: ['web'],
    });
  }

  isLogged(): Promise<TwitterLoggedResponse> {
    throw this.unimplemented('Not implemented on web.');
  }
  login(): Promise<TwitterLoginResponse> {
    throw this.unimplemented('Not implemented on web.');
  }
  logout(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }
}
