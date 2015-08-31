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
    // Extra query string arguments appended to URLs that RequireJS uses to fetch resources
    urlArgs: urlArgs,
    // path mappings for module names not found directly under baseUrl
    paths: {
      jquery:    '../../libs/jquery/jquery-min',
      jqueryui: '../../libs/jquery/jquery-ui-min',
      jquerydatatable: '../../libs/jquery/jquery.dataTables.min',
      underscore: '../../libs/underscore/underscore-min',
      backbone: '../../libs/backbone/backbone-min',
      text: '../../libs/require/text',
      t: '../../libs/twaver/t',
      twaver: '../../libs/twaver/twaver',
      mono: '../../libs/twaver/mono',
      monotoolkits: '../../libs/twaver/mono_toolkits',
      
    },
    
    // Configure the dependencies, exports, and custom initialization for older
    shim: {
      jquerydatatable: {
            deps: ['jqueryui'],
            exports: '$'
      },
      jqueryui: {
            deps: ['jquery'],
            exports: '$'
      },
      underscore: {
        exports: '_'
      },
      backbone: {
        deps: ['underscore', 'jquery'],
        exports: 'Backbone'
      },
      monotoolkits : ['jquery','t','twaver'],
      mono: ['monotoolkits'],
      bootstrap: ['jqueryui', 'underscore', 'backbone'],
      },
    deps: ['bootstrap'],
    waitSeconds: 40
  });

})(require);