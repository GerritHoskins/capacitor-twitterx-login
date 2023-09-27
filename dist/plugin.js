var capacitorPlugin = (function (exports, core) {
  'use strict';

  const TwitterX = core.registerPlugin('TwitterX', {
    web: () =>
      Promise.resolve()
        .then(function () {
          return web;
        })
        .then(m => new m.TwitterXWeb()),
  });

  class TwitterXWeb extends core.WebPlugin {
    login() {
      return Promise.reject('Not implemented on web.');
    }
    logout() {
      return Promise.reject('Not implemented on web.');
    }
  }

  var web = /*#__PURE__*/ Object.freeze({
    __proto__: null,
    TwitterXWeb: TwitterXWeb,
  });

  exports.TwitterX = TwitterX;

  Object.defineProperty(exports, '__esModule', { value: true });

  return exports;
})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
