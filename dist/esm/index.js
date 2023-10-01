import { registerPlugin } from '@capacitor/core';
const TwitterXLogin = registerPlugin('TwitterXLogin', {
    web: () => import('./web').then(m => new m.TwitterXLoginWeb()),
});
export * from './definitions';
export { TwitterXLogin };
//# sourceMappingURL=index.js.map