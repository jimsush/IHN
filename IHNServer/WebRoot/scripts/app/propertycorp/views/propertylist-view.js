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
            this.listenTo(this.collection, 'add', this.addOne);
            
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
        	$('#menu-div').toggle();
        },
        
        resetAll : function(){
        	debugger
        	
        	this.collection.each(function(model){
        		$("#proplist").append("<li style='line-height:38px;'><a href='index.html?id="+model.get('id')+"'>"+model.get('name')+","+model.get('city')+","+model.get('address')+"</a></li>");
        		
        		$( "#properties tbody" ).append( "<tr>" +
						"<td>" + model.get('id') + "</td>" +
						"<td>" + model.get('name') + "</td>" +
						"<td>" + model.get('city') + "</td>" +
						"<td>" + model.get('address') + "</td>" +
					"</tr>" );
        		
        	});
        },
        
        addOne: function(model){
        	debugger
        	
        	$("#proplist").append("<li style='line-height:38px;'><a href='#'>"+model.get('name')+","+model.get('city')+","+model.get('address')+"</a></li>");
        	
        	$( "#properties tbody" ).append( "<tr>" +
					"<td>" + model.get('id') + "</td>" +
					"<td>" + model.get('name') + "</td>" +
					"<td>" + model.get('city') + "</td>" +
					"<td>" + model.get('address') + "</td>" +
				"</tr>" );
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
                height: 500,
                width: 700,
                modal: true,
                close: function() {
                  $( this ).dialog( "close" );
                  self.router.navigate(""); 
                }
            });
        	$('#btnAddProperty').button().click(function(e){
        		$('#addproperty-div').css("display","block");
        	});
        	$('#btnCancelProperty').button().click(function(e){
        		$('#addproperty-div').css("display","none");
        	});
        	$('#btnSubmitProperty').button().click(function(e){
        		debugger
        		var id1='LJZ_P'+new Date().getTime();
        		var name1=$('#propertyName').val();
        		var city1=$('#city').val();
        		var address1=$('#address').val();
        		var longitude1=$('#longitude').val();
        		var latitude1=$('#latitude').val();
        		debugger
        		
        		//var newProperty=self.collection.create({id: id1, name: name1, city: city1, address: address1, longitude: longitude1, latitude: latitude1});
        		//self.collection.add(newProperty);
        		var PropertyAssetModel=Backbone.Model.extend({
                	url: '/rest/park/properties',
                	defaults:{
                		id:'',
                		name:'',
                		city:'',
                		address:'',
                		longitude:121,
                		latitude:32,
                		corp:'LJZ',
                		userObject:""
                	}
                });
        		
        		var username1=IHNCookie.readCookie("user");
        		var newItem=new PropertyAssetModel;
        		newItem.set({userObject: username1, id: id1, name: name1, city: city1, address: address1, longitude: longitude1, latitude: latitude1});
        		newItem.save();
        		//$('#addproperty-div').css("display","none");
        	});
        },
        openManagePropertyDialog: function(){
        	$( "#manageproperty-form" ).buttonset();
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