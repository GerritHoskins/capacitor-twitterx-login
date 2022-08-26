import { registerPlugin } from '@capacitor/core';
const Twitter = registerPlugin('Twitter', {
    web: () => import('./web').then((m) => new m.TwitterWeb()),
});
export * from './definitions';
export { Twitter };
//# sourceMappingURL=index.js.map