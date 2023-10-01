var capacitorTwitterXLogin = (function (exports, core) {
    'use strict';

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

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
