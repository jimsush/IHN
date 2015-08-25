define([
    'jqueryui',
    'underscore',
    'backbone',
    'views/propertylist-view'
], function($, _, Backbone, PropertyListView) {

  var slice = Array.prototype.slice;
  
  var AppRouter = Backbone.Router.extend({

    curRoute: 'propertylist',
    propertyListView: null,
    
    initialize: function(options) {
    	this.propertyListView=options.propertyListView;
    	this.propertyListView.router=this;
    },
    
    routes: {
      'resetpassword':  'resetpassword',
      'manageproperty': 'manageproperty',
      'adminaccount':   'adminaccount'
    },
    
    resetpassword: function(href) {
      curRoute = 'resetpassword';
      this.propertyListView.openResetPasswordDialog();
    },
    
    manageproperty: function(href){
    	curRoute = 'manageproperty';
        this.propertyListView.openManagePropertyDialog();
    },
    
    adminaccount: function(href){
    	curRoute = 'adminaccount';
    	this.propertyListView.openAdminAccountDialog();
    }
    
  });
  
  return AppRouter;
});