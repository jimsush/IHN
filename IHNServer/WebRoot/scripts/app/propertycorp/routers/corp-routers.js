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
    	//debugger
    	if(this.curRoute=='resetpassword')
    		return;
        this.curRoute = 'resetpassword';
        this.propertyListView.openResetPasswordDialog();
    },
    
    manageproperty: function(href){
    	//debugger
    	if(this.curRoute=='manageproperty')
    		return;
    	this.curRoute = 'manageproperty';
        this.propertyListView.openManagePropertyDialog();
    },
    
    adminaccount: function(href){
    	//debugger
    	if(this.curRoute=='adminaccount')
    		return;
    	this.curRoute = 'adminaccount';
    	this.propertyListView.openAdminAccountDialog();
    }
    
  });
  
  return AppRouter;
});