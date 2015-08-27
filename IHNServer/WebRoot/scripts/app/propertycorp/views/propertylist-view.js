define([
  'jqueryui',
  'text!templates/propertylist-template.html',
  'backbone',
  '../../cookie/IHNCookie'
], function ($, PropertyListTemplate, Backbone, IHNCookie) {

    var PropertyListView = Backbone.View.extend({
    	
    	router: null,
    	
        template: _.template(PropertyListTemplate),
        
        initialize: function () {
            this.render();
            this.listenTo(this.collection, 'reset', this.resetAll);
            
            this.initResetPasswordDialog();
            this.initManagePropertyDialog();
            this.initAdminAccountDialog();
        },
        
        render: function () {
            this.$el.html(this.template());
            return this;
        },
        
        events: {
        	'click #btnMenu' : 'doToggleMenu'
        },
        
        doToggleMenu: function(event){
        	var e = $('#menu-div');        	
        	if(e[0].style.display == 'block')
               e[0].style.display = 'none';
            else
               e[0].style.display = 'block';
        },
        
        resetAll : function(){
        	this.collection.each(function(model){
        		$("#proplist").append("<li style='line-height:38px;'><a href='index.html?id="+model.get('id')+"'>"+model.get('name')+","+model.get('city')+","+model.get('address')+"</a></li>");
        	});
        },
        
        addOne: function(model){
        	$("#proplist").append("<li style='line-height:38px;'><a href='#'>"+model.get('name')+","+model.get('city')+","+model.get('address')+"</a></li>");
        },
        
        initResetPasswordDialog: function(){
        	var password1 = $( "#password" );
        	var password2 = $( "#password2" );
        	var allFields = $( [] ).add( password1 ).add( password2 );
            var self=this;
            
        	$("#resetpassword-form").dialog({
                autoOpen: false,
                height: 300,
                width: 350,
                modal: true,
                buttons: {
                  "修改密码": function() {
                    allFields.removeClass( "ui-state-error" );
                    
                    var PasswordModel=Backbone.Model.extend({
                    	urlRoot: '/rest/sm/password',
                    	defaults:{
                    		userName:'',
                    		password:''
                    	}
                    });
                    
                    var username1=IHNCookie.readCookie("user");
                    var newPassword=new PasswordModel;
                    newPassword.set({userName: username1, password: password1.val()});
                    
                    var mythis=this;
                    newPassword.save();
                    
                    $( this ).dialog( "close" );
                    self.router.navigate("");
                  },
                  Cancel: function() {
                    $( this ).dialog( "close" );
                    self.router.navigate("");
                  }
                },
                close: function() {
                  allFields.val( "" ).removeClass( "ui-state-error" );
                  $( this ).dialog( "close" );
                  self.router.navigate(""); 
                }
              });
        },
        
        openResetPasswordDialog: function(){
        	$("#resetpassword-form").dialog("open");
        },
        
        initManagePropertyDialog: function(){
        	var self=this;
        	$("#manageproperty-div").dialog({
                autoOpen: false,
                height: 300,
                width: 500,
                modal: true,
                close: function() {
                  $( this ).dialog( "close" );
                  self.router.navigate(""); 
                }
              });
        },
        openManagePropertyDialog: function(){
        	$("#manageproperty-div").dialog("open");
        },
        
        initAdminAccountDialog: function(){
        	var self=this;
        	$("#adminaccount-div").dialog({
                autoOpen: false,
                height: 300,
                width: 500,
                modal: true,
                close: function() {
                  $( this ).dialog( "close" );
                  self.router.navigate(""); 
                }
              });
        },
        openAdminAccountDialog: function(){
        	$("#adminaccount-div").dialog("open");
        }
        
    });

    return PropertyListView;
    
});