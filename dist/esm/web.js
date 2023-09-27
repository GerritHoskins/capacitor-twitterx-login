import { WebPlugin } from '@capacitor/core';
export class TwitterXWeb extends WebPlugin {
    login() {
        return Promise.reject('Not implemented on web.');
    }
    logout() {
        return Promise.reject('Not implemented on web.');
    }
}
//# sourceMappingURL=web.js.map