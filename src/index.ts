import { registerPlugin } from '@capacitor/core';

import type { TwitterXLoginPlugin } from './definitions';

const TwitterXLogin = registerPlugin<TwitterXLoginPlugin>('TwitterXLogin', {
  web: () => import('./web').then(m => new m.TwitterXLoginWeb()),
});

export * from './definitions';
export { TwitterXLogin };
