import { registerPlugin } from '@capacitor/core';

import type { TwitterXPlugin } from './definitions';

const TwitterX = registerPlugin<TwitterXPlugin>('TwitterX', {
  web: () => import('./web').then(m => new m.TwitterXWeb()),
});

export * from './definitions';
export { TwitterX };
