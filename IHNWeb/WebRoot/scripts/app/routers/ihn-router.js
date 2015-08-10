define([
    'jquery',
    'underscore',
    'backbone',
    'views/ihn/demo/demo-view'
], function($, _, Backbone, DemoView) {

  var slice = Array.prototype.slice;
  
  var AppRouter = Backbone.Router.extend({
    
    curRoute: 'demo',
    headerView: null,
    sideMenuView: null,
    mainContentView: null,
    footerView: null,
    
    initialize: function(options) {
      this.headerView = options.headerView;
      this.sideMenuView = options.sideMenuView;
      this.mainContentView = options.mainContentView;
      this.footerView = options.footerView;
    },
    
    routes: {
      'demo': 'showDemoPage'
    },
    
    showDemoPage: function(href) {
      curRoute = 'demo';
      this.show(DemoView);
    },
    
    show: function(View, options) {
      var args = slice.call(arguments, _.isObject(options) ? 2 : 1);
      this.renderContent(View, args);
    },
    
    renderContent: function(ContentView, args){
      var main = this.mainContentView, $mainContainer = main.$el;
      var view = new ContentView({'linkOptions': args});
      $mainContainer.html(view.render().$el);
    }
    
  });
  
  return AppRouter;
});