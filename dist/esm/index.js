import { registerPlugin } from '@capacitor/core';
const TwitterX = registerPlugin('TwitterX', {
  web: () => import('./web').then(m => new m.TwitterXWeb()),
});
export * from './definitions';
export { TwitterX };
//# sourceMappingURL=index.js.map
