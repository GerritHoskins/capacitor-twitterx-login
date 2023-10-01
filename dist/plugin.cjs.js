'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var core = require('@capacitor/core');

const TwitterXLogin = core.registerPlugin('TwitterXLogin', {
    web: () => Promise.resolve().then(function () { return web; }).then(m => new m.TwitterXLoginWeb()),
});

class TwitterXLoginWeb extends core.WebPlugin {
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

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    TwitterXLoginWeb: TwitterXLoginWeb
});

exports.TwitterXLogin = TwitterXLogin;
//# sourceMappingURL=plugin.cjs.js.map
