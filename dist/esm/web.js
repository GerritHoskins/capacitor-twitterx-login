import { WebPlugin } from '@capacitor/core';
export class TwitterXLoginWeb extends WebPlugin {
    async login() {
        this.unimplemented('Not implemented on web.');
        return {
            accessToken: '',
        };
    }
    async logout() {
        this.unimplemented('Not implemented on web.');
    }
}
//# sourceMappingURL=web.js.map