import { WebPlugin } from '@capacitor/core';
export class TwitterXWeb extends WebPlugin {
    constructor() {
        super({
            name: 'Twitter',
            platforms: ['web'],
        });
    }
    isLogged() {
        return Promise.reject('Not implemented on web.');
    }
    login() {
        return Promise.reject('Not implemented on web.');
    }
    logout() {
        return Promise.reject('Not implemented on web.');
    }
}
//# sourceMappingURL=web.js.map