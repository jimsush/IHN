(function (require) {

  'use strict';

  var urlArgs = '';
  var isProductionInstance = false;
  if (isProductionInstance) {
    urlArgs = 'v=1';
  } else {
    urlArgs = 'dev=' + (new Date()).getTime();
  }

  require.config({
    // the root path to use for all module lookups
    baseUrl: 'scripts/app',
    // Extra query string arguments appended to URLs that RequireJS uses to fetch resources
    urlArgs: urlArgs,
    // path mappings for module names not found directly under baseUrl
    paths: {
      jquery: '../libs/jquery/jquery-min',
      underscore: '../libs/underscore/underscore-min',
      backbone: '../libs/backbone/backbone-min',
      text: '../libs/require/text',
      mono: '../libs/twaver/t'
    },
    // Configure the dependencies, exports, and custom initialization for older
    shim: {
      underscore: {
        exports: '_'
      },
      backbone: {
        deps: ['underscore', 'jquery'],
        exports: 'Backbone'
      },
      mono: ['jquery'],
      bootstrap: ['jquery', 'underscore', 'backbone'],
    },
    deps: ['bootstrap'],
    waitSeconds: 40
  });

})(require);