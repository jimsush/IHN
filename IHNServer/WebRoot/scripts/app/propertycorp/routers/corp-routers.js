define([
    'jquery',
    'underscore',
    'backbone',
    'views/propertylist-view'
], function($, _, Backbone, PropertyListView) {

  var slice = Array.prototype.slice;
  
  var AppRouter = Backbone.Router.extend({

    curRoute: 'propertylist',
    
    initialize: function() {
    	
    },
    
    routes: {
      'resetpassword': 'resetpassword',
      'noresetpassword': 'noresetpassword'
    },
    
    resetpassword: function(href) {
      curRoute = 'resetpassword';
      var div1=$('#resetpassword_div');
      div1[0].style.display='block';
    },
    
    noresetpassword: function(href) {
        curRoute = 'noresetpassword';
        var div1=$('#resetpassword_div');
        div1[0].style.display='none';
      }
    
  });
  
  return AppRouter;
});