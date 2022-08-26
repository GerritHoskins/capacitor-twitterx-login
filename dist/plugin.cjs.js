'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var core = require('@capacitor/core');

const Twitter = core.registerPlugin('Twitter', {
    web: () => Promise.resolve().then(function () { return web; }).then((m) => new m.TwitterWeb()),
});

class TwitterWeb extends core.WebPlugin {
    constructor() {
        super({
            name: 'Twitter',
            platforms: ['web'],
        });
    }
    isLogged() {
        throw this.unimplemented('Not implemented on web.');
    }
    login() {
        throw this.unimplemented('Not implemented on web.');
    }
    logout() {
        throw this.unimplemented('Not implemented on web.');
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    TwitterWeb: TwitterWeb
});

exports.Twitter = Twitter;
//# sourceMappingURL=plugin.cjs.js.map
