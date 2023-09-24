import { registerPlugin } from '@capacitor/core';
import { TwitterXPlugin } from './definitions';

const TwitterX = registerPlugin<TwitterXPlugin>('TwitterX', {
    web: () => import('./web').then((m) => new m.TwitterXWeb()),
});

export * from './definitions';
export { TwitterX };
