'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var core = require('@capacitor/core');

const TwitterX = core.registerPlugin('TwitterX', {
    web: () => Promise.resolve().then(function () { return web; }).then((m) => new m.TwitterXWeb()),
});

class TwitterXWeb extends core.WebPlugin {
    login() {
        return Promise.reject('Not implemented on web.');
    }
    logout() {
        return Promise.reject('Not implemented on web.');
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    TwitterXWeb: TwitterXWeb
});

exports.TwitterX = TwitterX;
//# sourceMappingURL=plugin.cjs.js.map
