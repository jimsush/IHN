define([
    'jquery',
    'underscore',
    'backbone',
    'views/ihn/maincontent-view'
], function($, _, Backbone, MainContentView) {

  var slice = Array.prototype.slice;
  
  var AppRouter = Backbone.Router.extend({
    
    curRoute: 'demo',
    bannerView: null,
    mainContentView: null,
    footerView: null,
    
    initialize: function(options) {
      this.bannerView = options.bannerView;
      this.mainContentView = options.mainContentView;
      this.footerView = options.footerView;
      
      this.showMapPage(null);
    },
    
    showMapPage: function(href) {
      curRoute = 'map';
      this.show(this.mainContentView);
    },
    
    show: function(View, options) {
      var args = slice.call(arguments, _.isObject(options) ? 2 : 1);
      //this.renderContent(View, args);
    },
    
    renderContent: function(ContentView, args){
      //var main = this.mainContentView;
      //var view = new ContentView({'linkOptions': args});
      //$mainContainer.html(view.render().$el);
    }
    
  });
  
  return AppRouter;
  
});